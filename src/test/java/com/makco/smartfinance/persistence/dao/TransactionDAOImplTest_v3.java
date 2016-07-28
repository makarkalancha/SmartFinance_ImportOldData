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
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Invoice_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Item_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Organization_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Transaction_v3;
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

    private TransactionDAOImpl_v3ForTest transactionDAOImpl_v3ForTest = new TransactionDAOImpl_v3ForTest();

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

    //TODO p386 and section 20.1 cursor and batch processing: list too large
    @Test
    public void test_11_saveTransaction_onlyCreditCategories_byCreditAccount() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Invoice_v3 invoiceV3_1 = generateInvoiceDebitCredit(randomInt, 1);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_1);

        AccountGroup accountGroup1 = new AccountGroupCredit("AG credit" + randomInt, "desc");
        Account account1 = new AccountCredit(accountGroup1, "A credit" + randomInt, "desc");

        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup1);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account1);

        Transaction_v3 transactionV3 = new Transaction_v3(
                generateTransactionNumber(randomInt),
                account1,
                invoiceV3_1,
                invoiceV3_1.getDateUnit(),
                "comment" + randomInt,
                new BigDecimal("0"),
                invoiceV3_1.getCreditTotal().subtract(invoiceV3_1.getDebitTotal())
        );
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transactionV3);
        assert(transactionV3.getId() != null);
        Long transactionId = transactionV3.getId();

        Transaction_v3 transactionV3FromDB1 = transactionDAOImpl_v3ForTest.getTransactionById(transactionId);
        LOG.debug(">>>transactionV3FromDB1 (invoice with lazy items): " + transactionV3FromDB1);
        assert(transactionV3FromDB1.getCreatedOn() != null);
        assert(transactionV3FromDB1.getUpdatedOn() != null);

        Transaction_v3 transactionV3FromDB2 = transactionDAOImpl_v3ForTest.getTransactionByIdWithInvoiceItems(transactionId);
        LOG.debug(">>>transactionV3FromDB2 (invoice with left join fetch items): " + transactionV3FromDB2);
        assert(transactionV3FromDB2.getCreatedOn() != null);
        assert(transactionV3FromDB2.getUpdatedOn() != null);
    }

    @Test
    public void test_12_saveTransaction_onlyCreditCategories_byDebitAccount() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Invoice_v3 invoiceV3_1 = generateInvoiceDebitCredit(randomInt, 1);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_1);

        AccountGroup accountGroup1 = new AccountGroupDebit("AG debit" + randomInt, "desc");
        Account account1 = new AccountDebit(accountGroup1, "A debit" + randomInt, "desc");

        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup1);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account1);

        Transaction_v3 transactionV3 = new Transaction_v3(
                generateTransactionNumber(randomInt),
                account1,
                invoiceV3_1,
                invoiceV3_1.getDateUnit(),
                "comment" + randomInt,
                invoiceV3_1.getCreditTotal().subtract(invoiceV3_1.getDebitTotal()),
                new BigDecimal("0")
        );
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transactionV3);
        assert(transactionV3.getId() != null);
        Long transactionId = transactionV3.getId();

        Transaction_v3 transactionV3FromDB1 = transactionDAOImpl_v3ForTest.getTransactionById(transactionId);
        LOG.debug(">>>transactionV3FromDB1 (invoice with lazy items): " + transactionV3FromDB1);
        assert(transactionV3FromDB1.getCreatedOn() != null);
        assert(transactionV3FromDB1.getUpdatedOn() != null);

        Transaction_v3 transactionV3FromDB2 = transactionDAOImpl_v3ForTest.getTransactionByIdWithInvoiceItems(transactionId);
        LOG.debug(">>>transactionV3FromDB2 (invoice with left join fetch items): " + transactionV3FromDB2);
        assert(transactionV3FromDB2.getCreatedOn() != null);
        assert(transactionV3FromDB2.getUpdatedOn() != null);
    }
}
