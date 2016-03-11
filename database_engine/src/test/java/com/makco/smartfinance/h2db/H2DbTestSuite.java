package com.makco.smartfinance.h2db;

import com.makco.smartfinance.h2db.functions.DateUnitFunctionsTest;
import com.makco.smartfinance.h2db.tables.DateUnitTest;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by mcalancea on 2016-03-08.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DateUnitTest.class,
        DateUnitFunctionsTest.class
//        ,
//        TriggerDateUnitTest.class
})
public class H2DbTestSuite {


//    public static Connection connection;

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("Suite->BeforeClass");
        if(!H2DbUtils.checkIfSchemaExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA())){
            H2DbUtils.createTestSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
        }
        H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        System.out.println("Suite->AfterClass");
        H2DbUtils.setSchema(dbConnectionResource.getConnection(),TestContext.INSTANCE.DB_SCHEMA());
        H2DbUtils.dropTable(dbConnectionResource.getConnection(), TestContext.INSTANCE.TABLE_DATEUNIT());
        if (H2DbUtils.checkIfSchemaExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA())) {
            H2DbUtils.dropTestSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
        }

    }


}
