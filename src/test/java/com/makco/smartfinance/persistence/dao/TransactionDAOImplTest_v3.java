package com.makco.smartfinance.persistence.dao;

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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 27/07/2016
 * Time: 16:36
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionDAOImplTest_v3 {
    private static final Logger LOG = LogManager.getLogger(TransactionDAOImplTest_v3.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private InvoiceDAOImpl_v3ForTest invoiceDAOImpl_v3ForTest = new InvoiceDAOImpl_v3ForTest();
    private InvoiceDAOImplTest_v3 invoiceDAOImplTest_v3 = new InvoiceDAOImplTest_v3();

    private AccountDAOImpl_v1ForTest accountDAOImpl_v1ForTest = new AccountDAOImpl_v1ForTest();
    private AccountGroupDAOImpl_v1ForTest accountGroupDAOImpl_v1ForTest = new AccountGroupDAOImpl_v1ForTest();
    private ItemDAOImpl_v3ForTest itemDAOImpl_v3ForTest = new ItemDAOImpl_v3ForTest();
    private OrganizationDAOImpl_v3ForTest organizationDAOImpl_v3ForTest = new OrganizationDAOImpl_v3ForTest();
    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();
    private CategoryDAOImplForTest categoryDAOImplForTest = new CategoryDAOImplForTest();
    private TaxDAOImpl_v1ForTest taxDAO = new TaxDAOImpl_v1ForTest();
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImplForTest();
    private FamilyMemberDAOImpl_v1ForTest familyMemberDAO = new FamilyMemberDAOImpl_v1ForTest();

    private void generateInvoiceDebitCredit(int randomInt) throws Exception{
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

        Item_v3 item1 = new Item_v3(1, invoice, category1, tax1, familyMember, "desc11", "desc21", "comment", new BigDecimal("1"));
        Item_v3 item2 = new Item_v3(2, invoice, category1, tax2, familyMember, "desc12", "desc22", "comment", new BigDecimal("2"));
        Item_v3 item3 = new Item_v3(3, invoice, category2, tax1, familyMember, "desc13", "desc23", "comment", new BigDecimal("3"));
        Item_v3 item4 = new Item_v3(4, invoice, category2, tax2, familyMember, "desc14", "desc24", "comment", new BigDecimal("4"));

        invoice.setItems(new ArrayList<Item_v3>(){{
            add(item1);
            add(item2);
            add(item3);
            add(item4);
        }});

        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoice);
    }

    //TODO p386 and section 20.1 cursor and batch processing: list too large
    @Test
    public void test_11_saveTransaction_onlyCreditCategories_byCreditAccount() throws Exception {
        int randomInt = randomWithinRange.getRandom();






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
        assertEquals(0, new BigDecimal("0").compareTo(invoice1.getDebitTotal()));
        assertEquals(0, new BigDecimal("12").compareTo(invoice1.getCreditTotal()));
        assert(invoice1.getCreatedOn() != null);
        assert(invoice1.getUpdatedOn() != null);

        for(Item_v3 item_v3 : invoice1.getItems()){
            LOG.debug(">>>item_v3: " + item_v3);
            assert(item_v3.getId() != null);
            assert(item_v3.getCategory() != null);
            assert(item_v3.getTax() != null);
            assert(item_v3.getFamilyMember() != null);
            assert(item_v3.getDateUnit() != null);
            assert(item_v3.getCreatedOn() != null);
            assert(item_v3.getUpdatedOn() != null);
        }
    }
}
