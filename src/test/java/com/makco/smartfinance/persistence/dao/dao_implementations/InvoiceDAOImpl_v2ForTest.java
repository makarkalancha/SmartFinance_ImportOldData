package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.persistence.entity.session.invoice_management.v2.Invoice_v2;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v2.Item_v2;
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
public class InvoiceDAOImpl_v2ForTest {
    private final static Logger LOG = LogManager.getLogger(InvoiceDAOImpl_v2ForTest.class);

    public Invoice_v2 getInvoiceById(Long id) throws Exception {
        Session session = null;
        Invoice_v2 invoice = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            invoice = session.get(Invoice_v2.class, id);
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

    public Invoice_v2 getInvoiceByIdWithItems(Long id) throws Exception {
        Session session = null;
        Invoice_v2 invoice = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
//            invoice = session.get(Invoice_v2.class, id);
//            if(invoice != null) {
//                Hibernate.initialize(invoice.getItems());
//                for(Item_v2 item_v1 : invoice.getItems()){
//                    Hibernate.initialize(item_v1.getTax());
//                    Hibernate.initialize(item_v1.getFamilyMember());
//                }
//            }
            invoice = (Invoice_v2) session.createQuery("select i from Invoice_v2 i left join fetch i.items where i.id = :invoiceId")
                    .setParameter("invoiceId", id)
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
        return invoice;
    }

    public Invoice_v2 refresh(Invoice_v2 invoice_v1) throws Exception {
        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.refresh(invoice_v1);
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
        return invoice_v1;
    }

    public List<Invoice_v2> getInvoiceByNumber(String invoiceNumber) throws Exception {
        Session session = null;
        List<Invoice_v2> invoices = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            invoices = session.createQuery("SELECT i FROM Invoice_v2 i WHERE invoiceNumber = :invoiceNumber ORDER BY i.invoiceNumber")
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
            Invoice_v2 invoice = session.get(Invoice_v2.class, id);
            invoice.setOrganization(null);
            session.saveOrUpdate(invoice);
            ////Illegal attempt to associate a collection with two open sessions
//            saveOrUpdateInvoice(invoice);

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

    public void saveOrUpdateInvoice(Invoice_v2 invoice) throws Exception {

        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
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
