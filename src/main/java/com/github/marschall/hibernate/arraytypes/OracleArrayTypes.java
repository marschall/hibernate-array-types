package com.github.marschall.hibernate.arraytypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.dialect.OracleArrayJdbcType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.query.BindableType;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.BasicArrayType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.ArrayJavaType;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.internal.BasicTypeImpl;

import oracle.jdbc.OracleConnection;

/**
 * Utility methods for creating {@link TypedParameterValue} instances on arrays
 * on Oracle.
 */
public final class OracleArrayTypes {

  private OracleArrayTypes() {
    throw new AssertionError("not instantiable");
  }

  public static BindableType<Integer[]> newIntegerArrayType(String typeName) {
    return new BasicArrayType<>(
        new BasicTypeImpl<>(IntegerJavaType.INSTANCE, IntegerJdbcType.INSTANCE),
        new OracleArrayJdbcType(typeName, IntegerJdbcType.INSTANCE),
        new ArrayJavaType<>(IntegerJavaType.INSTANCE));
  }

  public static BindableType<int[]> newIntArrayType(String typeName) {
    return new IntArrayType(
        new BasicTypeImpl<>(IntegerJavaType.INSTANCE, IntegerJdbcType.INSTANCE),
        new OraclePrimitiveArrayType(typeName, IntegerJdbcType.INSTANCE),
        new OraclePrimitiveArrayValueBinder<>(typeName));
  }

  public static BindableType<Long[]> newLongReferenceArrayType(String typeName) {
    return new BasicArrayType<>(
        new BasicTypeImpl<>(LongJavaType.INSTANCE, BigIntJdbcType.INSTANCE),
        new OracleArrayJdbcType(typeName, BigIntJdbcType.INSTANCE),
        new ArrayJavaType<>(LongJavaType.INSTANCE));
  }

  public static BindableType<long[]> newLongPrimitiveArrayType(String typeName) {
    return new LongArrayType(
        new BasicTypeImpl<>(LongJavaType.INSTANCE, BigIntJdbcType.INSTANCE),
        new OraclePrimitiveArrayType(typeName, BigIntJdbcType.INSTANCE),
        new OraclePrimitiveArrayValueBinder<>(typeName));
  }

}

/**
 * Specialization of {@link OracleArrayJdbcType} for arrays of primitive types.
 */
final class OraclePrimitiveArrayType extends OracleArrayJdbcType {
  
  private final String typeName;
  
  OraclePrimitiveArrayType(String typeName, JdbcType elementJdbcType) {
    super(typeName, elementJdbcType);
    this.typeName = typeName;
  }
  
  @Override
  public <X> ValueBinder<X> getBinder(JavaType<X> javaTypeDescriptor) {
    return new OraclePrimitiveArrayValueBinder<>(this.typeName);
  }
  
}

/**
 * {@link ValueBinder} for binding arrays of primitive values on Oracle.
 *
 * @param <T> the array type to bind
 */
final class OraclePrimitiveArrayValueBinder<T> implements ValueBinder<T> {

  private final String typeName;

  OraclePrimitiveArrayValueBinder(String typeName) {
    this.typeName = typeName;
  }

  @Override
  public void bind(PreparedStatement st, T value, int index, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    java.sql.Array array = createSqlArray(value, session);
    st.setObject(index, array);
  }

  private java.sql.Array createSqlArray(T value, SharedSessionContractImplementor session)
      throws SQLException {
    OracleConnection oracleConnection = session.getJdbcCoordinator().getLogicalConnection().getPhysicalConnection().unwrap(OracleConnection.class);
    return oracleConnection.createOracleArray(this.typeName, value);
  }

  @Override
  public void bind(CallableStatement st, T value, String name, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    java.sql.Array array = createSqlArray(value, session);
    st.setObject(name, array);
  }

}
