package com.github.marschall.hibernate.arraytypes;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class OracleObjectArrayType extends AbstractOracleArrayType {

  public OracleObjectArrayType(String typeName) {
    super(typeName);
  }

  @Override
  public Class returnedClass() {
    return Object[].class;
  }

  @Override
  public boolean equals(Object x, Object y) {
    return Arrays.equals((Object[]) x, (Object[]) y);
  }

  @Override
  public int hashCode(Object x) {
    return Arrays.hashCode((Object[]) x);
  }

  @Override
  public Object deepCopy(Object value) {
    if (value == null) {
      return null;
    }
    return ((Object[]) value).clone();
  }

  @Override
  public Object replace(Object original, Object target, Object owner) {
    System.arraycopy(original, 0, target, 0, Array.getLength(original));
    return target;
  }

}
