package com.makco.smartfinance.persistence.dao.dao_implementations;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.session.invoice_management.v3.Invoice_v3;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Makar Kalancha
 * Date: 17/07/2016
 * Time: 21:39
 */
public class InvoiceDAOImpl_v3ForTest {
    private final static Logger LOG = LogManager.getLogger(InvoiceDAOImpl_v3ForTest.class);

    public Invoice_v3 getInvoiceById(Long id) throws Exception {
        Session session = null;
        Invoice_v3 invoice = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            invoice = session.get(Invoice_v3.class, id);
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

    public Invoice_v3 getInvoiceByIdWithItems(Long id) throws Exception {
        Session session = null;
        Invoice_v3 invoice = null;
        try {
//            if(invoice != null) {
//                LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->before transaction: dateunit=%s, subtotal=%s, total=%s",
//                        invoice.getSubTotal(),
//                        invoice.getTotal()));
//                LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->before transaction: create=%s, update=%s",
//                        invoice.getCreatedOn(),
//                        invoice.getUpdatedOn()));
//            } else{
//                LOG.debug(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->before transaction: invoice == null");
//            }

            session = TestPersistenceSession.openSession();
            session.beginTransaction();
//            invoice = session.get(Invoice_v3.class, id);
//            if(invoice != null) {
//                Hibernate.initialize(invoice.getItems());
//                for(Item_v3 item_v3 : invoice.getItems()){
//                    Hibernate.initialize(item_v3.getTax());
//                    Hibernate.initialize(item_v3.getFamilyMember());
//                }
//            }
            invoice = (Invoice_v3) session.createQuery("select i from Invoice_v3 i left join fetch i.items where i.id = :invoiceId")
                    .setParameter("invoiceId", id)
                    .uniqueResult();
            session.getTransaction().commit();

//            if(invoice != null) {
//                LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->after commit: dateunit=%s, subtotal=%s, total=%s",
//                        invoice.getDateUnit(),
//                        invoice.getSubTotal(),
//                        invoice.getTotal()));
//                LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->after commit: create=%s, update=%s",
//                        invoice.getCreatedOn(),
//                        invoice.getUpdatedOn()));
//            } else{
//                LOG.debug(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->before transaction: invoice == null");
//            }
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

//            if(invoice != null) {
//                LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->after close: dateunit=%s, subtotal=%s, total=%s",
//                        invoice.getDateUnit(),
//                        invoice.getSubTotal(),
//                        invoice.getTotal()));
//                LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->after close: create=%s, update=%s",
//                        invoice.getCreatedOn(),
//                        invoice.getUpdatedOn()));
//            } else{
//                LOG.debug(">>>>InvoiceDAOImpl_v3ForTest->getInvoiceByIdWithItems->before transaction: invoice == null");
//            }
        }
        return invoice;
    }

    public Invoice_v3 refresh(Invoice_v3 invoice_v3) throws Exception {
        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.refresh(invoice_v3);
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
        return invoice_v3;
    }

    public Invoice_v3 merge(Invoice_v3 invoice_v3) throws Exception {
        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.merge(invoice_v3);
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
        return invoice_v3;
    }

    public Invoice_v3 update(Invoice_v3 invoice_v3) throws Exception {
        Session session = null;
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.update(invoice_v3);
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
        return invoice_v3;
    }

    public List<Invoice_v3> getInvoiceByNumber(String invoiceNumber) throws Exception {
        Session session = null;
        List<Invoice_v3> invoices = new ArrayList<>();
        try {
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            invoices = session.createQuery("SELECT i FROM Invoice_v3 i WHERE invoiceNumber = :invoiceNumber ORDER BY i.invoiceNumber")
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
            Invoice_v3 invoice = session.get(Invoice_v3.class, id);
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

    public void saveOrUpdateInvoice(Invoice_v3 invoice) throws Exception {

        Session session = null;
        try {
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateInvoice->before transaction: dateunit=%s, subtotal=%s, total=%s",
//                    invoice.getDateUnit(),
//                    invoice.getSubTotal(),
//                    invoice.getTotal()));
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateInvoice->before transaction: create=%s, update=%s",
//                    invoice.getCreatedOn(),
//                    invoice.getUpdatedOn()));
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            session.saveOrUpdate(invoice);

            session.getTransaction().commit();
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateInvoice->after commit: dateunit=%s, subtotal=%s, total=%s",
//                    invoice.getDateUnit(),
//                    invoice.getSubTotal(),
//                    invoice.getTotal()));
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateInvoice->after commit: create=%s, update=%s",
//                    invoice.getCreatedOn(),
//                    invoice.getUpdatedOn()));
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
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateInvoice->after close: dateunit=%s, subtotal=%s, total=%s",
//                    invoice.getDateUnit(),
//                    invoice.getSubTotal(),
//                    invoice.getTotal()));
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateInvoice->after close: create=%s, update=%s",
//                    invoice.getCreatedOn(),
//                    invoice.getUpdatedOn()));
        }
    }

    public void saveOrUpdateMultipleInvoices(List<Invoice_v3> invoiceList) throws Exception {

        Session session = null;
        try {
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateMultipleInvoices->before transaction: dateunit=%s, subtotal=%s, total=%s",
//                    invoice.getDateUnit(),
//                    invoice.getSubTotal(),
//                    invoice.getTotal()));
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateMultipleInvoices->before transaction: create=%s, update=%s",
//                    invoice.getCreatedOn(),
//                    invoice.getUpdatedOn()));
            session = TestPersistenceSession.openSession();
            session.beginTransaction();

            for (int i = 0; i < invoiceList.size(); i++) {
                Invoice_v3 invoiceV3 = invoiceList.get(i);
                LOG.debug(String.format(">>>>saveOrUpdateMultipleInvoices->(invoiceList.get(%s)): %s", i, invoiceV3.toString()));

                session.saveOrUpdate(invoiceV3);

                if(i % DataBaseConstants.BATCH_SIZE == 0){
                    LOG.debug(String.format(">>>>saveOrUpdateMultipleInvoices->(i %% DataBaseConstants.BATCH_SIZE): %s %% %s = %s",
                            i,
                            DataBaseConstants.BATCH_SIZE,
                            (i % DataBaseConstants.BATCH_SIZE)));
                    session.flush();
                    session.clear();
                    Thread.sleep(50);
                }
            }

            session.getTransaction().commit();
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateMultipleInvoices->after commit: dateunit=%s, subtotal=%s, total=%s",
//                    invoice.getDateUnit(),
//                    invoice.getSubTotal(),
//                    invoice.getTotal()));
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateMultipleInvoices->after commit: create=%s, update=%s",
//                    invoice.getCreatedOn(),
//                    invoice.getUpdatedOn()));
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
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateMultipleInvoices->after close: dateunit=%s, subtotal=%s, total=%s",
//                    invoice.getDateUnit(),
//                    invoice.getSubTotal(),
//                    invoice.getTotal()));
//            LOG.debug(String.format(">>>>InvoiceDAOImpl_v3ForTest->saveOrUpdateMultipleInvoices->after close: create=%s, update=%s",
//                    invoice.getCreatedOn(),
//                    invoice.getUpdatedOn()));
        }
    }

    public List<Invoice_v3> invoiceList() throws Exception {
        Session session = null;
        List<Invoice_v3> list = new ArrayList<>();
        try{
            session = TestPersistenceSession.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT i FROM Invoice_v3 i ORDER BY i.invoiceNumber").list();
            session.getTransaction().commit();

//        List<FamilyMember> list = new ArrayList<>();
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        list = em.createQuery("FROM FamilyMember AS f ORDER BY f.name").getResultList();
//        em.getTransaction().commit();
//        em.close();
//        return list;
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
}
