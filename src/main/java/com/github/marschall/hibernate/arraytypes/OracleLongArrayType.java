package com.github.marschall.hibernate.arraytypes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code long[]} to an Oracle array.
 */
public final class OracleLongArrayType extends AbstractLongArrayType {

  private final String oracleArrayTypeName;

  /**
   * Constructs a new OracleLongArrayType.
   *
   * @param oracleArrayTypeName the name of the oracle array type to use to bind the array
   */
  public OracleLongArrayType(String oracleArrayTypeName) {
    this.oracleArrayTypeName = oracleArrayTypeName;
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index,
          SharedSessionContractImplementor session)
          throws SQLException {

    OjdbcUtils.nullSafeSet(st, value, index, this.oracleArrayTypeName, session);
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

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   *
   * @param oracleArrayTypeName  the name of the oracle array type to use to bind the array
   * @return a TypedParameterValue binding the given value to an array of the given type name
   */
  public static TypedParameterValue newParameter(String typeName, long... values) {
    return new TypedParameterValue(newType(typeName), values);
  }

}
