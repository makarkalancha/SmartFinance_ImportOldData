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
import com.makco.smartfinance.persistence.dao.dao_implementations.V_AccountAggregationDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.V_CumulativeAccountAggregationByDateDAOImpl_v3ForTest;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.persistence.entity.AccountCredit;
import com.makco.smartfinance.persistence.entity.AccountDebit;
import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.persistence.entity.AccountGroupCredit;
import com.makco.smartfinance.persistence.entity.AccountGroupDebit;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Invoice_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Transaction_v3;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.V_AccountAggregation_v3;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * User: Makar Kalancha
 * Date: 29/07/2016
 * Time: 23:13
 */
public class V_AccountAggregationDAOImplTest_v3 {
    private static final Logger LOG = LogManager.getLogger(V_AccountAggregationDAOImplTest_v3.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private TransactionDAOImpl_v3ForTest transactionDAOImpl_v3ForTest = new TransactionDAOImpl_v3ForTest();
    private TransactionDAOImplTest_v3 transactionDAOImplTest_v3 = new TransactionDAOImplTest_v3();

    private V_AccountAggregationDAOImpl_v3ForTest accountAggregateDAOImplV3ForTest = new V_AccountAggregationDAOImpl_v3ForTest();
    private V_CumulativeAccountAggregationByDateDAOImpl_v3ForTest cumulativeAccountAggregationByDateDAOImpl_v3ForTest =
            new V_CumulativeAccountAggregationByDateDAOImpl_v3ForTest();

    private InvoiceDAOImpl_v3ForTest invoiceDAOImpl_v3ForTest = new InvoiceDAOImpl_v3ForTest();

    private AccountDAOImpl_v1ForTest accountDAOImpl_v1ForTest = new AccountDAOImpl_v1ForTest();
    private AccountGroupDAOImpl_v1ForTest accountGroupDAOImpl_v1ForTest = new AccountGroupDAOImpl_v1ForTest();

    @Test
    public void test_41_selectAccountAggregation() throws Exception {
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
                invoiceV3_1.getCreditTotal().subtract(invoiceV3_1.getDebitTotal()),
                new BigDecimal("0")
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
