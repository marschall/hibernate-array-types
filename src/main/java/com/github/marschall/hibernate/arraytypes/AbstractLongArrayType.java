package com.github.marschall.hibernate.arraytypes;

import java.util.Arrays;

/**
 * Abstract base class for {@code long[]} types.
 */
abstract class AbstractLongArrayType extends AbstractArrayType {

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

}
