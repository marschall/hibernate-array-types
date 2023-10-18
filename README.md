Hibernate Array Types [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/hibernate-array-types/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/hibernate-array-types) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/hibernate-array-types.svg)](https://www.javadoc.io/doc/com.github.marschall/hibernate-array-types)
=====================

User types for binding IN lists to arrays in Hibernate 5.

```xml
<dependency>
  <groupId>com.github.marschall</groupId>
  <artifactId>hibernate-array-types</artifactId>
  <version>1.0.1</version>
</dependency>
```


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
* the API for Hibernate 6 is quite different


H2 / PostgreSQL
---------------

```java
entityManager.createNativeQuery("""
                    SELECT u.*
                      FROM user_table u
                     WHERE u.id = ANY(:userids)
                     ORDER BY u.id
                    """, User.class)
              // you can leave out the "INTEGER" parameter and let ArrayType figure out the actual type
             .setParameter("userids", ArrayType.newParameter("INTEGER", 1, 3, 5, 7, 9))
             .getResultList()
```

Additionally PostgreSQL supports arrays of primitive types through the `PgIntArrayType` and `PgLongArrayType` types.


Oracle
------

```java
entityManager.createNativeQuery("""
                    SELECT u.*
                      FROM user_table u
                     WHERE u.id = ANY(SELECT column_value FROM TABLE(:userids))
                     ORDER BY u.id
                    """, User.class)
             .setParameter("userids", OracleIntArrayType.newParameter("USER_ID_TYPE", 1, 3, 5, 7, 9))
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
              // you can leave out the "INTEGER" parameter and let ArrayType figure out the actual type
             .setParameter("userids", ArrayType.newParameter("INTEGER", 1, 3, 5, 7, 9))
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
