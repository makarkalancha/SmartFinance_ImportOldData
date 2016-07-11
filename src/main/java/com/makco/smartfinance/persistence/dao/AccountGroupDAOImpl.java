package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.persistence.entity.AccountGroup;
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
 * Created by Makar Kalancha on 2016-06-06.
 */
public class AccountGroupDAOImpl implements AccountGroupDAO{
    private final static Logger LOG = LogManager.getLogger(AccountGroupDAOImpl.class);

    @Override
    public List<AccountGroup> accountGroupListWithoutCategories() throws Exception {
        Session session = null;
        List<AccountGroup> list = new ArrayList<>();
        //less queries
        StringBuilder query = new StringBuilder();
        query.append("SELECT ag FROM AccountGroup ag ");

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
        Collections.sort(list, (AccountGroup cg1, AccountGroup cg2) -> cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase()));
        list.forEach(accountGroup -> accountGroup.setAccounts(new ArrayList<>()));
        return list;
    }

    @Override
    public void saveOrUpdateAccountGroup(AccountGroup accountGroup) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateAccountGroup: start");
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(accountGroup);
            session.getTransaction().commit();
            LOG.debug(">>>saveOrUpdateAccountGroup: end");
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

    public void removeAccountGroup(Long id) throws Exception {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            AccountGroup accountGroup = (AccountGroup) session.get(AccountGroup.class, id);
            LOG.debug(">>>removeAccountGroup: " + accountGroup);
            session.delete(accountGroup);
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
    public List<AccountGroup> getAccountGroupByName(String accountGroupName) throws Exception {
        Session session = null;
        List<AccountGroup> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            //p.333 12.2.6 Dynamic eager fetching
            list = session.createQuery("SELECT ag FROM AccountGroup ag where LOWER(ag.name) = LOWER(:accountGroupName)")
                    .setString("accountGroupName", accountGroupName)
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
    public AccountGroup getAccountGroupById(Long id, boolean initializeCategories) throws Exception {
        Session session = null;
        AccountGroup accountGroup = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            accountGroup = (AccountGroup) session.get(AccountGroup.class, id);
            if(initializeCategories){
                Hibernate.initialize(accountGroup.getAccounts());
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
        return accountGroup;
    }

    @Override
    public List<AccountGroup> accountGroupListWithCategories() throws Exception {
        Session session = null;

        StringBuilder querySB = new StringBuilder();
        querySB.append("SELECT {ag.*}, {a.*} ");
        querySB.append("FROM {h-schema}");
        querySB.append(Table.Names.ACCOUNT_GROUP);
        querySB.append(" ag ");
        querySB.append("left join {h-schema}");
        querySB.append(Table.Names.ACCOUNT);
        querySB.append(" a on a.account_group_id = ag.id ");

        List<AccountGroup> result = new ArrayList();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Map<Long, AccountGroup> accountGroupById = new HashMap<>();
            List<Object[]> list = session.createSQLQuery(querySB.toString())
                    .addEntity("ag", AccountGroup.class)
                    .addEntity("a", Account.class)
                    .list();
            list.stream()
                    .forEach(obj -> {
                        AccountGroup ag = (obj[0] != null) ? ((AccountGroup) obj[0]) : null;
                        Account a = (obj[1] != null) ? ((Account) obj[1]) : null;
                        if (!accountGroupById.containsKey(ag.getId())) {
                            ag.setAccounts(new ArrayList<>());
                            accountGroupById.put(ag.getId(), ag);
                        }

                        if (a != null) {
                            ag.getAccounts().add(a);
                        }
                    });
            session.getTransaction().commit();
            result = new ArrayList(accountGroupById.values());
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
        Collections.sort(result, (AccountGroup cg1, AccountGroup cg2) -> {
            int type = cg1.getAccountGroupType().getDiscriminator().toLowerCase().compareTo(
                    cg2.getAccountGroupType().getDiscriminator().toLowerCase());
            if (type != 0) return type;

            return cg1.getName().toLowerCase().compareTo(cg2.getName().toLowerCase());
        });
        return result;
    }
}
