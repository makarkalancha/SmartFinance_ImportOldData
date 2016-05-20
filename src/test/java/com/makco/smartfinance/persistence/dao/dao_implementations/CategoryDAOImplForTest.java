package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcalancea on 2016-05-02.
 */
public class CategoryDAOImplForTest {
    private final static Logger LOG = LogManager.getLogger(CategoryDAOImplForTest.class);

    public void removeCategory(Long id) throws Exception {
        Session session = null;
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            Category category = (Category) session.load(Category.class, id);
            LOG.debug(">>>removeCategoryGroup: " + category);
            session.delete(category);
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
    }

    public Category getCategoryById(Long id) throws Exception {
        Session session = null;
        Category category = null;
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            category = (Category) session.get(Category.class, id);
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
        return category;
    }

    public List<Category> getCategoryByName(String categoryName) throws Exception {
        Session session = null;
        List<Category> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT c FROM Category c where c.name = :categoryName")
                    .setString("categoryName", categoryName)
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

    public void saveOrUpdateCategory(Category category) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateCategory: start");
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.saveOrUpdate(category);
            session.getTransaction().commit();
            LOG.debug(">>>saveOrUpdateCategory: end");
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
    }
}
