package com.github.marschall.hibernate.arraytypes;

import java.util.Arrays;

/**
 * Abstract base class for arrays of reference types.
 */
abstract class AbstractReferenceArrayType extends AbstractArrayType {

  @Override
  public Class<?> returnedClass() {
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

}
