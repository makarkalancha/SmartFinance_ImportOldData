package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 30/04/2016
 * Time: 23:52
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryGroupDAOImplTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDAOImplTest.class);
    private static final String debitCategoryGroupDebitName = "categoryGroupDebit_name";
    private static final String debitCategoryGroupDebitDesc = "categoryGroupDebit_description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);


    private void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception {
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



    @Test
    public void test_11_saveOrUpdateCategoryGroupDebit() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = debitCategoryGroupDebitName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, debitCategoryGroupDebitDesc);
        saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(CategoryGroup.CATEGORY_GROUP_TYPE_DEBIT, categoryGroup.getCategoryGroupType());
        assertEquals(name, categoryGroup.getName());
        assertEquals(debitCategoryGroupDebitDesc, categoryGroup.getDescription());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
    }

    @Test //TODO
    public void test_21_updateCategoryGroup() throws Exception {

    }

    @Test //TODO
    public void test_31_removeCategoryGroup() throws Exception {

    }

    @Test //TODO
    public void test_41_seleteAllCategoryGroups() throws Exception {
//        Session session = null;
//        List<FamilyMember> list = new ArrayList<>();
//        try{
//            session = HibernateUtil.openSession();
//            session.beginTransaction();
//            list = session.createQuery("SELECT f FROM FamilyMember f ORDER BY f.name").list();
//            session.getTransaction().commit();
//
//        } catch (Exception e) {
//            //hibernate persistence p.257
//            try {
//                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
//                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
//                    session.getTransaction().rollback();
//            } catch (Exception rbEx) {
//                LOG.error("Rollback of transaction failed, trace follows!");
//                LOG.error(rbEx, rbEx);
//            }
//            throw new RuntimeException(e);
//        } finally {
//            if(session != null){
//                session.close();
//            }
//        }
//        return list;
    }

    @Test //TODO
    public void test_42_seleteCategoryGroupById() throws Exception {

    }

    @Test //TODO
    public void test_43_seleteCategoryGroupByName() throws Exception {

    }
}
