-- Create schema
CREATE SCHEMA thule;

-- Create user
CREATE USER 'thule'@'%' IDENTIFIED BY 'thule';

-- Grants
GRANT ALL ON thule.* TO 'thule'@'%';