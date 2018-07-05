-- Drop flyway table to force migrations to start afresh
DROP TABLE IF EXISTS flyway_schema_history CASCADE;

-- Drop schema
DROP SCHEMA IF EXISTS thule;

-- Drop user
DELETE FROM user WHERE user='thule';
DELETE FROM db WHERE user='thule';

-- FLUSH PRIVILEGES required to overcome bug MySql https://bugs.mysql.com/bug.php?id=28331
FLUSH PRIVILEGES;