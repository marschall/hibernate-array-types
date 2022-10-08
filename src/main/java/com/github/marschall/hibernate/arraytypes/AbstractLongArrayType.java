package com.github.marschall.hibernate.arraytypes;

import java.util.Arrays;

/**
 * Abstract base class for {@code long[]} types.
 */
abstract class AbstractLongArrayType extends AbstractArrayType<long[]> {

  @Override
  public Class<long[]> returnedClass() {
    return long[].class;
  }

  @Override
  public boolean equals(long[] x, long[] y) {
    return Arrays.equals(x, y);
  }

  @Override
  public int hashCode(long[] x) {
    return Arrays.hashCode(x);
  }

  @Override
  public long[] deepCopy(long[] value) {
    if (value == null) {
      return null;
    }
    return value.clone();
  }

}
