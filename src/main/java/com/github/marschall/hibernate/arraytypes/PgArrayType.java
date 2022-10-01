package com.github.marschall.hibernate.arraytypes;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.PGConnection;

public final class PgArrayType implements UserType {

  private static final int[] SQL_TYPES = new int[] {Types.ARRAY};
  private final String typeName;

  /**
   *
   * @param typeName the SQL name of the type the elements of the array map to
   *
   * @see Connection#createArrayOf(String, Object[])
   */
  public PgArrayType(String typeName) {
    this.typeName = typeName;
  }

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
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
  public Object nullSafeGet(ResultSet rs, String[] names,
          SharedSessionContractImplementor session, Object owner)
          throws HibernateException, SQLException {

    Array array = rs.getArray(names[0]);
    if (array != null) {
      Object value = array.getArray();
      array.free();
      return value;
    } else {
      return null;
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index,
          SharedSessionContractImplementor session)
          throws HibernateException, SQLException {
    if (value != null) {
      Connection connection = session.getJdbcConnectionAccess().obtainConnection();
      try {
        PGConnection pgConnection = connection.unwrap(PGConnection.class);
        Array array = pgConnection.createArrayOf(this.typeName, value);
        st.setArray(index, array);
      } finally {
        session.getJdbcConnectionAccess().releaseConnection(connection);
      }
    } else {
      st.setNull(index, Types.ARRAY);
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

  @Override
  public Object replace(Object original, Object target, Object owner)
          throws HibernateException {
    System.arraycopy(original, 0, target, 0, java.lang.reflect.Array.getLength(original));
    return target;
  }



}
