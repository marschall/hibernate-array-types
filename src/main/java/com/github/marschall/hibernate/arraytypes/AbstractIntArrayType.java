package com.github.marschall.hibernate.arraytypes;

import java.util.Arrays;

/**
 * Abstract base class for {@code int[]} types.
 */
abstract class AbstractIntArrayType extends AbstractArrayType {

  @Override
  public Class<?> returnedClass() {
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

}
