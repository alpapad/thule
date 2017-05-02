-- States

-- Actions

-- Addresses
INSERT INTO addresses(id, address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, 'HOME', 'No. 1', 'Park lane', 'London', 'Middlesex', countries.id, 'ABC 123', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';
INSERT INTO addresses(id, address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, 'WORK', '55', 'Picadilly', 'London', 'Middlesex', countries.id, 'DEF 456', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';

-- Countries

-- State_actions

-- People
INSERT INTO people(id, date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, CURRENT_DATE, TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'was@serin-consultancy.co.uk', 'WebSphere', 'websphere', 'Mr', 'Application', 'Server', states.id, 'websphere', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(id, date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, TO_DATE('1970-07-29', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'scarlett@serin-consultancy.co.uk', 'Ruby', 'scarlett', 'Miss', null,  'Scarlett', states.id, 'scarlett', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(id, date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, TO_DATE('1971-03-15', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'plum@serin-consultancy.co.uk', 'Ben', 'plum', 'Professor', null,  'Plum', states.id, 'plum', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(id, date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, TO_DATE('1971-10-15', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'mustard@serin-consultancy.co.uk', 'Dijon', 'mustard', 'Colonel', null,  'Mustard', states.id, 'mustard', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(id, date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, TO_DATE('1971-11-15', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'peacock@serin-consultancy.co.uk', 'Fly', 'peacock', 'Mrs', null,  'Peacock', states.id, 'peacock', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(id, date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, TO_DATE('1971-12-16', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'white@serin-consultancy.co.uk', 'China', 'white', 'Mrs', null,  'White', states.id, 'white', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(id, date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT uid_sequence.NEXTVAL, TO_DATE('1971-01-12', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'), 'green@serin-consultancy.co.uk', 'Verde', 'green', 'Reverend', null,  'Green', states.id, 'green', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';

-- Roles

-- people_roles
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'websphere' AND roles.code = 'AROLE_DMINISTRATOR';
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

