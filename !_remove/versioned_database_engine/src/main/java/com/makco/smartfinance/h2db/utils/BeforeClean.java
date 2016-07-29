package com.makco.smartfinance.h2db.utils;

import org.flywaydb.core.api.callback.BaseFlywayCallback;

import java.sql.Connection;

/**
 * User: Makar Kalancha
 * Date: 21/04/2016
 * Time: 22:39
 */
public class BeforeClean extends BaseFlywayCallback {
    @Override
    public void beforeClean(Connection connection){
        try {
            H2DbUtils.backup("before_clean");
        }catch (Exception e){
            throw new RuntimeException("Exception in DB backup", e);
        }
    }
}
