--\SmartFinance\database_engine\src\main\resources\db\migration\V17__item_notr.sql
CREATE SEQUENCE IF NOT EXISTS SEQ_ITEM_NOTR;
CREATE TABLE ITEM_NOTR(
ID BIGINT DEFAULT SEQ_ITEM_NOTR.NEXTVAL PRIMARY KEY,
ORDER_NUMBER INT NOT NULL, -- 1,2,3
INVOICE_ID BIGINT,
CATEGORY_ID BIGINT,
TAX_ID BIGINT,
FAMILY_MEMBER_ID BIGINT,
DATEUNIT_UNITDAY BIGINT,
DESCRIPTION1 VARCHAR(512) NOT NULL,
DESCRIPTION2 VARCHAR(512) NOT NULL,
COMMENT VARCHAR(512) NOT NULL,
SUB_TOTAL DECIMAL(10,4) DEFAULT 0, --before tax
TOTAL DECIMAL(10,4) DEFAULT 0, --after tax
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
FOREIGN KEY (INVOICE_ID) REFERENCES INVOICE_NOTR(ID),
FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(ID),
FOREIGN KEY (TAX_ID) REFERENCES TAX(ID),
FOREIGN KEY (FAMILY_MEMBER_ID) REFERENCES FAMILY_MEMBER(ID),
FOREIGN KEY (DATEUNIT_UNITDAY) REFERENCES DATEUNIT(UNITDAY)
);

CREATE UNIQUE INDEX IF NOT EXISTS IDX_UNQ_TM_NVCDRDRNMBR2 ON ITEM_NOTR(INVOICE_ID,ORDER_NUMBER);--for equals/hashcode INVOICE_ID,ORDER_NUMBER
--The foreign key constraint alone does not provide the index - one must (and should) be created.
--https://asktom.oracle.com/pls/asktom/f?p=100:11:0::::P11_QUESTION_ID:292016138754
--table lock
CREATE INDEX IF NOT EXISTS IDX_TM_NVCD2 ON ITEM_NOTR(INVOICE_ID);
CREATE INDEX IF NOT EXISTS IDX_TM_CTGRD2 ON ITEM_NOTR(CATEGORY_ID);
CREATE INDEX IF NOT EXISTS IDX_TM_TXD2 ON ITEM_NOTR(TAX_ID);
CREATE INDEX IF NOT EXISTS IDX_TM_FMLMMBRD2 ON ITEM_NOTR(FAMILY_MEMBER_ID);
CREATE INDEX IF NOT EXISTS IDX_TM_DTNTNTD2 ON ITEM_NOTR(DATEUNIT_UNITDAY);

CREATE TRIGGER T_ITEM_NOTR_INS
BEFORE INSERT
ON ITEM_NOTR
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerItemNoTriggerTotals";

CREATE TRIGGER T_ITEM_NOTR_UPD
BEFORE UPDATE
ON ITEM_NOTR
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerItemNoTriggerTotals";

CREATE TRIGGER T_ITEM_NOTR_DEL
BEFORE DELETE
ON ITEM_NOTR
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerItemNoTriggerTotals";

GRANT SELECT, INSERT, UPDATE, DELETE ON
ITEM_NOTR
TO client;