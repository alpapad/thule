-- Create flyway table to force migrations to start afresh
DROP TABLE IF EXISTS flyway_schema_history CASCADE;
