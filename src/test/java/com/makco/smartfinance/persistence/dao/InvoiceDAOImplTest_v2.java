package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.DateUnitDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.FamilyMemberDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.InvoiceDAOImpl_v2ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.ItemDAOImpl_v2ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.OrganizationDAOImpl_v2ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.TaxDAOImpl_v1ForTest;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v2.Invoice_v2;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v2.Item_v2;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v2.Organization_v2;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 17/07/2016
 * Time: 21:35
 */
public class InvoiceDAOImplTest_v2 {

    private static final Logger LOG = LogManager.getLogger(InvoiceDAOImplTest_v2.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private InvoiceDAOImpl_v2ForTest invoiceDAOImpl_v2ForTest = new InvoiceDAOImpl_v2ForTest();
    private ItemDAOImpl_v2ForTest itemDAOImpl_v2ForTest = new ItemDAOImpl_v2ForTest();
    private OrganizationDAOImpl_v2ForTest organizationDAOImpl_v2ForTest = new OrganizationDAOImpl_v2ForTest();
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
    public void test_11_saveOrUpdateInvoice() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v2 organization = new Organization_v2(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
        Invoice_v2 invoice = new Invoice_v2(invoiceNumber, organization, dateUnit, "comment " + randomInt);

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

        Item_v2 item1 = new Item_v2(1, invoice, category1, tax1, familyMember, dateUnit, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v2 item2 = new Item_v2(2, invoice, category1, tax2, familyMember, dateUnit, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v2 item3 = new Item_v2(3, invoice, category1, tax1, familyMember, dateUnit, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v2 item4 = new Item_v2(4, invoice, category1, tax2, familyMember, dateUnit, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v2>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v2ForTest.saveOrUpdateInvoice(invoice);

        LOG.debug(">>>invoice: " + invoice);
        assert(invoice.getId() != null);
        assertEquals(4, invoice.getItems().size());
//        assertEquals(new BigDecimal("10"), invoice.getSubTotal());
//        assertEquals(new BigDecimal("12"), invoice.getTotal());
        assert(invoice.getCreatedOn() != null);
        assert(invoice.getUpdatedOn() != null);

        Invoice_v2 invoice1 = invoiceDAOImpl_v2ForTest.getInvoiceByIdWithItems(invoice.getId()); //pass
//        Invoice_v2 invoice1 = invoice; //fail
//        Invoice_v2 invoice1 = invoiceDAOImpl_v2ForTest.refresh(invoice);//fail
        LOG.debug(">>>invoice1: " + invoice1);
        assert(invoice1.getId() != null);
        assertEquals(4, invoice1.getItems().size());
        assertEquals(0, new BigDecimal("10").compareTo(invoice1.getSubTotal()));
        assertEquals(0, new BigDecimal("12").compareTo(invoice1.getTotal()));
        assert(invoice1.getCreatedOn() != null);
        assert(invoice1.getUpdatedOn() != null);

        for(Item_v2 item_v2 : invoice1.getItems()){
            LOG.debug(">>>item_v2: " + item_v2);
            assert(item_v2.getId() != null);
            assert(item_v2.getCategory() != null);
            assert(item_v2.getTax() != null);
            assert(item_v2.getFamilyMember() != null);
            assert(item_v2.getDateUnit() != null);
            assert(item_v2.getCreatedOn() != null);
            assert(item_v2.getUpdatedOn() != null);
        }
    }

    @Test
    public void test_31_removeInvoice() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        DateUnit dateUnit = new DateUnit(LocalDate.now());
        dateUnitDAO.addDateUnit(dateUnit);

        String organizationName = "OrgName" + randomInt;
        Organization_v2 organization = new Organization_v2(organizationName, "org desc " + randomInt);

        String invoiceNumber = generateInvoiceNumber(randomInt);
        Invoice_v2 invoice = new Invoice_v2(invoiceNumber, organization, dateUnit, "comment " + randomInt);

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

        Item_v2 item1 = new Item_v2(1, invoice, category1, tax1, familyMember, dateUnit, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v2 item2 = new Item_v2(2, invoice, category1, tax2, familyMember, dateUnit, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v2 item3 = new Item_v2(3, invoice, category1, tax1, familyMember, dateUnit, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v2 item4 = new Item_v2(4, invoice, category1, tax2, familyMember, dateUnit, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v2>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v2ForTest.saveOrUpdateInvoice(invoice);

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
        invoiceDAOImpl_v2ForTest.removeInvoice(invoiceId);

        Invoice_v2 invoice_v2_2 = invoiceDAOImpl_v2ForTest.getInvoiceByIdWithItems(invoiceId);
        Organization_v2 organization_v2_2 = organizationDAOImpl_v2ForTest.getOrganizationById(organizationId);
        Item_v2 item_v2_2_1= itemDAOImpl_v2ForTest.getItemById(itemId1);
        Item_v2 item_v2_2_2= itemDAOImpl_v2ForTest.getItemById(itemId2);
        Item_v2 item_v2_2_3= itemDAOImpl_v2ForTest.getItemById(itemId3);
        Item_v2 item_v2_2_4= itemDAOImpl_v2ForTest.getItemById(itemId4);
        LOG.debug(">>>invoice_2: " + invoice_v2_2);
        assert(invoice_v2_2 == null);
        LOG.debug(">>>organization_v2: " + organization_v2_2);
        assert(organization_v2_2 != null);
        LOG.debug(">>>item_v2_2_1: " + item_v2_2_1);
        assert(item_v2_2_1 == null);
        LOG.debug(">>>item_v2_2_2: " + item_v2_2_2);
        assert(item_v2_2_2 == null);
        LOG.debug(">>>item_v2_2_3: " + item_v2_2_3);
        assert(item_v2_2_3 == null);
        LOG.debug(">>>item_v2_2_4: " + item_v2_2_4);
        assert(item_v2_2_4 == null);
    }


    //todo tests
}
