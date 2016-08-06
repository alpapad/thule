-- Create flyway table to force migrations to start afresh
DROP TABLE IF EXISTS schema_version CASCADE;
