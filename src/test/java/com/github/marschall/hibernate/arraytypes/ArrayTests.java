package com.github.marschall.hibernate.arraytypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.marschall.hibernate.arraytypes.entity.User;
import com.github.marschall.hibernate.arraytypes.usertype.OracleIntArrayType;

@Transactional
@SpringJUnitConfig
class ArrayTests {

  private static final String ARRAY_TYPE = "test_array_type";

  @Autowired
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
    List<User> users = this.entityManager
            .createQuery(
                "SELECT u "
              + "FROM User u "
              + "WHERE id = ANY(SELECT column_value FROM TABLE(:userids))", User.class)
            .setParameter(
                    "userids",
                    new TypedParameterValue(
                        new CustomType(new OracleIntArrayType(ARRAY_TYPE)),
                        new int[]{1, 3, 5, 7, 9}
                    )
                )
            .getResultList();
    assertEquals(5, users.size());
  }

  @Test
  void bindParameterIn() {
    List<User> users = this.entityManager
            .createQuery(
                    "SELECT u "
                 +  "FROM User u "
                 +  "WHERE id IN(SELECT column_value FROM TABLE(:userids))", User.class)
            .setParameter(
                    "userids",
                    new TypedParameterValue(
                            new CustomType(new OracleIntArrayType(ARRAY_TYPE)),
                            new int[]{1, 3, 5, 7, 9}
                            )
                    )
            .getResultList();
    assertEquals(5, users.size());
  }

}
