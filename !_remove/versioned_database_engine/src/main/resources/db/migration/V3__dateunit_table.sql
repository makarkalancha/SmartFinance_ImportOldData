CREATE TABLE DATEUNIT(
    UNITDAY BIGINT NOT NULL,
    UNITDAYOFMONTH INT NOT NULL,
    UNITDAYOFYEAR INT NOT NULL,
    UNITMONTH BIGINT NOT NULL,
    UNITMONTHOFYEAR INT NOT NULL,
    UNITYEAR INT NOT NULL,
    UNITDAYOFWEEK INT NOT NULL,
    WEEKDAY BOOLEAN NOT NULL,--if weekend false
    UNITTIMESTAMP DATE UNIQUE NOT NULL,
    T_CREATEDON TIMESTAMP,
    PRIMARY KEY(UNITDAY),
    CONSTRAINT UNQDUROW UNIQUE(UNITDAY,UNITDAYOFMONTH,UNITDAYOFYEAR,UNITMONTH,UNITMONTHOFYEAR,UNITYEAR,UNITDAYOFWEEK,WEEKDAY,UNITTIMESTAMP)
);
--there is no weekofyear, because if you change first day of the week, week of year is changed and all reports need to re-calculated
--there is no unitweek, because if you change first day of the week, unit week is changed and all reports need to re-calculated
--PK: CREATE INDEX IF NOT EXISTS IDXUNITDAY ON DATEUNIT(UNITDAY);
CREATE INDEX IF NOT EXISTS IDXUNITDAYOFMONTH ON DATEUNIT(UNITDAYOFMONTH);
CREATE INDEX IF NOT EXISTS IDXUNITDAYOFYEAR ON DATEUNIT(UNITDAYOFYEAR);
CREATE INDEX IF NOT EXISTS IDXUNITMONTH ON DATEUNIT(UNITMONTH);
CREATE INDEX IF NOT EXISTS IDXUNITMONTHOFYEAR ON DATEUNIT(UNITMONTHOFYEAR);
CREATE INDEX IF NOT EXISTS IDXUNITYEAR ON DATEUNIT(UNITYEAR);
CREATE INDEX IF NOT EXISTS IDXUNITDAYOFWEEK ON DATEUNIT(UNITDAYOFWEEK);
CREATE INDEX IF NOT EXISTS IDXWEEKDAY ON DATEUNIT(WEEKDAY);
CREATE INDEX IF NOT EXISTS IDXUNITTIMESTAMP ON DATEUNIT(UNITTIMESTAMP);
        
--put in hibernate rather than in db engine
--CREATE ALIAS INSERT_SELECT_DATE FOR "com.makco.smartfinance.h2db.functions.DateUnitFunctions.insertSelectDate";

CREATE TRIGGER T_DATEUNIT_INS
BEFORE INSERT
ON DATEUNIT
FOR EACH ROW
CALL "com.makco.smartfinance.h2db.triggers.TriggerDateUnit";

GRANT SELECT, INSERT ON
DATEUNIT
TO client;
