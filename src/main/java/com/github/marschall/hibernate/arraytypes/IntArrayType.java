package com.github.marschall.hibernate.arraytypes;

import org.hibernate.type.BasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.IntegerPrimitiveArrayJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;

final class IntArrayType extends PrimitiveArrayType<int[], Integer> {

  IntArrayType(BasicType<Integer> baseDescriptor, JdbcType arrayJdbcType, ValueBinder<int[]> jdbcValueBinder) {
    super(baseDescriptor, IntegerPrimitiveArrayJavaType.INSTANCE, arrayJdbcType, jdbcValueBinder, IntArrayValueExtractor.INSTANCE);
  }

  @Override
  public String getName() {
    return "int[]";
  }

}