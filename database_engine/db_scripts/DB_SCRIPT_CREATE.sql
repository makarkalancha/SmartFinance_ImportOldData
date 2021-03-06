DROP SCHEMA IF EXISTS FINANCE;
CREATE SCHEMA FINANCE;

SET SCHEMA FINANCE;

CREATE SEQUENCE SEQ_FAMILY_MEMBER;
CREATE TABLE FAMILY_MEMBER(
ID BIGINT DEFAULT SEQ_FAMILY_MEMBER.NEXTVAL PRIMARY KEY,
NAME VARCHAR(65) NOT NULL,
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE
);
CREATE INDEX IF NOT EXISTS IDXFMLMMBRDEL ON FAMILY_MEMBER(ISDELETED);

CREATE SEQUENCE SEQ_CURRENCY;
CREATE TABLE CURRENCY(
ID BIGINT DEFAULT SEQ_CURRENCY.NEXTVAL PRIMARY KEY,
CODE VARCHAR(3) NOT NULL,
NAME VARCHAR(65),
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE
);
CREATE INDEX IF NOT EXISTS IDXCRRNCDEL ON CURRENCY(ISDELETED);
CREATE UNIQUE INDEX IF NOT EXISTS IDXCRRNCCD ON CURRENCY(CODE);--because we order by code

CREATE SEQUENCE SEQ_ACCOUNT_GROUP;
--@Entity
--@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
--@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR, length = 1)
CREATE TABLE ACCOUNT_GROUP(
ID BIGINT DEFAULT SEQ_ACCOUNT_GROUP.NEXTVAL,
TYPE CHAR(1) NOT NULL,--discriminator: CREDIT/DEBIT account group
NAME VARCHAR(65) NOT NULL,
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
PRIMARY KEY(ID,TYPE)
);
CREATE INDEX IF NOT EXISTS IDXACCNTGRPDEL ON ACCOUNT_GROUP(ISDELETED);
--http://stackoverflow.com/questions/179085/multiple-indexes-vs-multi-column-indexes
CREATE UNIQUE INDEX IF NOT EXISTS IDXACCNTGRPTPNM ON ACCOUNT_GROUP(TYPE,NAME);--because we search by TYPE,NAME

