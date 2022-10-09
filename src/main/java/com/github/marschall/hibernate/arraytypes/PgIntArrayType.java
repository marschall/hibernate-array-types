package com.github.marschall.hibernate.arraytypes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code int[]} to a PostgreSQL array.
 */
public final class PgIntArrayType extends AbstractIntArrayType {

  public static final UserType INSTANCE = new PgIntArrayType();

  public static final Type TYPE = new CustomType(INSTANCE);

  /**
   * Private constructor, no need for instantiation.
   */
  private PgIntArrayType() {
    super();
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index,
          SharedSessionContractImplementor session)
          throws SQLException {
    PgjdbcUtils.nullSafeSet(st, value, index, "integer", session);
  }

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   *
   * @param values the Java array of elements to bind
   * @return a TypedParameterValue binding the given value to an array
   */
  public static TypedParameterValue newParameter(int... values) {
    return new TypedParameterValue(TYPE, values);
  }

}
