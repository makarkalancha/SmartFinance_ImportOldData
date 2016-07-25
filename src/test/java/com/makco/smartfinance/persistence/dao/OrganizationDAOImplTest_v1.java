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
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Makar Kalancha on 25 Jul 2016 at 11:45.
 */
public class OrganizationDAOImplTest_v1 {
    private static final Logger LOG = LogManager.getLogger(OrganizationDAOImplTest_v1.class);

    public static final DateTimeFormatter INVOICE_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();
    private CategoryDAOImplForTest categoryDAOImplForTest = new CategoryDAOImplForTest();
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImplForTest();
    private FamilyMemberDAOImpl_v1ForTest familyMemberDAO = new FamilyMemberDAOImpl_v1ForTest();
    private InvoiceDAOImpl_v1ForTest invoiceDAOImpl_v1ForTest = new InvoiceDAOImpl_v1ForTest();
    private OrganizationDAOImpl_v1ForTest organizationDAOImpl_v1ForTest = new OrganizationDAOImpl_v1ForTest();
    private TaxDAOImpl_v1ForTest taxDAO = new TaxDAOImpl_v1ForTest();

    public String generateInvoiceNumber(int randomInt){
        return LocalDateTime.now().format(INVOICE_NUMBER_FORMAT) + randomInt;
    }


    @Test(expected = RuntimeException.class)
//    org.hibernate.exception.ConstraintViolationException
    /*
    Caused by: org.h2.jdbc.JdbcBatchUpdateException: Referential integrity constraint violation: "CONSTRAINT_9FA: TEST.INVOICE FOREIGN KEY(ORGANIZATION_ID) REFERENCES TEST.ORGANIZATION(ID) (1)"; SQL statement:
    delete from TEST.ORGANIZATION where ID=? [23503-191]
     */
    public void test_32_removeOrganization() throws Exception {
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

        organizationDAOImpl_v1ForTest.removeOrganization(organizationId);
    }
}
