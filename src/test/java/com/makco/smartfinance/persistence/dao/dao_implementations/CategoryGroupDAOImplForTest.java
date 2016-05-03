package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.persistence.dao.CategoryGroupDAOImplTest;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Created by mcalancea on 2016-05-02.
 */
public class CategoryGroupDAOImplForTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDAOImplForTest.class);

    public void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception {
        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.saveOrUpdate(categoryGroup);
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

    public CategoryGroup getCategoryGroupById(Long id, boolean initializeCategories) throws Exception {
        Session session = null;
        CategoryGroup categoryGroup = null;
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            categoryGroup = (CategoryGroup) session.get(CategoryGroup.class, id);
            if(initializeCategories){
                //wrongClassException check entity classes in session, again eager might interfere
                Hibernate.initialize(categoryGroup.getCategories());
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
        return categoryGroup;
    }

}
