package com.github.marschall.hibernate.arraytypes;

import org.hibernate.type.BasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.LongPrimitiveArrayJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;

final class LongArrayType extends PrimitiveArrayType<long[], Long> {

  LongArrayType(BasicType<Long> baseDescriptor, JdbcType arrayJdbcType, ValueBinder<long[]> jdbcValueBinder) {
    super(baseDescriptor, LongPrimitiveArrayJavaType.INSTANCE, arrayJdbcType, jdbcValueBinder, LongArrayValueExtractor.INSTANCE);
  }

  @Override
  public String getName() {
    return "long[]";
  }

}