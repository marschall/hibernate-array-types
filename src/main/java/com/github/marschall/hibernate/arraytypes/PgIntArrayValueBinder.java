package com.github.marschall.hibernate.arraytypes;

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;

final class PgIntArrayValueBinder extends AbstractPgPrimitiveArrayValueBinder<int[]> {
  
  static final ValueBinder<int[]> INSTANCE = new PgIntArrayValueBinder();

  private PgIntArrayValueBinder() {
    super(IntegerJdbcType.INSTANCE, IntegerJavaType.INSTANCE);
  }

}
