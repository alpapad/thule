USE ${schema-name};

-- Create tables
DROP TABLE IF EXISTS state_actions CASCADE;
CREATE TABLE state_actions(state_id BIGINT NOT NULL, action_id BIGINT NOT NULL, PRIMARY KEY (state_id, action_id));

DROP TABLE IF EXISTS actions CASCADE;
CREATE TABLE actions(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, code VARCHAR(30) NOT NULL, description VARCHAR(100) NOT NULL, next_state_id BIGINT, version BIGINT NOT NULL, created_at TIMESTAMP(6), created_by VARCHAR(100) NOT NULL, updated_at TIMESTAMP(6), updated_by VARCHAR(100) NOT NULL);

DROP TABLE IF EXISTS addresses CASCADE;
CREATE TABLE addresses(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, address_line1 VARCHAR(30) NOT NULL, address_line2 VARCHAR(30), address_type VARCHAR(10) NOT NULL, town VARCHAR(30) NOT NULL, county VARCHAR(30) NOT NULL, country_id BIGINT NOT NULL, post_code VARCHAR(9) NOT NULL, state_id BIGINT NOT NULL, version BIGINT NOT NULL, created_at TIMESTAMP(6), created_by VARCHAR(100) NOT NULL, updated_at TIMESTAMP(6), updated_by VARCHAR(100) NOT NULL);

DROP TABLE IF EXISTS countries CASCADE;
CREATE TABLE countries(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, iso_code_three_characters VARCHAR(3) NOT NULL UNIQUE, iso_code_two_characters VARCHAR(2) NOT NULL UNIQUE, iso_name VARCHAR(100) NOT NULL, iso_number VARCHAR(3) NOT NULL UNIQUE, version BIGINT NOT NULL, created_at TIMESTAMP(6), created_by VARCHAR(100) NOT NULL, updated_at TIMESTAMP(6), updated_by VARCHAR(100) NOT NULL);

DROP TABLE IF EXISTS people CASCADE;
CREATE TABLE people(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, date_of_birth DATE NOT NULL, date_of_expiry DATE NOT NULL, date_of_password_expiry DATE NOT NULL, email_address VARCHAR(100), first_name VARCHAR(30) NOT NULL, home_address_id BIGINT, last_name VARCHAR(30) NOT NULL, password VARCHAR(100) NOT NULL, second_name VARCHAR(30), state_id BIGINT NOT NULL, title VARCHAR(30), user_id VARCHAR(100) NOT NULL UNIQUE, work_address_id BIGINT, version BIGINT NOT NULL, created_at TIMESTAMP(6), created_by VARCHAR(100) NOT NULL, updated_at TIMESTAMP(6), updated_by VARCHAR(100) NOT NULL);

DROP TABLE IF EXISTS people_roles CASCADE;
CREATE TABLE people_roles(person_id BIGINT NOT NULL AUTO_INCREMENT, role_id BIGINT NOT NULL, PRIMARY KEY (person_id, role_id));

DROP TABLE IF EXISTS photographs CASCADE;
CREATE TABLE photographs(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, hash VARCHAR(255) NOT NULL, photo LONGBLOB NOT NULL, person_id BIGINT NOT NULL, positin BIGINT NOT NULL, version BIGINT NOT NULL, created_at TIMESTAMP(6), created_by VARCHAR(100) NOT NULL, updated_at TIMESTAMP(6), updated_by VARCHAR(100) NOT NULL);

DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, code VARCHAR(30) NOT NULL UNIQUE, description VARCHAR(30) NOT NULL, version BIGINT NOT NULL, created_at TIMESTAMP(6), created_by VARCHAR(100) NOT NULL, updated_at TIMESTAMP(6), updated_by VARCHAR(100) NOT NULL);

DROP TABLE IF EXISTS states CASCADE;
CREATE TABLE states(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, code VARCHAR(30) NOT NULL, description VARCHAR(100) NOT NULL, version BIGINT NOT NULL, created_at TIMESTAMP(6), created_by VARCHAR(100) NOT NULL, updated_at TIMESTAMP(6), updated_by VARCHAR(100) NOT NULL);

-- Create foreign key constraints
ALTER TABLE addresses ADD CONSTRAINT state_id02_fk FOREIGN KEY (state_id) REFERENCES states (id);
ALTER TABLE addresses ADD CONSTRAINT country_id01_fk FOREIGN KEY (country_id) REFERENCES countries (id);
ALTER TABLE people ADD CONSTRAINT state_id01_fk FOREIGN KEY (state_id) REFERENCES states (id);
ALTER TABLE people_roles ADD CONSTRAINT person_id01_fk FOREIGN KEY (person_id) REFERENCES people (id);
ALTER TABLE people_roles ADD CONSTRAINT role_id01_fk FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE photographs ADD CONSTRAINT person_id03_fk FOREIGN KEY (person_id) REFERENCES people (id);
ALTER TABLE state_actions ADD CONSTRAINT state_id03_fk FOREIGN KEY (state_id) REFERENCES states (id);
ALTER TABLE state_actions ADD CONSTRAINT actions_id01_fk FOREIGN KEY (action_id) REFERENCES actions (id);
