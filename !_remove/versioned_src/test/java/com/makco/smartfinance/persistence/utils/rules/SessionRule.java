package com.makco.smartfinance.persistence.utils.rules;

import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Created by Makar Kalancha on 2016-04-29.
 */
//http://blog.schauderhaft.de/2011/03/13/testing-databases-with-junit-and-hibernate-part-1-one-to-rule-them/
public class SessionRule implements MethodRule {
    private final static Logger LOG = LogManager.getLogger(SessionRule.class);
    private SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                LOG.debug(">>>SessionRule: start evaluate");
                session = TestPersistenceSession.openSession();
                beginTransaction();
                try{
                    base.evaluate();
                }finally {
                    LOG.debug(">>>SessionRule: shudown");
                    shutdown();
                }
            }
        };
    }

    public void beginTransaction() {
        transaction = session.beginTransaction();
    }

    private void shutdown(){
        try{
            try{
                try{
//                    transaction.rollback();
//                    transaction.commit();
                    LOG.debug(">>>SessionRule: shudown->isActiveOrMarkedRollback(): "+isActiveOrMarkedRollback());
                }catch (Exception e) {
                    LOG.error(e, e);
                }
                session.close();
            }catch (Exception e) {
                LOG.error(e, e);
            }
//            TestPersistenceSession.closeSessionFactory();
        }catch (Exception e) {
            LOG.error(e, e);
        }
    }

    public void commit(){
        transaction.commit();
    }

    public Session getSession() {
        return session;
    }

    public boolean isActiveOrMarkedRollback(){
        return transaction.getStatus() == TransactionStatus.ACTIVE
                || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK;
    }

    public void rollback() {
        transaction.rollback();
    }
}
