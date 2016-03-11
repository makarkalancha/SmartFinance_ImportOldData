package com.makco.smartfinance.h2db.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mcalancea on 2016-03-08.
 */
public class H2DbUtils {

    public static void createSchema(Connection connection, String dbSchemaName) throws SQLException{
        try(Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS "+dbSchemaName);
        }
    }

    public static void setSchema(Connection connection, String dbSchemaName) throws SQLException{
        try(Statement statement = connection.createStatement()) {
            statement.execute("SET SCHEMA "+dbSchemaName);
        }
    }

    public static boolean checkIfSchemaExists(Connection connection, String dbSchemaName)throws SQLException {
        return checkIfDBObjectExists(connection, dbSchemaName, DBObjectType.SCHEMA, null);
    }

    public static boolean checkIfDBObjectExists(Connection connection, String dbSchemaName, DBObjectType dbObjectType, String dbObjectName)
            throws SQLException {

        boolean result = false;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {

            if(dbObjectType.equals(DBObjectType.TRIGGER)) {
                String query = "SELECT TRIGGER_NAME FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_SCHEMA = ? AND TRIGGER_NAME = ? ";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, dbSchemaName);
                preparedStatement.setString(2, dbObjectName);
                rs = preparedStatement.executeQuery();
            }else {
                DatabaseMetaData metaData = connection.getMetaData();
                if (dbObjectType.equals(DBObjectType.SCHEMA)) {
                    rs = metaData.getSchemas(null, dbSchemaName);
                } else {
                    rs = metaData.getTables(null, dbSchemaName, dbObjectName, new String[] {dbObjectType.toString()});
                }
            }
            result = rs.next();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    public static void dropDBObject(Connection connection, DBObjectType dbObjectType, String dbObjectName) throws Exception {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DROP " + dbObjectType + " IF EXISTS " + dbObjectName);
        }
    }

    public static void createDB(Connection connection, String schemaName) throws Exception{
//        //snippet reads the attributes from one file and creates a new file, assigning the attributes from the original file to the new file:
//        Path srcPath = Paths.get(Context.INSTANCE.DB_SCRIPT_CREATE());
//        PosixFileAttributes srcAttrs =
//                Files.readAttributes(srcPath, PosixFileAttributes.class);
//        FileAttribute<Set<PosixFilePermission>> dstAttr =
//                PosixFilePermissions.asFileAttribute(srcAttrs.permissions());
//        Path directory = Paths.get(Context.INSTANCE.DB_SCRIPT_CREATE()).getParent();
//        System.out.println("directory:" + directory);
//        Path dstPath = Files.createTempFile(directory, "prefix_", "_suffix", dstAttr);
//        System.out.println("srcPath:" + srcPath);
//        System.out.println("dstPath:" + dstPath);
//
//        Files.copy(srcPath, dstPath, StandardCopyOption.COPY_ATTRIBUTES);
//
//        RunScript.execute(connection,  new InputStreamReader(H2DbUtils.class.getClassLoader().getResourceAsStream(Context.INSTANCE.DB_SCRIPT_CREATE())));
    }

}
