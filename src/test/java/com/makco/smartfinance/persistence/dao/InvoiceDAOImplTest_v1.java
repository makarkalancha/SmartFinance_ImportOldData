package com.makco.smartfinance.persistence.dao;

import com.google.common.collect.Lists;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.DateUnitDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.FamilyMemberDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.InvoiceDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.ItemDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.OrganizationDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.TaxDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Invoice_v1;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Item_v1;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Organization_v1;
import com.makco.smartfinance.persistence.utils.DateUnitUtil;
import com.makco.smartfinance.utils.Logs;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 17/07/2016
 * Time: 21:35
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InvoiceDAOImplTest_v1 {

    private static final Logger LOG = LogManager.getLogger(InvoiceDAOImplTest_v1.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private InvoiceDAOImpl_v1ForTest invoiceDAOImpl_v1ForTest = new InvoiceDAOImpl_v1ForTest();
    private OrganizationDAOImpl_v1ForTest organizationDAOImpl_v1ForTest = new OrganizationDAOImpl_v1ForTest();
    private ItemDAOImpl_v1ForTest itemDAOImpl_v1ForTest = new ItemDAOImpl_v1ForTest();
    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();
    private CategoryDAOImplForTest categoryDAOImplForTest = new CategoryDAOImplForTest();
    private TaxDAOImpl_v1ForTest taxDAO = new TaxDAOImpl_v1ForTest();
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImplForTest();
    private FamilyMemberDAOImpl_v1ForTest familyMemberDAO = new FamilyMemberDAOImpl_v1ForTest();

    public static final DateTimeFormatter INVOICE_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generateInvoiceNumber(int randomInt){
        return LocalDateTime.now().format(INVOICE_NUMBER_FORMAT) + randomInt;
    }

    @Test
    public void test_11_saveInvoice() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        //todo Caused by: org.hibernate.QueryException: could not resolve property: unitDate of: com.makco.smartfinance.persistence.entity.DateUnit

        List<DateUnit> dates = dateUnitDAO.dateUnitList();
        DateUnit dateUnit = new DateUnit(LocalDate.now());
        if(dates.isEmpty()) {
            dateUnitDAO.addDateUnit(dateUnit);
        } else {
            dateUnit = dates.get(0);
        }

        String organizationName = "OrgName" + randomInt;
        Organization_v1 organization = new Organization_v1(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
        Invoice_v1 invoice = new Invoice_v1(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}+{RATE}", "{NUM}+1", null, null, null);
        Tax tax2 = new Tax("tax2 " + randomInt, "tax2 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);
        taxDAO.saveOrUpdateTax(tax2);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v1 item1 = new Item_v1(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v1 item2 = new Item_v1(2, invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v1 item3 = new Item_v1(3, invoice, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v1 item4 = new Item_v1(4, invoice, category1, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v1>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);
        /*
        with two saveOrUpdateInvoice or just one number of queries is same:
        prep138: insert into TEST.DATEUNIT
        prep141: insert into TEST.CATEGORY_GROUP
        prep144: insert into TEST.CATEGORY
        prep147: insert into TEST.TAX
        prep150: insert into TEST.TAX
        prep153: insert into TEST.FAMILY_MEMBER
        prep162: insert into TEST.ORGANIZATION
        prep164: insert into TEST.INVOICE
        prep167: insert into TEST.ITEM

        saveOrUpdateInvoice-> didn't help
            session.flush();
            session.clear();
         */
//        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice); //works
//        invoiceDAOImpl_v1ForTest.update(invoice); //works
//        invoiceDAOImpl_v1ForTest.merge(invoice); //doesn't work
//        invoiceDAOImpl_v1ForTest.refresh(invoice); //doesn't work

        LOG.debug(">>>invoice: " + invoice);
        assert(invoice.getId() != null);
        assertEquals(4, invoice.getItems().size());
//        assertEquals(0, new BigDecimal("10").compareTo(invoice.getSubTotal()));//(!!!!) fail, but if you do another saveOrUpdateInvoice right after first one this line will be valid
//        assertEquals(0, new BigDecimal("12").compareTo(invoice.getTotal()));//(!!!!) fail, but if you do another saveOrUpdateInvoice right after first one this line will be valid
        assert(invoice.getCreatedOn() != null);
        assert(invoice.getUpdatedOn() != null);

        Invoice_v1 invoice1 = invoiceDAOImpl_v1ForTest.getInvoiceByIdWithItems(invoice.getId()); //pass
//        Invoice_v1 invoice1 = invoice; //(!!!!) fail, but if you do another saveOrUpdateInvoice right after first one this line will be valid
//        Invoice_v1 invoice1 = invoiceDAOImpl_v1ForTest.refresh(invoice);//fail
        LOG.debug(">>>invoice1: " + invoice1);
        assert(invoice1.getId() != null);
        assertEquals(4, invoice1.getItems().size());
        assertEquals(0, new BigDecimal("10").compareTo(invoice1.getSubTotal()));
        assertEquals(0, new BigDecimal("12").compareTo(invoice1.getTotal()));
        assert(invoice1.getCreatedOn() != null);
        assert(invoice1.getUpdatedOn() != null);

        List<Item_v1> items = new ArrayList<>(invoice1.getItems());
        for (int i = 0; i < items.size(); i++) {
            Item_v1 item_v1 = items.get(i);
            LOG.debug(">>>item_v1: " + item_v1);
            assert (item_v1.getId() != null);
            assertEquals((int)(i + 1), (int) item_v1.getOrderNumber());
            assert (item_v1.getCategory() != null);
            assert (item_v1.getTax() != null);
            assert (item_v1.getFamilyMember() != null);
            assert ((item_v1.getDateUnit() != null) && (item_v1.getDateUnit() == invoice1.getDateUnit()));
            assert (item_v1.getCreatedOn() != null);
            assert (item_v1.getUpdatedOn() != null);
        }
    }

    @Test
    public void test_21_updateInvoice_addingItem() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v1 organization = new Organization_v1(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
        Invoice_v1 invoice = new Invoice_v1(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}+{RATE}", "{NUM}+1", null, null, null);
        Tax tax2 = new Tax("tax2 " + randomInt, "tax2 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);
        taxDAO.saveOrUpdateTax(tax2);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v1 item1 = new Item_v1(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v1 item2 = new Item_v1(2, invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v1 item3 = new Item_v1(3, invoice, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v1 item4 = new Item_v1(4, invoice, category1, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v1>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);

        //UPDATING
        Item_v1 item5 = new Item_v1(invoice.getNextItemOrderNumber(), invoice, category1, tax1, familyMember, "desc14", "desc24", "comment", new BigDecimal("5"));
        invoice.setItems(new ArrayList<Item_v1>(){{
            addAll(invoice.getItems());
            add(item5);
        }});
        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);


        LOG.debug(">>>invoice: " + invoice);
        assert(invoice.getId() != null);
        assertEquals(5, invoice.getItems().size());
        assertEquals(0, new BigDecimal("15").compareTo(invoice.getSubTotal()));
        assertEquals(0, new BigDecimal("18").compareTo(invoice.getTotal()));
        assert(invoice.getCreatedOn() != null);
        assert(invoice.getUpdatedOn() != null);

//        Invoice_v1 invoice1 = invoiceDAOImpl_v1ForTest.getInvoiceByIdWithItems(invoice.getId()); //pass
        Invoice_v1 invoice1 = invoice; //fail
//        Invoice_v1 invoice1 = invoiceDAOImpl_v1ForTest.refresh(invoice);//fail
        LOG.debug(">>>invoice1: " + invoice1);
        assert(invoice1.getId() != null);
        assertEquals(5, invoice1.getItems().size());
        assertEquals(0, new BigDecimal("15").compareTo(invoice1.getSubTotal()));
        assertEquals(0, new BigDecimal("18").compareTo(invoice1.getTotal()));
        assert(invoice1.getCreatedOn() != null);
        assert(invoice1.getUpdatedOn() != null);

        List<Item_v1> items = new ArrayList<>(invoice1.getItems());
        for (int i = 0; i < items.size(); i++) {
            Item_v1 item_v1 = items.get(i);
            LOG.debug(">>>item_v1: " + item_v1);
            assert (item_v1.getId() != null);
            assertEquals((int)(i + 1), (int) item_v1.getOrderNumber());
            assert (item_v1.getCategory() != null);
            assert (item_v1.getTax() != null);
            assert (item_v1.getFamilyMember() != null);
            assert (item_v1.getDateUnit() != null);
            assert (item_v1.getCreatedOn() != null);
            assert (item_v1.getUpdatedOn() != null);
        }
    }

    @Test
    //todo
    public void test_22_updateInvoice_removingItem() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v1 organization = new Organization_v1(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
        Invoice_v1 invoice = new Invoice_v1(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}+{RATE}", "{NUM}+1", null, null, null);
        Tax tax2 = new Tax("tax2 " + randomInt, "tax2 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);
        taxDAO.saveOrUpdateTax(tax2);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v1 item1 = new Item_v1(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v1 item2 = new Item_v1(2, invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v1 item3 = new Item_v1(3, invoice, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v1 item4 = new Item_v1(4, invoice, category1, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v1>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);

        //UPDATING
        itemDAOImpl_v1ForTest.removeItem(item1.getId());

//        LOG.debug(">>>invoice: " + invoice);
//        assert(invoice.getId() != null);
//        assertEquals(4, invoice.getItems().size());
//        assertEquals(0, new BigDecimal("14").compareTo(invoice.getSubTotal()));
//        assertEquals(0, new BigDecimal("16").compareTo(invoice.getTotal()));
//        assert(invoice.getCreatedOn() != null);
//        assert(invoice.getUpdatedOn() != null);

//        Invoice_v1 invoice1 = invoiceDAOImpl_v1ForTest.getInvoiceByIdWithItems(invoice.getId()); //pass
        Invoice_v1 invoice1 = invoice; //fail
//        Invoice_v1 invoice1 = invoiceDAOImpl_v1ForTest.refresh(invoice);//fail
        LOG.debug(">>>invoice1: " + invoice1);
        assert(invoice1.getId() != null);
        assertEquals(5, invoice1.getItems().size());
        assertEquals(0, new BigDecimal("15").compareTo(invoice1.getSubTotal()));
        assertEquals(0, new BigDecimal("18").compareTo(invoice1.getTotal()));
        assert(invoice1.getCreatedOn() != null);
        assert(invoice1.getUpdatedOn() != null);

        List<Item_v1> items = new ArrayList<>(invoice1.getItems());
        for (int i = 0; i < items.size(); i++) {
            Item_v1 item_v1 = items.get(i);
            LOG.debug(">>>item_v1: " + item_v1);
            assert (item_v1.getId() != null);
            assertEquals((int)(i + 1), (int) item_v1.getOrderNumber());
            assert (item_v1.getCategory() != null);
            assert (item_v1.getTax() != null);
            assert (item_v1.getFamilyMember() != null);
            assert (item_v1.getDateUnit() != null);
            assert (item_v1.getCreatedOn() != null);
            assert (item_v1.getUpdatedOn() != null);
        }
    }

    @Test
    public void test_31_removeInvoice() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v1 organization = new Organization_v1(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
        Invoice_v1 invoice = new Invoice_v1(invoiceNumber, organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit" + randomInt, "desc");

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);
        categoryDAOImplForTest.saveOrUpdateCategory(category1);

        Tax tax1 = new Tax("tax1 " + randomInt, "tax1 desc", new BigDecimal("1"), "{NUM}+{RATE}", "{NUM}+1", null, null, null);
        Tax tax2 = new Tax("tax2 " + randomInt, "tax2 desc", new BigDecimal("1"), "{NUM}*{RATE}", "{NUM}*1", null, null, null);
        taxDAO.saveOrUpdateTax(tax1);
        taxDAO.saveOrUpdateTax(tax2);

        FamilyMember familyMember = new FamilyMember("FM " + randomInt, "family member desc");
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);

        Item_v1 item1 = new Item_v1(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v1 item2 = new Item_v1(2, invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v1 item3 = new Item_v1(3, invoice, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v1 item4 = new Item_v1(4, invoice, category1, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v1>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);

        LOG.debug(">>>invoice: " + invoice);
        assert(invoice.getId() != null);
        assertEquals(4, invoice.getItems().size());
//        assertEquals(new BigDecimal("10"), invoice.getSubTotal());
//        assertEquals(new BigDecimal("12"), invoice.getTotal());
        assert(invoice.getCreatedOn() != null);
        assert(invoice.getUpdatedOn() != null);

        long invoiceId = invoice.getId();
        long organizationId = invoice.getOrganization().getId();
        long itemId1 = item1.getId();
        long itemId2 = item2.getId();
        long itemId3 = item3.getId();
        long itemId4 = item4.getId();

        //todo fails here
        invoiceDAOImpl_v1ForTest.removeInvoice(invoiceId);

        Invoice_v1 invoice_v1_2 = invoiceDAOImpl_v1ForTest.getInvoiceByIdWithItems(invoiceId);
        Organization_v1 organization_v1_2 = organizationDAOImpl_v1ForTest.getOrganizationById(organizationId);
        Item_v1 item_v1_2_1= itemDAOImpl_v1ForTest.getItemById(itemId1);
        Item_v1 item_v1_2_2= itemDAOImpl_v1ForTest.getItemById(itemId2);
        Item_v1 item_v1_2_3= itemDAOImpl_v1ForTest.getItemById(itemId3);
        Item_v1 item_v1_2_4= itemDAOImpl_v1ForTest.getItemById(itemId4);
        LOG.debug(">>>invoice_2: " + invoice_v1_2);
        assert(invoice_v1_2 == null);
        LOG.debug(">>>organization_v1: " + organization_v1_2);
        assert(organization_v1_2 != null);
        LOG.debug(">>>item_v1_2_1: " + item_v1_2_1);
        assert(item_v1_2_1 == null);
        LOG.debug(">>>item_v1_2_2: " + item_v1_2_2);
        assert(item_v1_2_2 == null);
        LOG.debug(">>>item_v1_2_3: " + item_v1_2_3);
        assert(item_v1_2_3 == null);
        LOG.debug(">>>item_v1_2_4: " + item_v1_2_4);
        assert(item_v1_2_4 == null);

    }


    @Test
    public void test_Invoice_v1_benchmark() throws Exception{
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

        int random = randomWithinRange.getRandom();
        Long startProcessing;
        Long endProcessing;
        String testName = "benchmark invoice_v1 ";

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
        Category category21 = new CategoryCredit(categoryGroup1, "C cr2-1-" + random, testName + "category credit 2-1");
        Category category22 = new CategoryCredit(categoryGroup1, "C cr2-2-" + random, testName + "category credit 2-2");
        Category category23 = new CategoryCredit(categoryGroup1, "C cr2-3-" + random, testName + "category credit 2-3");

        CategoryGroup categoryGroup3 = new CategoryGroupCredit("CG cr3-" + random, testName + "category group credit 3");
        Category category31 = new CategoryCredit(categoryGroup1, "C cr3-1-" + random, testName + "category credit 3-1");
        Category category32 = new CategoryCredit(categoryGroup1, "C cr3-2-" + random, testName + "category credit 3-2");
        Category category33 = new CategoryCredit(categoryGroup1, "C cr3-3-" + random, testName + "category credit 3-3");
        Category category34 = new CategoryCredit(categoryGroup1, "C cr3-4-" + random, testName + "category credit 3-4");

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
        /////////////////////ITEM
        int itemAmount = 5;
        /////////////////////BENCHMARK
        int recordsAmount = 20_000;
//        int recordsAmount = 1_000;
        try(PrintWriter printWriter = new PrintWriter("test_Invoice_v1_benchmark.log", "UTF-8");) {
            for (int i = 0; i < recordsAmount; i++) {
                startProcessing = System.nanoTime();

                String organizationName = random + "-" + i;
                Organization_v1 organization = new Organization_v1(organizationName, "org desc " + organizationName);

                String invoiceNumber = random + "-" + i;
                DateUnit dateUnit = dateUnitList_2011ToNow.get(dateRandom.getRandom());
                Invoice_v1 invoice = new Invoice_v1(
                        invoiceNumber,
                        organization,
                        dateUnit,
                        "comment " + invoiceNumber);
                List<Item_v1> items = new ArrayList<>();
                for (int j = 0; j < itemAmount; j++) {
                    Item_v1 item = new Item_v1(
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

                invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);
                endProcessing = System.nanoTime();

                printWriter.append(">>>>invoice_v1" + i + "\ttime\t" + Logs.benchmarkCalcultaion(startProcessing, endProcessing) + "\tnanoTime\t" + (endProcessing - startProcessing));
                printWriter.append("\r\n");

                if (i % 10 == 0) {
                    Thread.sleep(100);
                }
            }
        }
    }
}
