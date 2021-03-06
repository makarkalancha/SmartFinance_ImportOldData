CREATE SEQUENCE IF NOT EXISTS SEQ_TRANSACTION;
CREATE TABLE TRANSACTION(
ID BIGINT DEFAULT SEQ_TRANSACTION.NEXTVAL PRIMARY KEY,
TRANSACTION_NUMBER VARCHAR(14) NOT NULL,--20160707121201 which 2016-July-07 12:12:01
ACCOUNT_ID BIGINT,
INVOICE_ID BIGINT,
DATEUNIT_UNITDAY BIGINT,
COMMENT VARCHAR(512),
DEBIT_AMOUNT DECIMAL(10,4) DEFAULT 0,
CREDIT_AMOUNT DECIMAL(10,4) DEFAULT 0,
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID),
FOREIGN KEY (INVOICE_ID) REFERENCES INVOICE(ID),
FOREIGN KEY (DATEUNIT_UNITDAY) REFERENCES DATEUNIT(UNITDAY)
);

CREATE UNIQUE INDEX IF NOT EXISTS IDX_UNQ_TRNSCTN_TRNSCTNNMBR ON TRANSACTION(TRANSACTION_NUMBER);--for equals/hashcode TRANSACTION_NUMBER
--The foreign key constraint alone does not provide the index - one must (and should) be created.
--https://asktom.oracle.com/pls/asktom/f?p=100:11:0::::P11_QUESTION_ID:292016138754
--table lock
CREATE INDEX IF NOT EXISTS IDX_TRNSCTN_CCNTD ON TRANSACTION(ACCOUNT_ID);
CREATE INDEX IF NOT EXISTS IDX_TRNSCTN_NVCD ON TRANSACTION(INVOICE_ID);
CREATE INDEX IF NOT EXISTS IDX_TRNSCTN_DTNTNTD ON TRANSACTION(DATEUNIT_UNITDAY);

CREATE TRIGGER T_TRANSACTION_INS
BEFORE INSERT
ON TRANSACTION
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerTransaction";

CREATE TRIGGER T_TRANSACTION_UPD
BEFORE UPDATE
ON TRANSACTION
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerTransaction";

CREATE TRIGGER T_TRANSACTION_DEL
BEFORE DELETE
ON TRANSACTION
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerTransaction";

GRANT SELECT, INSERT, UPDATE, DELETE ON
TRANSACTION
TO client;