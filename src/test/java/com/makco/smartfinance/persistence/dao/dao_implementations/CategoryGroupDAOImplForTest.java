package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroupCredit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroupDebit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroup_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.Category_v1;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mcalancea on 2016-05-02.
 */
public class CategoryGroupDAOImplForTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDAOImplForTest.class);

    /**
     * check file saveOrUpdateCategoryGroup_v1.log: find "statement [prep" 6 hits,
     * and 2 (! vs 3 in merge) statements registered, released and closed:
     * 1) update TEST.CATEGORY_GROUP set DESCRIPTION=?, NAME=? where ID=? {1: 'CategoryGroupDebit_v1_description', 2: '_changed_v167092_changed_v511301', 3: 9}]
     * 2) update TEST.CATEGORY set DESCRIPTION=?, NAME=?, CATEGORY_GROUP_ID=? where ID=? {1: 'debit category #1 ''description''', 2: 'nged_v1_461186_changed_v1_167092', 3: 9, 4: 7}]
     * !!!so use saveOrUpdate and not merge!!!
     */
    public void saveOrUpdateCategoryGroup_v1(CategoryGroup_v1 categoryGroup_v1) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateCategoryGroup_v1: start");
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.saveOrUpdate(categoryGroup_v1);
            session.getTransaction().commit();
            LOG.debug(">>>saveOrUpdateCategoryGroup_v1: end");
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

    /**
     * check file saveOrUpdateWithMergeCategoryGroup_v1.log: find "statement [prep" 10 hits,
     * and 3 (! vs 2 in saveOrUpdate) statements registered, released and closed:
     * 1) select categorygr0_.ID as ID2_1_1_, categorygr0_.T_CREATEDON as T_CREATE3_1_1_, categorygr0_.DESCRIPTION as DESCRIPT4_1_1_, categorygr0_.NAME as NAME5_1_1_, categorygr0_.T_UPDATEDON as T_UPDATE6_1_1_, categories1_.CATEGORY_GROUP_ID as CATEGORY7_0_3_, categories1_.ID as ID2_0_3_, categories1_.ID as ID2_0_0_, categories1_.T_CREATEDON as T_CREATE3_0_0_, categories1_.DESCRIPTION as DESCRIPT4_0_0_, categories1_.NAME as NAME5_0_0_, categories1_.T_UPDATEDON as T_UPDATE6_0_0_, categories1_.CATEGORY_GROUP_ID as CATEGORY7_0_0_ from TEST.CATEGORY_GROUP categorygr0_ left outer join TEST.CATEGORY categories1_ on categorygr0_.ID=categories1_.CATEGORY_GROUP_ID and categories1_.CATEGORY_GROUP_TYPE='D' where categorygr0_.ID=? and categorygr0_.TYPE='D' order by categories1_.NAME {1: 9}]
     * 2) update TEST.CATEGORY set DESCRIPTION=?, NAME=?, CATEGORY_GROUP_ID=? where ID=? {1: 'debit category #2 ''description''', 2: 'nged_v1_511301_changed_v1_417087', 3: 9, 4: 8}]
     * 3) update TEST.CATEGORY_GROUP set DESCRIPTION=?, NAME=? where ID=? {1: 'CategoryGroupDebit_v1_description', 2: '_changed_v511301_changed_v417087', 3: 9}]
     * !!!so use saveOrUpdate and not merge!!!
     */
    public void saveOrUpdateWithMergeCategoryGroup_v1(CategoryGroup_v1 categoryGroup_v1) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateWithMergeCategoryGroup_v1: start");
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.merge(categoryGroup_v1);
            session.getTransaction().commit();
            LOG.debug(">>>saveOrUpdateWithMergeCategoryGroup_v1: end");
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

    public CategoryGroup_v1 getCategoryGroup_v1ById(Long id, boolean initializeCategories) throws Exception {
        Session session = null;
        CategoryGroup_v1 CategoryGroup_v1 = null;
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            CategoryGroup_v1 = (CategoryGroup_v1) session.get(CategoryGroup_v1.class, id);
            if(initializeCategories){
                //wrongClassException check entity classes in session, again eager might interfere
                //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                Hibernate.initialize(CategoryGroup_v1.getCategories());
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
        return CategoryGroup_v1;
    }

    //NOT USED
    public List<CategoryGroup_v1> getCategoryGroup_v1ByName_withCategories(String categoryGroup_v1Name, boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup_v1> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching: see getCategoryGroup_v1ByName_withLeftJoinFetch
            list = session.createQuery("SELECT cg FROM CategoryGroup_v1 cg where cg.name = :categoryGroup_v1Name")
                    .setString("categoryGroup_v1Name", categoryGroup_v1Name)
                    .list();
            //byName return list as it might be debit or credit and return categories
            if(initializeCategories){
                for(CategoryGroup_v1 CategoryGroup_v1 : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(CategoryGroup_v1.getCategories());
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

    public List<CategoryGroup_v1> getCategoryGroup_v1ByName_forValidation(String categoryGroup_v1Name) throws Exception {
        Session session = null;
        List<CategoryGroup_v1> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroup_v1 cg where LOWER(cg.name) = LOWER(:categoryGroup_v1Name)")
                    .setString("categoryGroup_v1Name", categoryGroup_v1Name)
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

    //NOT USED
    public CategoryGroup_v1 getCategoryGroup_v1ByNameAndType(String categoryGroup_v1Name, DataBaseConstants.CATEGORY_GROUP_TYPE type, boolean initializeCategories) throws Exception {
        Session session = null;
        CategoryGroup_v1 CategoryGroup_v1 = null;
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            CategoryGroup_v1 = (CategoryGroup_v1) session
                    .createQuery("SELECT cg FROM CategoryGroup_v1 cg where cg.name = :categoryGroup_v1Name and cg.class = :type")
                    .setString("categoryGroup_v1Name", categoryGroup_v1Name)
                    .setString("type", type.getDiscriminator())
                    .uniqueResult();
            if(initializeCategories){
                //wrongClassException check entity classes in session, again eager might interfere
                //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                Hibernate.initialize(CategoryGroup_v1.getCategories());
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
        return CategoryGroup_v1;
    }

    public void removeCategoryGroup_v1(Long id) throws Exception {
        Session session = null;
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            /**
             * session.load()
             * - It will always return a “proxy” (Hibernate term) without hitting the database.
             * In Hibernate, proxy is an object with the given identifier value, its properties are not initialized yet,
             * it just look like a temporary fake object.
             * - If no row found , it will throws an ObjectNotFoundException.

             */
            CategoryGroup_v1 CategoryGroup_v1 = (CategoryGroup_v1) session.load(CategoryGroup_v1.class, id);
            /**
             * session.get()
             * - It always hit the database and return the real object, an object that represent the database row,
             * not proxy.
             * - If no row found , it return null.

             */
//            CategoryGroup_v1 CategoryGroup_v1 = (CategoryGroup_v1) session.get(CategoryGroup_v1.class, id);
            LOG.debug(">>>removeCategoryGroup_v1: " + CategoryGroup_v1);
            session.delete(CategoryGroup_v1);
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

    public <T extends CategoryGroup_v1> List<T> categoryGroup_v1ByType(Class<T> type, boolean initializeCategories) throws Exception {
        Session session = null;
        List<T> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching: see categoryGroup_v1ByType_withLeftJoinFetch
            list = session.createQuery("SELECT cg FROM CategoryGroup_v1 cg WHERE cg.class = :type ORDER BY cg.name")
                    .setParameter("type", type.newInstance().getCategoryGroupType())
                    .list();
            if(initializeCategories){
                for(CategoryGroup_v1 CategoryGroup_v1 : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(CategoryGroup_v1.getCategories());
                }
            }
            Collections.sort(list, (CategoryGroup_v1 cg1,  CategoryGroup_v1 cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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

    public <T extends CategoryGroup_v1> List<T> categoryGroup_v1ByType_withLeftJoinFetch(Class<T> type, boolean initializeCategories) throws Exception {
        Session session = null;
        List<T> list = new ArrayList<>();

        StringBuilder query = new StringBuilder();
//        query.append("SELECT cg FROM CategoryGroupDebit_v1 cg "); //works
//        query.append("SELECT cg FROM CategoryGroup_v1 cg ");
        query.append("SELECT cg FROM ");
        query.append(type.getSimpleName());
        query.append(" cg ");
        if(initializeCategories){
            query.append("left join fetch cg.categories ");
        }
        query.append("WHERE cg.class = :type ORDER BY cg.name ");

        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching
            list = session.createQuery(query.toString())
                    .setParameter("type", type.newInstance().getCategoryGroupType())
                    .list();
            Collections.sort(list, (CategoryGroup_v1 cg1,  CategoryGroup_v1 cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));

//            LOG.debug(">>>query: " + query.toString());
//            System.out.println(">>>query: " + query.toString());
//            for(CategoryGroup_v1 CategoryGroup_v1 : list){
//                LOG.debug(">>>CategoryGroup_v1: " + CategoryGroup_v1);
//                if (CategoryGroup_v1.getCategories().size() > 0) {
//                    LOG.debug(">>>greaterThan0");
//                } else {
//                    LOG.debug(">>>0");
//                }
//            }

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
        //test
        //todo read https://developer.jboss.org/wiki/OpenSessioninView
        LOG.debug(">>>test");
        LOG.debug(">>>query: " + query.toString());
        for(CategoryGroup_v1 CategoryGroup_v1 : list){
            LOG.debug(">>>CategoryGroup_v1: " + CategoryGroup_v1);
            if (CategoryGroup_v1.getCategories().size() > 0) {
                LOG.debug(">>>greaterThan0");
            } else {
                LOG.debug(">>>0");
            }
        }
        return list;
    }

    public List<CategoryGroup_v1> categoryGroup_v1List(boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup_v1> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching -> see categoryGroup_v1ListWithLeftJoinFetch
            list = session.createQuery("SELECT cg FROM CategoryGroup_v1 cg").list();
            if(initializeCategories){
                for(CategoryGroup_v1 CategoryGroup_v1 : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(CategoryGroup_v1.getCategories());
                }
            }
            Collections.sort(list, (CategoryGroup_v1 cg1,  CategoryGroup_v1 cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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

    public List<CategoryGroup_v1> categoryGroup_v1List_withoutBoolean() throws Exception {
        Session session = null;
        List<CategoryGroup_v1> list = new ArrayList<>();
        //less queries
        StringBuilder query = new StringBuilder();
        query.append("SELECT cg FROM CategoryGroup_v1 cg ");

        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery(query.toString()).list();
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
        Collections.sort(list, (CategoryGroup_v1 cg1, CategoryGroup_v1 cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
        list.forEach(categoryGroup -> categoryGroup.setCategories(new ArrayList<>()));
        return list;
    }

    public List<CategoryGroupCredit_v1> categoryGroupCredit_v1List_old(boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroupCredit_v1> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroupCredit_v1 cg").list();
            if(initializeCategories){
                for(CategoryGroup_v1 CategoryGroup_v1 : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(CategoryGroup_v1.getCategories());
                }
            }
            Collections.sort(list, (CategoryGroup_v1 cg1,  CategoryGroup_v1 cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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

    public List<CategoryGroupDebit_v1> categoryGroupDebit_v1List_old(boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroupDebit_v1> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroupDebit_v1 cg ORDER BY cg.name").list();
            if(initializeCategories){
                for(CategoryGroup_v1 CategoryGroup_v1 : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections
                    Hibernate.initialize(CategoryGroup_v1.getCategories());
                }
            }
//            Collections.sort(list, (CategoryGroup_v1 cg1,  CategoryGroup_v1 cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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

    //NOT USED
    public List<CategoryGroup_v1> getCategoryGroup_v1ByName_withLeftJoinFetch(String categoryGroup_v1Name, boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup_v1> list = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT cg FROM CategoryGroup_v1 as cg ");
        if(initializeCategories){
            query.append("left join fetch cg.categories as categories ");
        }
        query.append("where cg.name = :categoryGroup_v1Name ");

        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching
            list = session.createQuery(query.toString())
                    .setString("categoryGroup_v1Name", categoryGroup_v1Name)
                    .list();
            //byName return list as it might be debit or credit and return categories
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

    //p.333 12.2.6 Dynamic eager fetching
    //not working without eager loading
    public List<CategoryGroup_v1> categoryGroup_v1ListWithLeftJoinFetch(boolean initializeCategories) throws Exception {
        Session session = null;

        StringBuilder query = new StringBuilder();
        query.append("SELECT cg FROM CategoryGroup_v1 cg ");
//        query.append("SELECT cg FROM CategoryGroupDebit_v1 cg "); //left join works with concrete class
        if(initializeCategories){
            query.append("left join fetch cg.categories ");
        }

        List<CategoryGroup_v1> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching
            list = session.createQuery(query.toString())
                    .list();
            Collections.sort(list, (CategoryGroup_v1 cg1,  CategoryGroup_v1 cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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

    public List<CategoryGroup_v1> categoryGroup_v1ListWithNativeQuery() throws Exception {
        //http://www.thoughts-on-java.org/result-set-mapping-hibernate-specific-mappings/
        Session session = null;

        StringBuilder querySB = new StringBuilder();
        querySB.append("SELECT {cg.*}, {c.*} ");
        querySB.append("FROM {h-schema}");
        querySB.append(Table.Names.CATEGORY_GROUP);
        querySB.append(" cg ");
        querySB.append("left join {h-schema}");
        querySB.append(Table.Names.CATEGORY);
        querySB.append(" c on c.category_group_id = cg.id ");
        //comment for prod
        querySB.append("where cg.TYPE = 'C' or cg.TYPE = 'D'");

        List<CategoryGroup_v1> result = new ArrayList();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            Map<Long, CategoryGroup_v1> categoryGroupById = new HashMap<>();
            List<Object[]> list = session.createSQLQuery(querySB.toString())
                    .addEntity("cg",CategoryGroup_v1.class)
                    .addEntity("c",Category_v1.class)
                    .list();
            list.stream()
                    .forEach(obj -> {
                        CategoryGroup_v1 cg = (obj[0] != null) ? ((CategoryGroup_v1) obj[0]) : null;
                        Category_v1 c = (obj[1] != null) ? ((Category_v1) obj[1]) : null;
                        if(!categoryGroupById.containsKey(cg.getId())){
                            cg.setCategories(new ArrayList<>());
                            categoryGroupById.put(cg.getId(), cg);
                        }

                        if(c != null) {
                            cg.getCategories().add(c);
                        }
                    });
            session.getTransaction().commit();

            result = new ArrayList(categoryGroupById.values());
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
        Collections.sort(result, (CategoryGroup_v1 cg1,  CategoryGroup_v1 cg2) -> {
            int type = cg1.getCategoryGroupType().toLowerCase().compareTo(cg2.getCategoryGroupType().toLowerCase());
            if(type != 0) return type;

            return cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase());
        });
        return result;
    }
}
