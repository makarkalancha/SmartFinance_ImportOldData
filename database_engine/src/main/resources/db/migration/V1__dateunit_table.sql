CREATE ALIAS CREATE_DATE_UNIT_TABLE FOR "com.makco.smartfinance.h2db.functions.DateUnitFunctions.createDateUnitTable";
CREATE ALIAS INSERT_SELECT_DATE FOR "com.makco.smartfinance.h2db.functions.DateUnitFunctions.insertSelectDate";

CALL CREATE_DATE_UNIT_TABLE();

CREATE TRIGGER T_DATEUNIT_INS
BEFORE INSERT
ON DATEUNIT
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerDateUnit";

CREATE TRIGGER T_DATEUNIT_UPD
BEFORE UPDATE
ON DATEUNIT
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerDateUnit";