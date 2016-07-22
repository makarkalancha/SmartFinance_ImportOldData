package com.makco.smartfinance.persistence.dao;

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
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

        Item_v1 item1 = new Item_v1(1, invoice, category1, tax1, familyMember, dateUnit, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v1 item2 = new Item_v1(2, invoice, category1, tax2, familyMember, dateUnit, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v1 item3 = new Item_v1(3, invoice, category1, tax1, familyMember, dateUnit, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v1 item4 = new Item_v1(4, invoice, category1, tax2, familyMember, dateUnit, "desc14", "desc24", "comment", new BigDecimal("4"));

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
         */
//        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);

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
            assert (item_v1.getDateUnit() != null);
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

        Item_v1 item1 = new Item_v1(1, invoice, category1, tax1, familyMember, dateUnit, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v1 item2 = new Item_v1(2, invoice, category1, tax2, familyMember, dateUnit, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v1 item3 = new Item_v1(3, invoice, category1, tax1, familyMember, dateUnit, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v1 item4 = new Item_v1(4, invoice, category1, tax2, familyMember, dateUnit, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v1>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);
        invoiceDAOImpl_v1ForTest.saveOrUpdateInvoice(invoice);

        LOG.debug(">>>invoice: " + invoice);
        assert(invoice.getId() != null);
        assertEquals(4, invoice.getItems().size());
        assertEquals(0, new BigDecimal("10").compareTo(invoice.getSubTotal()));
        assertEquals(0, new BigDecimal("12").compareTo(invoice.getTotal()));
        assert(invoice.getCreatedOn() != null);
        assert(invoice.getUpdatedOn() != null);

        //UPDATING
        Item_v1 item5 = new Item_v1(invoice.getNextItemOrderNumber(), invoice, category1, tax1, familyMember, dateUnit, "desc14", "desc24", "comment", new BigDecimal("5"));
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

        Item_v1 item1 = new Item_v1(1, invoice, category1, tax1, familyMember, dateUnit, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v1 item2 = new Item_v1(2, invoice, category1, tax2, familyMember, dateUnit, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v1 item3 = new Item_v1(3, invoice, category1, tax1, familyMember, dateUnit, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v1 item4 = new Item_v1(4, invoice, category1, tax2, familyMember, dateUnit, "desc14", "desc24", "comment", new BigDecimal("4"));

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


    //todo tests
}
