package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.hibernate.query.BindableType;
import org.hibernate.query.TypedParameterValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.github.marschall.hibernate.arraytypes.configuration.OracleConfiguration;
import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateConfiguration;
import com.github.marschall.hibernate.arraytypes.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
@Rollback
@TestPropertySource(properties = "persistence-unit-name=oracle-batched")
@SpringJUnitConfig({OracleConfiguration.class, SpringHibernateConfiguration.class})
class OracleArrayTests {


  private static final String ARRAY_TYPE_NAME = "TEST_ARRAY_TYPE";

  private static final BindableType<Integer[]> INTEGER_ARRAY_TYPE = OracleArrayTypes.newIntegerArrayType(ARRAY_TYPE_NAME);

  private static final BindableType<int[]> INT_ARRAY_TYPE = OracleArrayTypes.newIntArrayType(ARRAY_TYPE_NAME);

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
  void referenceParameter() {
    List<User> users = this.entityManager.createNativeQuery("""
            SELECT u.*
              FROM user_table u
             WHERE u.id IN(SELECT column_value FROM TABLE(:userids))
             ORDER BY u.id
            """, User.class)
            .setParameter("userids", new TypedParameterValue<>(INTEGER_ARRAY_TYPE, new Integer[] {1, 3, 5, 7, 9}))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void primitiveParameter() {
    List<User> users = this.entityManager.createNativeQuery("""
            SELECT u.*
              FROM user_table u
             WHERE u.id IN(SELECT column_value FROM TABLE(:userids))
             ORDER BY u.id
            """, User.class)
            .setParameter("userids", new TypedParameterValue<>(INT_ARRAY_TYPE, new int[] {1, 3, 5, 7, 9}))
            .getResultList();
    assertEquals(5, users.size());
  }

}
