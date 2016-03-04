SET AUTOCOMMIT FALSE;
SET SCHEMA FINANCE;

INSERT INTO FAMILY_MEMBER (NAME,DESCRIPTION) VALUES('Fred','husband');
select @husband := scope_identity();
INSERT INTO FAMILY_MEMBER (NAME,DESCRIPTION) VALUES('Wilma','wife');
select @wife := scope_identity();
INSERT INTO FAMILY_MEMBER (NAME,DESCRIPTION) VALUES('the Flintstones','family');
select @family := scope_identity();

INSERT INTO ORGANIZATION (NAME,DESCRIPTION) VALUES('job','my job');
select @job := scope_identity();
INSERT INTO ORGANIZATION (NAME,DESCRIPTION) VALUES('walmart','shop');
select @walmart := scope_identity();
INSERT INTO ORGANIZATION (NAME,DESCRIPTION) VALUES('rbc','bank');
select @rbc := scope_identity();
INSERT INTO ORGANIZATION (NAME,DESCRIPTION) VALUES('sami fruits','shop');
select @sami_fruits := scope_identity();

INSERT INTO CURRENCY (CODE, NAME, DESCRIPTION) VALUES('CAD','canadian dollar','canadian dollar desc');
select @cad := scope_identity();
INSERT INTO CURRENCY (CODE, NAME, DESCRIPTION) VALUES('USD','american dollar','american dollar desc');
select @usd := scope_identity();
INSERT INTO CURRENCY (CODE, NAME, DESCRIPTION) VALUES('MDL','moldavian lei','moldavian lei desc');
select @mdl := scope_identity();

INSERT INTO CATEGORY_GROUP (TYPE, NAME, DESCRIPTION) VALUES('D', 'доходы', 'income');
select @income := scope_identity();
INSERT INTO CATEGORY_GROUP (TYPE, NAME, DESCRIPTION) VALUES('C', 'личные расходы', 'what person needs');
select @pers_exp := scope_identity();
INSERT INTO CATEGORY_GROUP (TYPE, NAME, DESCRIPTION) VALUES('C', 'кредиторская задолж', 'credit cards');
select @credit_exp := scope_identity();

INSERT INTO CATEGORY(CATEGORY_GROUP_ID, CATEGORY_GROUP_TYPE, NAME, DESCRIPTION) VALUES(@income,'D', 'зп','salary from job');
select @salary := scope_identity();
INSERT INTO CATEGORY(CATEGORY_GROUP_ID, CATEGORY_GROUP_TYPE, NAME, DESCRIPTION) VALUES(@pers_exp,'C', 'еда','food');
select @food := scope_identity();
INSERT INTO CATEGORY(CATEGORY_GROUP_ID, CATEGORY_GROUP_TYPE, NAME, DESCRIPTION) VALUES(@pers_exp,'C', 'одежда','clothes');
select @clothes := scope_identity();
INSERT INTO CATEGORY(CATEGORY_GROUP_ID, CATEGORY_GROUP_TYPE, NAME, DESCRIPTION) VALUES(@credit_exp,'C', 'вернул кредит','credit return');
select @credit_return := scope_identity();

INSERT INTO TAX (NAME,DESCRIPTION) VALUES('GST/TPS','The goods and services tax / La taxe sur les produits et services');
select @gst_tps := scope_identity();
INSERT INTO TAX_RATE (TAX_ID,RATE,DESCRIPTION,STARTDATE,ENDDATE) VALUES(@gst_tps,5,null,default,default);
select @qst_tvq5 := scope_identity();

INSERT INTO TAX (NAME,DESCRIPTION) VALUES('QST/TVQ','The Québec sales tax / La taxe de vente du Québec');
select @qst_tvq := scope_identity();
INSERT INTO TAX_RATE (TAX_ID,RATE,DESCRIPTION,STARTDATE,ENDDATE) VALUES(@qst_tvq,9.5,null,'2015-06-01 00:00:00',default);
select @qst_tvq95 := scope_identity();

INSERT INTO TAX (NAME,DESCRIPTION) VALUES('no tax','no tax');
select @no_tax := scope_identity();
INSERT INTO TAX_RATE (TAX_ID,RATE,DESCRIPTION,STARTDATE,ENDDATE) VALUES(@no_tax,default, null,default,default);
select @no_tax0 := scope_identity();

INSERT INTO ACCOUNT_GROUP (TYPE, NAME, DESCRIPTION) VALUES('D', 'cash', 'money in wallet');
select @cash := scope_identity();
INSERT INTO ACCOUNT_GROUP (TYPE, NAME, DESCRIPTION) VALUES('D', 'bank account', 'money on the bank account');
select @bank_acc := scope_identity();
INSERT INTO ACCOUNT_GROUP (TYPE, NAME, DESCRIPTION) VALUES('C', 'credit card', 'credit cards');
select @credit_card := scope_identity();

