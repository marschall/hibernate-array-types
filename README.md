Hibernate Array Types
=====================

User types for binding IN lists to arrays in Hibernate 6.

Versions 1.x are fore Hibernate 5.x.

Advantages
----------

* reduces the number of hard parses
* improves the effectives of cursor sharing
* works well together with client side statement caching to reduce the number of soft parses
* supports more than 1000 parameters in IN lists in Oracle

Disadvantages
-------------

* requires native queries with non-portable syntax
* only a small number of databases are supported
* works only with Hibernate, not other JPA providers


H2 / PostgreSQL
---------------

```java
entityManager.createNativeQuery("""
                    SELECT u.*
                      FROM user_table u
                     WHERE u.id = ANY(:userids)
                     ORDER BY u.id
                    """, User.class)
             .setParameter("userids", ArrayTypes.newIntegerArrayParameter(1, 3, 5, 7, 9))
             .getResultList()
```

Additionally PostgreSQL supports arrays of primitive types through the `PgArrayTypes` class.


Oracle
------

```java
// we recommend you create a constant for this
BindableType<int[]> intArrayType = OracleArrayTypes.newIntArrayType(ARRAY_TYPE_NAME)

entityManager.createNativeQuery("""
                    SELECT u.*
                      FROM user_table u
                     WHERE u.id = ANY(SELECT column_value FROM TABLE(:userids))
                     ORDER BY u.id
                    """, User.class)
             .setParameter("userids", new TypedParameterValue<>(intArrayType, new int[] {1, 3, 5, 7, 9}))
             .getResultList();
```

You'll have to create a type of the appropriate name, for example like this

```sql
CREATE OR REPLACE TYPE USER_ID_TYPE IS TABLE OF NUMBER;
```

HSQLDB
------

```java
entityManager.createNativeQuery("""
                    SELECT u.*
                      FROM user_table u
                     WHERE u.id IN(UNNEST(:userids))
                     ORDER BY u.id
                    """, User.class)
             .setParameter("userids", ArrayTypes.newIntegerArrayParameter(1, 3, 5, 7, 9))
             .getResultList();
```

Tested Databases
----------------

* H2
* HSQLDB
* Oracle
* PostgreSQL

Limitations
-----------

* Works only with native queries.
* HSQLDB and Oracle require custom SQL syntax.
* Supporting Oracle requires different classes and the creation of custom database types.
* Primitive types are only supported on Oracle and PostgresSQL and require different classes.
* Does not work with the following databases as yet do not support SQL arrays:
  * Derby
  * Firebird
  * MariaDB
  * MySQL
  * SQL Server
