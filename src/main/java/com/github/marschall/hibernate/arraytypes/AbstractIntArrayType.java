package com.github.marschall.hibernate.arraytypes;

import java.util.Arrays;

/**
 * Abstract base class for {@code int[]} types.
 */
abstract class AbstractIntArrayType extends AbstractArrayType<int[]> {

  @Override
  public Class<int[]> returnedClass() {
    return int[].class;
  }

  @Override
  public boolean equals(int[] x, int[] y) {
    return Arrays.equals(x, y);
  }

  @Override
  public int hashCode(int[] x) {
    return Arrays.hashCode(x);
  }

  @Override
  public int[] deepCopy(int[] value) {
    if (value == null) {
      return null;
    }
    return value.clone();
  }

}
