-- States

-- Actions

-- Addresses
INSERT INTO addresses(address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, updated_by, created_at, updated_at)
    SELECT 'HOME', 'No. 1', 'Park lane', 'London', 'Middlesex', countries.id, 'ABC 123', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';
INSERT INTO addresses(address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, updated_by, created_at, updated_at)
    SELECT 'WORK', '55', 'Picadilly', 'London', 'Middlesex', countries.id, 'DEF 456', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';

-- Countries

-- State_actions

-- People
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT CURRENT_DATE, '2030-12-31', '2030-12-31', 'was@serin-consultancy.co.uk', 'WebSphere', 'Server', 'websphere', 'Application', states.id, 'Mr', 'websphere', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT '1970-07-29', '2030-12-31', '2030-12-31', 'scarlett@serin-consultancy.co.uk', 'Ruby', 'Scarlett', 'scarlett', null, states.id, 'Miss', 'scarlett', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT '1971-03-15', '2030-12-31', '2030-12-31', 'plum@serin-consultancy.co.uk', 'Ben', 'Plum', 'plum', null, states.id, 'Professor', 'plum', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT '1971-10-15', '2030-12-31', '2030-12-31', 'mustard@serin-consultancy.co.uk', 'Dijon', 'Mustard', 'mustard', null, states.id, 'Colonel', 'mustard', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT '1971-11-15', '2030-12-31', '2030-12-31', 'peacock@serin-consultancy.co.uk', 'Fly', 'Peacock', 'peacock', null, states.id, 'Mrs', 'peacock', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT '1971-12-16', '2030-12-31', '2030-12-31', 'white@serin-consultancy.co.uk', 'China', 'White', 'white', null, states.id, 'Mrs', 'white', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT '1971-01-12', '2030-12-31', '2030-12-31', 'green@serin-consultancy.co.uk', 'Verde', 'Green', 'green', null, states.id, 'Reverend', 'green', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';

-- Roles

-- people_roles
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'websphere' AND roles.code = 'ROLE_ADMINISTRATOR';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'websphere' AND roles.code = 'ROLE_MANAGER';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'websphere' AND roles.code = 'ROLE_CLERK';
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
