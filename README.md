Hibernate Array Types
=====================

User types for binding IN lists to arrays in Hibernate.

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
* Supporting Oracle requires different classes and the creation of custom types.
* Primtive types are only supported on Oracle and PostgresSQL and requires different classes.
* Does not work with the following databases as yet do not support SQL arrays:
  * Derby
  * Firebird
  * MariaDB
  * MySQL
  * SQL Server
