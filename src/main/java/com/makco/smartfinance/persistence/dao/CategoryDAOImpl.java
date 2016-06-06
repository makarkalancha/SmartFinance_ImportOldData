package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mcalancea on 2016-05-20.
 */
public class CategoryDAOImpl implements CategoryDAO {
    private final static Logger LOG = LogManager.getLogger(CategoryDAOImpl.class);

    @Override
    public <T extends Category> List<T> categoryByType(Class<T> type) throws Exception {
        Session session = null;
        List<T> list = new ArrayList<>();

        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            LOG.debug("type.newInstance().getCategoryGroupType():" + type.newInstance().getCategoryGroupType());
            list = session.createQuery("SELECT c FROM Category c WHERE c.class = :type ORDER BY c.name")
                    .setParameter("type", type.newInstance().getCategoryGroupType())
                    .list();
            Collections.sort(list, (Category c1, Category c2) -> c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase()));
            session.getTransaction().commit();

        } catch (Exception e) {
            //hibernate persistence p.257
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

    @Override
    public void saveOrUpdateCategory(Category category) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateCategory: start");
            session = HibernateUtil.openSession();
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

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Session session = null;
        Category category = null;
        try{
            session = HibernateUtil.openSession();
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

    @Override
    public void removeCategory(Long id) throws Exception {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Category category = (Category) session.load(Category.class, id);
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

    @Override
    public List<Category> getCategoryByName(String categoryName) throws Exception {
        Session session = null;
        List<Category> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
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
}
