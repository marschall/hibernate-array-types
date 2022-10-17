package com.github.marschall.hibernate.arraytypes;

import org.hibernate.dialect.OracleArrayJdbcType;
import org.hibernate.query.BindableType;
import org.hibernate.type.BasicArrayType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.ArrayJavaType;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.internal.BasicTypeImpl;

public final class OracleArrayTypes {

  private OracleArrayTypes() {
    throw new AssertionError("not instantiable");
  }

  public static BindableType<Integer[]> newIntegerArrayType(String typeName) {
    return new BasicArrayType<>(
        new BasicTypeImpl<>(IntegerJavaType.INSTANCE, IntegerJdbcType.INSTANCE),
        new OracleArrayJdbcType(typeName, IntegerJdbcType.INSTANCE),
        new ArrayJavaType<>(IntegerJavaType.INSTANCE));
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
