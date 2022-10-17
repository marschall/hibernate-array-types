package com.github.marschall.hibernate.arraytypes;

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;

final class PgLongArrayValueBinder extends AbstractPgPrimitiveArrayValueBinder<long[]> {
  
  static final ValueBinder<long[]> INSTANCE = new PgLongArrayValueBinder();

  private PgLongArrayValueBinder() {
    super(IntegerJdbcType.INSTANCE, IntegerJavaType.INSTANCE);
  }

}
