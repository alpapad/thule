-- Drop schema
DROP SCHEMA IF EXISTS ${schema-name};

-- Drop user
DROP USER IF EXISTS '${username}';

-- FLUSH PRIVILEGES required to overcome bug MySql https://bugs.mysql.com/bug.php?id=28331
FLUSH PRIVILEGES;