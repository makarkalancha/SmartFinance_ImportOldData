package com.makco.smartfinance.persistence.utils.rules;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by Makar Kalancha on 2016-04-28.
 */
public class EntityManagerRule implements MethodRule{
    private final static Logger LOG = LogManager.getLogger(EntityManagerRule.class);
    private EntityManager em;
    private EntityTransaction transaction;

    @Override
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                LOG.debug(">>>EntityManagerRule: start evaluate");
                em = TestPersistenceManager.INSTANCE.getEntityManager();
                beginTransaction();
                try{
                    base.evaluate();
                }finally {
                    LOG.debug(">>>EntityManagerRule: shudown");
                    shutdown();
                }
            }
        };
    }

    public void beginTransaction(){
        transaction = em.getTransaction();
        transaction.begin();
    }

    private void shutdown(){
        try{
            try{
                try{
//                    transaction.rollback();
//                    transaction.commit();
                    LOG.debug(">>>EntityManagerRule: shudown->transaction.isActive(): "+(transaction.isActive()));
                }catch (Exception e) {
                    LOG.error(e, e);
                }
                em.close();
            }catch (Exception e) {
                LOG.error(e, e);
            }
            TestPersistenceManager.INSTANCE.closeFactory();
        }catch (Exception e) {
            LOG.error(e, e);
        }
    }

    public void commit(){
        LOG.debug(">>>EntityManagerRule: before commit->transaction.isActive(): "+(transaction.isActive()));
        transaction.commit();
        LOG.debug(">>>EntityManagerRule: after commit->transaction.isActive(): " + (transaction.isActive()));
    }

    public EntityManager getEntityManager(){
        return em;
    }

    public boolean isActive(){
        return transaction.isActive();
    }

    public void rollback() {
        transaction.rollback();
    }
}
