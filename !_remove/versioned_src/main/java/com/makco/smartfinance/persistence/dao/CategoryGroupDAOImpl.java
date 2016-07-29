package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
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
 * Created by Makar Kalancha on 2016-05-12.
 */
public class CategoryGroupDAOImpl implements CategoryGroupDAO{
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDAOImpl.class);

    @Override
    public List<CategoryGroup> categoryGroupListWithoutCategories() throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        //less queries
        StringBuilder query = new StringBuilder();
        query.append("SELECT cg FROM CategoryGroup cg ");

        try{
            session = HibernateUtil.openSession();
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
            CategoryGroup categoryGroup = (CategoryGroup) session.get(CategoryGroup.class, id);
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
    public List<CategoryGroup> getCategoryGroupByName(String categoryGroupName) throws Exception {
        Session session = null;
        List<CategoryGroup> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching
            list = session.createQuery("SELECT cg FROM CategoryGroup cg where LOWER(cg.name) = LOWER(:categoryGroupName)")
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

    @Override
    public CategoryGroup getCategoryGroupById(Long id, boolean initializeCategories) throws Exception {
        Session session = null;
        CategoryGroup categoryGroup = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            categoryGroup = (CategoryGroup) session.get(CategoryGroup.class, id);
            if(initializeCategories && categoryGroup != null){
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

    @Override
    public List<CategoryGroup> categoryGroupListWithCategories() throws Exception {
        Session session = null;

        StringBuilder querySB = new StringBuilder();
        querySB.append("SELECT {cg.*}, {c.*} ");
        querySB.append("FROM {h-schema}");
        querySB.append(Table.Names.CATEGORY_GROUP);
        querySB.append(" cg ");
        querySB.append("left join {h-schema}");
        querySB.append(Table.Names.CATEGORY);
        querySB.append(" c on c.category_group_id = cg.id ");

        List<CategoryGroup> result = new ArrayList();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Map<Long, CategoryGroup> categoryGroupById = new HashMap<>();
            List<Object[]> list = session.createSQLQuery(querySB.toString())
                    .addEntity("cg", CategoryGroup.class)
                    .addEntity("c", Category.class)
                    .list();
            list.stream()
                    .forEach(obj -> {
                        CategoryGroup cg = (obj[0] != null) ? ((CategoryGroup) obj[0]) : null;
                        Category c = (obj[1] != null) ? ((Category) obj[1]) : null;
                        if (!categoryGroupById.containsKey(cg.getId())) {
                            cg.setCategories(new ArrayList<>());
                            categoryGroupById.put(cg.getId(), cg);
                        }

                        if (c != null) {
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
            if (session != null) {
                session.close();
            }
        }
        Collections.sort(result, (CategoryGroup cg1, CategoryGroup cg2) -> {
            int type = cg1.getCategoryGroupType().getDiscriminator().toLowerCase().compareTo(
                    cg2.getCategoryGroupType().getDiscriminator().toLowerCase());
            if (type != 0) return type;

            return cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase());
        });
        return result;
    }
}
