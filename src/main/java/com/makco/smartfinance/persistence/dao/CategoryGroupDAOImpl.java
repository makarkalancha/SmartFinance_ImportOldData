package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mcalancea on 2016-05-12.
 */
public class CategoryGroupDAOImpl implements CategoryGroupDAO{
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDAOImpl.class);

    @Override
    public List<CategoryGroup> categoryGroupList(boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroup cg").list();
            if(initializeCategories){
                for(CategoryGroup categoryGroup : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(categoryGroup.getCategories());
                }
            }
            Collections.sort(list, (CategoryGroup cg1, CategoryGroup cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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
    public <T extends CategoryGroup> List<T> categoryGroupByType(Class<T> type, boolean initializeCategories) throws Exception {
        Session session = null;
        List<T> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroup cg WHERE cg.class = :type ORDER BY cg.name")
                    .setParameter("type", type.newInstance().getCategoryGroupType())
                    .list();
            if(initializeCategories){
                for(CategoryGroup categoryGroup : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(categoryGroup.getCategories());
                }
            }
            Collections.sort(list, (CategoryGroup cg1,  CategoryGroup cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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
    public void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateCategoryGroup: start");
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(categoryGroup);
            session.getTransaction().commit();
            LOG.debug(">>>saveOrUpdateCategoryGroup: end");
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

    public void removeCategoryGroup(Long id) throws Exception {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            /**
             * session.load()
             * - It will always return a “proxy” (Hibernate term) without hitting the database.
             * In Hibernate, proxy is an object with the given identifier value, its properties are not initialized yet,
             * it just look like a temporary fake object.
             * - If no row found , it will throws an ObjectNotFoundException.

             */
            CategoryGroup categoryGroup = (CategoryGroup) session.load(CategoryGroup.class, id);
            /**
             * session.get()
             * - It always hit the database and return the real object, an object that represent the database row,
             * not proxy.
             * - If no row found , it return null.

             */
//            CategoryGroup categoryGroup = (CategoryGroup) session.get(CategoryGroup.class, id);
            LOG.debug(">>>removeCategoryGroup: " + categoryGroup);
            session.delete(categoryGroup);
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
    public List<CategoryGroup> getCategoryGroupByName(String categoryGroupName, boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroup cg where cg.name = :categoryGroupName")
                    .setString("categoryGroupName", categoryGroupName)
                    .list();
            //byName return list as it might be debit or credit and return categories
            if(initializeCategories){
                for(CategoryGroup categoryGroup : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(categoryGroup.getCategories());
                }
            }
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
