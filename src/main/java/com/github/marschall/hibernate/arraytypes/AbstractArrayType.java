package com.github.marschall.hibernate.arraytypes;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * Abstract base class for all array types.
 */
abstract class AbstractArrayType implements UserType {

  private static final int[] SQL_TYPES = new int[] {Types.ARRAY};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names,
          SharedSessionContractImplementor session, Object owner)
                  throws SQLException {
    java.sql.Array array = rs.getArray(names[0]);
    if (array != null) {
      Object value = array.getArray();
      array.free();
      return value;
    } else {
      return null;
    }
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) {
    System.arraycopy(original, 0, target, 0, java.lang.reflect.Array.getLength(original));
    return target;
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
