package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.V_AccountAggregation_v3;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Makar Kalancha
 * Date: 17/07/2016
 * Time: 21:39
 */
public class V_AccountAggregationDAOImpl_v3ForTest {
    private final static Logger LOG = LogManager.getLogger(V_AccountAggregationDAOImpl_v3ForTest.class);

    public List<V_AccountAggregation_v3> getAggregateByAccountGroupId(Long accountGroupId) throws Exception {
        Session session = null;
        List<V_AccountAggregation_v3> list = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT a FROM V_AccountAggregation_v3 a WHERE accountGroupId = :accountGroupId ")
                    .setLong("accountGroupId", accountGroupId)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public V_AccountAggregation_v3 getAggregateByAccountId(Long accountId) throws Exception {
        Session session = null;
        V_AccountAggregation_v3 accountAggregation_v3 = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            accountAggregation_v3 = (V_AccountAggregation_v3) session.createQuery("SELECT a FROM V_AccountAggregation_v3 a WHERE accountId = :accountId ")
                    .setLong("accountId", accountId)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return accountAggregation_v3;
    }

    public List<V_AccountAggregation_v3> accountsAggregate() throws Exception {
        Session session = null;
        List<V_AccountAggregation_v3> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT a FROM V_AccountAggregation_v3 a")
                    .list();
            session.getTransaction().commit();

        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return list;
    }
}
