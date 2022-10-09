package com.github.marschall.hibernate.arraytypes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} that binds {@code long[]} to a PostgreSQL array.
 */
public final class PgLongArrayType extends AbstractLongArrayType {

  public static final UserType INSTANCE = new PgLongArrayType();

  public static final Type TYPE = new CustomType(INSTANCE);

  /**
   * Private constructor, no need for instantiation.
   */
  private PgLongArrayType() {
    super();
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index,
          SharedSessionContractImplementor session)
          throws SQLException {
    PgjdbcUtils.nullSafeSet(st, value, index, "bigint", session);
  }

  /**
   * Convenience method that creates an instance of this class wrapped in a {@link TypedParameterValue}
   * so that it can be passed as a bind parameter.
   *
   * @param values the Java array of elements to bind
   * @return a TypedParameterValue binding the given value to an array
   */
  public static TypedParameterValue newParameter(long... values) {
    return new TypedParameterValue(TYPE, values);
  }

}
