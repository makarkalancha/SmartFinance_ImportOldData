package com.makco.smartfinance.h2db.functions;

import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.TestContext;
import com.makco.smartfinance.h2db.triggers.TriggerDateUnit;
import com.makco.smartfinance.h2db.utils.DBObjectType;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private Date dateToInsert;
    private long insertedDateId;

//    @ClassRule
//    public static dbConnectionResource dbConnectionResource = new dbConnectionResource();
    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Test->BeforeClass");
        if(!H2DbUtils.checkIfSchemaExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA())){
            H2DbUtils.createSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
        }
        H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
//        System.out.println("Test->AfterClass");
//        H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
//        H2DbUtils.dropDBObject(dbConnectionResource.getConnection(), DBObjectType.TABLE, TestContext.INSTANCE.TABLE_DATEUNIT());
//        if (H2DbUtils.checkIfSchemaExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA())) {
//            H2DbUtils.dropDBObject(dbConnectionResource.getConnection(), DBObjectType.SCHEMA, TestContext.INSTANCE.DB_SCHEMA());
//        }

    }

    @Before
    public void setUp() throws Exception {
//        dbConnectionResource.getConnection() = DriverManager.getdbConnectionResource.getConnection()(DB_dbConnectionResource.getConnection()_IF_EXISTS, DB_USER, DB_PASSWORD);
//        if(!checkIfSchemaExists()){
//            createSchema();
//        }
//        setTestSchema();
        dateToInsert = new SimpleDateFormat("yyyy-MM-dd").parse("2016-02-12");
        System.out.println("Test->Before");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Test->After");
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
                DBObjectType.TABLE, TestContext.INSTANCE.TABLE_DATEUNIT())){
            H2DbUtils.setSchema(dbConnectionResource.getConnection(),TestContext.INSTANCE.DB_SCHEMA());
            H2DbUtils.dropDBObject(dbConnectionResource.getConnection(), DBObjectType.TABLE, TestContext.INSTANCE.TABLE_DATEUNIT());
        }

        if(!H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                DBObjectType.TABLE, TestContext.INSTANCE.TABLE_DATEUNIT())) {
            H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
            DateUnitFunctions.createDateUnitTable(dbConnectionResource.getConnection());
            assert (H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                    DBObjectType.TABLE, TestContext.INSTANCE.TABLE_DATEUNIT()));

            testCreateDateUnitTrigger();

            testInsertSelectDateSinceEpochDate_insert();
            testDateUnitTrigger_onInsert();

            testDateUnitTable_fakeUpdate();
            testDateUnitTrigger_onUpdate();
                    

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

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testCreateDateUnitTrigger() throws Exception {
        if (H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_INS())) {

            H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
            H2DbUtils.dropDBObject(dbConnectionResource.getConnection(), DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_INS());
        }

        if (H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_UPD())) {

            H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
            H2DbUtils.dropDBObject(dbConnectionResource.getConnection(), DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_UPD());
        }

        if (!H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_INS()) &&
            !H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                    DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_UPD())
            ) {

            H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());

            TriggerDateUnit.createDateUnitInsertUpdateTriggers(dbConnectionResource.getConnection());
            boolean insTrExists = H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                    DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_INS());
            boolean updTrExists = H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                            DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_UPD());
            System.out.println("insTrExists:"+insTrExists);
            System.out.println("updTrExists:"+updTrExists);
            assert(H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                        DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_INS()) &&
                    H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                        DBObjectType.TRIGGER, TestContext.INSTANCE.TRIGGER_UPD())
            );
        } else {
            assert (false);
        }
    }

    public void testDateUnitTrigger_onInsert() throws Exception {
        String query = "SELECT ID FROM " + TestContext.INSTANCE.TABLE_DATEUNIT() + " WHERE ID = ?" +
                " AND T_CREATEDON IS NOT NULL AND T_UPDATEDON IS NOT NULL AND "+
                " T_CREATEDON = T_UPDATEDON";
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

    //fake, because table DateUnit should be updated normally, only if epoch date is changed
    public void testDateUnitTable_fakeUpdate() throws Exception {
        String query = "UPDATE " + TestContext.INSTANCE.TABLE_DATEUNIT() + " SET UNITYEAR = UNITYEAR WHERE ID = ?";
        PreparedStatement preparedStatement = null;
        boolean isPassed = false;
        try {
            preparedStatement = dbConnectionResource.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, insertedDateId);
            isPassed = (preparedStatement.executeUpdate() > 0);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    
    public void testDateUnitTrigger_onUpdate() throws Exception {
        String query = "SELECT ID FROM " + TestContext.INSTANCE.TABLE_DATEUNIT() + " WHERE ID = ?" +
                " AND T_CREATEDON != T_UPDATEDON";
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