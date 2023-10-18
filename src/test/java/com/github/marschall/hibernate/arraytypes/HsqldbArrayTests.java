package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.github.marschall.hibernate.arraytypes.configuration.HsqldbConfiguration;
import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateConfiguration;
import com.github.marschall.hibernate.arraytypes.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
@Rollback
@TestPropertySource(properties = "persistence-unit-name=hsqldb-batched")
@SpringJUnitConfig({HsqldbConfiguration.class, SpringHibernateConfiguration.class})
class HsqldbArrayTests {

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
    List<User> users = this.entityManager.createNativeQuery("""
            SELECT u.*
              FROM user_table u
             WHERE u.id IN(UNNEST(:userids))
             ORDER BY u.id
             """, User.class)
            .setParameter("userids", ArrayTypes.newIntegerArrayParameter(1, 3, 5, 7, 9))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyLong() {
    List<User> users = this.entityManager.createNativeQuery("""
            SELECT u.*
             FROM user_table u
             WHERE u.id IN(UNNEST(:userids))
             ORDER BY u.id
             """, User.class)
            .setParameter("userids", ArrayTypes.newLongArrayParameter(1L, 3L, 5L, 7L, 9L))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test  void bindParameterAnyBigDecimal() {
    List<User> users = this.entityManager.createNativeQuery("""
            SELECT u.*
              FROM user_table u
             WHERE u.id IN(UNNEST(:userids))
             ORDER BY u.id
             """, User.class)
            .setParameter("userids", ArrayTypes.newBigDecimalArrayParameter(
                    BigDecimal.valueOf(1L), BigDecimal.valueOf(3L), BigDecimal.valueOf(5L), BigDecimal.valueOf(7L), BigDecimal.valueOf(9L)))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyString() {
    List<User> users = this.entityManager.createNativeQuery("""
            SELECT u.*
             FROM user_table u
             WHERE u.login IN(UNNEST(:logins))
             ORDER BY u.id
             """, User.class)
            .setParameter("logins", ArrayTypes.newLongArrayParameter("login1", "login3", "login5", "login7", "login9"))
            .getResultList();
    assertEquals(5, users.size());
  }

}