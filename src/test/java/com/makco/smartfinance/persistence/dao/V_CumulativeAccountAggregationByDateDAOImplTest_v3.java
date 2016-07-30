package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.h2db.utils.H2DbUtilsTest;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.persistence.dao.dao_implementations.AccountDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.AccountGroupDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.DateUnitDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.FamilyMemberDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.InvoiceDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.TaxDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.TransactionDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.V_AccountAggregationDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.V_CumulativeAccountAggregationByDateDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.persistence.entity.AccountCredit;
import com.makco.smartfinance.persistence.entity.AccountDebit;
import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.persistence.entity.AccountGroupCredit;
import com.makco.smartfinance.persistence.entity.AccountGroupDebit;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryCredit;
import com.makco.smartfinance.persistence.entity.CategoryDebit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Invoice_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Item_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Organization_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Transaction_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.V_AccountAggregation_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.V_CumulativeAccountAggregationByDate_v3;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 29/07/2016
 * Time: 23:16
 */
public class V_CumulativeAccountAggregationByDateDAOImplTest_v3 {
    private static final Logger LOG = LogManager.getLogger(V_CumulativeAccountAggregationByDateDAOImplTest_v3.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private TransactionDAOImpl_v3ForTest transactionDAOImpl_v3ForTest = new TransactionDAOImpl_v3ForTest();
    private TransactionDAOImplTest_v3 transactionDAOImplTest_v3 = new TransactionDAOImplTest_v3();

    private V_AccountAggregationDAOImpl_v3ForTest accountAggregateDAOImplV3ForTest = new V_AccountAggregationDAOImpl_v3ForTest();
    private V_CumulativeAccountAggregationByDateDAOImpl_v3ForTest cumulativeAccountAggregationByDateDAOImpl_v3ForTest =
            new V_CumulativeAccountAggregationByDateDAOImpl_v3ForTest();

    private InvoiceDAOImpl_v3ForTest invoiceDAOImpl_v3ForTest = new InvoiceDAOImpl_v3ForTest();
    private InvoiceDAOImplTest_v3 invoiceDAOImplTest_v3 = new InvoiceDAOImplTest_v3();

    private AccountDAOImpl_v1ForTest accountDAOImpl_v1ForTest = new AccountDAOImpl_v1ForTest();
    private AccountGroupDAOImpl_v1ForTest accountGroupDAOImpl_v1ForTest = new AccountGroupDAOImpl_v1ForTest();

    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();
    private CategoryDAOImplForTest categoryDAOImplForTest = new CategoryDAOImplForTest();
    private TaxDAOImpl_v1ForTest taxDAO = new TaxDAOImpl_v1ForTest();
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImplForTest();
    private FamilyMemberDAOImpl_v1ForTest familyMemberDAO = new FamilyMemberDAOImpl_v1ForTest();

    public static final DateTimeFormatter TRANSACTION_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Test
    public void test_41_selectCumulativeAccountAggregationByDate() throws Exception {
        int randomInt1 = randomWithinRange.getRandom();
        Invoice_v3 invoiceV3_1 = transactionDAOImplTest_v3.generateInvoiceDebitCredit(randomInt1, 1);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_1);
        AccountGroup accountGroup1 = new AccountGroupDebit("AG debit" + randomInt1, "desc");
        Account account1 = new AccountDebit(accountGroup1, "A debit" + randomInt1, "desc");
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup1);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account1);
        Transaction_v3 transactionV3_1 = new Transaction_v3(
                transactionDAOImplTest_v3.generateTransactionNumber(randomInt1),
                account1,
                invoiceV3_1,
                invoiceV3_1.getDateUnit(),
                "comment" + randomInt1,
                invoiceV3_1.getDebitTotal(),
                invoiceV3_1.getCreditTotal()
        );
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transactionV3_1);

        int randomInt2 = randomWithinRange.getRandom();
        Invoice_v3 invoiceV3_2 = transactionDAOImplTest_v3.generateInvoiceDebitCredit(randomInt2, 2);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_2);
        AccountGroup accountGroup2 = new AccountGroupCredit("AG credit" + randomInt2, "desc");
        Account account2 = new AccountCredit(accountGroup2, "A credit" + randomInt2, "desc");
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup2);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account2);
        Transaction_v3 transactionV3_2 = new Transaction_v3(
                transactionDAOImplTest_v3.generateTransactionNumber(randomInt2),
                account2,
                invoiceV3_2,
                invoiceV3_2.getDateUnit(),
                "comment" + randomInt2,
                invoiceV3_2.getDebitTotal(),
                invoiceV3_2.getCreditTotal()
        );
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transactionV3_2);

        int randomInt3 = randomWithinRange.getRandom();
        Invoice_v3 invoiceV3_3 = transactionDAOImplTest_v3.generateInvoiceDebitCredit(randomInt3, 3);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_3);
        AccountGroup accountGroup3 = new AccountGroupCredit("AG credit3" + randomInt3, "desc");
        Account account3 = new AccountCredit(accountGroup3, "A credit3" + randomInt3, "desc");
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup3);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account3);
        Transaction_v3 transactionV3_3 = new Transaction_v3(
                transactionDAOImplTest_v3.generateTransactionNumber(randomInt3),
                account3,
                invoiceV3_3,
                invoiceV3_3.getDateUnit(),
                "comment" + randomInt3,
                invoiceV3_3.getDebitTotal(),
                invoiceV3_3.getCreditTotal()
        );
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transactionV3_3);

        List<V_AccountAggregation_v3> accountAggregateV3s = accountAggregateDAOImplV3ForTest.accountsAggregate();
        accountAggregateV3s.forEach(accountAggregateV3 ->
                LOG.debug(">>>>" + accountAggregateV3)
        );

        List<V_CumulativeAccountAggregationByDate_v3> cumulativeAccountAggregateV3s = cumulativeAccountAggregationByDateDAOImpl_v3ForTest.cumulativeAccountsAggregation();
        cumulativeAccountAggregateV3s.forEach(cumulativeAccountAggregateV3 ->
                LOG.debug(">>>>" + cumulativeAccountAggregateV3)
        );
    }

    public Invoice_v3 generateInvoice_1(LocalDate localDate) throws Exception{
        DateUnit dateUnit = new DateUnit(localDate);
        dateUnitDAO.addDateUnit(dateUnit);

        int randomInt = randomWithinRange.getRandom();

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = invoiceDAOImplTest_v3.generateInvoiceNumber(randomInt);
        Invoice_v3 invoice = new Invoice_v3(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit1" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit1" + randomInt, "desc");

        CategoryGroup categoryGroup2 = new CategoryGroupCredit("CG credit2" + randomInt, "desc");
        Category category2 = new CategoryCredit(categoryGroup2, "C credit2" + randomInt, "desc");

        CategoryGroup categoryGroup3 = new CategoryGroupDebit("CG debit3" + randomInt, "desc");
        Category category3 = new CategoryDebit(categoryGroup3, "C debit3" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup2);
        categoryDAOImplForTest.saveOrUpdateCategory(category2);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup3);
        categoryDAOImplForTest.saveOrUpdateCategory(category3);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v3 item1 = new Item_v3(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v3 item2 = new Item_v3(2, invoice, category2, tax1, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v3 item3 = new Item_v3(3, invoice, category2, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v3 item4 = new Item_v3(4, invoice, category3, tax1, familyMember, "desc14", "desc24", "comment", new BigDecimal("1"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});
        LOG.debug(">>>>invoice" + invoice);
        return invoice;
    }

    public Invoice_v3 generateInvoice_2(LocalDate localDate) throws Exception{
        DateUnit dateUnit = new DateUnit(localDate);
        dateUnitDAO.addDateUnit(dateUnit);

        int randomInt = randomWithinRange.getRandom();

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = invoiceDAOImplTest_v3.generateInvoiceNumber(randomInt);
        Invoice_v3 invoice = new Invoice_v3(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit1" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit1" + randomInt, "desc");

        CategoryGroup categoryGroup2 = new CategoryGroupCredit("CG credit2" + randomInt, "desc");
        Category category2 = new CategoryCredit(categoryGroup2, "C credit2" + randomInt, "desc");

        CategoryGroup categoryGroup3 = new CategoryGroupDebit("CG debit3" + randomInt, "desc");
        Category category3 = new CategoryDebit(categoryGroup3, "C debit3" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup2);
        categoryDAOImplForTest.saveOrUpdateCategory(category2);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup3);
        categoryDAOImplForTest.saveOrUpdateCategory(category3);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v3 item1 = new Item_v3(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("2"));
        Item_v3 item2 = new Item_v3(2, invoice, category2, tax1, familyMember, "desc12", "desc22", "comment", new BigDecimal("4"));
        Item_v3 item3 = new Item_v3(3, invoice, category2, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("6"));
        Item_v3 item4 = new Item_v3(4, invoice, category3, tax1, familyMember, "desc14", "desc24", "comment", new BigDecimal("1"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});
        LOG.debug(">>>>invoice" + invoice);
        return invoice;
    }

    public Invoice_v3 generateInvoice_3(LocalDate localDate) throws Exception{
        DateUnit dateUnit = new DateUnit(localDate);
        dateUnitDAO.addDateUnit(dateUnit);

        int randomInt = randomWithinRange.getRandom();

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = invoiceDAOImplTest_v3.generateInvoiceNumber(randomInt);
        Invoice_v3 invoice = new Invoice_v3(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit1" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit1" + randomInt, "desc");

        CategoryGroup categoryGroup2 = new CategoryGroupCredit("CG credit2" + randomInt, "desc");
        Category category2 = new CategoryCredit(categoryGroup2, "C credit2" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup2);
        categoryDAOImplForTest.saveOrUpdateCategory(category2);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v3 item1 = new Item_v3(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("3"));
        Item_v3 item2 = new Item_v3(2, invoice, category2, tax1, familyMember, "desc12", "desc22", "comment", new BigDecimal("6"));
        Item_v3 item3 = new Item_v3(3, invoice, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("9"));
        Item_v3 item4 = new Item_v3(4, invoice, category2, tax1, familyMember, "desc14", "desc24", "comment", new BigDecimal("10"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});
        LOG.debug(">>>>invoice" + invoice);
        return invoice;
    }

    public Invoice_v3 generateInvoice_4(LocalDate localDate) throws Exception{
        DateUnit dateUnit = new DateUnit(localDate);
        dateUnitDAO.addDateUnit(dateUnit);

        int randomInt = randomWithinRange.getRandom();

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = invoiceDAOImplTest_v3.generateInvoiceNumber(randomInt);
        Invoice_v3 invoice = new Invoice_v3(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit1" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit1" + randomInt, "desc");

        CategoryGroup categoryGroup2 = new CategoryGroupCredit("CG credit2" + randomInt, "desc");
        Category category2 = new CategoryCredit(categoryGroup2, "C credit2" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup2);
        categoryDAOImplForTest.saveOrUpdateCategory(category2);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v3 item1 = new Item_v3(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("4"));
        Item_v3 item2 = new Item_v3(2, invoice, category2, tax1, familyMember, "desc12", "desc22", "comment", new BigDecimal("8"));
        Item_v3 item3 = new Item_v3(3, invoice, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("12"));
        Item_v3 item4 = new Item_v3(4, invoice, category2, tax1, familyMember, "desc14", "desc24", "comment", new BigDecimal("10"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});
        LOG.debug(">>>>invoice" + invoice);
        return invoice;
    }

    public Invoice_v3 generateInvoice_5(LocalDate localDate) throws Exception{
        DateUnit dateUnit = new DateUnit(localDate);
        dateUnitDAO.addDateUnit(dateUnit);

        int randomInt = randomWithinRange.getRandom();

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = invoiceDAOImplTest_v3.generateInvoiceNumber(randomInt);
        Invoice_v3 invoice = new Invoice_v3(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupDebit("CG debit1" + randomInt, "desc");
        Category category1 = new CategoryDebit(categoryGroup1, "C debit1" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v3 item1 = new Item_v3(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("500"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
        }});
        LOG.debug(">>>>invoice" + invoice);
        return invoice;
    }

    public String generateTransactionNumber(int randomInt){
        return LocalDateTime.now().format(TRANSACTION_NUMBER_FORMAT) + randomInt;
    }

    private Transaction_v3 createTransaction(int randomInt, Invoice_v3 invoiceV3, Account account){
        return new Transaction_v3(
                transactionDAOImplTest_v3.generateTransactionNumber(randomInt),
                account,
                invoiceV3,
                invoiceV3.getDateUnit(),
                "comment" + randomInt,
                invoiceV3.getDebitTotal(),
                invoiceV3.getCreditTotal()
        );
    }

    @Test
    /*
    like in file V_CumulativeAccountAggregationByDate.ods
    to clean transactions use script
    \SmartFinance\database_engine\src\test\java\com\makco\smartfinance\h2db\triggers\TriggerTransactionTest.java
    setUpClass()

    there's no need in the same method but for Credit Account data will be the same
     */
    //todo in order test to work empty tables from test

    public void test_42_selectCumulativeAccountAggregationByDate_AccountDebit() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        LocalDate localDate = LocalDate.of(1970, Month.MARCH, 1);

        Invoice_v3 invoiceV3_1 = generateInvoice_1(localDate);
        Invoice_v3 invoiceV3_2 = generateInvoice_2(localDate.plus(1, ChronoUnit.DAYS));
        Invoice_v3 invoiceV3_3 = generateInvoice_3(localDate.plus(1, ChronoUnit.DAYS));
        Invoice_v3 invoiceV3_4 = generateInvoice_4(localDate.plus(2, ChronoUnit.DAYS));
        Invoice_v3 invoiceV3_5 = generateInvoice_5(localDate.plus(3, ChronoUnit.DAYS));
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_1);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_2);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_3);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_4);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_5);

        AccountGroup accountGroup1 = new AccountGroupDebit("AG debit" + randomInt, "desc");
        Account account1 = new AccountDebit(accountGroup1, "A debit" + randomInt, "desc");
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup1);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account1);

        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(createTransaction(randomInt, invoiceV3_1, account1));
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(createTransaction(randomInt + 1, invoiceV3_2, account1));
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(createTransaction(randomInt + 2, invoiceV3_3, account1));
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(createTransaction(randomInt + 3, invoiceV3_4, account1));
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(createTransaction(randomInt + 4, invoiceV3_5, account1));

        List<V_CumulativeAccountAggregationByDate_v3> cumulativeAccountAggregateV3s = cumulativeAccountAggregationByDateDAOImpl_v3ForTest.cumulativeAccountsAggregation();
        cumulativeAccountAggregateV3s.forEach(cumulativeAccountAggregateV3 ->
                LOG.debug(">>>>" + cumulativeAccountAggregateV3)
        );
        assertEquals(new BigDecimal("1.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumDebit());
        assertEquals(new BigDecimal("6.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumCredit());

        assertEquals(new BigDecimal("2.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumDebit());
        assertEquals(new BigDecimal("46.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumCredit());

        assertEquals(new BigDecimal("2.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumDebit());
        assertEquals(new BigDecimal("80.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumCredit());

        assertEquals(new BigDecimal("502.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumDebit());
        assertEquals(new BigDecimal("80.0000"),cumulativeAccountAggregateV3s.get(0).getCumSumCredit());
    }

    //todo several accounts, use same invoices, but different accounts

}
