package com.github.marschall.hibernate.arraytypes;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.AdjustableBasicType;
import org.hibernate.type.BasicPluralType;
import org.hibernate.type.BasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.java.JavaType;
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
abstract class PrimitiveArrayType<C, E> extends AbstractSingleColumnStandardBasicType<C>
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