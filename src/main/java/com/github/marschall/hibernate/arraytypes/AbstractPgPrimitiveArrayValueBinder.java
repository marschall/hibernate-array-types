package com.github.marschall.hibernate.arraytypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.Size;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.postgresql.PGConnection;

/**
 * Abstract base class for {@link ValueBinder}s for arrays of primitive types on PostgreSQL.
 *
 * @param <T> the array type to bind
 */
abstract class AbstractPgPrimitiveArrayValueBinder<T> implements ValueBinder<T> {

  private final JdbcType elementJdbcType;
  private final JavaType<?> elementJavaType;

  protected AbstractPgPrimitiveArrayValueBinder(JdbcType elementJdbcType, JavaType<?> elementJavaType) {
    this.elementJdbcType = elementJdbcType;
    this.elementJavaType = elementJavaType;
  }

  @Override
  public void bind(PreparedStatement st, T value, int index, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    String typeName = getTypeName(session);
    java.sql.Array array = createSqlArray(value, session, typeName);
    st.setObject(index, array);
  }

  private java.sql.Array createSqlArray(T value, SharedSessionContractImplementor session, String typeName)
      throws SQLException {
    PGConnection pgConnection = session.getJdbcCoordinator().getLogicalConnection().getPhysicalConnection().unwrap(PGConnection.class);
    return pgConnection.createArrayOf(typeName, value);
  }

  private String getTypeName(SharedSessionContractImplementor session) {
    Size size = session.getJdbcServices()
        .getDialect()
        .getSizeStrategy()
        .resolveSize(elementJdbcType, elementJavaType, null, null, null);
    String typeName = session.getTypeConfiguration()
        .getDdlTypeRegistry()
        .getDescriptor(elementJdbcType.getDefaultSqlTypeCode())
        .getTypeName(size);
    int cutIndex = typeName.indexOf('(');
    if (cutIndex > 0) {
      // getTypeName for this case required length, etc, parameters.
      // Cut them out and use database defaults.
      typeName = typeName.substring( 0, cutIndex );
    }
    return typeName;
  }

  @Override
  public void bind(CallableStatement st, T value, String name, WrapperOptions options) throws SQLException {
    SharedSessionContractImplementor session = options.getSession();
    String typeName = getTypeName(session);
    java.sql.Array array = createSqlArray(value, session, typeName);
    st.setObject(name, array);
  }

}
