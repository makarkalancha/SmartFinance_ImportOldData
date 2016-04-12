package com.makco.smartfinance.persistence.utils;

import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by mcalancea on 2016-04-05.
 */
//http://www.devx.com/Java/Article/48193/0/page/2
public class HibernateUtil {
    private final static Logger LOG = LogManager.getLogger(HibernateUtil.class);
    private static final SessionFactory SESSION_FACTORY;
    private static final ServiceRegistry SERVICE_REGISTRY;
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
        }catch (Throwable throwable){
            LOG.error(throwable,throwable);
            throw new ExceptionInInitializerError(throwable);
        }
    }

    private static Configuration getConfiguration() {
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(FamilyMember.class);
        cfg.addAnnotatedClass(Currency.class);
//        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        cfg.setProperty("javax.persistence.jdbc.driver", "org.h2.Driver");
        cfg.setProperty("hibernate.connection.url", DataBaseConstants.URL);
        cfg.setProperty("hibernate.connection.username", DataBaseConstants.USERNAME);
        cfg.setProperty("hibernate.connection.password", DataBaseConstants.PASSWORD);
        cfg.setProperty("hibernate.show_sql", "true");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "validate");
        //http://www.tutorialspoint.com/hibernate/hibernate_caching.htm
//        cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        cfg.setProperty("hibernate.current_session_context_class", "thread");

        cfg.setProperty("hibernate.default_schema", "FINANCE");
        return cfg;
    }

    public static Session openSession() {
        return SESSION_FACTORY.openSession();
    }
}
