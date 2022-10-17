package com.github.marschall.hibernate.arraytypes;

import org.hibernate.dialect.OracleArrayJdbcType;
import org.hibernate.query.BindableType;
import org.hibernate.type.BasicArrayType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.ArrayJavaType;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.internal.BasicTypeImpl;

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

  static final class OraclePrimitiveArrayType extends OracleArrayJdbcType {

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

}
