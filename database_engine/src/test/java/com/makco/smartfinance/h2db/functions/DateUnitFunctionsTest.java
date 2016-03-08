package com.makco.smartfinance.h2db.functions;

import com.makco.smartfinance.h2db.utils.H2DbUtils;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-02-26.
 */
public class DateUnitFunctionsTest {

    private Date dateToInsert;
    private Connection connection;
    private long insertedDateId;

    @Before
    public void setUp() throws Exception {
//        connection = DriverManager.getConnection(DB_CONNECTION_IF_EXISTS, DB_USER, DB_PASSWORD);
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
    public void testCreateDateUnitTable() throws Exception {
        if(H2DbUtils.checkIfTableExists(connection, DB_SCHEMA, TABLE_DATEUNIT)){
            H2DbUtils.dropTable(connection, TABLE_DATEUNIT);
        }

        if(!H2DbUtils.checkIfTableExists(connection, DB_SCHEMA, TABLE_DATEUNIT)) {
            DateUnitFunctions.createDateUnitTable(connection);
            assert (H2DbUtils.checkIfTableExists(connection, DB_SCHEMA, TABLE_DATEUNIT));

            testInsertSelectDateSinceEpochDate_insert();

            testInsertSelectDateSinceEpochDate_select();
        } else {
            assert (false);
        }
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testInsertSelectDateSinceEpochDate_insert() throws Exception {
        insertedDateId = DateUnitFunctions.insertSelectDate(connection, dateToInsert);
        assert (insertedDateId > 0);
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testInsertSelectDateSinceEpochDate_select() throws Exception {
        long selectedDateId = DateUnitFunctions.insertSelectDate(connection, dateToInsert);
        assertEquals(insertedDateId, selectedDateId);
    }
}