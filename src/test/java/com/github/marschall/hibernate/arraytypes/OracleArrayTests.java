package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.marschall.hibernate.arraytypes.configuration.OracleConfiguration;
import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateConfiguration;
import com.github.marschall.hibernate.arraytypes.entity.User;

@Transactional
@Rollback
@TestPropertySource(properties = "persistence-unit-name=oracle-batched")
@SpringJUnitConfig({OracleConfiguration.class, SpringHibernateConfiguration.class})
class OracleArrayTests {


  private static final String ARRAY_TYPE_NAME = "TEST_ARRAY_TYPE";

  private static final Type ARRAY_TYPE = OracleIntArrayType.newType(ARRAY_TYPE_NAME);

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
  void bindParameterAny() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                            + "FROM user_table u "
                            + "WHERE u.id = ANY(SELECT column_value FROM TABLE(:userids))"
                            + "ORDER BY u.id", User.class)
            .setParameter("userids", OracleIntArrayType.newParameter(ARRAY_TYPE_NAME, 1, 3, 5, 7, 9))
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
            .setParameter("userids", new TypedParameterValue(ARRAY_TYPE, new int[] {1, 3, 5, 7, 9}))
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
        .setParameter("userids", OracleObjectArrayType.newParameter(ARRAY_TYPE_NAME, 1, 3, 5, 7, 9))
        .getResultList();
    assertEquals(5, users.size());
  }

}
