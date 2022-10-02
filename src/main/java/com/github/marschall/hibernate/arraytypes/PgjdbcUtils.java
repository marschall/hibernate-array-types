package com.github.marschall.hibernate.arraytypes;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.postgresql.PGConnection;

/**
 * Utility methods for PostgreSQL JDBC driver.
 */
final class PgjdbcUtils {

  private PgjdbcUtils() {
    throw new AssertionError("not instantiable");
  }

  static void nullSafeSet(PreparedStatement st, Object value, int index, String typeName,
          SharedSessionContractImplementor session)
          throws SQLException {

    if (value == null) {
      st.setNull(index, Types.ARRAY);
      return;
    }

    Connection connection = session.getJdbcConnectionAccess().obtainConnection();
    try {
      PGConnection pgConnection = connection.unwrap(PGConnection.class);
      Array array = pgConnection.createArrayOf(typeName, value);
      st.setArray(index, array);
    } finally {
      session.getJdbcConnectionAccess().releaseConnection(connection);
    }
  }

}
