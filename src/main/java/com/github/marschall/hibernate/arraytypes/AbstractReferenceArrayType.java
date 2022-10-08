package com.github.marschall.hibernate.arraytypes;

import java.util.Arrays;

/**
 * Abstract base class for arrays of reference types.
 */
abstract class AbstractReferenceArrayType extends AbstractArrayType<Object[]> {

  @Override
  public Class<Object[]> returnedClass() {
    return Object[].class;
  }

  @Override
  public boolean equals(Object[] x, Object[] y) {
    return Arrays.equals(x, y);
  }

  @Override
  public int hashCode(Object[] x) {
    return Arrays.hashCode(x);
  }

  @Override
  public Object[] deepCopy(Object[] value) {
    if (value == null) {
      return null;
    }
    return value.clone();
  }

}
