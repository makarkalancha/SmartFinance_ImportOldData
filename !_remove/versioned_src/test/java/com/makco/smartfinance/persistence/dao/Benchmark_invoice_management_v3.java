package com.makco.smartfinance.persistence.dao;

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
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void test_51_selectAccountAggregation_1() throws Exception {
        int randomInt1 = randomWithinRange.getRandom();
        Invoice_v3 invoiceV3_1 = generateInvoiceDebitCredit(randomInt1, 1);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_1);
        AccountGroup accountGroup1 = new AccountGroupDebit("AG debit" + randomInt1, "desc");
        Account account1 = new AccountDebit(accountGroup1, "A debit" + randomInt1, "desc");
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup1);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account1);
        Transaction_v3 transactionV3_1 = new Transaction_v3(
                generateTransactionNumber(randomInt1),
                account1,
                invoiceV3_1,
                invoiceV3_1.getDateUnit(),
                "comment" + randomInt1,
                invoiceV3_1.getCreditTotal().subtract(invoiceV3_1.getDebitTotal()),
                new BigDecimal("0")
        );
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transactionV3_1);

        int randomInt2 = randomWithinRange.getRandom();
        Invoice_v3 invoiceV3_2 = generateInvoiceDebitCredit(randomInt2, 1);
        invoiceDAOImpl_v3ForTest.saveOrUpdateInvoice(invoiceV3_2);
        AccountGroup accountGroup2 = new AccountGroupCredit("AG credit" + randomInt2, "desc");
        Account account2 = new AccountCredit(accountGroup2, "A credit" + randomInt2, "desc");
        accountGroupDAOImpl_v1ForTest.saveOrUpdateAccountGroup(accountGroup2);
        accountDAOImpl_v1ForTest.saveOrUpdateAccount(account2);
        Transaction_v3 transactionV3_2 = new Transaction_v3(
                generateTransactionNumber(randomInt2),
                account2,
                invoiceV3_2,
                invoiceV3_2.getDateUnit(),
                "comment" + randomInt2,
                new BigDecimal("0"),
                invoiceV3_2.getCreditTotal().subtract(invoiceV3_2.getDebitTotal())
        );
        transactionDAOImpl_v3ForTest.saveOrUpdateTransaction(transactionV3_2);


        List<V_AccountAggregation_v3> accountAggregateV3s = accountAggregateDAOImplV3ForTest.accountsAggregate();

        accountAggregateV3s.forEach(accountAggregateV3 ->
                LOG.debug(">>>>" + accountAggregateV3)
        );
    }
}
