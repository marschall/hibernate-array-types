package com.github.marschall.hibernate.arraytypes;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;

/**
 * Abstract base class for all array types.
 */
abstract class AbstractArrayType<J> implements UserType<J> {

  @Override
  public int getSqlType() {
    return SqlTypes.ARRAY;
  }

  @Override
  public J nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
      throws SQLException {
    java.sql.Array array = rs.getArray(position);
    if (array != null) {
      Object value = array.getArray();
      array.free();
      return returnedClass().cast(value);
    } else {
      return null;
    }
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public J replace(J original, J target, Object owner) {
    System.arraycopy(original, 0, target, 0, java.lang.reflect.Array.getLength(original));
    return target;
  }

  @Override
  public Serializable disassemble(J value) {
    return (Serializable) this.deepCopy(value);
  }

  @Override
  public J assemble(Serializable cached, Object owner) {
    return this.deepCopy((J) cached);
  }

}
