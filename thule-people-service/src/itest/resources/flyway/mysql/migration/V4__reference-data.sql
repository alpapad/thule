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
    VALUES('PERSON_UPDATE', 'Update',  1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
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
INSERT INTO people(date_of_birth, date_of_expiry, date_of_password_expiry, email_address, first_name, password, salutation, second_name, surname, state_id, user_id, version, updated_by, created_at, updated_at)
    SELECT CURRENT_DATE, DATE_FORMAT('2030-12-31', '%Y-%m-%d'), DATE_FORMAT('2030-12-31', '%Y-%m-%d'), 'superuser@serin-consultancy.co.uk', 'Super', 'superuser', 'Mr', '', 'User', states.id, 'superuser', 1, 'superuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM states WHERE states.code = 'PERSON_ENABLED';

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
