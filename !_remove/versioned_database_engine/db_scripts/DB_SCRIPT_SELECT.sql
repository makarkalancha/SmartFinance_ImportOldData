--http://www.h2database.com/html/tutorial.html#creating_new_databases
--http://www.h2database.com/html/tutorial.html#connection_pool
--http://www.h2database.com/javadoc/org/h2/jdbcx/JdbcConnectionPool.html
--For H2, it is about twice as faster to get a connection from the built-in connection pool than to get one using DriverManager.getConnection()


--balance per account
SELECT A.ID,A.NAME,A.ACCOUNT_GROUP_TYPE,SUM(T.DEBIT_AMOUNT) ,SUM(T.CREDIT_AMOUNT),SUM(T.DEBIT_AMOUNT)-SUM(T.CREDIT_AMOUNT) BALANCE FROM FINANCE.TRANSACTION T join FINANCE.ACCOUNT A ON T.ACCOUNT_ID = A.ID GROUP BY T.ACCOUNT_ID
--reverse: calculate with credit/debit in mind
SELECT A.ID,A.NAME,A.ACCOUNT_GROUP_TYPE,SUM(T.DEBIT_AMOUNT) ,SUM(T.CREDIT_AMOUNT),
    CASEWHEN(A.ACCOUNT_GROUP_TYPE='D',
        SUM(T.DEBIT_AMOUNT)-SUM(T.CREDIT_AMOUNT),
        CASEWHEN(A.ACCOUNT_GROUP_TYPE='C',SUM(T.CREDIT_AMOUNT)-SUM(T.DEBIT_AMOUNT), 'not supported')) BALANCE
FROM FINANCE.TRANSACTION T join FINANCE.ACCOUNT A ON T.ACCOUNT_ID = A.ID GROUP BY T.ACCOUNT_ID

--account balance per day (no accumulating)
SELECT T.DATE,A.ID,A.NAME ,SUM(T.DEBIT_AMOUNT) ,SUM(T.CREDIT_AMOUNT),SUM(T.DEBIT_AMOUNT)-SUM(T.CREDIT_AMOUNT) BALANCE FROM FINANCE.TRANSACTION T join FINANCE.ACCOUNT A ON T.ACCOUNT_ID = A.ID GROUP BY T.DATE,T.ACCOUNT_ID ORDER BY T.DATE,T.ACCOUNT_ID


SELECT C.ID,C.CODE,C.NAME,A.ID,A.NAME ,SUM(T.DEBIT_AMOUNT) ,SUM(T.CREDIT_AMOUNT),SUM(T.DEBIT_AMOUNT)-SUM(T.CREDIT_AMOUNT) BALANCE FROM FINANCE.CURRENCY C JOIN FINANCE.ACCOUNT A ON A.CURRENCY_ID = C.ID JOIN FINANCE.TRANSACTION T ON T.ACCOUNT_ID = A.ID GROUP BY C.ID,A.ID

--categories
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


SELECT CG.ID CAT_GROUP_ID,
CG.NAME AS CAT_GROUP_NAME,
        C.ID CAT_ID,
       C.NAME CAT_NAME,
       C.CATEGORY_GROUP_TYPE,
       IFNULL(SUM(IT.AMOUNT)*(-1),0)
FROM CATEGORY_GROUP CG
JOIN CATEGORY C ON C.CATEGORY_GROUP_ID = CG.ID
LEFT JOIN ITEM IT ON IT.CATEGORY_ID = C.ID
LEFT JOIN INVOICE IV ON IV.ID = IT.INVOICE_ID
LEFT JOIN TRANSACTION T ON T.INVOICE_ID = IV.ID
WHERE (C.ISDELETED = FALSE AND IT.ISDELETED = FALSE AND IV.ISDELETED = FALSE AND T.ISDELETED = FALSE) AND
C.CATEGORY_GROUP_TYPE = 'C'
GROUP BY C.ID;


SELECT CG.ID CAT_GROUP_ID,
CG.NAME AS CAT_GROUP_NAME,
        C.ID CAT_ID,
       C.NAME CAT_NAME,
       C.CATEGORY_GROUP_TYPE,
       IFNULL(SUM(IT.AMOUNT),0)
FROM CATEGORY_GROUP CG
JOIN CATEGORY C ON C.CATEGORY_GROUP_ID = CG.ID
LEFT JOIN ITEM IT ON IT.CATEGORY_ID = C.ID
LEFT JOIN INVOICE IV ON IV.ID = IT.INVOICE_ID
LEFT JOIN TRANSACTION T ON T.INVOICE_ID = IV.ID
WHERE (C.ISDELETED = FALSE AND IT.ISDELETED = FALSE AND IV.ISDELETED = FALSE AND T.ISDELETED = FALSE) AND
C.CATEGORY_GROUP_TYPE = 'D'
GROUP BY C.ID;