CREATE SEQUENCE SEQ_ACCOUNT;
--@Entity
--@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
--@DiscriminatorColumn(name = "ACCOUNT_GROUP_TYPE", discriminatorType = DiscriminatorType.CHAR, length = 1)
CREATE TABLE ACCOUNT(
ID BIGINT DEFAULT SEQ_ACCOUNT.NEXTVAL PRIMARY KEY,
ACCOUNT_GROUP_ID BIGINT NOT NULL,
ACCOUNT_GROUP_TYPE CHAR(1) NOT NULL,--discriminator: CREDIT/DEBIT account group
CURRENCY_ID BIGINT NOT NULL,
NAME VARCHAR(65) NOT NULL,
DESCRIPTION VARCHAR(1024),
INITIAL_BALANCE DECIMAL(20,6) DEFAULT 0.00,
ACC_LIMIT DECIMAL(20,6) DEFAULT NULL,--there is no limit for debit accounts=>null, not 0.00
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
FOREIGN KEY (ACCOUNT_GROUP_ID,ACCOUNT_GROUP_TYPE) REFERENCES ACCOUNT_GROUP(ID,TYPE) ON DELETE SET NULL ON UPDATE CASCADE,
FOREIGN KEY (CURRENCY_ID) REFERENCES CURRENCY(ID) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXACCNTDEL ON ACCOUNT(ISDELETED);
CREATE INDEX IF NOT EXISTS IDXACCNTNM ON ACCOUNT(NAME);--because we sort by NAME
CREATE UNIQUE INDEX IF NOT EXISTS IDXACCNTNMUNQ ON ACCOUNT(ACCOUNT_GROUP_ID, CURRENCY_ID, NAME);--same group (cash) same name (Husband_cash) but different currency

CREATE TABLE BILL_COIN_AMT(
ACCOUNT_ID BIGINT NOT NULL,
DATE DATE NOT NULL,
AMT_CENT1 SMALLINT DEFAULT 0,
AMT_CENT5 SMALLINT DEFAULT  0,
AMT_CENT10 SMALLINT DEFAULT 0,
AMT_CENT25 SMALLINT DEFAULT 0,
AMT_DOLLAR1 SMALLINT DEFAULT 0,
AMT_DOLLAR2 SMALLINT DEFAULT 0,
AMT_DOLLAR5 SMALLINT DEFAULT 0,
AMT_DOLLAR10 SMALLINT DEFAULT 0,
AMT_DOLLAR20 SMALLINT DEFAULT 0,
AMT_DOLLAR50 SMALLINT DEFAULT 0,
AMT_DOLLAR100 SMALLINT DEFAULT 0,
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
PRIMARY KEY (ACCOUNT_ID,DATE),
FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXBLLCNAMTDEL ON BILL_COIN_AMT(ISDELETED);
CREATE INDEX IF NOT EXISTS IDXBLLCNAMTDT ON BILL_COIN_AMT(DATE);--because we search by DATE

CREATE TABLE ACCOUNT_BALANCE(
ACCOUNT_ID BIGINT,
DATE DATE NOT NULL,
BALANCE DECIMAL(20,6) DEFAULT 0.00,
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
PRIMARY KEY (ACCOUNT_ID,DATE),
FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXACCNTBLNCDEL ON ACCOUNT_BALANCE(ISDELETED);
CREATE INDEX IF NOT EXISTS IDXACCNTBLNCDT ON ACCOUNT_BALANCE(DATE);--because we search by DATE

CREATE SEQUENCE SEQ_CATEGORY_GROUP;
--@Entity
--@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
--@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR, length = 1)
CREATE TABLE CATEGORY_GROUP(
ID BIGINT DEFAULT SEQ_CATEGORY_GROUP.NEXTVAL,
TYPE CHAR(1) NOT NULL,--discriminator: CREDIT/DEBIT category group, i.e. expenses/income
NAME VARCHAR(65) NOT NULL,
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
PRIMARY KEY (ID,TYPE)
);
CREATE INDEX IF NOT EXISTS IDXCTGRGRPDEL ON CATEGORY_GROUP(ISDELETED);
CREATE UNIQUE INDEX IF NOT EXISTS IDXCTGRGRPTPNM ON CATEGORY_GROUP(TYPE, NAME);--because we search by TYPE,NAME

CREATE SEQUENCE SEQ_CATEGORY;
--@Entity
--@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
--@DiscriminatorColumn(name = "CATEGORY_GROUP_TYPE", discriminatorType = DiscriminatorType.CHAR, length = 1)
CREATE TABLE CATEGORY(
ID BIGINT DEFAULT SEQ_CATEGORY.NEXTVAL PRIMARY KEY,
CATEGORY_GROUP_ID BIGINT NOT NULL,
CATEGORY_GROUP_TYPE CHAR(1) NOT NULL,--discriminator: CREDIT/DEBIT account group
NAME VARCHAR(65) NOT NULL,
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
FOREIGN KEY (CATEGORY_GROUP_ID,CATEGORY_GROUP_TYPE) REFERENCES CATEGORY_GROUP(ID,TYPE) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXCTGRDEL ON CATEGORY(ISDELETED);
CREATE INDEX IF NOT EXISTS IDXCTGRNM ON CATEGORY(NAME);--because we sort by NAME
CREATE UNIQUE INDEX IF NOT EXISTS IDXCTGRNMUNQ ON CATEGORY(CATEGORY_GROUP_ID, NAME);--because we sort by NAME

CREATE SEQUENCE SEQ_ORGANIZATION;
CREATE TABLE ORGANIZATION(
ID BIGINT DEFAULT SEQ_ORGANIZATION.NEXTVAL PRIMARY KEY,
NAME VARCHAR(65) NOT NULL,
DESCRIPTION VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE
);
CREATE INDEX IF NOT EXISTS IDXORGDEL ON ORGANIZATION(ISDELETED);
CREATE UNIQUE INDEX IF NOT EXISTS IDXORGNMUNQ ON ORGANIZATION(NAME);--because we sort by NAME

CREATE SEQUENCE SEQ_TAX;
--http://www.revenuquebec.ca/fr/entreprises/taxes/tpstvhtvq/reglesdebase/historiquetauxtpstvq.aspx
--https://fr.wikipedia.org/wiki/Taxe_de_vente_du_Qu%C3%A9bec
CREATE TABLE TAX(
ID BIGINT DEFAULT SEQ_TAX.NEXTVAL PRIMARY KEY,
NAME VARCHAR(100) NOT NULL,
DESCRIPTION VARCHAR(512),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE
);
CREATE INDEX IF NOT EXISTS IDXTAXDEL ON TAX(ISDELETED);
CREATE UNIQUE INDEX IF NOT EXISTS IDXTAXNMUNQ ON TAX(NAME);--because we sort by NAME

CREATE SEQUENCE SEQ_TAX_RATE;
--load tax in a context class when app is starting to avoid query this table everytime
--put a listener in UI part if tax rate is changed then update in config file
--2 types of app: general and specific for Canada
CREATE TABLE TAX_RATE(
ID BIGINT DEFAULT SEQ_TAX_RATE.NEXTVAL PRIMARY KEY,
TAX_ID BIGINT NOT NULL,
RATE DECIMAL(20,2) DEFAULT 0.00,
FORMULA VARCHAR(512),
DESCRIPTION VARCHAR(512),
STARTDATE TIMESTAMP DEFAULT '1970-01-01 00:00:00',
ENDDATE TIMESTAMP DEFAULT '2037-01-01 00:00:00',
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
FOREIGN KEY (TAX_ID) REFERENCES TAX(ID) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXTAXRTDEL ON TAX_RATE(ISDELETED);

CREATE SEQUENCE SEQ_INVOICE;
CREATE TABLE INVOICE(
ID BIGINT DEFAULT SEQ_INVOICE.NEXTVAL PRIMARY KEY,
ORGANIZATION_ID BIGINT NOT NULL,
COMMENT VARCHAR(512),
CUSTOM1 DECIMAL(20,6) DEFAULT 0.00,
CUSTOM2 DECIMAL(20,6) DEFAULT 0.00,
BALANCE DECIMAL(20,6) DEFAULT 0.00,
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
FOREIGN KEY (ORGANIZATION_ID) REFERENCES ORGANIZATION(ID) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXINVCDEL ON INVOICE(ISDELETED);

CREATE SEQUENCE SEQ_ITEM;
CREATE TABLE ITEM(
ID BIGINT DEFAULT SEQ_ITEM.NEXTVAL PRIMARY KEY,
INVOICE_ID BIGINT NOT NULL,
CATEGORY_ID BIGINT NOT NULL,
TAX_ID BIGINT NOT NULL,
FAMILY_MEMBER_ID BIGINT NOT NULL,
--add index for desc1 and desc2 to provide prompt while typing
--bi-directional:
--type in desc1-> prompt desc1 linked with desc2
--type in desc2-> prompt desc2 linked with desc1
DESCRIPTION1 VARCHAR(64) NOT NULL,
DESCRIPTION2 VARCHAR(64),
AMOUNT DECIMAL(20,6) DEFAULT 0.00,
COMMENT VARCHAR(512),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
FOREIGN KEY (INVOICE_ID) REFERENCES INVOICE(ID) ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(ID) ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (TAX_ID) REFERENCES TAX(ID) ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (FAMILY_MEMBER_ID) REFERENCES FAMILY_MEMBER(ID) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXITMDEL ON ITEM(ISDELETED);

--to prompt description by category id
--autocomplete: everything.regex.RegexAutocomplete
CREATE TABLE ITEM_DESC(
CATEGORY_ID BIGINT NOT NULL,
DESCRIPTION1 VARCHAR(64),
DESCRIPTION2 VARCHAR(64),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
PRIMARY KEY(CATEGORY_ID,DESCRIPTION1,DESCRIPTION2),
FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXITMDSCDEL ON ITEM_DESC(ISDELETED);
CREATE INDEX IF NOT EXISTS IDXITMDSCDSC1 ON ITEM_DESC(DESCRIPTION1);--sort by desc1
CREATE INDEX IF NOT EXISTS IDXITMDSCDSC2 ON ITEM_DESC(DESCRIPTION2);--sort by desc1
CREATE UNIQUE INDEX IF NOT EXISTS IDXITMDSCDSC1 ON ITEM_DESC(DESCRIPTION1, DESCRIPTION2);

CREATE TABLE TRANSACTION(
ACCOUNT_ID BIGINT NOT NULL,
ACCOUNT_GROUP_TYPE CHAR(1) NOT NULL,--discriminator: CREDIT/DEBIT account group
INVOICE_ID BIGINT NOT NULL,
DATE DATE NOT NULL,
DEBIT_AMOUNT DECIMAL(20,6) DEFAULT 0.00,
CREDIT_AMOUNT DECIMAL(20,6) DEFAULT 0.00,
COMMENT VARCHAR(512),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
PRIMARY KEY (ACCOUNT_ID,INVOICE_ID,DATE),
FOREIGN KEY (ACCOUNT_ID, ACCOUNT_GROUP_TYPE) REFERENCES ACCOUNT(ID,ACCOUNT_GROUP_TYPE) ON DELETE SET NULL ON UPDATE CASCADE,
FOREIGN KEY (INVOICE_ID) REFERENCES INVOICE(ID) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXTRNSCTNDEL ON TRANSACTION(ISDELETED);
CREATE INDEX IF NOT EXISTS IDXTRNSCTNDT ON TRANSACTION(DATE);

CREATE TABLE CURRENCY_RATE(
SRC_CURRENCY_ID BIGINT NOT NULL,
DST_CURRENCY_ID BIGINT NOT NULL,
DATE DATE NOT NULL,
INVOICE_ID BIGINT NOT NULL,
RATE DECIMAL(20,6) DEFAULT 0.00,
COMMENT VARCHAR(1024),
T_CREATEDON TIMESTAMP,
T_UPDATEDON TIMESTAMP,
ISDELETED BOOLEAN DEFAULT FALSE,
PRIMARY KEY (SRC_CURRENCY_ID,DST_CURRENCY_ID,DATE),
FOREIGN KEY (SRC_CURRENCY_ID) REFERENCES CURRENCY(ID) ON DELETE SET NULL ON UPDATE CASCADE,
FOREIGN KEY (DST_CURRENCY_ID) REFERENCES CURRENCY(ID) ON DELETE SET NULL ON UPDATE CASCADE,
FOREIGN KEY (INVOICE_ID) REFERENCES INVOICE(ID) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX IF NOT EXISTS IDXCRRNCRTDEL ON CURRENCY_RATE(ISDELETED);
CREATE INDEX IF NOT EXISTS IDXCRRNCRTDT ON CURRENCY_RATE(DATE);

CREATE OR REPLACE VIEW DEBIT_SUMMARY
(ACCOUNT_ID,ACCOUNT_NAME,ACCOUNT_GROUP_TYPE,SUM_DEBIT_AMOUNT,SUM_CREDIT_AMOUNT,BALANCE)
AS
SELECT A.ID,
       A.NAME,
       A.ACCOUNT_GROUP_TYPE,
       IFNULL(SUM(T.DEBIT_AMOUNT),0),
       IFNULL(SUM(T.CREDIT_AMOUNT),0),
       IFNULL(SUM(T.DEBIT_AMOUNT)-SUM(T.CREDIT_AMOUNT),0)
FROM ACCOUNT A
LEFT JOIN TRANSACTION T ON T.ACCOUNT_ID = A.ID
WHERE (T.ISDELETED = FALSE
       OR T.ISDELETED IS NULL)
  AND A.ISDELETED = FALSE
  AND A.ACCOUNT_GROUP_TYPE = 'D'
GROUP BY A.ID;

CREATE OR REPLACE VIEW CREDIT_SUMMARY
(ACCOUNT_ID,ACCOUNT_NAME,ACCOUNT_GROUP_TYPE,SUM_DEBIT_AMOUNT,SUM_CREDIT_AMOUNT,BALANCE)
AS
SELECT A.ID,
       A.NAME,
       A.ACCOUNT_GROUP_TYPE,
       SUM(T.DEBIT_AMOUNT),
       SUM(T.CREDIT_AMOUNT),
       SUM(T.CREDIT_AMOUNT)-SUM(T.DEBIT_AMOUNT) BALANCE
FROM ACCOUNT A
LEFT JOIN TRANSACTION T ON T.ACCOUNT_ID = A.ID
WHERE T.ISDELETED = FALSE AND
A.ISDELETED = FALSE AND
A.ACCOUNT_GROUP_TYPE = 'C'
GROUP BY A.ID;

CREATE OR REPLACE VIEW CATEGORY_DEBIT_SUMMARY
(CATEGORY_ID,CATEGORY_NAME,CATEGORY_GROUP_TYPE,DEBIT_BALANCE)
AS
SELECT C.ID,
       C.NAME,
       C.CATEGORY_GROUP_TYPE,
       IFNULL(SUM(I.AMOUNT),0)
FROM CATEGORY C
LEFT JOIN ITEM I ON I.CATEGORY_ID = C.ID
WHERE (I.ISDELETED = FALSE OR I.ISDELETED IS NULL) AND
C.ISDELETED = FALSE AND
C.CATEGORY_GROUP_TYPE = 'D'
GROUP BY C.ID;

CREATE OR REPLACE VIEW CATEGORY_CREDIT_SUMMARY
(CATEGORY_ID,CATEGORY_NAME,CATEGORY_GROUP_TYPE,CREDIT_BALANCE)
AS
SELECT C.ID,
       C.NAME,
       C.CATEGORY_GROUP_TYPE,
       IFNULL(SUM(I.AMOUNT)*(-1),0)
FROM CATEGORY C
LEFT JOIN ITEM I ON I.CATEGORY_ID = C.ID
WHERE (I.ISDELETED = FALSE OR I.ISDELETED IS NULL) AND
C.ISDELETED = FALSE AND
C.CATEGORY_GROUP_TYPE = 'C'
GROUP BY C.ID;