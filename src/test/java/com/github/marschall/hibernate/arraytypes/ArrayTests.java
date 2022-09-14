package com.github.marschall.hibernate.arraytypes;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.CustomType;
import org.junit.jupiter.api.Test;

class ArrayTests {

  private EntityManager entityManager;

  @Test
  void bindParameterAny() {
    List<User> users = this.entityManager
            .createQuery(
                "SELECT u " +
                "FROM User u " +
                "WHERE userid = ANY(SELECT column_value FROM TABLE(:userids))", User.class)
            .setParameter(
                    "arrayValues",
                    new TypedParameterValue(
                        new CustomType(new OracleObjectArrayType("test_array_type")),
                        new int[]{2, 3}
                    )
                )
//            .unwrap(org.hibernate.query.Query.class)
            .getResultList();
  }

  @Test
  void bindParameterIn() {
    List<User> users = this.entityManager
            .createQuery(
                    "SELECT u " +
                            "FROM User u " +
                            "WHERE userid IN(SELECT column_value FROM TABLE(:userids))", User.class)
            .setParameter(
                    "arrayValues",
                    new TypedParameterValue(
                            new CustomType(new OracleObjectArrayType("test_array_type")),
                            new int[]{2, 3}
                            )
                    )
            .getResultList();
  }

}
