package com.makco.smartfinance.h2db;

import com.makco.smartfinance.h2db.functions.DateUnitFunctionsTest;
import com.makco.smartfinance.h2db.tables.DateUnitTest;
import com.makco.smartfinance.h2db.triggers.TriggerDateUnitTest;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by mcalancea on 2016-03-08.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DateUnitTest.class,
        DateUnitFunctionsTest.class,
        TriggerDateUnitTest.class
})
public class H2DbTestSuite {
    private static final String DB_DIR = "~/smart_finance";
    private static final String DB_NAME = "finance";
    private static final String DB_SCHEMA = "TEST";
    private static final String DB_SCHEMA1 = "FINANCE";
    private static final String DB_CONNECTION_IF_EXISTS = "jdbc:h2:"+DB_DIR+"/"+DB_NAME+";IFEXISTS=TRUE";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String TABLE_DATEUNIT = "DATEUNIT";

    private Connection connection;

    @BeforeClass
    public void setUp() throws Exception {
        connection = DriverManager.getConnection(DB_CONNECTION_IF_EXISTS, DB_USER, DB_PASSWORD);
        if(!H2DbUtils.checkIfSchemaExists(connection, DB_SCHEMA)){
            H2DbUtils.createTestSchema(connection, DB_SCHEMA);
        }
        H2DbUtils.setSchema(connection, DB_SCHEMA);
    }

    @AfterClass
    public void tearDown() throws Exception {
        H2DbUtils.dropTable(connection, TABLE_DATEUNIT);
        if (H2DbUtils.checkIfSchemaExists(connection, DB_SCHEMA)) {
            H2DbUtils.dropTestSchema(connection, DB_SCHEMA);
        }
        if (connection != null) {
            connection.close();
        }
    }
}
