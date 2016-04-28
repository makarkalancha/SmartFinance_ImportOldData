package com.makco.smartfinance.persistence.utils;

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
        if(!emFactory.isOpen()){
            emFactory = Persistence.createEntityManagerFactory("test");
        }
        return emFactory.createEntityManager();
    }

    public void closeFactory(){
        emFactory.close();
    }
}
