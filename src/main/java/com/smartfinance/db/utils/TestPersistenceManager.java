package com.smartfinance.db.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 * Created by mcalancea on 2016-03-02.
 */
public enum TestPersistenceManager {
    INSTANCE;

    @PersistenceUnit
    private EntityManagerFactory emFactory;

    private TestPersistenceManager() {
        emFactory = Persistence.createEntityManagerFactory("test");
    }

    public EntityManager getEntityManager(){
        return emFactory.createEntityManager();
    }

    public void close(){
        emFactory.close();
    }
}
