-- REFERENCE DATA

-- States
INSERT INTO states(code, description, version, updated_by, created_at, updated_at)
    VALUES('PERSON_ENABLED', 'Enabled', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO states(code, description, version, updated_by, created_at, updated_at)
    VALUES('PERSON_DISABLED', 'Disabled', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO states(code, description, version, updated_by, created_at, updated_at)
    VALUES('PERSON_DISCARDED', 'Discarded', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO states(code, description, version, updated_by, created_at, updated_at)
    VALUES('ADDRESS_ENABLED', 'Enabled', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO states(code, description, version, updated_by, created_at, updated_at)
    VALUES('ADDRESS_DISABLED', 'Disabled', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO states(code, description, version, updated_by, created_at, updated_at)
    VALUES('ADDRESS_DISCARDED', 'Discarded', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Actions
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'PERSON_ENABLE', 'Enable', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'PERSON_DISABLE', 'Disable', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_DISABLED';
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'PERSON_DISCARD', 'Discard', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_DISCARDED';
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'PERSON_RECOVER', 'Recover', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO actions(code, description, version, updated_by, created_at, updated_at)
    VALUES('PERSON_UPDATE', 'Update', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO actions(code, description, version, updated_by, created_at, updated_at)
    VALUES('PERSON_VIEW', 'View', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'ADDRESS_ENABLE', 'Enable', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'ADDRESS_ENABLED';
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'ADDRESS_DISABLE', 'Disable', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'ADDRESS_DISABLED';
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'ADDRESS_DISCARD', 'Discard', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'ADDRESS_DISCARDED';
INSERT INTO actions(code, description, next_state_id, version, updated_by, created_at, updated_at)
    SELECT 'ADDRESS_RECOVER', 'Recover', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'ADDRESS_ENABLED';
INSERT INTO actions(code, description, version, updated_by, created_at, updated_at)
    VALUES('ADDRESS_UPDATE', 'Update', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO actions(code, description, version, updated_by, created_at, updated_at)
    VALUES('ADDRESS_VIEW', 'View', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- State_actions
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_ENABLED' AND actions.code = 'PERSON_DISABLE';
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_ENABLED' AND actions.code = 'PERSON_DISCARD';
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_ENABLED' AND actions.code = 'PERSON_UPDATE';
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_ENABLED' AND actions.code = 'PERSON_VIEW';
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_DISABLED' AND actions.code = 'PERSON_ENABLE';
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_DISABLED' AND actions.code = 'PERSON_VIEW';
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_DISCARDED' AND actions.code = 'PERSON_RECOVER';
INSERT INTO state_actions(state_id, action_id)
    SELECT states.id, actions.id FROM states, actions WHERE states.code = 'PERSON_DISCARDED' AND actions.code = 'PERSON_VIEW';

-- People
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, last_name, password, second_name, state_id, title, user_id, version, updated_by, created_at, updated_at)
    SELECT CURRENT_DATE, '2030-12-31', '2030-12-31', 'superuser@serin-consultancy.co.uk', 'Super', 'User', 'superuser', '', states.id, 'Mr', 'superuser', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';

-- Roles
INSERT INTO roles(code, description, version, updated_by, created_at, updated_at)
    VALUES('ROLE_ADMINISTRATOR', 'Administrator', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO roles(code, description, version, updated_by, created_at, updated_at)
    VALUES('ROLE_MANAGER', 'Manager', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO roles(code, description, version, updated_by, created_at, updated_at)
    VALUES('ROLE_CLERK', 'Clerk', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- people_roles
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'superuser' AND roles.code = 'ROLE_ADMINISTRATOR';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'superuser' AND roles.code = 'ROLE_MANAGER';
INSERT INTO people_roles(person_id, role_id)
    SELECT people.id, roles.id FROM people, roles WHERE people.user_id = 'superuser' AND roles.code = 'ROLE_CLERK';

-- Countries
INSERT INTO countries(iso_code_three_digit, iso_code_two_digit, iso_name, iso_number, version, updated_by, created_at, updated_at)
    VALUES('GBR', 'GB', 'United Kingdom', '826', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- DEMO DATA

-- States

-- Actions

-- Addresses
INSERT INTO addresses(address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, updated_by, created_at, updated_at)
    SELECT 'HOME', 'No. 1', 'Park lane', 'London', 'Middlesex', countries.id, 'ABC 123', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';
INSERT INTO addresses(address_type, address_line1, address_line2, town, county, country_id, post_code, state_id, version, updated_by, created_at, updated_at)
    SELECT 'WORK', '55', 'Picadilly', 'London', 'Middlesex', countries.id, 'DEF 456', states.id, 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    FROM countries, states WHERE countries.iso_code_three_digit = 'GBR' AND states.code= 'ADDRESS_ENABLED';

-- Countries

-- State_actions

-- People
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, updated_by, created_at, updated_at)
    SELECT '1970-07-29', '2030-12-31', '2030-12-31', 'scarlett@serin-consultancy.co.uk', 'Ruby', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Scarlett', 'scarlett', null, states.id, 'Miss', 'scarlett', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, updated_by, created_at, updated_at)
    SELECT '1971-03-15', '2030-12-31', '2030-12-31', 'plum@serin-consultancy.co.uk', 'Ben', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Plum', 'plum', null, states.id, 'Professor', 'plum', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, updated_by, created_at, updated_at)
    SELECT '1971-10-15', '2030-12-31', '2030-12-31', 'mustard@serin-consultancy.co.uk', 'Dijon', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Mustard', 'mustard', null, states.id, 'Colonel', 'mustard', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, updated_by, created_at, updated_at)
    SELECT '1971-11-15', '2030-12-31', '2030-12-31', 'peacock@serin-consultancy.co.uk', 'Fly', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Peacock', 'peacock', null, states.id, 'Mrs', 'peacock', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, updated_by, created_at, updated_at)
    SELECT '1971-12-16', '2030-12-31', '2030-12-31', 'white@serin-consultancy.co.uk', 'China', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'White', 'white', null, states.id, 'Mrs', 'white', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    FROM states WHERE states.code = 'PERSON_ENABLED';
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, home_address_id, last_name, password, second_name, state_id, title, user_id, version, work_address_id, updated_by, created_at, updated_at)
    SELECT '1971-01-12', '2030-12-31', '2030-12-31', 'green@serin-consultancy.co.uk', 'Verde', (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'HOME'), 'Green', 'green', null, states.id, 'Reverend', 'green', 1, (SELECT addresses.id FROM addresses WHERE addresses.address_type = 'WORK'), 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
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
