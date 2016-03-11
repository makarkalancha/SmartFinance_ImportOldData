package com.makco.smartfinance.h2db.functions;

import com.makco.smartfinance.h2db.H2DbTestSuite;
import com.makco.smartfinance.h2db.TestContext;
import com.makco.smartfinance.h2db.triggers.TriggerDateUnit;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.h2db.utils.ObjectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-02-26.
 */
public class DateUnitFunctionsTest {

    private Date dateToInsert;
    private long insertedDateId;

//    @ClassRule
//    public static H2DbTestSuite.dbConnectionResource H2DbTestSuite.dbConnectionResource = new H2DbTestSuite.dbConnectionResource();

    @Before
    public void setUp() throws Exception {
//        H2DbTestSuite.dbConnectionResource.getConnection() = DriverManager.getH2DbTestSuite.dbConnectionResource.getConnection()(DB_H2DbTestSuite.dbConnectionResource.getConnection()_IF_EXISTS, DB_USER, DB_PASSWORD);
//        if(!checkIfSchemaExists()){
//            createTestSchema();
//        }
//        setTestSchema();
        dateToInsert = new SimpleDateFormat("yyyy-MM-dd").parse("2016-02-12");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testContext() {
        String dbName = TestContext.INSTANCE.DB_NAME();
        System.out.println(dbName);
        assertEquals(dbName, "finance");
    }

    @Test
    public void testCreateDateUnitTable() throws Exception {
        if(H2DbUtils.checkIfObjectExists(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT(), ObjectType.TABLE)){
            H2DbUtils.setSchema(H2DbTestSuite.dbConnectionResource.getConnection(),TestContext.INSTANCE.DB_SCHEMA());
            H2DbUtils.dropTable(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.TABLE_DATEUNIT());
        }

        if(!H2DbUtils.checkIfObjectExists(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT(), ObjectType.TABLE)) {
            H2DbUtils.setSchema(H2DbTestSuite.dbConnectionResource.getConnection(),TestContext.INSTANCE.DB_SCHEMA());
            DateUnitFunctions.createDateUnitTable(H2DbTestSuite.dbConnectionResource.getConnection());
            assert (H2DbUtils.checkIfObjectExists(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT(), ObjectType.TABLE));



            testInsertSelectDateSinceEpochDate_insert();

            testInsertSelectDateSinceEpochDate_select();
        } else {
            assert (false);
        }
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testInsertSelectDateSinceEpochDate_insert() throws Exception {
        insertedDateId = DateUnitFunctions.insertSelectDate(H2DbTestSuite.dbConnectionResource.getConnection(), dateToInsert);
        assert (insertedDateId > 0);
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testInsertSelectDateSinceEpochDate_select() throws Exception {
        long selectedDateId = DateUnitFunctions.insertSelectDate(H2DbTestSuite.dbConnectionResource.getConnection(), dateToInsert);
        assertEquals(insertedDateId, selectedDateId);
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testCreateDateUnitTrigger() throws Exception {
        if(H2DbUtils.checkIfObjectExists(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT(), ObjectType.TRIGGER)){
            H2DbUtils.setSchema(H2DbTestSuite.dbConnectionResource.getConnection(),TestContext.INSTANCE.DB_SCHEMA());
            H2DbUtils.dropTable(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.TABLE_DATEUNIT());
        }

        if(!H2DbUtils.checkIfObjectExists(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT(), ObjectType.TRIGGER)) {
            H2DbUtils.setSchema(H2DbTestSuite.dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());

            TriggerDateUnit.createDateUnitTable(H2DbTestSuite.dbConnectionResource.getConnection());
            assertEquals(insertedDateId, selectedDateId);
        } else {
            assert(false);
        }
    }
}