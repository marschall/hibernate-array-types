package com.github.marschall.hibernate.arraytypes.usertype;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class OracleIntArrayType extends AbstractOracleArrayType {

  public OracleIntArrayType(String typeName) {
    super(typeName);
  }

  @Override
  public Class returnedClass() {
    return int[].class;
  }

  @Override
  public boolean equals(Object x, Object y) {
    return Arrays.equals((int[]) x, (int[]) y);
  }

  @Override
  public int hashCode(Object x) {
    return Arrays.hashCode((int[]) x);
  }

  @Override
  public Object deepCopy(Object value) {
    if (value == null) {
      return null;
    }
    return ((int[]) value).clone();
  }

  @Override
  public Object replace(Object original, Object target, Object owner) {
    System.arraycopy(original, 0, target, 0, Array.getLength(original));
    return target;
  }

}
