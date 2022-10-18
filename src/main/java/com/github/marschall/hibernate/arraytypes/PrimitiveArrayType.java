package com.github.marschall.hibernate.arraytypes;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.AdjustableBasicType;
import org.hibernate.type.BasicPluralType;
import org.hibernate.type.BasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.java.IntegerPrimitiveArrayJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.LongPrimitiveArrayJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcLiteralFormatter;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;
import org.hibernate.type.descriptor.jdbc.internal.JdbcLiteralFormatterArray;

/**
 * Abstract base class for types of arrays of primitive types.
 *
 * @param <C> the array type
 * @param <E> the array element type
 */
final class PrimitiveArrayType<C, E> extends AbstractSingleColumnStandardBasicType<C>
  implements AdjustableBasicType<C>, BasicPluralType<C, E> {

  private final BasicType<E> baseDescriptor;
  private final ValueBinder<C> jdbcValueBinder;
  private final ValueExtractor<C> jdbcValueExtractor;
  private final JdbcLiteralFormatter<C> jdbcLiteralFormatter;
  private final String name;

  PrimitiveArrayType(BasicType<E> baseDescriptor, String name, JavaType<C> arrayTypeDescriptor,
      JdbcType arrayJdbcType, ValueBinder<C> jdbcValueBinder, ValueExtractor<C> jdbcValueExtractor) {
    super(arrayJdbcType, arrayTypeDescriptor);
    this.baseDescriptor = baseDescriptor;
    this.name = name;
    this.jdbcValueBinder = jdbcValueBinder;
    this.jdbcValueExtractor = jdbcValueExtractor;
    this.jdbcLiteralFormatter = new JdbcLiteralFormatterArray(
        baseDescriptor.getJavaTypeDescriptor(),
        super.getJdbcLiteralFormatter()
        );
  }
  
  static PrimitiveArrayType<int[], Integer> newPrimitiveIntArrayType(BasicType<Integer> baseDescriptor, JdbcType arrayJdbcType, ValueBinder<int[]> jdbcValueBinder) {
    return new PrimitiveArrayType<>(baseDescriptor, "int[]", IntegerPrimitiveArrayJavaType.INSTANCE, arrayJdbcType, jdbcValueBinder, IntArrayValueExtractor.INSTANCE);
  }
  
  static PrimitiveArrayType<long[], Long> newPrimitiveLongArrayType(BasicType<Long> baseDescriptor, JdbcType arrayJdbcType, ValueBinder<long[]> jdbcValueBinder) {
    return new PrimitiveArrayType<>(baseDescriptor, "long[]", LongPrimitiveArrayJavaType.INSTANCE, arrayJdbcType, jdbcValueBinder, LongArrayValueExtractor.INSTANCE);
  }

  @Override
  public String getName() {
    return this.name;
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