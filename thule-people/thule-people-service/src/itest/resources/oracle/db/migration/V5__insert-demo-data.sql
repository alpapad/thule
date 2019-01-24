ALTER SESSION SET CURRENT_SCHEMA = THULE;

-- States

-- Actions

-- Addresses
INSERT INTO addresses(address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, created_at, created_by, updated_at, updated_by)
    SELECT 'HOME', 'No. 1', 'Park lane', 'London', 'Middlesex', countries.id, 'ABC 123', states.id, 1, CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';
INSERT INTO addresses(address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, created_at, created_by, updated_at, updated_by)
    SELECT 'WORK', '55', 'Picadilly', 'London', 'Middlesex', countries.id, 'DEF 456', states.id, 1, CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';

-- Countries

-- State_actions

-- People
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, created_at, created_by, updated_at, updated_by)
    SELECT TO_DATE('1970-07-29', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'scarlett@serin-consultancy.co.uk', 'Ruby', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Scarlett', 'scarlett', null, states.id, 'Miss', 'scarlett', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, created_at, created_by, updated_at, updated_by)
    SELECT TO_DATE('1971-03-15', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'plum@serin-consultancy.co.uk', 'Ben', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Plum', 'plum', null, states.id, 'Professor', 'plum', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, created_at, created_by, updated_at, updated_by)
    SELECT TO_DATE('1971-10-15', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'mustard@serin-consultancy.co.uk', 'Dijon', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Mustard', 'mustard', null, states.id, 'Colonel', 'mustard', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, created_at, created_by, updated_at, updated_by)
    SELECT TO_DATE('1971-11-15', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'peacock@serin-consultancy.co.uk', 'Fly', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Peacock', 'peacock', null, states.id, 'Mrs', 'peacock', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, created_at, created_by, updated_at, updated_by)
    SELECT TO_DATE('1971-12-16', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'white@serin-consultancy.co.uk', 'China', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'White', 'white', null, states.id, 'Mrs', 'white', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, created_at, created_by, updated_at, updated_by)
    SELECT TO_DATE('1971-01-12', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'green@serin-consultancy.co.uk', 'Verde', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Green', 'green', null, states.id, 'Reverend', 'green', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser'
    FROM states WHERE states.code = 'PERSON_ENABLED';

-- Roles

-- people_roles
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'scarlett' AND roles.code = 'ROLE_ADMINISTRATOR';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'scarlett' AND roles.code = 'ROLE_MANAGER';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'scarlett' AND roles.code = 'ROLE_CLERK';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'plum' AND roles.code = 'ROLE_ADMINISTRATOR';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'mustard' AND roles.code = 'ROLE_MANAGER';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'peacock' AND roles.code = 'ROLE_ADMINISTRATOR';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'peacock' AND roles.code = 'ROLE_CLERK';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'white' AND roles.code = 'ROLE_ADMINISTRATOR';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'white' AND roles.code = 'ROLE_MANAGER';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'green' AND roles.code = 'ROLE_MANAGER';

