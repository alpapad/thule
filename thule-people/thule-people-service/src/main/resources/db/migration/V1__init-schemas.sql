-- Create user
CREATE USER '${username}'@'%' IDENTIFIED BY '${password}';

-- Grants
GRANT SELECT, INSERT, UPDATE, DELETE ON ${schema-name}.* TO '${username}'@'%';

-- FLUSH PRIVILEGES required to overcome bug MySql https://bugs.mysql.com/bug.php?id=28331
FLUSH PRIVILEGES;