select @init_bal_cash_cad := 100;
INSERT INTO ACCOUNT (ACCOUNT_GROUP_ID, ACCOUNT_GROUP_TYPE, CURRENCY_ID, NAME, DESCRIPTION, INITIAL_BALANCE) VALUES(@cash,'D',@cad,'cash CAD','money in wallet cad', @init_bal_cash_cad);
select @cash_cad := scope_identity();

INSERT INTO INVOICE (ORGANIZATION_ID, COMMENT) VALUES(@job,'INITIAL CASH CAD BALANCE');
select @inv_init_bal_cad := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv_init_bal_cad, @salary, @no_tax, @husband,'INITIAL CASH CAD BALANCE','INITIAL CASH CAD BALANCE',@init_bal_cash_cad,'INITIAL BALANCE CAD comm');
UPDATE INVOICE SET BALANCE = @init_bal_cash_cad WHERE ID = @inv_init_bal;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@cash_cad,'D',@inv_init_bal_cad,'2016-01-31',@init_bal_cash_cad,DEFAULT,'trans comm');

select @init_bal_cash_usd := 100;
INSERT INTO ACCOUNT (ACCOUNT_GROUP_ID, ACCOUNT_GROUP_TYPE, CURRENCY_ID, NAME, DESCRIPTION, INITIAL_BALANCE) VALUES(@cash,'D',@usd,'cash USD','money in wallet usd', @init_bal_cash_usd);
select @cash_usd := scope_identity();

INSERT INTO INVOICE (ORGANIZATION_ID, COMMENT) VALUES(@job,'INITIAL CASH USD BALANCE');
select @inv_init_bal_usd := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv_init_bal_usd, @salary, @no_tax, @husband,'INITIAL CASH USD BALANCE','INITIAL CASH USD BALANCE',@init_bal_cash_usd,'INITIAL BALANCE USD comm');
UPDATE INVOICE SET BALANCE = @init_bal_cash_usd WHERE ID = @inv_init_bal_usd;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@cash_usd,'D',@inv_init_bal_usd,'2016-01-30',@init_bal_cash_usd,DEFAULT,'trans comm');


INSERT INTO ACCOUNT (ACCOUNT_GROUP_ID, ACCOUNT_GROUP_TYPE, CURRENCY_ID, NAME, DESCRIPTION, INITIAL_BALANCE,ACC_LIMIT) VALUES(@credit_card,'C',@cad,'visa 1234 CAD','credit card ???initial balance is a limit??', 0, 500);
select @visa_1234_cad := scope_identity();
INSERT INTO ACCOUNT (ACCOUNT_GROUP_ID, ACCOUNT_GROUP_TYPE, CURRENCY_ID, NAME, DESCRIPTION, INITIAL_BALANCE,ACC_LIMIT) VALUES(@credit_card,'C',@cad,'mastercard 4321 CAD','credit card ???initial balance is a limit??', 0, 500);
select @mastercard_4321_cad := scope_identity();
INSERT INTO ACCOUNT (ACCOUNT_GROUP_ID, ACCOUNT_GROUP_TYPE, CURRENCY_ID, NAME, DESCRIPTION, INITIAL_BALANCE,ACC_LIMIT) VALUES(@credit_card,'C',@usd,'american express 5678 USD','credit card ???initial balance is a limit??', 0, 500);
select @amex_5678_usd := scope_identity();

INSERT INTO BILL_COIN_AMT(ACCOUNT_ID,DATE,AMT_CENT1,AMT_CENT5,AMT_CENT10,AMT_CENT25,AMT_DOLLAR1,AMT_DOLLAR2,AMT_DOLLAR5,AMT_DOLLAR10,AMT_DOLLAR20,AMT_DOLLAR50,AMT_DOLLAR100)
VALUES(@cash_cad,'2016-02-03',0,0,0,0,0,0,0,0,0,0,1);

INSERT INTO ACCOUNT_BALANCE(ACCOUNT_ID, DATE, BALANCE) VALUES(@cash_cad,'2016-02-03',100);

INSERT INTO INVOICE (ORGANIZATION_ID, COMMENT) VALUES(@job,'SALARY comment');
select @inv1 := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv1, @salary, @no_tax, @husband,'ЗП','SALARY',1000,'salary comm');
SELECT @inv1_balance := (SELECT SUM(AMOUNT) FROM ITEM WHERE INVOICE_ID = @inv1);
UPDATE INVOICE SET BALANCE = @inv1_balance WHERE ID = @inv1;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@cash_cad,'D',@inv1,'2016-02-03',@inv1_balance,DEFAULT,'trans comm');

