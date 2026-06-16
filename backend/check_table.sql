SET LINESIZE 200
SET PAGESIZE 100
COLUMN constraint_name FORMAT A30
COLUMN search_condition FORMAT A50
COLUMN trigger_name FORMAT A25
COLUMN trigger_body FORMAT A80

SELECT constraint_name, constraint_type, search_condition FROM user_constraints WHERE table_name = 'USERS';
SELECT trigger_name, trigger_type, triggering_event, status FROM user_triggers WHERE table_name = 'USERS';
SELECT column_name, nullable, data_type, data_length, data_precision, data_scale FROM user_tab_columns WHERE table_name = 'USERS' ORDER BY column_id;

EXIT;
