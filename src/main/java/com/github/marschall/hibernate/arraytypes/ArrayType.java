package com.github.marschall.hibernate.arraytypes;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.procedure.ParameterMisuseException;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code Object[]} to a SQL array.
 */
public final class ArrayType extends AbstractReferenceArrayType {

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
  public void nullSafeSet(PreparedStatement st, Object value, int index,
          SharedSessionContractImplementor session)
          throws HibernateException, SQLException {
    if (value != null) {
      Connection connection = session.getJdbcConnectionAccess().obtainConnection();
      try {
        Dialect dialect = session.getJdbcServices().getDialect();
        Array array = connection.createArrayOf(this.getTypeName(value, dialect), (Object[]) value);
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
    Class<?> componentType = clazz.getComponentType();
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

  private static String stripPrecision(String typeName) {
    int parenthesesIndex = typeName.indexOf('(');
    if (parenthesesIndex == -1) {
      return typeName;
    } else {
      return typeName.substring(0, parenthesesIndex);
    }
  }

}
