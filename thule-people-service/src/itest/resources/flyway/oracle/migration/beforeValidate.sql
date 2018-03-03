-- Create flyway table to force migrations to start afresh
BEGIN EXECUTE IMMEDIATE 'DROP TABLE "flyway_schema_history" CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
