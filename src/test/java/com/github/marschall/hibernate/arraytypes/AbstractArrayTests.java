package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import com.github.marschall.hibernate.arraytypes.entity.User;

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
      user.setLogin("login" + (i + 1));
      this.entityManager.persist(user);
    }
  }

  @Test
  void bindParameterAnyGenericInteger() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.*"
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", ArrayType.newParameter(1, 3, 5, 7, 9))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyGenericLong() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.*"
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", ArrayType.newParameter(1L, 3L, 5L, 7L, 9L))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyGenericBigDecimal() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", ArrayType.newParameter(
                    BigDecimal.valueOf(1L), BigDecimal.valueOf(3L), BigDecimal.valueOf(5L), BigDecimal.valueOf(7L), BigDecimal.valueOf(9L)))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyInteger() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", ArrayType.newParameter("INTEGER", 1, 3, 5, 7, 9))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyString() {
    List<User> users = this.entityManager.createNativeQuery(
        "SELECT u.* "
            + " FROM user_table u"
            + " WHERE u.login = ANY(:logins)"
            + " ORDER BY u.id", User.class)
        .setParameter("logins", ArrayType.newParameter("VARCHAR", "login1", "login3", "login5", "login7", "login9"))
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
