package com.github.marschall.hibernate.arraytypes;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code long[]} to an Oracle array.
 */
public final class OracleLongArrayType extends AbstractOracleArrayType {

  /**
   * Constructs a new OracleLongArrayType.
   * 
   * @param oracleArrayTypeName the name of the oracle array type to use to bind the array
   */
  public OracleLongArrayType(String oracleArrayTypeName) {
    super(oracleArrayTypeName);
  }

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

  @Override
  public Object replace(Object original, Object target, Object owner) {
    System.arraycopy(original, 0, target, 0, Array.getLength(original));
    return target;
  }

  /**
   * Convenience method that creates an instance of this class adapted as a {@link Type}.
   * 
   * @param oracleArrayTypeName  the name of the oracle array type to use to bind the array
   * @return an instance of this class adapted as a {@link Type}
   */
  public static Type newType(String oracleArrayTypeName) {
    return new CustomType(new OracleLongArrayType(oracleArrayTypeName));
  }

  public static TypedParameterValue newParameter(String typeName, long... values) {
    return new TypedParameterValue(newType(typeName), values);
  }

}
