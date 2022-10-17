package com.github.marschall.hibernate.arraytypes;

import org.hibernate.query.BindableType;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.AdjustableBasicType;
import org.hibernate.type.BasicPluralType;
import org.hibernate.type.BasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.jdbc.ArrayJdbcType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcLiteralFormatter;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;
import org.hibernate.type.descriptor.jdbc.internal.JdbcLiteralFormatterArray;
import org.hibernate.type.internal.BasicTypeImpl;

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

  static final class PgPrimitiveIntArrayType extends ArrayJdbcType {

    PgPrimitiveIntArrayType(JdbcType elementJdbcType) {
      super(IntegerJdbcType.INSTANCE);
    }

    @Override
    public <X> ValueBinder<X> getBinder(JavaType<X> javaTypeDescriptor) {
      return (ValueBinder<X>) PgIntArrayValueBinder.INSTANCE;
    }

  }

  static final class PgPrimitiveLongArrayType extends ArrayJdbcType {

    PgPrimitiveLongArrayType(JdbcType elementJdbcType) {
      super(BigIntJdbcType.INSTANCE);
    }

    @Override
    public <X> ValueBinder<X> getBinder(JavaType<X> javaTypeDescriptor) {
      return (ValueBinder<X>) PgLongArrayValueBinder.INSTANCE;
    }

  }

  static abstract class PrimitiveArrayType<C, E> extends AbstractSingleColumnStandardBasicType<C>
    implements AdjustableBasicType<C>, BasicPluralType<C, E> {

    private final BasicType<E> baseDescriptor;
    private final ValueBinder<C> jdbcValueBinder;
    private final ValueExtractor<C> jdbcValueExtractor;
    private final JdbcLiteralFormatter<C> jdbcLiteralFormatter;

    PrimitiveArrayType(BasicType<E> baseDescriptor, JavaType<C> arrayTypeDescriptor,
        JdbcType arrayJdbcType, ValueBinder<C> jdbcValueBinder, ValueExtractor<C> jdbcValueExtractor) {
      super(arrayJdbcType, arrayTypeDescriptor);
      this.baseDescriptor = baseDescriptor;
      this.jdbcValueBinder = jdbcValueBinder;
      this.jdbcValueExtractor = jdbcValueExtractor;
      this.jdbcLiteralFormatter = new JdbcLiteralFormatterArray(
          baseDescriptor.getJavaTypeDescriptor(),
          super.getJdbcLiteralFormatter()
          );
    }

    @Override
    public BasicType<E> getElementType() {
      return baseDescriptor;
    }

    @Override
    protected boolean registerUnderJavaType() {
      return true;
    }

    @Override
    public ValueExtractor<C> getJdbcValueExtractor() {
      return jdbcValueExtractor;
    }

    @Override
    public ValueBinder<C> getJdbcValueBinder() {
      return jdbcValueBinder;
    }

    @Override
    public JdbcLiteralFormatter getJdbcLiteralFormatter() {
      return jdbcLiteralFormatter;
    }

    @Override
    public <X> BasicType<X> resolveIndicatedType(JdbcTypeIndicators indicators, JavaType<X> domainJtd) {
      // TODO: maybe fallback to some encoding by default if the DB doesn't support arrays natively?
      //  also, maybe move that logic into the ArrayJdbcType
      //noinspection unchecked
      return (BasicType<X>) this;
    }

  }

}
