package com.github.marschall.hibernate.arraytypes;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;

public final class OracleLongArrayType extends AbstractOracleArrayType {

  public OracleLongArrayType(String typeName) {
    super(typeName);
  }

  @Override
  public Class<?> returnedClass() {
    return long[].class;
  }

  @Override
  public boolean equals(Object x, Object y) {
    return Arrays.equals((long[]) x, (long[]) y);
  }

  @Override
  public int hashCode(Object x) {
    return Arrays.hashCode((long[]) x);
  }

  @Override
  public Object deepCopy(Object value) {
    if (value == null) {
      return null;
    }
    return ((long[]) value).clone();
  }

  @Override
  public Object replace(Object original, Object target, Object owner) {
    System.arraycopy(original, 0, target, 0, Array.getLength(original));
    return target;
  }

  public static Type newType(String typeName) {
    return new CustomType(new OracleLongArrayType(typeName));
  }

  public static TypedParameterValue newParameter(String typeName, long... values) {
    return new TypedParameterValue(newType(typeName), values);
  }

}
