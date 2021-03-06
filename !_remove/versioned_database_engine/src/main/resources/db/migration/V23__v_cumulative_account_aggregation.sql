CREATE OR REPLACE VIEW V_CUM_ACCT_AGG_DATE AS
SELECT
    D.UNITDAY UNITDAY,
    AG.ID ACCOUNT_GROUP_ID,
    AG.NAME ACCOUNT_GROUP_NAME,
    A.ID ACCOUNT_ID,
    A.NAME ACCOUNT_NAME,
    AG.TYPE TYPE,
    T.DEBIT_AMOUNT DEBIT,
    T.CREDIT_AMOUNT CREDIT,
    SUM(T2.DEBIT_AMOUNT) CUM_SUM_DEBIT,
    SUM(T2.CREDIT_AMOUNT) CUM_SUM_CREDIT
FROM DATEUNIT D
LEFT JOIN TRANSACTION_V3 T ON T.DATEUNIT_UNITDAY = D.UNITDAY
JOIN TRANSACTION_V3 T2 ON T.ID >= T2.ID
JOIN ACCOUNT A ON T.ACCOUNT_ID = A.ID
JOIN ACCOUNT_GROUP AG ON A.ACCOUNT_GROUP_ID = AG.ID
GROUP BY D.UNITDAY, AG.ID, AG.NAME, A.ID, A.NAME, AG.TYPE
ORDER BY D.UNITDAY, AG.TYPE, AG.NAME, A.NAME;