package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.persistence.dao.TaxDAO;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Makar Kalancha on 06 Jul 2016.
 */
public class TaxDAOImpl_v1ForTest implements TaxDAO{
    //http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
    private final static Logger LOG = LogManager.getLogger(TaxDAOImpl_v1ForTest.class);

    public Tax getTaxById(Long id) throws Exception {
        Session session = null;
        Tax tax = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            tax = session.get(Tax.class, id);
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
            if (session != null) {
                session.close();
            }
        }
        return tax;
    }

    public Tax getTaxByIdWithParentsChildren(Long id) throws Exception {
        Session session = null;
        Tax tax = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
//            tax = session.get(Tax.class, id);
//            if(tax != null) {
//                Hibernate.initialize(tax.getParentTaxes());
//                Hibernate.initialize(tax.getChildTaxes());
//            }
            //JPQL
            StringBuilder jpql = new StringBuilder("select t from Tax t ");
            jpql.append(" left join fetch t.childTaxes ");
            jpql.append(" left join fetch t.parentTaxes ");
            jpql.append(" where t.id = :taxId");
            tax = (Tax) session.createQuery(jpql.toString())
                    .setParameter("taxId",id)
                    .uniqueResult();

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
            if (session != null) {
                session.close();
            }
        }
        return tax;
    }

    public List<Tax> taxList() throws Exception {
        Session session = null;
        List<Tax> list = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT t FROM Tax t ORDER BY t.name").list();
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
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    /*
    see test_Tax_42_select_benchMark.ods
         100 times = 0:0:144,169106
         1_000 times = 0:0:95,216793
     */

    public List<Tax> taxListWithAssociations() throws Exception {
        Session session = null;
        List<Tax> list = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            ////http://stackoverflow.com/questions/12425835/jpql-manytomany-select
            list = session.createQuery("SELECT t FROM Tax t left join fetch t.childTaxes ORDER BY t.name").list();
//            //https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/queryhql.html
//            list = session.createQuery("SELECT t FROM Tax t all properties t.childTaxes ORDER BY t.name").list();
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
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    /*
    cartesian product: 2 left join
    fastest
    see test_Tax_42_select_benchMark.ods
    100 times = 0:0:178,10545
    1_000 times = 0:0:104,793171

    query:
     SELECT tax0_.ID AS ID1_3_0_,
       tax2_.ID AS ID1_3_1_,
       tax4_.ID AS ID1_3_2_,
       tax0_.T_CREATEDON AS T_CREATE2_3_0_,
       tax0_.DENORMALIZED_FORMULA AS DENORMAL3_3_0_,
       tax0_.DESCRIPTION AS DESCRIPT4_3_0_,
       tax0_.ENDDATE AS ENDDATE5_3_0_,
       tax0_.FORMULA AS FORMULA6_3_0_,
       tax0_.NAME AS NAME7_3_0_,
       tax0_.RATE AS RATE8_3_0_,
       tax0_.STARTDATE AS STARTDAT9_3_0_,
       tax0_.T_UPDATEDON AS T_UPDAT10_3_0_,
       tax2_.T_CREATEDON AS T_CREATE2_3_1_,
       tax2_.DENORMALIZED_FORMULA AS DENORMAL3_3_1_,
       tax2_.DESCRIPTION AS DESCRIPT4_3_1_,
       tax2_.ENDDATE AS ENDDATE5_3_1_,
       tax2_.FORMULA AS FORMULA6_3_1_,
       tax2_.NAME AS NAME7_3_1_,
       tax2_.RATE AS RATE8_3_1_,
       tax2_.STARTDATE AS STARTDAT9_3_1_,
       tax2_.T_UPDATEDON AS T_UPDAT10_3_1_,
       childtaxes1_.TAX_ID AS TAX_ID1_4_0__,
       childtaxes1_.CHILD_TAX_ID AS CHILD_TA2_4_0__,
       tax4_.T_CREATEDON AS T_CREATE2_3_2_,
       tax4_.DENORMALIZED_FORMULA AS DENORMAL3_3_2_,
       tax4_.DESCRIPTION AS DESCRIPT4_3_2_,
       tax4_.ENDDATE AS ENDDATE5_3_2_,
       tax4_.FORMULA AS FORMULA6_3_2_,
       tax4_.NAME AS NAME7_3_2_,
       tax4_.RATE AS RATE8_3_2_,
       tax4_.STARTDATE AS STARTDAT9_3_2_,
       tax4_.T_UPDATEDON AS T_UPDAT10_3_2_,
       parenttaxe3_.CHILD_TAX_ID AS CHILD_TA2_4_1__,
       parenttaxe3_.TAX_ID AS TAX_ID1_4_1__
FROM TEST.TAX tax0_
LEFT OUTER JOIN TEST.TAX_CHILD childtaxes1_ ON tax0_.ID=childtaxes1_.TAX_ID
LEFT OUTER JOIN TEST.TAX tax2_ ON childtaxes1_.CHILD_TAX_ID=tax2_.ID
LEFT OUTER JOIN TEST.TAX_CHILD parenttaxe3_ ON tax0_.ID=parenttaxe3_.CHILD_TAX_ID
LEFT OUTER JOIN TEST.TAX tax4_ ON parenttaxe3_.TAX_ID=tax4_.ID
ORDER BY tax0_.NAME
     */
    public List<Tax> taxListWithChildrenAndParents_leftJoinFetch() throws Exception {
        Session session = null;
        List<Tax> list = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            ////http://stackoverflow.com/questions/12425835/jpql-manytomany-select
            list = session.createQuery("SELECT t FROM Tax t left join fetch t.childTaxes left join fetch t.parentTaxes ORDER BY t.name").list();
//            //https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/queryhql.html
//            list = session.createQuery("SELECT t FROM Tax t all properties t.childTaxes ORDER BY t.name").list();
            session.getTransaction().commit();

            //clean cartesian product: LinkedHashSet to save the order
            Set<Tax> set = new LinkedHashSet<>(list);
            list = new ArrayList<>(set);
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
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    /*
    select + 1
    see test_Tax_42_select_benchMark.ods
    100 times = 0:0:350,873650
    1_000 times = 0:0:277,691992

    total count tax = 50
    1 query to fetch all taxes
    50 queries to fetch children for each tax
    50 queries to fetch parents for each tax
    -------------------------------------------
    total: 101

    1
SELECT tax0_.ID AS ID1_3_,
       tax0_.T_CREATEDON AS T_CREATE2_3_,
       tax0_.DENORMALIZED_FORMULA AS DENORMAL3_3_,
       tax0_.DESCRIPTION AS DESCRIPT4_3_,
       tax0_.ENDDATE AS ENDDATE5_3_,
       tax0_.FORMULA AS FORMULA6_3_,
       tax0_.NAME AS NAME7_3_,
       tax0_.RATE AS RATE8_3_,
       tax0_.STARTDATE AS STARTDAT9_3_,
       tax0_.T_UPDATEDON AS T_UPDAT10_3_
FROM TEST.TAX tax0_
ORDER BY tax0_.NAME

50
SELECT childtaxes0_.TAX_ID AS TAX_ID1_4_0_,
       childtaxes0_.CHILD_TAX_ID AS CHILD_TA2_4_0_,
       tax1_.ID AS ID1_3_1_,
       tax1_.T_CREATEDON AS T_CREATE2_3_1_,
       tax1_.DENORMALIZED_FORMULA AS DENORMAL3_3_1_,
       tax1_.DESCRIPTION AS DESCRIPT4_3_1_,
       tax1_.ENDDATE AS ENDDATE5_3_1_,
       tax1_.FORMULA AS FORMULA6_3_1_,
       tax1_.NAME AS NAME7_3_1_,
       tax1_.RATE AS RATE8_3_1_,
       tax1_.STARTDATE AS STARTDAT9_3_1_,
       tax1_.T_UPDATEDON AS T_UPDAT10_3_1_
FROM TEST.TAX_CHILD childtaxes0_
INNER JOIN TEST.TAX tax1_ ON childtaxes0_.CHILD_TAX_ID=tax1_.ID
WHERE childtaxes0_.TAX_ID=?
50
SELECT parenttaxe0_.CHILD_TAX_ID AS CHILD_TA2_4_0_,
       parenttaxe0_.TAX_ID AS TAX_ID1_4_0_,
       tax1_.ID AS ID1_3_1_,
       tax1_.T_CREATEDON AS T_CREATE2_3_1_,
       tax1_.DENORMALIZED_FORMULA AS DENORMAL3_3_1_,
       tax1_.DESCRIPTION AS DESCRIPT4_3_1_,
       tax1_.ENDDATE AS ENDDATE5_3_1_,
       tax1_.FORMULA AS FORMULA6_3_1_,
       tax1_.NAME AS NAME7_3_1_,
       tax1_.RATE AS RATE8_3_1_,
       tax1_.STARTDATE AS STARTDAT9_3_1_,
       tax1_.T_UPDATEDON AS T_UPDAT10_3_1_
FROM TEST.TAX_CHILD parenttaxe0_
INNER JOIN TEST.TAX tax1_ ON parenttaxe0_.TAX_ID=tax1_.ID
WHERE parenttaxe0_.CHILD_TAX_ID=?

     */
    public List<Tax> taxListWithChildrenAndParents_HibernateInitialize() throws Exception {
        Session session = null;
        List<Tax> list = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            ////http://stackoverflow.com/questions/12425835/jpql-manytomany-select
            list = session.createQuery("SELECT t FROM Tax t ORDER BY t.name").list();
            for(Tax tax : list) {
                Hibernate.initialize(tax.getChildTaxes());
                Hibernate.initialize(tax.getParentTaxes());
            }
//            //https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/queryhql.html
//            list = session.createQuery("SELECT t FROM Tax t all properties t.childTaxes ORDER BY t.name").list();
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
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public List<Tax> getTaxByName(String name) throws Exception {
        Session session = null;
        List<Tax> taxes = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            taxes = session.createQuery("SELECT t FROM Tax t WHERE name = :name ORDER BY t.name")
                    .setString("name", name)
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
            if (session != null) {
                session.close();
            }
        }
        return taxes;
    }

    public void removeTax(Long id) throws Exception {
        Session session = null;
        try {
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
            Tax tax = session.get(Tax.class, id);
            tax.setChildTaxes(new HashSet<>());
            tax.setParentTaxes(new HashSet<>());
//            //no error like: Illegal attempt to associate a collection with two open sessions
//            saveOrUpdateTax(tax);
            session.saveOrUpdate(tax);

            /*
            Referential integrity constraint violation: "CONSTRAINT_DBB3: TEST.TAX_CHILD FOREIGN KEY(CHILD_TAX_ID) REFERENCES TEST.TAX(ID) (560)"
            @ManyToMany(mappedBy = "childTaxes", cascade = CascadeType.ALL)
            so:
            @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
            @JoinTable(name = "TAX_CHILD",
                joinColumns = {@JoinColumn(name = "CHILD_TAX_ID", referencedColumnName = "ID")},
                inverseJoinColumns = {@JoinColumn(name = "TAX_ID", referencedColumnName = "ID")}
            )
            private Set<Tax> parentTaxes = new HashSet<>();
             */
            session.delete(tax);
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
            if (session != null) {
                session.close();
            }
        }
    }

    public void saveOrUpdateTax(Tax tax) throws Exception {

        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.saveOrUpdate(tax);
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
            if (session != null) {
                session.close();
            }
        }
    }
}
