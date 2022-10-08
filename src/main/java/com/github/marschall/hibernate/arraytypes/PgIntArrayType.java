package com.github.marschall.hibernate.arraytypes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code int[]} to a PostgreSQL array.
 */
public final class PgIntArrayType extends AbstractIntArrayType {

  public static final UserType<int[]> INSTANCE = new PgIntArrayType();

  public static final Type TYPE = new CustomType(INSTANCE);

  /**
   * Private constructor, no need for instantiation.
   */
  private PgIntArrayType() {
    super();
  }
  
  @Override
  public void nullSafeSet(PreparedStatement st, int[] value, int index, SharedSessionContractImplementor session)
      throws SQLException {
    
    PgjdbcUtils.nullSafeSet(st, value, index, "integer", session);
  }

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   *
   * @return a TypedParameterValue binding the given value to an array
   */
  public static TypedParameterValue<int[]> newParameter(int... values) {
    return new TypedParameterValue<>(TYPE, values);
  }

}
