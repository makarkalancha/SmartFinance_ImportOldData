package com.makco.smartfinance.h2db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mcalancea on 2016-03-10.
 */
public enum TestContext {

    INSTANCE;

    private String DB_NAME;
    private String DB_DIR;
    private String DB_SCHEMA;
    private String DB_USER;
    private String DB_PASSWORD;
    private String TABLE_DATEUNIT;
    private String TRIGGER_INS;
    private String TRIGGER_UPD;

    private boolean isFirstRun = true;

    public String DB_DIR() {
        return DB_DIR;
    }

    public String DB_NAME() {
        return DB_NAME;
    }

    public String DB_PASSWORD() {
        return DB_PASSWORD;
    }

    public String DB_SCHEMA() {
        return DB_SCHEMA;
    }

    public String DB_USER() {
        return DB_USER;
    }

    public String TABLE_DATEUNIT() {
        return TABLE_DATEUNIT;
    }

    public String TRIGGER_INS() {
        return TRIGGER_INS;
    }

    public String TRIGGER_UPD() {
        return TRIGGER_UPD;
    }

    private TestContext() {
        if (isFirstRun) {

            Properties prop = new Properties();
            InputStream input = null;
            String propFileName = "database.properties";

            try {
//                input = new FileInputStream(propFileName);
                input = getClass().getClassLoader().getResourceAsStream(propFileName);
                // load a properties file
                prop.load(input);

                // get the property value and print it out
                DB_DIR = prop.getProperty("DB_DIR");
                DB_NAME = prop.getProperty("DB_NAME");
                DB_SCHEMA = prop.getProperty("DB_SCHEMA");
                DB_USER = prop.getProperty("DB_USER");
                DB_PASSWORD = prop.getProperty("DB_PASSWORD");
                TABLE_DATEUNIT = prop.getProperty("TABLE_DATEUNIT");
                TRIGGER_INS = prop.getProperty("TRIGGER_INS");
                TRIGGER_UPD = prop.getProperty("TRIGGER_UPD");

                isFirstRun = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
