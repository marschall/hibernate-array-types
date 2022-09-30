package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateOracleConfiguration;
import com.github.marschall.hibernate.arraytypes.entity.User;

@Transactional
@Rollback
@SpringJUnitConfig(SpringHibernateOracleConfiguration.class)
class ArrayTests {

  // https://vladmihalcea.com/bind-custom-hibernate-parameter-type-jpa-query/

  private static final String ARRAY_TYPE = "TEST_ARRAY_TYPE";

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
  void bindParameterAny() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                            + "FROM user_table u "
                            + "WHERE u.id = ANY(SELECT column_value FROM TABLE(:userids))"
                            + "ORDER BY u.id", User.class)
            .setParameter("userids", OracleIntArrayType.newParameter(ARRAY_TYPE, 1, 3, 5, 7, 9))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterIn() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                 + "FROM user_table u "
                 + "WHERE u.id IN(SELECT column_value FROM TABLE(:userids))"
                 + "ORDER BY u.id", User.class)
            .setParameter("userids", OracleIntArrayType.newParameter(ARRAY_TYPE, 1, 3, 5, 7, 9))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterInReferenceType() {
    List<User> users = this.entityManager.createNativeQuery(
                  "SELECT u.* "
                + "FROM user_table u "
                + "WHERE u.id IN(SELECT column_value FROM TABLE(:userids))"
                + "ORDER BY u.id", User.class)
        .setParameter("userids", OracleObjectArrayType.newParameter(ARRAY_TYPE, 1, 3, 5, 7, 9))
        .getResultList();
    assertEquals(5, users.size());
  }

}
