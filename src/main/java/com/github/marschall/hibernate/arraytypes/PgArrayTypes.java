package com.github.marschall.hibernate.arraytypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.Size;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.query.BindableType;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.jdbc.ArrayJdbcType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.internal.BasicTypeImpl;
import org.postgresql.PGConnection;

/**
 * Utility methods for creating {@link TypedParameterValue} instances on {@code int[]} and {@code long[]}
 * on PostgreSQL.
 */
public final class PgArrayTypes {

  private static final BindableType<int[]> INTEGER_ARRAY_TYPE;

  private static final BindableType<long[]> LONG_ARRAY_TYPE;

  static {
    INTEGER_ARRAY_TYPE = new IntArrayType(
        new BasicTypeImpl<>(IntegerJavaType.INSTANCE, IntegerJdbcType.INSTANCE),
        new ArrayJdbcType(IntegerJdbcType.INSTANCE),
        PgIntArrayValueBinder.INSTANCE);
    LONG_ARRAY_TYPE = new LongArrayType(
        new BasicTypeImpl<>(LongJavaType.INSTANCE, BigIntJdbcType.INSTANCE),
        new ArrayJdbcType(BigIntJdbcType.INSTANCE),
        PgLongArrayValueBinder.INSTANCE);
  }

  private PgArrayTypes() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Creates a new {@link TypedParameterValue} for an {@code int[]}
   * 
   * @param value the parameter value, not {@code null}
   * @return the {@link TypedParameterValue}
   */
  public static TypedParameterValue<int[]> newIntegerArrayParameter(int... value) {
    return new TypedParameterValue<>(INTEGER_ARRAY_TYPE, value);
  }

  /**
   * Creates a new {@link TypedParameterValue} for an {@code long[]}
   * 
   * @param value the parameter value, not {@code null}
   * @return the {@link TypedParameterValue}
   */
  public static TypedParameterValue<long[]> newLongArrayParameter(long... value) {
    return new TypedParameterValue<>(LONG_ARRAY_TYPE, value);
  }

}

/**
 * Abstract base class for {@link ValueBinder}s for arrays of primitive types on PostgreSQL.
 *
 * @param <T> the array type to bind
 */
abstract class PgPrimitiveArrayValueBinder<T> implements ValueBinder<T> {

  private final JdbcType elementJdbcType;
  private final JavaType<?> elementJavaType;

  protected PgPrimitiveArrayValueBinder(JdbcType elementJdbcType, JavaType<?> elementJavaType) {
    this.elementJdbcType = elementJdbcType;
    this.elementJavaType = elementJavaType;
  }

  @Override
  public void bind(PreparedStatement st, T value, int index, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    String typeName = getTypeName(session);
    java.sql.Array array = createSqlArray(value, session, typeName);
    st.setObject(index, array);
  }

  private java.sql.Array createSqlArray(T value, SharedSessionContractImplementor session, String typeName)
      throws SQLException {
    PGConnection pgConnection = session.getJdbcCoordinator().getLogicalConnection().getPhysicalConnection().unwrap(PGConnection.class);
    return pgConnection.createArrayOf(typeName, value);
  }

  private String getTypeName(SharedSessionContractImplementor session) {
    Size size = session.getJdbcServices()
        .getDialect()
        .getSizeStrategy()
        .resolveSize(elementJdbcType, elementJavaType, null, null, null);
    String typeName = session.getTypeConfiguration()
        .getDdlTypeRegistry()
        .getDescriptor(elementJdbcType.getDefaultSqlTypeCode())
        .getTypeName(size);
    int cutIndex = typeName.indexOf('(');
    if (cutIndex > 0) {
      // getTypeName for this case required length, etc, parameters.
      // Cut them out and use database defaults.
      typeName = typeName.substring( 0, cutIndex );
    }
    return typeName;
  }

  @Override
  public void bind(CallableStatement st, T value, String name, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    String typeName = getTypeName(session);
    java.sql.Array array = createSqlArray(value, session, typeName);
    st.setObject(name, array);
  }

}

final class PgIntArrayValueBinder extends PgPrimitiveArrayValueBinder<int[]> {

  static final ValueBinder<int[]> INSTANCE = new PgIntArrayValueBinder();

  private PgIntArrayValueBinder() {
    super(IntegerJdbcType.INSTANCE, IntegerJavaType.INSTANCE);
  }

}

final class PgLongArrayValueBinder extends PgPrimitiveArrayValueBinder<long[]> {

  static final ValueBinder<long[]> INSTANCE = new PgLongArrayValueBinder();

  private PgLongArrayValueBinder() {
    super(BigIntJdbcType.INSTANCE, LongJavaType.INSTANCE);
  }

}
