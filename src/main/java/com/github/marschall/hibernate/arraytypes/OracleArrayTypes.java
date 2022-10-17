package com.github.marschall.hibernate.arraytypes;

import org.hibernate.dialect.OracleArrayJdbcType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;

final class OracleArrayTypes {

  private OracleArrayTypes() {
    throw new AssertionError("not instantiable");
  }


  static final class OraclePrimitiveArrayType extends OracleArrayJdbcType {

    OraclePrimitiveArrayType(String typeName, JdbcType elementJdbcType) {
      super(typeName, elementJdbcType);
    }

    @Override
    public <X> ValueBinder<X> getBinder(JavaType<X> javaTypeDescriptor) {
      // TODO Auto-generated method stub
      return super.getBinder(javaTypeDescriptor);
    }
    
  }
  
}
