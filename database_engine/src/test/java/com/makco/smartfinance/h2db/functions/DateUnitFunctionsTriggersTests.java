package com.makco.smartfinance.h2db.functions;

import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.TestContext;
import com.makco.smartfinance.h2db.utils.DBObjectType;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import com.makco.smartfinance.h2db.utils.schema_constants.Trigger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-02-26.
 */
public class DateUnitFunctionsTriggersTests {
    private static final Logger LOG = LogManager.getLogger(DateUnitFunctionsTriggersTests.class);
    private Date dateToInsert;
    private long insertedDateId;

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "DateUnitFunctionsTriggersTests: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "DateUnitFunctionsTriggersTests: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        Random rand = new Random();
        String dateToParse = (rand.nextInt((2016 - 2000) + 1) + 2000)+
                "-"+
                (rand.nextInt((12 - 1) + 1) + 1)+
                "-"+
                (rand.nextInt((28 - 1) + 1) + 1);
        String mess1 = "random date (from 2000-01-01 to 2016-12-28): " + dateToParse;
        System.out.println(mess1);
        LOG.debug(mess1);
        dateToInsert = new SimpleDateFormat("yyyy-MM-dd").parse(dateToParse);
        mess1 = "DateUnitFunctionsTriggersTests: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "DateUnitFunctionsTriggersTests: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Test
    public void testContext() {
        String dbName = TestContext.INSTANCE.DB_NAME();
        System.out.println(dbName);
        assertEquals(dbName, "finance");
    }

    @Test
    public void testCreateDateUnitTable() throws Exception {
        if(H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                DBObjectType.TABLE, Table.Names.DATEUNIT.toString())){
            H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
            assert (H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                    DBObjectType.TABLE, Table.Names.DATEUNIT.toString()));

            assert(H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                        DBObjectType.TRIGGER, Trigger.DATEUNIT.T_DATEUNIT_INS.toString()));

            testInsertSelectDateSinceEpochDate_insert();
            testDateUnitTrigger_onInsert();
            testInsertSelectDateSinceEpochDate_select();
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


    public void testDateUnitTrigger_onInsert() throws Exception {
        String query = "SELECT " + Table.DATEUNIT.UNITDATE + " FROM " + Table.Names.DATEUNIT + " WHERE " + Table.DATEUNIT.UNITDATE + " = ?" +
                " AND " + Table.DATEUNIT.T_CREATEDON + " IS NOT NULL";
        LOG.debug(query);
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        boolean isPassed = false;
        try {
            preparedStatement = dbConnectionResource.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, insertedDateId);
            rs = preparedStatement.executeQuery();
            isPassed = rs.next();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        assert (isPassed);
    }
}