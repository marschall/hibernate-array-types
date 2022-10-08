package com.github.marschall.hibernate.arraytypes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.procedure.ParameterMisuseException;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.type.spi.TypeConfiguration;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code Object[]} to a SQL array.
 */
public final class ArrayType extends AbstractReferenceArrayType {
  
  private static final TypeConfiguration TYPE_CONFIGURATION = new TypeConfiguration();

  public static final UserType<Object[]> INSTANCE = new ArrayType();

  public static final Type TYPE = new CustomType<>(INSTANCE, TYPE_CONFIGURATION);

  private final String typeName;

  /**
   * Constructs a new {@link ArrayType} with the given element type name.
   *
   * @param typeName the SQL name of the type the elements of the array map to
   *
   * @see Connection#createArrayOf(String, Object[])
   */
  public ArrayType(String typeName) {
    this.typeName = typeName;
  }

  /**
   * Constructs a new {@link ArrayType} with an inferred element type name.
   */
  public ArrayType() {
    this.typeName = null;
  }
  
  @Override
  public void nullSafeSet(PreparedStatement st, Object[] value, int index, SharedSessionContractImplementor session)
      throws SQLException {
    if (value != null) {
      Connection connection = session.getJdbcConnectionAccess().obtainConnection();
      try {
        Dialect dialect = session.getJdbcServices().getDialect();
        java.sql.Array array = connection.createArrayOf(this.getTypeName(value, dialect), (Object[]) value);
        st.setArray(index, array);
      } finally {
        session.getJdbcConnectionAccess().releaseConnection(connection);
      }
    } else {
      st.setNull(index, Types.ARRAY);
    }
  }

  private String getTypeName(Object value, Dialect dialect) {
    if (this.typeName != null) {
      return this.typeName;
    }
    return inferType(value, dialect);
  }

  private static String inferType(Object value, Dialect dialect) {
    Class<? extends Object> clazz = value.getClass();
    if (!clazz.isArray()) {
      throw new ParameterMisuseException("value must be an array but was: " + clazz);
    }
    Class<?> componentType = inferComponentType(clazz, value);
    if (componentType == String.class) {
      return dialect.getTypeName(Types.VARCHAR);
    } else if (componentType == Short.class) {
      return dialect.getTypeName(Types.SMALLINT);
    } else if (componentType == Integer.class) {
      return dialect.getTypeName(Types.INTEGER);
    } else if (componentType == Long.class) {
      return dialect.getTypeName(Types.BIGINT);
    } else if (componentType == BigDecimal.class) {
      return stripPrecision(dialect.getTypeName(Types.DECIMAL));
//    } else if ((componentType == Float.class) || (componentType == Double.class)) {
//      return dialect.getTypeName(Types.NUMERIC);
    } else if (componentType == Float.class) {
      return dialect.getTypeName(Types.FLOAT);
    } else if (componentType == Double.class) {
      return dialect.getTypeName(Types.DOUBLE);
    } else if (componentType == Boolean.class) {
      return dialect.getTypeName(Types.BOOLEAN);
    } else {
      throw new ParameterMisuseException("unuspported component type: " + componentType);
    }
  }

  private static Class<?> inferComponentType(Class<? extends Object> clazz, Object value) {
    Class<?> componentType = clazz.getComponentType();
    if (componentType.isPrimitive()) {
      throw new ParameterMisuseException("component type must be reference but was: " + componentType);
    }
    if (componentType == Object.class) {
      int length = java.lang.reflect.Array.getLength(value);
      if (length == 0) {
        throw new ParameterMisuseException("can not infer component type of empty array");
      }
      Class<?> candidate = null;
      for (int i = 0; i < length; i++) {
        Object element = java.lang.reflect.Array.get(value, i);
        if (element != null) {
          if (candidate == null) {
            candidate = element.getClass();
          } else if (candidate != element.getClass()) {
            throw new ParameterMisuseException("can not infer component type of heterogenious array");
          }
        }
      }
      if (candidate == null) {
        throw new ParameterMisuseException("can not infer component type of array with only null");
      }
      return candidate;
    } else {
      return componentType;
    }
  }

  private static String stripPrecision(String typeName) {
    int parenthesesIndex = typeName.indexOf('(');
    if (parenthesesIndex == -1) {
      return typeName;
    } else {
      return typeName.substring(0, parenthesesIndex);
    }
  }

  /**
   * Convenience method that creates an instance of this class adapted as a {@link Type}.
   *
   * @param typeName the SQL name of the type the elements of the array map to
   * @return an instance of this class adapted as a {@link Type}
   */
  public static Type newType(String typeName) {
    return new CustomType<>(new ArrayType(typeName), TYPE_CONFIGURATION);
  }

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   *
   * @param typeName the SQL name of the type the elements of the array map to
   * @param values the Java array of elements to bind
   * @param <T> the element type
   * @return a TypedParameterValue binding the given value to an array
   * @see Connection#createArrayOf(String, Object[])
   */
  @SafeVarargs
  public static <T> TypedParameterValue<Object[]> newParameter(String typeName, T... values) {
    return new TypedParameterValue<>(newType(typeName), values);
  }

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   *
   * @param values the Java array of elements to bind
   * @param <T> the element type
   * @return a TypedParameterValue binding the given value to an array
   */
  @SafeVarargs
  public static <T> TypedParameterValue<Object[]> newParameter(T... values) {
    return new TypedParameterValue(TYPE, values);
  }

}
