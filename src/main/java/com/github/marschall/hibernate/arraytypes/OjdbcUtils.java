package com.github.marschall.hibernate.arraytypes;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import oracle.jdbc.OracleConnection;

final class OjdbcUtils {

  private OjdbcUtils() {
    throw new AssertionError("not instantiable");
  }

  static void nullSafeSet(PreparedStatement st, Object value, int index, String oracleArrayTypeName,
          SharedSessionContractImplementor session)
          throws SQLException {

    if (value == null) {
      st.setNull(index, Types.ARRAY);
      return;
    }

    Connection connection = session.getJdbcConnectionAccess().obtainConnection();
    try {
      OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
      Array array = oracleConnection.createOracleArray(oracleArrayTypeName, value);
      st.setArray(index, array);
    } finally {
      session.getJdbcConnectionAccess().releaseConnection(connection);
    }
  }

}
