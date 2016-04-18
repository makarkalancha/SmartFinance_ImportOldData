package com.makco.smartfinance.h2db.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.h2.tools.Script;

/**
 * Created by mcalancea on 2016-03-08.
 */
public class H2DbUtils {
    private final static Logger LOG = LogManager.getLogger(H2DbUtils.class);

    public static void createSchema(Connection connection, String dbSchemaName) throws SQLException{
        try(Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS "+dbSchemaName);
        }catch (SQLException e){
            LOG.error(e, e);
            throw e;
        }
    }

    public static void setSchema(Connection connection, String dbSchemaName) throws SQLException{
        try(Statement statement = connection.createStatement()) {
            statement.execute("SET SCHEMA "+dbSchemaName);
        }catch (SQLException e){
            LOG.error(e, e);
            throw e;
        }
    }

    public static boolean checkIfSchemaExists(Connection connection, String dbSchemaName)throws SQLException {
        try{
            return checkIfDBObjectExists(connection, dbSchemaName, DBObjectType.SCHEMA, null);
        } catch (SQLException e){
            LOG.error(e, e);
            throw e;
        }
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
        } catch (SQLException e){
            LOG.error(e, e);
            throw e;
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
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP " + dbObjectType + " IF EXISTS " + dbObjectName);
        } catch (SQLException e) {
            LOG.error(e, e);
            throw e;
        }
    }

    public static InputStream getCreateDBScript() {
        return H2DbUtils.class.getClassLoader().getResourceAsStream(Context.INSTANCE.DB_SCRIPT_CREATE());
    }

    public static boolean checkIfSchemaExists(String dbSchemaName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:" + Context.INSTANCE.DB_DIR() + "/" + Context.INSTANCE.DB_NAME(),
                    Context.INSTANCE.DB_USER(), Context.INSTANCE.DB_PASSWORD());
            return checkIfSchemaExists(connection, "FINANCE");
        } catch (SQLException e) {
            LOG.error(e, e);
            return false;
        }
    }

    public static int migrate(String schemaName) {
        Flyway flyway = new Flyway();
//IFEXISTS=TRUE -> throws exception if DB doesn't exist
//        flyway.setDataSource("jdbc:h2:" + Context.INSTANCE.DB_DIR() + "/" + Context.INSTANCE.DB_NAME() + ";IFEXISTS=TRUE;",Context.INSTANCE.DB_USER(),Context.INSTANCE.DB_PASSWORD());
        flyway.setDataSource("jdbc:h2:" + Context.INSTANCE.DB_DIR() + "/" + Context.INSTANCE.DB_NAME(),Context.INSTANCE.DB_USER(),Context.INSTANCE.DB_PASSWORD());
        flyway.setTable("_SCHEMA_VERSION");
        flyway.setSchemas(schemaName);
        flyway.setBaselineOnMigrate(true);
        return flyway.migrate();
    }

    public static void backup(String prefix) throws SQLException{
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDateTimeStr = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));
        Script script = new Script();

        List<String> argsList = new ArrayList<>();
        //url
        argsList.add("-url");
        argsList.add("jdbc:h2:" + Context.INSTANCE.DB_DIR() + "/" + Context.INSTANCE.DB_NAME());
        //user
        argsList.add("-user");
        argsList.add(Context.INSTANCE.DB_USER());
        //password
        argsList.add("-password");
        argsList.add(Context.INSTANCE.DB_PASSWORD());
        //script 2016-04-17T22-23-01_start_backup.zip
        //prefix (start, end)
        argsList.add("-script");
        argsList.add("backups/" + currentDateTimeStr + "_" + prefix + "_backup.zip");
        //options
        argsList.add("-options");
        argsList.add("compression");
        argsList.add("zip");

//        "jdbc:h2:" + Context.INSTANCE.DB_DIR() + "/" + Context.INSTANCE.DB_NAME()
//
//        java org.h2.tools.Script -url jdbc:h2:~/test -user sa -script test.zip -options compression zip
//        DB_DIR = ~/smart_finance
//        DB_NAME = finance
//        DB_SCHEMA = FINANCE
//        DB_USER = root
//        DB_PASSWORD = root

        script.runTool(argsList.toArray(new String[argsList.size()]));

    }

    public static boolean isDateUnitTableEmpty(String schemaName) throws SQLException{
        boolean isEmpty = true;
        Connection connection = DriverManager.getConnection("jdbc:h2:" + Context.INSTANCE.DB_DIR() + "/" + Context.INSTANCE.DB_NAME(),
                Context.INSTANCE.DB_USER(), Context.INSTANCE.DB_PASSWORD());
        return isEmpty;
    }
}
