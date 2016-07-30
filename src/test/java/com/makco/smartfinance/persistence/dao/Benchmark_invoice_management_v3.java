package com.makco.smartfinance.persistence.dao;

import com.google.common.collect.Lists;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.dao.dao_implementations.V_AccountAggregationDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.AccountDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.AccountGroupDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.DateUnitDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.FamilyMemberDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.InvoiceDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.ItemDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.OrganizationDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.TaxDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.TransactionDAOImpl_v3ForTest;
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
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.V_AccountAggregation_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Invoice_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Item_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Organization_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Transaction_v3;
import com.makco.smartfinance.persistence.utils.DateUnitUtil;
import com.makco.smartfinance.utils.Logs;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Makar Kalancha on 28 Jul 2016 at 17:46.
 */
public class Benchmark_invoice_management_v3 {
    /*
    TODO p357 scrollable result:
    One of the situations where you should scroll through the results of a query
    instead of loading them all into memory involves result sets that are too large to fit
    into memory.
     */
    //TODO p386 and section 20.1 cursor and batch processing: list too large

    private static final Logger LOG = LogManager.getLogger(Benchmark_invoice_management_v3.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private V_AccountAggregationDAOImpl_v3ForTest accountAggregateDAOImplV3ForTest = new V_AccountAggregationDAOImpl_v3ForTest();

    private TransactionDAOImpl_v3ForTest transactionDAOImpl_v3ForTest = new TransactionDAOImpl_v3ForTest();
    private AccountDAOImpl_v1ForTest accountDAOImpl_v1ForTest = new AccountDAOImpl_v1ForTest();
    private AccountGroupDAOImpl_v1ForTest accountGroupDAOImpl_v1ForTest = new AccountGroupDAOImpl_v1ForTest();

    private InvoiceDAOImpl_v3ForTest invoiceDAOImpl_v3ForTest = new InvoiceDAOImpl_v3ForTest();
    private InvoiceDAOImplTest_v3 invoiceDAOImplTest_v3 = new InvoiceDAOImplTest_v3();

    private ItemDAOImpl_v3ForTest itemDAOImpl_v3ForTest = new ItemDAOImpl_v3ForTest();
    private OrganizationDAOImpl_v3ForTest organizationDAOImpl_v3ForTest = new OrganizationDAOImpl_v3ForTest();
    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();
    private CategoryDAOImplForTest categoryDAOImplForTest = new CategoryDAOImplForTest();
    private TaxDAOImpl_v1ForTest taxDAO = new TaxDAOImpl_v1ForTest();
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImplForTest();
    private FamilyMemberDAOImpl_v1ForTest familyMemberDAO = new FamilyMemberDAOImpl_v1ForTest();

    public static final DateTimeFormatter TRANSACTION_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private Invoice_v3 generateInvoiceDebitCredit(int randomInt, int multiplicator) throws Exception{
        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = invoiceDAOImplTest_v3.generateInvoiceNumber(randomInt);
        Invoice_v3 invoice = new Invoice_v3(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupDebit("CG debit" + randomInt, "desc");
        Category category1 = new CategoryDebit(categoryGroup1, "C debit" + randomInt, "desc");

        CategoryGroup categoryGroup2 = new CategoryGroupCredit("CG credit" + randomInt, "desc");
        Category category2 = new CategoryCredit(categoryGroup2, "C credit" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup2);
        categoryDAOImplForTest.saveOrUpdateCategory(category2);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}+{RATE}", "{NUM}+1", null, null, null);
        Tax tax2 = new Tax("tax2 " + randomInt, "tax2 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);
        taxDAO.saveOrUpdateTax(tax2);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        BigDecimal multiplicatorBD = new BigDecimal(Integer.toString(multiplicator));

        Item_v3 item1 = new Item_v3(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1").multiply(multiplicatorBD));
        Item_v3 item2 = new Item_v3(2, invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2").multiply(multiplicatorBD));
        Item_v3 item3 = new Item_v3(3, invoice, category2, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3").multiply(multiplicatorBD));
        Item_v3 item4 = new Item_v3(4, invoice, category2, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4").multiply(multiplicatorBD));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});
        return invoice;
    }

    public String generateTransactionNumber(int randomInt){
        return LocalDateTime.now().format(TRANSACTION_NUMBER_FORMAT) + randomInt;
    }

    //    @Test
    public void test_Invoice_v3_benchmark() throws Exception{
        /*
        comment any logs in triggers and daos
        dates: amount = from 2011-01-01 to today randomly choose
        family member: 3 random
        category: 10 random
        tax: 3 random

        count time for creation of:
        organization
        invoice
        item
        create around 20_000 invoices with organizations
         */
        int recordsAmount = 20_000;
//        int recordsAmount = 1_000;

        int random = randomWithinRange.getRandom();
        Long startProcessing;
        Long endProcessing;
        String testName = "benchmark invoice_v3 ";

        /////////////////////DATES
        LocalDate Jan_01_2011 = LocalDate.of(2011, Month.JANUARY, 1);
        LocalDate now = LocalDate.now();

        List<DateUnit> dateUnitList_2011ToNow = DateUnitUtil.getListOfDateUnitEntities(Jan_01_2011, now);

        Set<DateUnit> dateUnitList_toCreate_Set = new HashSet<>(dateUnitList_2011ToNow);
        Set<DateUnit> dateUnitList_DB_Set = new HashSet<>(dateUnitDAO.dateUnitList());
        dateUnitList_toCreate_Set.removeAll(dateUnitList_DB_Set);
        List<DateUnit> dateUnitList_toCreate_List = new ArrayList<>(dateUnitList_toCreate_Set);

        for(List<DateUnit> batch : Lists.partition(dateUnitList_toCreate_List, DataBaseConstants.BATCH_SIZE)) {
            dateUnitDAO.addDateUnitList(batch);
        }

        RandomWithinRange dateRandom = new RandomWithinRange(0, dateUnitList_2011ToNow.size() - 1);
        /////////////////////Family
        FamilyMember familyMember1 = new FamilyMember("FM-1-" + random, testName + "family member1 desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember1);

        FamilyMember familyMember2 = new FamilyMember("FM-2-" + random, testName + "family member2 desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember2);

        FamilyMember familyMember3 = new FamilyMember("FM-3-" + random, testName + "family member3 desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember3);

        List<FamilyMember> familyMemberList = new ArrayList<>();
        familyMemberList.add(familyMember1);
        familyMemberList.add(familyMember2);
        familyMemberList.add(familyMember3);
        RandomWithinRange familyRandom = new RandomWithinRange(0, familyMemberList.size() - 1);
        /////////////////////Category
        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG cr1-" + random, testName + "category group credit 1");
        Category category11 = new CategoryCredit(categoryGroup1, "C cr1-1-" + random, testName + "category credit 1-1");
        Category category12 = new CategoryCredit(categoryGroup1, "C cr1-2-" + random, testName + "category credit 1-2");
        Category category13 = new CategoryCredit(categoryGroup1, "C cr1-3-" + random, testName + "category credit 1-3");

        CategoryGroup categoryGroup2 = new CategoryGroupCredit("CG cr2-" + random, testName + "category group credit 2");
        Category category21 = new CategoryCredit(categoryGroup2, "C cr2-1-" + random, testName + "category credit 2-1");
        Category category22 = new CategoryCredit(categoryGroup2, "C cr2-2-" + random, testName + "category credit 2-2");
        Category category23 = new CategoryCredit(categoryGroup2, "C cr2-3-" + random, testName + "category credit 2-3");

        CategoryGroup categoryGroup3 = new CategoryGroupDebit("CG dt3-" + random, testName + "category group debit 3");
        Category category31 = new CategoryDebit(categoryGroup3, "C dt3-1-" + random, testName + "category debit 3-1");
        Category category32 = new CategoryDebit(categoryGroup3, "C dt3-2-" + random, testName + "category debit 3-2");
        Category category33 = new CategoryDebit(categoryGroup3, "C dt3-3-" + random, testName + "category debit 3-3");
        Category category34 = new CategoryDebit(categoryGroup3, "C dt3-4-" + random, testName + "category debit 3-4");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup2);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup3);
        categoryDAOImplForTest.saveOrUpdateCategory(category11);
        categoryDAOImplForTest.saveOrUpdateCategory(category12);
        categoryDAOImplForTest.saveOrUpdateCategory(category13);
        categoryDAOImplForTest.saveOrUpdateCategory(category21);
        categoryDAOImplForTest.saveOrUpdateCategory(category22);
        categoryDAOImplForTest.saveOrUpdateCategory(category23);
        categoryDAOImplForTest.saveOrUpdateCategory(category31);
        categoryDAOImplForTest.saveOrUpdateCategory(category32);
        categoryDAOImplForTest.saveOrUpdateCategory(category33);
        categoryDAOImplForTest.saveOrUpdateCategory(category34);

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category11);
        categoryList.add(category12);
        categoryList.add(category13);
        categoryList.add(category21);
        categoryList.add(category22);
        categoryList.add(category23);
        categoryList.add(category31);
        categoryList.add(category32);
        categoryList.add(category33);
        categoryList.add(category34);
        RandomWithinRange categoryRandom = new RandomWithinRange(0, categoryList.size() - 1);
        /////////////////////Tax
        Tax tax1 = new Tax("TX-1-" + random, testName + "tax1 desc", new BigDecimal("1"), "{NUM}+{RATE}", "{NUM}+1", null, null, null);
        Tax tax2 = new Tax("TX-2-" + random, testName + "tax2 desc", new BigDecimal("2"), "{NUM}+{RATE}*2", "{NUM}+2*2", null, null, null);
        Tax tax3 = new Tax("TX-3-" + random, " tax3 desc", new BigDecimal("3"), "{NUM}*{RATE}", "{NUM}*3", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);
        taxDAO.saveOrUpdateTax(tax2);
        taxDAO.saveOrUpdateTax(tax3);

        List<Tax> taxList = new ArrayList<>();
        taxList.add(tax1);
        taxList.add(tax2);
        taxList.add(tax3);
        RandomWithinRange taxRandom = new RandomWithinRange(0, taxList.size() - 1);
        /////////////////////Organization
        Organization_v3 organization1 = new Organization_v3("createdBefore1" + random, "organization1 created before invoice_management loop desc " + random);
        Organization_v3 organization2 = new Organization_v3("createdBefore2" + random, "organization2 created before invoice_management loop desc " + random);
        Organization_v3 organization3 = new Organization_v3("createdBefore3" + random, "organization3 created before invoice_management loop desc " + random);
        Organization_v3 organization4 = new Organization_v3("createdBefore4" + random, "organization4 created before invoice_management loop desc " + random);
        Organization_v3 organization5 = new Organization_v3("createdBefore5" + random, "organization5 created before invoice_management loop desc " + random);
        Organization_v3 organization6 = new Organization_v3("createdBefore6" + random, "organization6 created before invoice_management loop desc " + random);
        Organization_v3 organization7 = new Organization_v3("createdBefore7" + random, "organization7 created before invoice_management loop desc " + random);
        Organization_v3 organization8 = new Organization_v3("createdBefore8" + random, "organization8 created before invoice_management loop desc " + random);
        Organization_v3 organization9 = new Organization_v3("createdBefore9" + random, "organization9 created before invoice_management loop desc " + random);
        Organization_v3 organization10 = new Organization_v3("createdBefore10" + random, "organization10 created before invoice_management loop desc " + random);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization1);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization2);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization3);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization4);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization5);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization6);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization7);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization8);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization9);
        organizationDAOImpl_v3ForTest.saveOrUpdateOrganization(organization10);
        
        List<Organization_v3> organizationList = new ArrayList<>();
        organizationList.add(organization1);
        organizationList.add(organization2);
        organizationList.add(organization3);
        organizationList.add(organization4);
        organizationList.add(organization5);
        organizationList.add(organization6);
        organizationList.add(organization7);
        organizationList.add(organization8);
        organizationList.add(organization9);
        organizationList.add(organization10);
        RandomWithinRange organizationRandomWithinRange = new RandomWithinRange(0, (organizationList.size() - 1) + ((organizationList.size() - 1) / 3));
        /////////////////////Account
        AccountGroup accountGroup1 = new AccountGroupCredit("AG cr1-" + random, testName + "account group credit 1");
        Account account11 = new AccountCredit(accountGroup1, "A cr1-1-" + random, testName + "account credit 1-1");
        Account account12 = new AccountCredit(accountGroup1, "A cr1-2-" + random, testName + "account credit 1-2");
        Account account13 = new AccountCredit(accountGroup1, "A cr1-3-" + random, testName + "account credit 1-3");

        AccountGroup accountGroup2 = new AccountGroupCredit("AG cr2-" + random, testName + "account group credit 2");
        Account account21 = new AccountCredit(accountGroup2, "A cr2-1-" + random, testName + "account credit 2-1");
        Account account22 = new AccountCredit(accountGroup2, "A cr2-2-" + random, testName + "account credit 2-2");
        Account account23 = new AccountCredit(accountGroup2, "A cr2-3-" + random, testName + "account credit 2-3");

        AccountGroup accountGroup3 = new AccountGroupDebit("AG dt3-" + random, testName + "account group debit 3");
        Account account31 = new AccountDebit(accountGroup3, "A dt3-1-" + random, testName + "account debit 3-1");
        Account account32 = new AccountDebit(accountGroup3, "A dt3-2-" + random, testName + "account debit 3-2");
        Account account33 = new AccountDebit(accountGroup3, "A dt3-3-" + random, testName + "account debit 3-3");
        Account account34 = new AccountDebit(accountGroup3, "A dt3-4-" + random, testName + "account debit 3-4");

        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup1);
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup2);
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup3);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account11);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account12);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account13);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account21);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account22);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account23);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account31);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account32);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account33);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account34);

        List<Account> accountList = new ArrayList<>();
        accountList.add(account11);
        accountList.add(account12);
        accountList.add(account13);
        accountList.add(account21);
        accountList.add(account22);
        accountList.add(account23);
        accountList.add(account31);
        accountList.add(account32);
        accountList.add(account33);
        accountList.add(account34);
        RandomWithinRange accountRandom = new RandomWithinRange(0, accountList.size() - 1);
        /////////////////////ITEM
        int itemAmount = 5;
        /////////////////////BENCHMARK
        try(PrintWriter printWriter = new PrintWriter("test_Invoice_v3_benchmark.log", "UTF-8");) {
            for (int i = 0; i < recordsAmount; i++) {
                startProcessing = System.nanoTime();

                int organizationRandom = organizationRandomWithinRange.getRandom();
                Organization_v3 organization = null;
                if(organizationRandom >= organizationList.size()) {
                    String organizationName = random + "-i" + i;
                    organization = new Organization_v3(organizationName, "org desc " + organizationName);
                }else{
                    organization = organizationList.get(organizationRandom);
                }
                
                String invoiceNumber = random + "-" + i;
                DateUnit dateUnit = dateUnitList_2011ToNow.get(dateRandom.getRandom());
                Invoice_v3 invoice = new Invoice_v3(
                        invoiceNumber,
                        organization,
                        dateUnit,
                        "comment " + invoiceNumber);
                List<Item_v3> items = new ArrayList<>();
                for (int j = 0; j < itemAmount; j++) {
                    Item_v3 item = new Item_v3(
                            (j + 1),
                            invoice,
                            categoryList.get(categoryRandom.getRandom()),
                            taxList.get(taxRandom.getRandom()),
                            familyMemberList.get(familyRandom.getRandom()),
                            "desc11",
                            "desc21",
                            "comment",
                            new BigDecimal(Integer.toString(j)));
                    items.add(item);
                }

                invoice.setItems(items);

                invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoice);

                String transactionNumber = random + "-t" + i;
//                Transaction_v3 transaction = new Transaction_v3(
//                        transactionNumber,
//                        accountList.get(accountRandom.getRandom()),
//                        invoice,
//                        dateUnit,
//                        "comment " + transactionNumber + " for " + invoiceNumber,
//                        invoice.getDebitTotal(),
//                        invoice.getCreditTotal()
//                );
                /*
                if matters total for debit/credit account, for example:
                invoice with credit categories, but one debit as discount for the item
                we want to see in account report only total,
                like credit account total credit amount (credit - debit),
                we don't want to see debit as if we returned item and were refunded
                or paid a credit
                 */
                BigDecimal totalDebit = new BigDecimal("0");
                BigDecimal totalCredit = new BigDecimal("0");
                Account randomAccount = accountList.get(accountRandom.getRandom());
                if(randomAccount.getAccountGroupType().equals(DataBaseConstants.ACCOUNT_GROUP_TYPE.CREDIT)){
                    totalCredit = invoice.getCreditTotal().subtract(invoice.getCreditTotal());
                } else if (randomAccount.getAccountGroupType().equals(DataBaseConstants.ACCOUNT_GROUP_TYPE.CREDIT)) {
                    totalDebit = invoice.getCreditTotal().subtract(invoice.getCreditTotal());
                }

                Transaction_v3 transaction = new Transaction_v3(
                        transactionNumber,
                        randomAccount,
                        invoice,
                        dateUnit,
                        "comment " + transactionNumber + " for " + invoiceNumber,
                        totalDebit,
                        totalCredit
                        );

                transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transaction);
                endProcessing = System.nanoTime();

                printWriter.append(">>>>_v3-" + i + "\ttime\t" + Logs.benchmarkCalcultaion(startProcessing, endProcessing) + "\tnanoTime\t" + (endProcessing - startProcessing));
                printWriter.append("\r\n");

                if (i % 100 == 0) {
                    Thread.sleep(100);
                }
            }
        }
    }
}
