package com.github.marschall.hibernate.arraytypes;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code Object[]} to an Oracle array.
 */
public final class OracleObjectArrayType extends AbstractOracleArrayType {

  /**
   * Constructs a new OracleObjectArrayType.
   * 
   * @param oracleArrayTypeName the name of the oracle array type to use to bind the array
   */
  public OracleObjectArrayType(String oracleArrayTypeName) {
    super(oracleArrayTypeName);
  }

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
    return new CustomType(new OracleObjectArrayType(oracleArrayTypeName));
  }

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   * 
   * @param oracleArrayTypeName  the name of the oracle array type to use to bind the array
   * @return a TypedParameterValue binding the given value to an array of the given type name
   */
  public static TypedParameterValue newParameter(String typeName, Object... values) {
    return new TypedParameterValue(newType(typeName), values);
  }

}
