package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.BindableType;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.query.sqm.SqmExpressible;
import org.hibernate.type.BasicArrayType;
import org.hibernate.type.JavaObjectType;
import org.hibernate.type.descriptor.java.ArrayJavaType;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.hibernate.type.descriptor.jdbc.ArrayJdbcType;
import org.hibernate.type.descriptor.jdbc.IntegerJdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.internal.BasicTypeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.github.marschall.hibernate.arraytypes.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
@Rollback
abstract class AbstractArrayTests {

  @PersistenceContext
  private EntityManager entityManager;

  @BeforeEach
  void setUp() {
    for (int i = 0; i < 10; i++) {
      User user = new User();
      user.setId(i);
      this.entityManager.persist(user);
    }
  }

  @Test
  void bindParameterAnyInteger() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.*"
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", ArrayTypes.newIntegerArrayParameter(1, 3, 5, 7, 9))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Disabled
  @Test
  void bindParameterAnyIntegerJpql() {
    List<User> users = this.entityManager.createQuery(
        "SELECT u"
            + " FROM user_table u"
            + " WHERE u.id IN(:userids)"
            + " ORDER BY u.id", User.class)
        .setParameter("userids", ArrayTypes.newIntegerArrayParameter(1, 3, 5, 7, 9))
        .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyLong() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.*"
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", ArrayTypes.newLongArrayParameter(1L, 3L, 5L, 7L, 9L))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyBigDecimal() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", ArrayTypes.newBigDecimalArrayParameter(
                    BigDecimal.valueOf(1L), BigDecimal.valueOf(3L), BigDecimal.valueOf(5L), BigDecimal.valueOf(7L), BigDecimal.valueOf(9L)))
            .getResultList();
    assertEquals(5, users.size());
  }

//  @Test
//  void bindParameterAnyInteger() {
//    List<User> users = this.entityManager.createNativeQuery(
//                    "SELECT u.* "
//                    + " FROM user_table u"
//                    + " WHERE u.id = ANY(:userids)"
//                    + " ORDER BY u.id", User.class)
//            .setParameter("userids", ArrayType.newParameter("INTEGER", 1, 3, 5, 7, 9))
//            .getResultList();
//    assertEquals(5, users.size());
//  }

}
