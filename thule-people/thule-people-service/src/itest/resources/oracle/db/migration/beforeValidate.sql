-- Drop flyway table to force migrations to start afresh
BEGIN EXECUTE IMMEDIATE 'DROP TABLE "flyway_schema_history" CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- Allow creating/deleting of users without a C## prefix
ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;


-- Drop user
BEGIN EXECUTE IMMEDIATE 'DROP USER thule CASCADE'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
