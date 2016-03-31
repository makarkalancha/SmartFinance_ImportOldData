CREATE SEQUENCE IF NOT EXISTS SEQ_DELETED_ROWS;
CREATE TABLE _DELETED_ROWS(
ID BIGINT DEFAULT SEQ_DELETED_ROWS.NEXTVAL PRIMARY KEY,
SCHEMA_NAME VARCHAR(128) NOT NULL,
TABLE_NAME VARCHAR(128) NOT NULL,
JSON_ROW CLOB NOT NULL,
T_CREATEDON TIMESTAMP
);

CREATE TRIGGER T_DELETED_ROWS_INS
BEFORE INSERT
ON _DELETED_ROWS
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerDeleteRows";