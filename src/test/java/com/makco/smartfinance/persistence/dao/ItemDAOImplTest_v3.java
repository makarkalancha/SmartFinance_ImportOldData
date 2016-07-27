package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.DateUnitDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.FamilyMemberDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.InvoiceDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.ItemDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.TaxDAOImpl_v1ForTest;
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
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 17/07/2016
 * Time: 21:35
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemDAOImplTest_v3 {

    private static final Logger LOG = LogManager.getLogger(ItemDAOImplTest_v3.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private ItemDAOImpl_v3ForTest itemDAOImpl_v3ForTest = new ItemDAOImpl_v3ForTest();
    private InvoiceDAOImpl_v3ForTest invoiceDAOImpl_v3ForTest = new InvoiceDAOImpl_v3ForTest();
    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();
    private CategoryDAOImplForTest categoryDAOImplForTest = new CategoryDAOImplForTest();
    private TaxDAOImpl_v1ForTest taxDAO = new TaxDAOImpl_v1ForTest();
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImplForTest();
    private FamilyMemberDAOImpl_v1ForTest familyMemberDAO = new FamilyMemberDAOImpl_v1ForTest();

    public static final DateTimeFormatter INVOICE_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generateInvoiceNumber(int randomInt){
        return LocalDateTime.now().format(INVOICE_NUMBER_FORMAT) + randomInt;
    }


    /*
    @Test(expected = RuntimeException.class)
    ---items cannot be saved without invoice first to be saved:
    Caused by: org.hibernate.TransientPropertyValueException: Not-null property references a transient value - transient
    instance must be saved before current operation : com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Item_v1.invoice
    -> com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Invoice_v1
     */
    public void test_11_saveItems() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
        Invoice_v3 invoice = new Invoice_v3(invoiceNumber, organization, dateUnit, "comment " + randomInt);

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

        Item_v3 item1 = new Item_v3(invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v3 item2 = new Item_v3(invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v3 item3 = new Item_v3(invoice, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v3 item4 = new Item_v3(invoice, category1, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        List<Item_v3> itemsToSave = new ArrayList<>();
        itemsToSave.add(item1);
        itemsToSave.add(item2);
        itemsToSave.add(item3);
        itemsToSave.add(item4);

        invoice.setItems(itemsToSave);

        itemDAOImpl_v3ForTest.saveOrUpdateMultipleItems(itemsToSave);
    }

    //todo save multiple items for different invoices
    @Test
    public void test_12_saveItems_Multiple() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        Invoice_v3 invoice1 = new Invoice_v3(generateInvoiceNumber(randomWithinRange.getRandom()), organization, dateUnit, "comment " + randomInt);
        Invoice_v3 invoice2 = new Invoice_v3(generateInvoiceNumber(randomWithinRange.getRandom()), organization, dateUnit, "comment " + randomInt);
        Invoice_v3 invoice3 = new Invoice_v3(generateInvoiceNumber(randomWithinRange.getRandom()), organization, dateUnit, "comment " + randomInt);

        CategoryGroup categoryGroup1 = new CategoryGroupCredit("CG credit" + randomInt, "desc");
        Category category1 = new CategoryCredit(categoryGroup1, "C credit" + randomInt, "desc");

        CategoryGroup categoryGroup2 = new CategoryGroupDebit("CG debit" + randomInt, "desc");
        Category category2 = new CategoryDebit(categoryGroup2, "C debit" + randomInt, "desc");

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

        Item_v3 item1 = new Item_v3(invoice1, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v3 item2 = new Item_v3(invoice2, category2, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v3 item3 = new Item_v3(invoice3, category1, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v3 item4 = new Item_v3(invoice1, category2, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        List<Item_v3> itemsToSave = new ArrayList<>();
        itemsToSave.add(item1);
        itemsToSave.add(item2);
        itemsToSave.add(item3);
        itemsToSave.add(item4);

//        invoice.setItems(itemsToSave);

        invoiceDAOImpl_v3ForTest.saveOrUpdateMultipleInvoices(Arrays.asList(invoice1,invoice2,invoice3));

        itemDAOImpl_v3ForTest.saveOrUpdateMultipleItems(itemsToSave);

        Long invoice1Id = invoice1.getId();
        Long invoice2Id = invoice2.getId();
        Long invoice3Id = invoice3.getId();


    }


    @Test
    public void test_31_removeItem_FromInvoice_DebitCreditCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
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

        Item_v3 item1 = new Item_v3(invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v3 item2 = new Item_v3(invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v3 item3 = new Item_v3(invoice, category2, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v3 item4 = new Item_v3(invoice, category2, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoice);

        Invoice_v3 invoice_v3_1 = invoiceDAOImpl_v3ForTest.getInvoiceByIdWithItems(invoice.getId());
        Item_v3 item_v3_1 = new ArrayList<>(invoice_v3_1.getItems()).get(0);

        itemDAOImpl_v3ForTest.removeItem(item_v3_1.getId());

        Invoice_v3 invoice_v3_2 = invoiceDAOImpl_v3ForTest.getInvoiceByIdWithItems(invoice.getId());
        LOG.debug(">>>invoice_v3_2: " + invoice_v3_2);
        assert(invoice_v3_2.getId() != null);
        assertEquals(3, invoice_v3_2.getItems().size());
        assertEquals(0, new BigDecimal("2").compareTo(invoice_v3_2.getDebitTotal()));
        assertEquals(0, new BigDecimal("8").compareTo(invoice_v3_2.getCreditTotal()));
        assert(invoice_v3_2.getCreatedOn() != null);
        assert(invoice_v3_2.getUpdatedOn() != null);

        for(Item_v3 item_v3 : invoice_v3_2.getItems()){
            LOG.debug(">>>item_v3: " + item_v3);
            assert(item_v3.getOrderNumber() == 2 ||
                    item_v3.getOrderNumber() == 3 ||
                    item_v3.getOrderNumber() == 4);
            assert(item_v3.getId() != null);
            assert(item_v3.getCategory() != null);
            assert(item_v3.getTax() != null);
            assert(item_v3.getFamilyMember() != null);
            assert(item_v3.getDateUnit() != null);
            assert(item_v3.getCreatedOn() != null);
            assert(item_v3.getUpdatedOn() != null);
        }
    }

    @Test
    public void test_21_updateItem_updateItemDebitCreditCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v3 organization = new Organization_v3(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
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

        Item_v3 item1 = new Item_v3(invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v3 item2 = new Item_v3(invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v3 item3 = new Item_v3(invoice, category2, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v3 item4 = new Item_v3(invoice, category2, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoice);

        ///////////////update item
        item1.setSubTotal(new BigDecimal("11"));
        itemDAOImpl_v3ForTest.saveOrUpdateItem(item1);

        LOG.debug(">>>invoice: " + invoice);
        assert(invoice.getId() != null);
        assertEquals(4, invoice.getItems().size());
//        assertEquals(new BigDecimal("10"), invoice.getSubTotal());
//        assertEquals(new BigDecimal("12"), invoice.getTotal());
        assert(invoice.getCreatedOn() != null);
        assert(invoice.getUpdatedOn() != null);

        Invoice_v3 invoice1 = invoiceDAOImpl_v3ForTest.getInvoiceByIdWithItems(invoice.getId()); //pass
//        Invoice_v3 invoice1 = invoice; //fail
//        Invoice_v3 invoice1 = invoiceDAOImpl_v3ForTest.refresh(invoice);//fail
        LOG.debug(">>>invoice1: " + invoice1);
        assert(invoice1.getId() != null);
        assertEquals(4, invoice1.getItems().size());
        assertEquals(0, new BigDecimal("14").compareTo(invoice1.getDebitTotal()));
        assertEquals(0, new BigDecimal("8").compareTo(invoice1.getCreditTotal()));
        assert(invoice1.getCreatedOn() != null);
        assert(invoice1.getUpdatedOn() != null);

        for(Item_v3 item_v3 : invoice1.getItems()){
            LOG.debug(">>>item_v3: " + item_v3);
            assert(item_v3.getId() != null);
            assert(item_v3.getCategory() != null);
            assert(item_v3.getTax() != null);
            assert(item_v3.getFamilyMember() != null);
            assert(item_v3.getDateUnit() != null && item_v3.getDateUnit() == invoice1.getDateUnit());
            assert(item_v3.getCreatedOn() != null);
            assert(item_v3.getUpdatedOn() != null);
        }
    }
}
