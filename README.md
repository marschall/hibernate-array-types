Hibernate Array Types
=====================

User types for binding IN lists to arrays in Hibernate.

Tested Databases
----------------

* H2
* Oracle
* PostgreSQL

Limitations
-----------

* Works only with native queries.
* Supporting Oracle requires different classes and the creation of custom types.
* Primtive types are only supported on Oracle and PostgresSQL and requires different classes.
