CREATE SEQUENCE IF NOT EXISTS SEQ_TAX;
CREATE TABLE TAX(
ID BIGINT DEFAULT SEQ_TAX.NEXTVAL PRIMARY KEY,
NAME VARCHAR(32) NOT NULL,
DESCRIPTION VARCHAR(512),
RATE DECIMAL(10,4) NOT NULL,
FORMULA VARCHAR(512),
STARTDATE DATE,
ENDDATE DATE,
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP
);
CREATE UNIQUE INDEX IF NOT EXISTS IDX_UNQ_TX_NM ON TAX(NAME);--because we order by code

CREATE TRIGGER T_TAX_INS
BEFORE INSERT
ON TAX
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerTax";

CREATE TRIGGER T_TAX_UPD
BEFORE UPDATE
ON TAX
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerTax";

CREATE TRIGGER T_TAX_DEL
BEFORE DELETE
ON TAX
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerTax";

GRANT SELECT, INSERT, UPDATE, DELETE ON
TAX
TO client;