--todo start/end date as dateunit???
--http://www.revenuquebec.ca/en/entreprises/taxes/tpstvhtvq/default.aspx
--http://www.revenuquebec.ca/en/entreprises/taxes/tpstvhtvq/reglesdebase/default.aspx
CREATE SEQUENCE IF NOT EXISTS SEQ_TAX;
CREATE TABLE TAX(
ID BIGINT DEFAULT SEQ_TAX.NEXTVAL PRIMARY KEY,
NAME VARCHAR(32) NOT NULL,
DESCRIPTION VARCHAR(512),
RATE DECIMAL(10,4) DEFAULT 0,
FORMULA VARCHAR(512),
DENORMALIZED_FORMULA VARCHAR(512),
STARTDATE DATE,
ENDDATE DATE,
--STARTDATE BIGINT,
--ENDDATE BIGINT,
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP
--,FOREIGN KEY (STARTDATE) REFERENCES DATEUNIT(UNITDAY),  DON'T FORGET FK INDEX
--FOREIGN KEY (ENDDATE) REFERENCES DATEUNIT(UNITDAY) DON'T FORGET FK INDEX
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