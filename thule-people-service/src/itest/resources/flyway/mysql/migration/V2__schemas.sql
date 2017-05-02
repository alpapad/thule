-- Create schema
DROP SCHEMA IF EXISTS thule;
CREATE SCHEMA thule;

-- Create user
DELETE FROM user WHERE user='thule';
DELETE FROM db WHERE user='thule';
FLUSH PRIVILEGES;
CREATE USER 'thule'@'%' IDENTIFIED BY 'thule';

-- Grants
GRANT ALL ON thule.* TO 'thule'@'%';
