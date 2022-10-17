package com.github.marschall.hibernate.arraytypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.WrapperOptions;

import oracle.jdbc.OracleConnection;

/**
 * {@link ValueBinder} for binding arrays of primitive values on Oracle.
 *
 * @param <T> the array type to bind
 */
final class OraclePrimitiveArrayValueBinder<T> implements ValueBinder<T> {

  private final String typeName;

  OraclePrimitiveArrayValueBinder(String typeName) {
    this.typeName = typeName;
  }

  @Override
  public void bind(PreparedStatement st, T value, int index, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    java.sql.Array array = createSqlArray(value, session);
    st.setObject(index, array);
  }

  private java.sql.Array createSqlArray(T value, SharedSessionContractImplementor session)
      throws SQLException {
    OracleConnection oracleConnection = session.getJdbcCoordinator().getLogicalConnection().getPhysicalConnection().unwrap(OracleConnection.class);
    return oracleConnection.createOracleArray(this.typeName, value);
  }

  @Override
  public void bind(CallableStatement st, T value, String name, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    java.sql.Array array = createSqlArray(value, session);
    st.setObject(name, array);
  }

}
