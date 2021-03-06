package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Makar Kalancha on 2016-05-02.
 *
 * CategoryGroup_v1DAOImplForTest and CategoryGroupDAOImplForTest are the same,
 * only CategoryGroup_v1DAOImplForTest is using entities with "_v1" from test and
 * CategoryGroupDAOImplForTest real from prod
 */
public class CategoryGroupDAOImplForTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDAOImplForTest.class);

    /**
     * check file CategoryGroupSaveOrUpdate.log: find "statement [prep" 6 hits,
     * and 2 (! vs 3 in merge) statements registered, released and closed:
     * 1) update TEST.CATEGORY_GROUP set DESCRIPTION=?, NAME=? where ID=? {1: 'CategoryGroupDebit_description', 2: '_changed67092_changed_v511301', 3: 9}]
     * 2) update TEST.CATEGORY set DESCRIPTION=?, NAME=?, CATEGORY_GROUP_ID=? where ID=? {1: 'debit category #1 ''description''', 2: 'nged_461186_changed_167092', 3: 9, 4: 7}]
     * !!!so use saveOrUpdate and not merge!!!
     */
    public void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateCategoryGroup: start");
            session = TestPersistenceSession.openSession();
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

    /**
     * check file CategoryGroupSaveOrUpdateWithMerge.log: find "statement [prep" 10 hits,
     * and 3 (! vs 2 in saveOrUpdate) statements registered, released and closed:
     * 1) select categorygr0_.ID as ID2_1_1_, categorygr0_.T_CREATEDON as T_CREATE3_1_1_, categorygr0_.DESCRIPTION as DESCRIPT4_1_1_, categorygr0_.NAME as NAME5_1_1_, categorygr0_.T_UPDATEDON as T_UPDATE6_1_1_, categories1_.CATEGORY_GROUP_ID as CATEGORY7_0_3_, categories1_.ID as ID2_0_3_, categories1_.ID as ID2_0_0_, categories1_.T_CREATEDON as T_CREATE3_0_0_, categories1_.DESCRIPTION as DESCRIPT4_0_0_, categories1_.NAME as NAME5_0_0_, categories1_.T_UPDATEDON as T_UPDATE6_0_0_, categories1_.CATEGORY_GROUP_ID as CATEGORY7_0_0_ from TEST.CATEGORY_GROUP categorygr0_ left outer join TEST.CATEGORY categories1_ on categorygr0_.ID=categories1_.CATEGORY_GROUP_ID and categories1_.CATEGORY_GROUP_TYPE='D' where categorygr0_.ID=? and categorygr0_.TYPE='D' order by categories1_.NAME {1: 9}]
     * 2) update TEST.CATEGORY set DESCRIPTION=?, NAME=?, CATEGORY_GROUP_ID=? where ID=? {1: 'debit category #2 ''description''', 2: 'nged_511301_changed_417087', 3: 9, 4: 8}]
     * 3) update TEST.CATEGORY_GROUP set DESCRIPTION=?, NAME=? where ID=? {1: 'CategoryGroupDebit_description', 2: '_changed_v511301_changed_v417087', 3: 9}]
     * !!!so use saveOrUpdate and not merge!!!
     */
    public void saveOrUpdateWithMergeCategoryGroup(CategoryGroup categoryGroup) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateWithMergeCategoryGroup: start");
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.merge(categoryGroup);
            session.getTransaction().commit();
            LOG.debug(">>>saveOrUpdateWithMergeCategoryGroup: end");
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
                //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections

                ////JPQL doesn't work because of inheritance
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

    //NOT USED
    public List<CategoryGroup> getCategoryGroupByName_withCategories(String categoryGroupName, boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching: see getCategoryGroupByName_withLeftJoinFetch
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

                    ////JPQL doesn't work because of inheritance
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

    public List<CategoryGroup> getCategoryGroupByName_forValidation(String categoryGroupName) throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroup cg where LOWER(cg.name) = LOWER(:categoryGroupName)")
                    .setString("categoryGroupName", categoryGroupName)
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
    public CategoryGroup getCategoryGroupByNameAndType(String categoryGroupName, DataBaseConstants.CATEGORY_GROUP_TYPE type, boolean initializeCategories) throws Exception {
        Session session = null;
        CategoryGroup categoryGroup = null;
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            categoryGroup = (CategoryGroup) session
                    .createQuery("SELECT cg FROM CategoryGroup cg where cg.name = :categoryGroupName and cg.class = :type")
                    .setString("categoryGroupName", categoryGroupName)
                    .setString("type", type.getDiscriminator())
                    .uniqueResult();
            if(initializeCategories){
                //wrongClassException check entity classes in session, again eager might interfere
                //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections

                ////JPQL doesn't work because of inheritance
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

    public void removeCategoryGroup(Long id) throws Exception {
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
//            CategoryGroup CategoryGroup = (CategoryGroup) session.load(CategoryGroup.class, id);
            /**
             * session.get()
             * - It always hit the database and return the real object, an object that represent the database row,
             * not proxy.
             * - If no row found , it return null.

             */
            CategoryGroup CategoryGroup = (CategoryGroup) session.get(CategoryGroup.class, id);
            LOG.debug(">>>removeCategoryGroup: " + CategoryGroup);
            session.delete(CategoryGroup);
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

    public <T extends CategoryGroup> List<T> categoryGroupByType(Class<T> type, boolean initializeCategories) throws Exception {
        Session session = null;
        List<T> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching: see categoryGroupByType_withLeftJoinFetch
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

                    ////JPQL doesn't work because of inheritance
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

    public <T extends CategoryGroup> List<T> categoryGroupByType_withLeftJoinFetch(Class<T> type, boolean initializeCategories) throws Exception {
        Session session = null;
        List<T> list = new ArrayList<>();

        StringBuilder query = new StringBuilder();
//        query.append("SELECT cg FROM CategoryGroupDebit cg "); //works
//        query.append("SELECT cg FROM CategoryGroup cg ");
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
            Collections.sort(list, (CategoryGroup cg1,  CategoryGroup cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));

//            LOG.debug(">>>query: " + query.toString());
//            System.out.println(">>>query: " + query.toString());
//            for(CategoryGroup CategoryGroup : list){
//                LOG.debug(">>>CategoryGroup: " + CategoryGroup);
//                if (CategoryGroup.getCategories().size() > 0) {
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
        LOG.debug(">>>test");
        LOG.debug(">>>query: " + query.toString());
        for(CategoryGroup CategoryGroup : list){
            LOG.debug(">>>CategoryGroup: " + CategoryGroup);
            if (CategoryGroup.getCategories().size() > 0) {
                LOG.debug(">>>greaterThan0");
            } else {
                LOG.debug(">>>0");
            }
        }
        return list;
    }

    public List<CategoryGroup> categoryGroupList(boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching -> see categoryGroupListWithLeftJoinFetch
            list = session.createQuery("SELECT cg FROM CategoryGroup cg").list();
            if(initializeCategories){
                for(CategoryGroup categoryGroup : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections

                    ////JPQL doesn't work because of inheritance
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

    public List<CategoryGroup> categoryGroupList_withoutBoolean() throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        //less queries
        StringBuilder query = new StringBuilder();
        query.append("SELECT cg FROM CategoryGroup cg ");

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
        Collections.sort(list, (CategoryGroup cg1, CategoryGroup cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
        list.forEach(categoryGroup -> categoryGroup.setCategories(new ArrayList<>()));
        return list;
    }

    public List<CategoryGroupCredit> categoryGroupCreditList_old(boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroupCredit> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroupCredit cg").list();
            if(initializeCategories){
                for(CategoryGroup categoryGroup : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections

                    ////JPQL doesn't work because of inheritance
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

    public List<CategoryGroupDebit> categoryGroupDebitList_old(boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroupDebit> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT cg FROM CategoryGroupDebit cg ORDER BY cg.name").list();
            if(initializeCategories){
                for(CategoryGroup categoryGroup : list) {
                    //wrongClassException check entity classes in session, again eager might interfere
                    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
                    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
                    //http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
                    //http://stackoverflow.com/questions/19928568/hibernate-best-practice-to-pull-all-lazy-collections

                    ////JPQL doesn't work because of inheritance
                    Hibernate.initialize(categoryGroup.getCategories());
                }
            }
//            Collections.sort(list, (CategoryGroup cg1,  CategoryGroup cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
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
    public List<CategoryGroup> getCategoryGroupByName_withLeftJoinFetch(String categoryGroupName, boolean initializeCategories) throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT cg FROM CategoryGroup as cg ");
        if(initializeCategories){
            query.append("left join fetch cg.categories as categories ");
        }
        query.append("where cg.name = :categoryGroupName ");

        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching
            list = session.createQuery(query.toString())
                    .setString("categoryGroupName", categoryGroupName)
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
    public List<CategoryGroup> categoryGroupListWithLeftJoinFetch(boolean initializeCategories) throws Exception {
        Session session = null;

        StringBuilder query = new StringBuilder();
        query.append("SELECT cg FROM CategoryGroup cg ");
//        query.append("SELECT cg FROM CategoryGroupDebit cg "); //left join works with concrete class
        if(initializeCategories){
            query.append("left join fetch cg.categories ");
        }

        List<CategoryGroup> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching
            list = session.createQuery(query.toString())
                    .list();
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

    public List<CategoryGroup> categoryGroupListWithNativeQuery() throws Exception {
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

        List<CategoryGroup> result = new ArrayList();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            Map<Long, CategoryGroup> categoryGroupById = new HashMap<>();
            List<Object[]> list = session.createSQLQuery(querySB.toString())
                    .addEntity("cg",CategoryGroup.class)
                    .addEntity("c",Category.class)
                    .list();
            list.stream()
                    .forEach(obj -> {
                        CategoryGroup cg = (obj[0] != null) ? ((CategoryGroup) obj[0]) : null;
                        Category c = (obj[1] != null) ? ((Category) obj[1]) : null;
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
        Collections.sort(result, (CategoryGroup cg1,  CategoryGroup cg2) -> {
            int type = cg1.getCategoryGroupType().getDiscriminator().toLowerCase().compareTo(
                    cg2.getCategoryGroupType().getDiscriminator().toLowerCase());
            if(type != 0) return type;

            return cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase());
        });
        return result;
    }
}
