package com.github.marschall.hibernate.arraytypes;

import java.math.BigDecimal;

import org.hibernate.query.BindableType;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.BasicArrayType;
import org.hibernate.type.descriptor.java.ArrayJavaType;
import org.hibernate.type.descriptor.java.BigDecimalJavaType;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.hibernate.type.descriptor.jdbc.ArrayJdbcType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.descriptor.jdbc.DecimalJdbcType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.hibernate.type.internal.BasicTypeImpl;

/**
 * Utility methods for creating {@link TypedParameterValue} instances on {@code Object[]}.
 */
public final class ArrayTypes {

  private static final BindableType<Integer[]> INTEGER_ARRAY_TYPE;

  private static final BindableType<Long[]> LONG_ARRAY_TYPE;

  private static final BindableType<BigDecimal[]> BIG_DECIMAL_ARRAY_TYPE;

  private static final BindableType<String[]> STRING_ARRAY_TYPE;

  static {
    INTEGER_ARRAY_TYPE = new BasicArrayType<>(
            new BasicTypeImpl<>(IntegerJavaType.INSTANCE, IntegerJdbcType.INSTANCE),
            new ArrayJdbcType(IntegerJdbcType.INSTANCE),
            new ArrayJavaType<>(IntegerJavaType.INSTANCE));
    LONG_ARRAY_TYPE = new BasicArrayType<>(
            new BasicTypeImpl<>(LongJavaType.INSTANCE, BigIntJdbcType.INSTANCE),
            new ArrayJdbcType(BigIntJdbcType.INSTANCE),
            new ArrayJavaType<>(LongJavaType.INSTANCE));
    BIG_DECIMAL_ARRAY_TYPE = new BasicArrayType<>(
            new BasicTypeImpl<>(BigDecimalJavaType.INSTANCE, DecimalJdbcType.INSTANCE),
            new ArrayJdbcType(DecimalJdbcType.INSTANCE),
            new ArrayJavaType<>(BigDecimalJavaType.INSTANCE));
    STRING_ARRAY_TYPE = new BasicArrayType<>(
            new BasicTypeImpl<>(StringJavaType.INSTANCE, VarcharJdbcType.INSTANCE),
            new ArrayJdbcType(VarcharJdbcType.INSTANCE),
            new ArrayJavaType<>(StringJavaType.INSTANCE));
  }

  private ArrayTypes() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Creates a new {@link TypedParameterValue} for an {@code Integer[]}
   * 
   * @param value the parameter value, not {@code null}
   * @return the {@link TypedParameterValue}
   */
  public static TypedParameterValue<Integer[]> newIntegerArrayParameter(Integer... value) {
    return new TypedParameterValue<>(INTEGER_ARRAY_TYPE, value);
  }

  /**
   * Creates a new {@link TypedParameterValue} for an {@code Long[]}
   * 
   * @param value the parameter value, not {@code null}
   * @return the {@link TypedParameterValue}
   */
  public static TypedParameterValue<Long[]> newLongArrayParameter(Long... value) {
    return new TypedParameterValue<>(LONG_ARRAY_TYPE, value);
  }

  /**
   * Creates a new {@link TypedParameterValue} for an {@code BigDecimal[]}
   * 
   * @param value the parameter value, not {@code null}
   * @return the {@link TypedParameterValue}
   */
  public static TypedParameterValue<BigDecimal[]> newBigDecimalArrayParameter(BigDecimal... value) {
    return new TypedParameterValue<>(BIG_DECIMAL_ARRAY_TYPE, value);
  }

  /**
   * Creates a new {@link TypedParameterValue} for an {@code String[]}
   * 
   * @param value the parameter value, not {@code null}
   * @return the {@link TypedParameterValue}
   */
  public static TypedParameterValue<String[]> newLongArrayParameter(String... value) {
    return new TypedParameterValue<>(STRING_ARRAY_TYPE, value);
  }

}
