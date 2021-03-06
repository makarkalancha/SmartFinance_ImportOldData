CREATE SEQUENCE IF NOT EXISTS SEQ_ORGANIZATION;
CREATE TABLE ORGANIZATION(
ID BIGINT DEFAULT SEQ_ORGANIZATION.NEXTVAL PRIMARY KEY,
NAME VARCHAR(32),
DESCRIPTION VARCHAR(512),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP
);
CREATE UNIQUE INDEX IF NOT EXISTS IDX_UNQ_ORGNZTN_NM ON ORGANIZATION(NAME);--because we order by code

CREATE TRIGGER T_ORGANIZATION_INS
BEFORE INSERT
ON ORGANIZATION
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerOrganization";

CREATE TRIGGER T_ORGANIZATION_UPD
BEFORE UPDATE
ON ORGANIZATION
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerOrganization";

CREATE TRIGGER T_ORGANIZATION_DEL
BEFORE DELETE
ON ORGANIZATION
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerOrganization";

GRANT SELECT, INSERT, UPDATE, DELETE ON
ORGANIZATION
TO client;