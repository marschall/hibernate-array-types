package com.github.marschall.hibernate.arraytypes;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;

final class IntArrayValueExtractor implements ValueExtractor<int[]> {

  static final ValueExtractor<int[]> INSTANCE = new IntArrayValueExtractor();

  private IntArrayValueExtractor() {
    super();
  }

  @Override
  public int[] extract(ResultSet rs, int paramIndex, WrapperOptions options) throws SQLException {
    java.sql.Array array = rs.getArray(paramIndex);
    try {
      return convertToValue(array);
    } finally {
      array.free();
    }
  }

  private int[] convertToValue(Array sql) throws SQLException {
    Object javaArray = sql.getArray();
    if (javaArray == null) {
      return null;
    }
    if (javaArray instanceof int[]) {
      return (int[]) javaArray;
    }
    return toIntArray((Object[]) javaArray);
  }

  private int[] toIntArray(Object[] javaArray) {
    int[] result = new int[javaArray.length];
    for (int i = 0; i < javaArray.length; i++) {
      result[i] = (int) javaArray[i];
    }
    return result;
  }

  @Override
  public int[] extract(CallableStatement statement, int paramIndex, WrapperOptions options) throws SQLException {
    java.sql.Array array = statement.getArray(paramIndex);
    try {
      return convertToValue(array);
    } finally {
      array.free();
    }
  }

  @Override
  public int[] extract(CallableStatement statement, String paramName, WrapperOptions options) throws SQLException {
    java.sql.Array array = statement.getArray(paramName);
    try {
      return convertToValue(array);
    } finally {
      array.free();
    }
  }

}
