ALTER SESSION SET CURRENT_SCHEMA = THULE;

-- Create tables
BEGIN EXECUTE IMMEDIATE 'DROP TABLE actions CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE actions(id INTEGER NOT NULL, code VARCHAR(30) NOT NULL, description VARCHAR(100) NOT NULL, next_state_id INTEGER, version INTEGER NOT NULL, updated_by VARCHAR(100) NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE addresses CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE addresses(id INTEGER NOT NULL, address_line1 VARCHAR(30) NOT NULL, address_line2 VARCHAR(30), address_type VARCHAR(10) NOT NULL, town VARCHAR(30) NOT NULL, county VARCHAR(30) NOT NULL, country_id INTEGER NOT NULL, post_code VARCHAR(9) NOT NULL, state_id INTEGER NOT NULL, version INTEGER NOT NULL, updated_by VARCHAR(100) NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE countries CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE countries(id INTEGER NOT NULL, iso_code_three_digit VARCHAR(3) NOT NULL UNIQUE, iso_code_two_digit VARCHAR(2) NOT NULL UNIQUE, iso_name VARCHAR(100) NOT NULL, iso_number VARCHAR(3) NOT NULL UNIQUE, version INTEGER NOT NULL, updated_by VARCHAR(100) NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE people CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE people(id INTEGER NOT NULL, date_of_birth DATE NOT NULL, date_of_expiry DATE NOT NULL, date_of_password_expiry DATE NOT NULL, email_address VARCHAR(100), first_name VARCHAR(30) NOT NULL, home_address_id INTEGER, last_name VARCHAR(30) NOT NULL, password VARCHAR(100) NOT NULL, second_name VARCHAR(30), state_id INTEGER NOT NULL, title VARCHAR(30), user_id VARCHAR(100) NOT NULL UNIQUE, work_address_id INTEGER, version INTEGER NOT NULL, updated_by VARCHAR(100) NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE people_roles CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE people_roles(person_id INTEGER NOT NULL, role_id INTEGER NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE photographs CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE photographs(id INTEGER NOT NULL, hash VARCHAR(255) NOT NULL, photo BLOB NOT NULL, person_id INTEGER NOT NULL, positin INTEGER NOT NULL, version INTEGER NOT NULL, updated_by VARCHAR(100) NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE roles CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE roles(id INTEGER NOT NULL, code VARCHAR(30) NOT NULL UNIQUE, description VARCHAR(30) NOT NULL, version INTEGER NOT NULL, updated_by VARCHAR(100) NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE state_actions CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE state_actions(state_id INTEGER NOT NULL, action_id INTEGER NOT NULL);


BEGIN EXECUTE IMMEDIATE 'DROP TABLE states CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE TABLE states(id INTEGER NOT NULL, code VARCHAR(30) NOT NULL, description VARCHAR(100) NOT NULL, version INTEGER NOT NULL, updated_by VARCHAR(100) NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);


-- Create primary key constraints
ALTER TABLE actions ADD CONSTRAINT actions_pk PRIMARY KEY (id);
ALTER TABLE addresses ADD CONSTRAINT address_pk PRIMARY KEY (id);
ALTER TABLE countries ADD CONSTRAINT countries_pk PRIMARY KEY (id);
ALTER TABLE people ADD CONSTRAINT people_pk PRIMARY KEY (id);
ALTER TABLE people_roles ADD CONSTRAINT people_roles_pk PRIMARY KEY (person_id, role_id);
ALTER TABLE photographs ADD CONSTRAINT photographs_pk PRIMARY KEY (id);
ALTER TABLE roles ADD CONSTRAINT roles_pk PRIMARY KEY (id);
ALTER TABLE state_actions ADD CONSTRAINT state_actions_pk PRIMARY KEY (state_id, action_id);
ALTER TABLE states ADD CONSTRAINT states_pk PRIMARY KEY (id);

-- Create foreign key constraints
ALTER TABLE addresses ADD CONSTRAINT state_id02_fk FOREIGN KEY (state_id) REFERENCES states (id);
ALTER TABLE addresses ADD CONSTRAINT country_id01_fk FOREIGN KEY (country_id) REFERENCES countries (id);
ALTER TABLE people ADD CONSTRAINT state_id01_fk FOREIGN KEY (state_id) REFERENCES states (id);
ALTER TABLE people_roles ADD CONSTRAINT person_id01_fk FOREIGN KEY (person_id) REFERENCES people (id);
ALTER TABLE people_roles ADD CONSTRAINT role_id01_fk FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE photographs ADD CONSTRAINT person_id03_fk FOREIGN KEY (person_id) REFERENCES people (id);
ALTER TABLE state_actions ADD CONSTRAINT state_id03_fk FOREIGN KEY (state_id) REFERENCES states (id);
ALTER TABLE state_actions ADD CONSTRAINT actions_id01_fk FOREIGN KEY (action_id) REFERENCES actions (id);


-- Create sequences
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE uid_sequence'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
CREATE SEQUENCE uid_sequence START WITH 2000 INCREMENT BY 1 NOMAXVALUE NOCYCLE CACHE 20;
