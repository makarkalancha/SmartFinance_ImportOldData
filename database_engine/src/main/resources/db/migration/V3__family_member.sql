CREATE SEQUENCE SEQ_FAMILY_MEMBER;
CREATE TABLE FAMILY_MEMBER(
ID BIGINT DEFAULT SEQ_FAMILY_MEMBER.NEXTVAL PRIMARY KEY,
NAME VARCHAR(65) NOT NULL,
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP
);
--http://dba.stackexchange.com/questions/144/when-should-i-use-a-unique-constraint-instead-of-a-unique-index
--prefere unique index instead unique constraint, because of searching/sorting/filtering on that field to be quick
CREATE UNIQUE INDEX IF NOT EXISTS IDX_UNQ_FMLMMBR_NM ON FAMILY_MEMBER(NAME);--because we order by code

CREATE TRIGGER T_FAMILY_MEMBER_INS 
BEFORE INSERT 
ON FAMILY_MEMBER 
FOR EACH ROW 
CALL "com.makco.smartfinance.h2db.triggers.TriggerFamilyMember";

CREATE TRIGGER T_FAMILY_MEMBER_UPD 
BEFORE UPDATE 
ON FAMILY_MEMBER 
FOR EACH ROW 
CALL "com.makco.smartfinance.h2db.triggers.TriggerFamilyMember";

CREATE TRIGGER T_FAMILY_MEMBER_DEL
BEFORE DELETE
ON FAMILY_MEMBER
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerFamilyMember";
