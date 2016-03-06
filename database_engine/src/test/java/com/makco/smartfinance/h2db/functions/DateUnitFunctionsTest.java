package com.makco.smartfinance.h2db.functions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.makco.smartfinance.h2db.functions.DateUnitFunctions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-02-26.
 */
public class DateUnitFunctionsTest {
    private static final String DB_DIR = "~/smart_finance";
    private static final String DB_NAME = "finance";
    private static final String DB_SCHEMA = "TEST";
    private static final String DB_SCHEMA1 = "FINANCE";
    private static final String DB_CONNECTION_IF_EXISTS = "jdbc:h2:"+DB_DIR+"/"+DB_NAME+";IFEXISTS=TRUE";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String TABLE_DATEUNIT = "DATEUNIT";

    private Date dateToInsert;
    private Connection connection;
    private long insertedDateId;


    public boolean checkIfSchemaExists() throws Exception{
        boolean result = false;
        ResultSet rs = null;
        try{
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getSchemas(null, DB_SCHEMA);
            result = rs.next();
        }finally {
            if(rs != null) rs.close();
        }
        return result;
    }

    public void dropTestSchema() throws Exception {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DROP SCHEMA " + DB_SCHEMA);
        }
    }

    public void dropTable(String tableName) throws Exception {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS " + tableName);
        }
    }

    public boolean checkIfTableExists(String schemaName, String tableName) throws Exception{
        boolean result = false;
        ResultSet rs = null;
        try{
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, schemaName, tableName, new String[] {"TABLE"});
            result = rs.next();
        }finally {
            if(rs != null) rs.close();
        }
        return result;
    }

    public void createTestSchema() throws Exception{
        try(Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS "+DB_SCHEMA);
        }
    }

    public void setTestSchema() throws Exception{
        try(Statement statement = connection.createStatement()) {
            statement.execute("SET SCHEMA "+DB_SCHEMA);
        }
    }

    @Before
    public void setUp() throws Exception {
        connection = DriverManager.getConnection(DB_CONNECTION_IF_EXISTS, DB_USER, DB_PASSWORD);
        if(!checkIfSchemaExists()){
            createTestSchema();
        }
        setTestSchema();
        dateToInsert = new SimpleDateFormat("yyyy-MM-dd").parse("2016-02-12");
    }

    @After
    public void tearDown() throws Exception {
        dropTable(TABLE_DATEUNIT);
        if (checkIfSchemaExists()) {
            dropTestSchema();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testCreateDateUnitTable() throws Exception {
        if(checkIfTableExists(DB_SCHEMA, TABLE_DATEUNIT)){
            dropTable(TABLE_DATEUNIT);
        }

        if(!checkIfTableExists(DB_SCHEMA, TABLE_DATEUNIT)) {
            DateUnitFunctions.createDateUnitTable(connection);
            assert (checkIfTableExists(DB_SCHEMA, TABLE_DATEUNIT));

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