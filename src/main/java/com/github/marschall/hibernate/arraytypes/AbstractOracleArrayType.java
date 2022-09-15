package com.github.marschall.hibernate.arraytypes;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import oracle.jdbc.OracleConnection;

/**
 * Abstract base class for {@link UserType}s that bind Java arrays to Oracle arrays.
 * 
 * @see OracleConnection#createOracleArray(String, Object)
 */
abstract class AbstractOracleArrayType implements UserType {

  private static final int[] SQL_TYPES = new int[] {Types.ARRAY};

  private final String oracleArrayTypeName;

  AbstractOracleArrayType(String oracleArrayTypeName) {
    Objects.requireNonNull(oracleArrayTypeName, "oracleArrayTypeName");
    this.oracleArrayTypeName = oracleArrayTypeName;
  }

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names,
          SharedSessionContractImplementor session, Object owner)
          throws SQLException {
    return rs.getArray(names[0]);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index,
          SharedSessionContractImplementor session)
          throws SQLException {

    if (value == null) {
      st.setNull(index, Types.ARRAY);
      return;
    }

    Connection connection = session.getJdbcConnectionAccess().obtainConnection();
    try {
      OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
      Array array = oracleConnection.createOracleArray(this.oracleArrayTypeName, value);
      st.setArray(index, array);
    } finally {
      session.getJdbcConnectionAccess().releaseConnection(connection);
    }
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Serializable disassemble(Object value) {
    return (Serializable) this.deepCopy(value);
  }

  @Override
  public Object assemble(Serializable cached, Object owner) {
    return this.deepCopy(cached);
  }

}
