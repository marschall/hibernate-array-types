package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.marschall.hibernate.arraytypes.configuration.PostgresConfiguration;
import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateConfiguration;
import com.github.marschall.hibernate.arraytypes.entity.User;

@Transactional
@Rollback
@TestPropertySource(properties = "persistence-unit-name=postgres-batched")
@SpringJUnitConfig({PostgresConfiguration.class, SpringHibernateConfiguration.class})
class ArrayTests {

  private static final Type GENERIC_ARRAY_TYPE = new CustomType(new ArrayType(null));

  private static final Type INTEGER_ARRAY_TYPE = new CustomType(new ArrayType("INTEGER"));

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
  void bindParameterAnyGeneric() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                    + "FROM user_table u "
                    + "WHERE u.id = ANY(:userids)"
                    + "ORDER BY u.id", User.class)
            .setParameter("userids", new TypedParameterValue(GENERIC_ARRAY_TYPE, new Integer[] {1, 3, 5, 7, 9}))
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterAnyInteger() {
    List<User> users = this.entityManager.createNativeQuery(
                    "SELECT u.* "
                    + "FROM user_table u "
                    + "WHERE u.id = ANY(:userids)"
                    + "ORDER BY u.id", User.class)
            .setParameter("userids", new TypedParameterValue(INTEGER_ARRAY_TYPE, new Integer[] {1, 3, 5, 7, 9}))
            .getResultList();
    assertEquals(5, users.size());
  }

}
