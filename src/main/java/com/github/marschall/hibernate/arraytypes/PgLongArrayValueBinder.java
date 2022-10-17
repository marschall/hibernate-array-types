package com.github.marschall.hibernate.arraytypes;

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;

final class PgLongArrayValueBinder extends AbstractPgPrimitiveArrayValueBinder<long[]> {
  
  static final ValueBinder<long[]> INSTANCE = new PgLongArrayValueBinder();

  private PgLongArrayValueBinder() {
    super(BigIntJdbcType.INSTANCE, LongJavaType.INSTANCE);
  }

}
