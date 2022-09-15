package com.github.marschall.hibernate.arraytypes;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code int[]} to an Oracle array.
 */
public final class OracleIntArrayType extends AbstractOracleArrayType {

  /**
   * Constructs a new OracleIntArrayType.
   * 
   * @param oracleArrayTypeName the name of the oracle array type to use to bind the array
   */
  public OracleIntArrayType(String oracleArrayTypeName) {
    super(oracleArrayTypeName);
  }

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
    return new CustomType(new OracleIntArrayType(oracleArrayTypeName));
  }

  public static TypedParameterValue newParameter(String typeName, int... values) {
    return new TypedParameterValue(newType(typeName), values);
  }

}
