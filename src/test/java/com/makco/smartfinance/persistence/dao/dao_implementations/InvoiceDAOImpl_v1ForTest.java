package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.persistence.entity.Invoice;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.entity.Transaction;
import com.makco.smartfinance.persistence.utils.HibernateUtilTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * User: Makar Kalancha
 * Date: 17/07/2016
 * Time: 21:39
 */
public class InvoiceDAOImpl_v1ForTest {
    private final static Logger LOG = LogManager.getLogger(InvoiceDAOImpl_v1ForTest.class);

    public Invoice getInvoiceById(Long id) throws Exception {
        Session session = null;
        Invoice invoice = null;
        try {
            session = HibernateUtilTest.openSession();
            session.beginTransaction();
            invoice = session.get(Invoice.class, id);
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
        return invoice;
    }

    public List<Invoice> getInvoiceByNumber(String invoiceNumber) throws Exception {
        Session session = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            session = HibernateUtilTest.openSession();
            session.beginTransaction();
            invoices = session.createQuery("SELECT i FROM Invoice i WHERE invoiceNumber = :invoiceNumber ORDER BY i.invoiceNumber")
                    .setString("invoiceNumber", invoiceNumber)
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
        return invoices;
    }

    public void removeInvoice(Long id) throws Exception {
        Session session = null;
        try {
            session = HibernateUtilTest.openSession();
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
            Invoice invoice = session.load(Invoice.class, id);

            //todo organization don't delete
            //todo items DO delete

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
            session.delete(invoice);
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

    public void saveOrUpdateTax(Invoice invoice) throws Exception {

        Session session = null;
        try {
            session = HibernateUtilTest.openSession();
            session.beginTransaction();
            session.saveOrUpdate(invoice);
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
