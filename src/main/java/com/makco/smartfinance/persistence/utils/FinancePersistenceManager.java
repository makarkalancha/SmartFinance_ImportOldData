package com.makco.smartfinance.persistence.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 * Created by mcalancea on 2016-03-02.
 */
public enum FinancePersistenceManager {
    INSTANCE;

    @PersistenceUnit
    private EntityManagerFactory emFactory;

    private FinancePersistenceManager() {
        emFactory = Persistence.createEntityManagerFactory("finance");
    }

    public EntityManager getEntityManager(){
        return emFactory.createEntityManager();
    }

    public void close(){
        emFactory.close();
    }
}