package com.makco.smartfinance.persistence.utils;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryCredit;
import com.makco.smartfinance.persistence.entity.CategoryDebit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.EagerCategoryDebit;
import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.EagerCategoryGroupDebit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by mcalancea on 2016-04-29.
 */
//http://www.devx.com/Java/Article/48193/0/page/2
public class TestPersistenceSession {
    private final static Logger LOG = LogManager.getLogger(TestPersistenceSession.class);
    private static final SessionFactory SESSION_FACTORY;
    private static final ServiceRegistry SERVICE_REGISTRY;

    private static int instanceCount = 0;

    static {
        try{
            Configuration config = getConfiguration();
            SERVICE_REGISTRY = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties())
                    .build();
            config.setSessionFactoryObserver(new SessionFactoryObserver() {
                @Override
                public void sessionFactoryCreated(SessionFactory factory) {

                }

                @Override
                public void sessionFactoryClosed(SessionFactory factory) {
                    StandardServiceRegistryBuilder.destroy(SERVICE_REGISTRY);
                }
            });
            SESSION_FACTORY = config.buildSessionFactory(SERVICE_REGISTRY);
            LOG.debug(">>>>>>>HibernateUtil.instanceCount: " + (++instanceCount));
        }catch (Throwable throwable){
            LOG.error(throwable,throwable);
            throw new ExceptionInInitializerError(throwable);
        }
    }

    private static Configuration getConfiguration() {
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(Category.class);
        cfg.addAnnotatedClass(CategoryCredit.class);
        cfg.addAnnotatedClass(CategoryDebit.class);
        cfg.addAnnotatedClass(EagerCategoryDebit.class);
        cfg.addAnnotatedClass(CategoryGroup.class);
        cfg.addAnnotatedClass(CategoryGroupCredit.class);
        cfg.addAnnotatedClass(CategoryGroupDebit.class);
        cfg.addAnnotatedClass(EagerCategoryGroupDebit.class);
        cfg.addAnnotatedClass(Currency.class);
        cfg.addAnnotatedClass(DateUnit.class);
        cfg.addAnnotatedClass(FamilyMember.class);
        cfg.addAnnotatedClass(Organization.class);
//        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        cfg.setProperty("javax.persistence.jdbc.driver", "org.h2.Driver");
        cfg.setProperty("hibernate.connection.url", DataBaseConstants.URL);
        cfg.setProperty("hibernate.connection.username", "root");
        cfg.setProperty("hibernate.connection.password", "root");
        cfg.setProperty("hibernate.show_sql", "true");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "validate");
        //http://www.tutorialspoint.com/hibernate/hibernate_caching.htm
//        cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        cfg.setProperty("hibernate.current_session_context_class", "thread");

        cfg.setProperty("hibernate.default_schema", "TEST");
        cfg.setProperty("hibernate.jdbc.batch_size", DataBaseConstants.BATCH_SIZE.toString());

        //JTA
        //https://github.com/press0/hibernate-jpa-best-practices/blob/master/environment/src/main/java/org/jpwh/env/TransactionManagerSetup.java
//        cfg.setProperty("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
        return cfg;
    }

    public static Session openSession(){
        if(instanceCount > 1) {
            throw new RuntimeException("There is more than one (1) instance of SESSION_FACTORY configured");
        }
        return SESSION_FACTORY.openSession();
    }

    public static void closeSessionFactory() {
        SESSION_FACTORY.close();
    }
}
