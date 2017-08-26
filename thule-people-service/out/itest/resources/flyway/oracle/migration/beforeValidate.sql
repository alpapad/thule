-- Create flyway table to force migrations to start afresh
BEGIN EXECUTE IMMEDIATE 'DROP TABLE "schema_version" CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
