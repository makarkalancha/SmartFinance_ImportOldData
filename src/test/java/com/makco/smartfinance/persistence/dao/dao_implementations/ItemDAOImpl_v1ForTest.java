package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Item_v1;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Item_v1;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Makar Kalancha
 * Date: 17/07/2016
 * Time: 21:39
 */
public class ItemDAOImpl_v1ForTest {
    private final static Logger LOG = LogManager.getLogger(ItemDAOImpl_v1ForTest.class);

    public Item_v1 getItemById(Long id) throws Exception {
        Session session = null;
        Item_v1 item_v1 = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            item_v1 = session.get(Item_v1.class, id);
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
        return item_v1;
    }

    public void removeItem(Long id) throws Exception {
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
            Item_v1 item_v1 = session.load(Item_v1.class, id);

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
            session.delete(item_v1);
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

    public void saveOrUpdateItem(Item_v1 item_v1) throws Exception {

        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.saveOrUpdate(item_v1);
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

    public void saveOrUpdateMultipleItem(List<Item_v1> item_v1_list) throws Exception {

        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();


            /*
            batch saving is working if "cascade = CascadeType.ALL" is only in Item_v1
            if it's in both Invoice_v1 and Item_v1 than saving is as follows:
            1) item0 is saved (invoice id is null)
            2) invoice is saved
            3) item1 is saved (invoice id is NOT null)
            4) item2 is saved
            5) item3 is saved
            6) invoice triggers saving of item1 (that was saved) that's why
            unique index (invoice id and order number) is broken

            if "cascade = CascadeType.ALL" is in both Invoice_v1 and Item_v1 than
            it is enough to save item0 and invoice with other items will be saved:
//            session.save(item_v1_list.get(0));
             */
            for (int i = 0; i < item_v1_list.size(); i++) {
                LOG.debug(String.format(">>>>item_v1_list.get(%s): %s", i, item_v1_list.get(i).toString()));
                session.save(item_v1_list.get(i));
                if(i % DataBaseConstants.BATCH_SIZE == 0){
                    LOG.debug(String.format(">>>>i %% DataBaseConstants.BATCH_SIZE: %s %% %s = %s",
                            i,
                            DataBaseConstants.BATCH_SIZE,
                            (i % DataBaseConstants.BATCH_SIZE)));
                    session.flush();
                    session.clear();
                    Thread.sleep(50);
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
            if (session != null) {
                session.close();
            }
        }
    }
}
