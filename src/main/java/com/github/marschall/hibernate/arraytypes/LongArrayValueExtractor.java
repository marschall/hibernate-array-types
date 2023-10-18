package com.github.marschall.hibernate.arraytypes;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;

final class LongArrayValueExtractor implements ValueExtractor<long[]> {

  static final ValueExtractor<long[]> INSTANCE = new LongArrayValueExtractor();

  private LongArrayValueExtractor() {
    super();
  }

  @Override
  public long[] extract(ResultSet rs, int paramIndex, WrapperOptions options) throws SQLException {
    java.sql.Array array = rs.getArray(paramIndex);
    try {
      return convertToValue(array);
    } finally {
      array.free();
    }
  }

  private long[] convertToValue(Array sql) throws SQLException {
    Object javaArray = sql.getArray();
    if (javaArray == null) {
      return null;
    }
    if (javaArray instanceof long[]) {
      return (long[]) javaArray;
    }
    return toIntArray((Object[]) javaArray);
  }

  private long[] toIntArray(Object[] javaArray) {
    long[] result = new long[javaArray.length];
    for (int i = 0; i < javaArray.length; i++) {
      result[i] = (long) javaArray[i];
    }
    return result;
  }

  @Override
  public long[] extract(CallableStatement statement, int paramIndex, WrapperOptions options) throws SQLException {
    java.sql.Array array = statement.getArray(paramIndex);
    try {
      return convertToValue(array);
    } finally {
      array.free();
    }
  }

  @Override
  public long[] extract(CallableStatement statement, String paramName, WrapperOptions options) throws SQLException {
    java.sql.Array array = statement.getArray(paramName);
    try {
      return convertToValue(array);
    } finally {
      array.free();
    }
  }

}
