package com.makco.smartfinance.h2db.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mcalancea on 2016-03-08.
 */
public class H2DbUtils {

    public static boolean checkIfSchemaExists(Connection connection, String dbSchemaName) throws SQLException{
        boolean result = false;
        ResultSet rs = null;
        try{
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getSchemas(null, dbSchemaName);
            result = rs.next();
        }finally {
            if(rs != null) rs.close();
        }
        return result;
    }

    public static void createTestSchema(Connection connection, String dbSchemaName) throws SQLException{
        try(Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS "+dbSchemaName);
        }
    }

    public static void setSchema(Connection connection, String dbSchemaName) throws SQLException{
        try(Statement statement = connection.createStatement()) {
            statement.execute("SET SCHEMA "+dbSchemaName);
        }
    }

    public static boolean checkIfObjectExists(Connection connection, String dbSchemaName, String tableName, ObjectType objectType) throws SQLException{
        boolean result = false;
        ResultSet rs = null;
        try{
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, dbSchemaName, tableName, new String[] {objectType.toString()});
            result = rs.next();
        }finally {
            if(rs != null) rs.close();
        }
        return result;
    }


    public static void dropTestSchema(Connection connection, String dbSchemaName) throws Exception {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DROP SCHEMA " + dbSchemaName);
        }
    }

    public static void dropTable(Connection connection, String tableName) throws Exception {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS " + tableName);
        }
    }

}
