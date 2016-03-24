CREATE SEQUENCE SEQ_CURRENCY;
CREATE TABLE CURRENCY(
ID BIGINT DEFAULT SEQ_CURRENCY.NEXTVAL PRIMARY KEY,
CODE VARCHAR(3) NOT NULL,
NAME VARCHAR(65),
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP
);
CREATE INDEX IF NOT EXISTS IDXCRRNCCD ON CURRENCY(CODE);--because we order by code

CREATE TRIGGER T_CURRENCY_INS
BEFORE INSERT
ON CURRENCY
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerFamilyMember";

CREATE TRIGGER T_CURRENCY_UPD
BEFORE UPDATE
ON FAMILY_MEMBER
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerFamilyMember";
