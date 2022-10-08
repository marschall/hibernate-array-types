package com.github.marschall.hibernate.arraytypes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code Object[]} to an Oracle PL/SQL array.
 */
public final class OracleObjectArrayType extends AbstractReferenceArrayType {

  private final String oracleArrayTypeName;

  /**
   * Constructs a new OracleObjectArrayType.
   *
   * @param oracleArrayTypeName the name of the oracle array type to use to bind the array
   */
  public OracleObjectArrayType(String oracleArrayTypeName) {
    this.oracleArrayTypeName = oracleArrayTypeName;
  }
  
  @Override
  public void nullSafeSet(PreparedStatement st, Object[] value, int index, SharedSessionContractImplementor session)
      throws SQLException {
    
    OjdbcUtils.nullSafeSet(st, value, index, this.oracleArrayTypeName, session);
  }


  /**
   * Convenience method that creates an instance of this class adapted as a {@link Type}.
   *
   * @param oracleArrayTypeName the name of the oracle array type to use to bind the array
   * @return an instance of this class adapted as a {@link Type}
   */
  public static Type newType(String oracleArrayTypeName) {
    return new CustomType(new OracleObjectArrayType(oracleArrayTypeName));
  }

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   *
   * @param oracleArrayTypeName the name of the oracle array type to use to bind the array
   * @param values the Java array of elements to bind
   * @param <T> the element type
   * @return a TypedParameterValue binding the given value to an array of the given type name
   */
  @SafeVarargs
  public static <T> TypedParameterValue<Object[]> newParameter(String typeName, T... values) {
    return new TypedParameterValue<>(newType(typeName), values);
  }

}