INSERT INTO INVOICE (ORGANIZATION_ID,COMMENT) VALUES(@walmart,'GROCERY comme');
select @inv2 := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv2, @food, @no_tax, @family,'хлеб','bread',4,'bread comm');
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv2, @food, @no_tax, @family,'масло','butter',3.69,'butter comm');
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv2, @food, @no_tax, @family,'колбаса','kolbasa',11.58,'kolbasa comm');
SELECT @inv2_balance := (SELECT SUM(AMOUNT) FROM ITEM WHERE INVOICE_ID = @inv2);
UPDATE INVOICE SET BALANCE = @inv2_balance WHERE ID = @inv2;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@visa_1234_cad,'C',@inv2,'2016-02-02',@inv2_balance,DEFAULT,'trans comm');

--zero credit card balance
INSERT INTO INVOICE (ORGANIZATION_ID,COMMENT) VALUES(@rbc,'bank comm');
select @inv3 := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv3, @credit_return, @no_tax, @family,'rbc visa_1234_cad period [2016-01-05 to 2016-02-05]',default,@inv2_balance,'return inv from walmart');
SELECT @inv3_balance := (SELECT SUM(AMOUNT) FROM ITEM WHERE INVOICE_ID = @inv3);
UPDATE INVOICE SET BALANCE = @inv3_balance WHERE ID = @inv3;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@cash_cad,'D',@inv3,'2016-02-02',DEFAULT,@inv3_balance,'transfer from cash to visa');
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@visa_1234_cad,'C',@inv3,'2016-02-02',DEFAULT,@inv3_balance,'transfer from cash to visa');

INSERT INTO INVOICE (ORGANIZATION_ID,COMMENT) VALUES(@walmart,'GROCERY2 comme');
select @inv4 := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv4, @food, @no_tax, @family,'печенье','cookies',2.5,'cookies comm');
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv4, @food, @no_tax, @family,'молоко','milk',6.58,'milk comm');
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv4, @food, @no_tax, @family,'дрожжи','yeast',4.69,'yeast comm');
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv4, @food, @no_tax, @family,'помидоры','tomato',5.69,'tomato comm');
SELECT @inv4_balance := (SELECT SUM(AMOUNT) FROM ITEM WHERE INVOICE_ID = @inv4);
UPDATE INVOICE SET BALANCE = @inv4_balance WHERE ID = @inv4;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@mastercard_4321_cad,'C',@inv4,'2016-02-05',@inv4_balance,DEFAULT,'trans comm');

--positive credit card balance (transfer more than credit card balance)
INSERT INTO INVOICE (ORGANIZATION_ID,COMMENT) VALUES(@rbc,'bank comm');
select @inv5 := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv5, @credit_return, @no_tax, @family,'rbc mastercard_4321_cad period [2016-01-05 to 2016-02-05]',default,@inv2_balance,'return inv from walmart');
SELECT @inv5_balance := (SELECT SUM(AMOUNT) FROM ITEM WHERE INVOICE_ID = @inv5);
UPDATE INVOICE SET BALANCE = @inv5_balance WHERE ID = @inv5;
select @return1 := 120;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@cash_cad,'D',@inv5,'2016-02-07',DEFAULT,@return1,'transfer from cash to mastercard_4321_cad');
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@mastercard_4321_cad,'C',@inv5,'2016-02-07',DEFAULT,@return1,'transfer from cash to mastercard_4321_cad');

--negative credit card balance (transfer less than credit card balance)
INSERT INTO INVOICE (ORGANIZATION_ID,COMMENT) VALUES(@walmart,'GROCERY3 comme');
select @inv6 := scope_identity();
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv6, @food, @no_tax, @husband,'молоко','milk',6.58,'milk comm');
INSERT INTO ITEM (INVOICE_ID, CATEGORY_ID, TAX_ID, FAMILY_MEMBER_ID, DESCRIPTION1, DESCRIPTION2, AMOUNT, COMMENT) VALUES(@inv6, @food, @no_tax, @husband,'сол огурцы','pickles',20,'pickles comm');
SELECT @inv6_balance := (SELECT SUM(AMOUNT) FROM ITEM WHERE INVOICE_ID = @inv6);
UPDATE INVOICE SET BALANCE = @inv6_balance WHERE ID = @inv4;
INSERT INTO TRANSACTION (ACCOUNT_ID, ACCOUNT_GROUP_TYPE, INVOICE_ID, DATE, DEBIT_AMOUNT, CREDIT_AMOUNT, COMMENT) VALUES(@amex_5678_usd,'C',@inv6,'2016-02-08',@inv6_balance,DEFAULT,'trans comm');


COMMIT;


