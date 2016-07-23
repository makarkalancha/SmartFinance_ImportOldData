package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v1.Invoice_v1;
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
public class InvoiceDAOImpl_v1ForTest {
    private final static Logger LOG = LogManager.getLogger(InvoiceDAOImpl_v1ForTest.class);

    public Invoice_v1 getInvoiceById(Long id) throws Exception {
        Session session = null;
        Invoice_v1 invoice = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            invoice = session.get(Invoice_v1.class, id);
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

    public Invoice_v1 getInvoiceByIdWithItems(Long id) throws Exception {
        Session session = null;
        Invoice_v1 invoice = null;
        try {
            if(invoice != null) {
                LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->before transaction: dateunit=%s, subtotal=%s, total=%s",
                        invoice.getSubTotal(),
                        invoice.getTotal()));
                LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->before transaction: create=%s, update=%s",
                        invoice.getCreatedOn(),
                        invoice.getUpdatedOn()));
            } else{
                LOG.debug(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->before transaction: invoice == null");
            }

            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            invoice = session.get(Invoice_v1.class, id);
            if(invoice != null) {
                Hibernate.initialize(invoice.getItems());
                for(Item_v1 item_v1 : invoice.getItems()){
                    Hibernate.initialize(item_v1.getTax());
                    Hibernate.initialize(item_v1.getFamilyMember());
                }
            }
            session.getTransaction().commit();

            if(invoice != null) {
                LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->after commit: dateunit=%s, subtotal=%s, total=%s",
                        invoice.getDateUnit(),
                        invoice.getSubTotal(),
                        invoice.getTotal()));
                LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->after commit: create=%s, update=%s",
                        invoice.getCreatedOn(),
                        invoice.getUpdatedOn()));
            } else{
                LOG.debug(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->before transaction: invoice == null");
            }
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

            if(invoice != null) {
                LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->after close: dateunit=%s, subtotal=%s, total=%s",
                        invoice.getDateUnit(),
                        invoice.getSubTotal(),
                        invoice.getTotal()));
                LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->after close: create=%s, update=%s",
                        invoice.getCreatedOn(),
                        invoice.getUpdatedOn()));
            } else{
                LOG.debug(">>>>InvoiceDAOImpl_v1ForTest->getInvoiceByIdWithItems->before transaction: invoice == null");
            }
        }
        return invoice;
    }

    public Invoice_v1 refresh(Invoice_v1 invoice_v1) throws Exception {
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

    public Invoice_v1 merge(Invoice_v1 invoice_v1) throws Exception {
        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.merge(invoice_v1);
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

    public Invoice_v1 update(Invoice_v1 invoice_v1) throws Exception {
        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.update(invoice_v1);
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

    public List<Invoice_v1> getInvoiceByNumber(String invoiceNumber) throws Exception {
        Session session = null;
        List<Invoice_v1> invoices = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            invoices = session.createQuery("SELECT i FROM Invoice_v1 i WHERE invoiceNumber = :invoiceNumber ORDER BY i.invoiceNumber")
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
            Invoice_v1 invoice = session.get(Invoice_v1.class, id);
            invoice.setOrganization(null);
            session.saveOrUpdate(invoice);
//            ////Illegal attempt to associate a collection with two open sessions
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

    public void saveOrUpdateInvoice(Invoice_v1 invoice) throws Exception {

        Session session = null;
        try {
            LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->saveOrUpdateInvoice->before transaction: dateunit=%s, subtotal=%s, total=%s",
                    invoice.getDateUnit(),
                    invoice.getSubTotal(),
                    invoice.getTotal()));
            LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->saveOrUpdateInvoice->before transaction: create=%s, update=%s",
                    invoice.getCreatedOn(),
                    invoice.getUpdatedOn()));
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.saveOrUpdate(invoice);

            session.getTransaction().commit();
            LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->saveOrUpdateInvoice->after commit: dateunit=%s, subtotal=%s, total=%s",
                    invoice.getDateUnit(),
                    invoice.getSubTotal(),
                    invoice.getTotal()));
            LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->saveOrUpdateInvoice->after commit: create=%s, update=%s",
                    invoice.getCreatedOn(),
                    invoice.getUpdatedOn()));
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
            LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->saveOrUpdateInvoice->after close: dateunit=%s, subtotal=%s, total=%s",
                    invoice.getDateUnit(),
                    invoice.getSubTotal(),
                    invoice.getTotal()));
            LOG.debug(String.format(">>>>InvoiceDAOImpl_v1ForTest->saveOrUpdateInvoice->after close: create=%s, update=%s",
                    invoice.getCreatedOn(),
                    invoice.getUpdatedOn()));
        }
    }
}
