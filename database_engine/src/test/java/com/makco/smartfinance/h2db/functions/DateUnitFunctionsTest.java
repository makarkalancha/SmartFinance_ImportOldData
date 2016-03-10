package com.makco.smartfinance.h2db.functions;

import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.TestContext;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-02-26.
 */
public class DateUnitFunctionsTest {

    private Date dateToInsert;
    private long insertedDateId;

    @ClassRule
    public DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @Before
    public void setUp() throws Exception {
//        dbConnectionResource.getConnection() = DriverManager.getdbConnectionResource.getConnection()(DB_dbConnectionResource.getConnection()_IF_EXISTS, DB_USER, DB_PASSWORD);
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
        if(H2DbUtils.checkIfTableExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT())){
            H2DbUtils.dropTable(dbConnectionResource.getConnection(), TestContext.INSTANCE.TABLE_DATEUNIT());
        }

        if(!H2DbUtils.checkIfTableExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT())) {
            DateUnitFunctions.createDateUnitTable(dbConnectionResource.getConnection());
            assert (H2DbUtils.checkIfTableExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(), TestContext.INSTANCE.TABLE_DATEUNIT()));

            testInsertSelectDateSinceEpochDate_insert();

            testInsertSelectDateSinceEpochDate_select();
            assert (true);
        } else {
            assert (false);
        }
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testInsertSelectDateSinceEpochDate_insert() throws Exception {
        insertedDateId = DateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(), dateToInsert);
        assert (insertedDateId > 0);
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testInsertSelectDateSinceEpochDate_select() throws Exception {
        long selectedDateId = DateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(), dateToInsert);
        assertEquals(insertedDateId, selectedDateId);
    }
}