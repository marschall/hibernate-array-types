package com.github.marschall.hibernate.arraytypes.usertype;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class OracleLongArrayType extends AbstractOracleArrayType {

  public OracleLongArrayType(String typeName) {
    super(typeName);
  }

  @Override
  public Class returnedClass() {
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

}
