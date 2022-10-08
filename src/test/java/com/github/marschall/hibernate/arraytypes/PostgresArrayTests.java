package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.marschall.hibernate.arraytypes.configuration.PostgresConfiguration;
import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateConfiguration;
import com.github.marschall.hibernate.arraytypes.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@TestPropertySource(properties = "persistence-unit-name=postgres-batched")
@SpringJUnitConfig({PostgresConfiguration.class, SpringHibernateConfiguration.class})
class PostgresArrayTests extends AbstractArrayTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  void bindParameterAnyInt() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", PgIntArrayType.newParameter(1, 3, 5, 7, 9))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyLong() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                    + " FROM user_table u"
                    + " WHERE u.id = ANY(:userids)"
                    + " ORDER BY u.id", User.class)
            .setParameter("userids", PgLongArrayType.newParameter(1L, 3L, 5L, 7L, 9L))
            .getResultList();
    assertEquals(5, users.size());
  }

}
