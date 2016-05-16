package com.makco.smartfinance.h2db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by mcalancea on 2016-03-10.
 */
public class DBConnectionResource extends ExternalResource {
    private final static Logger LOG = LogManager.getLogger(DBConnectionResource.class);
    private static Connection connection;
    //http://www.h2database.com/html/grammar.html#set_schema
    private static final String DB_CONNECTION_IF_EXISTS = "jdbc:h2:" + TestContext.INSTANCE.DB_DIR() + "/" + TestContext.INSTANCE.DB_NAME() + ";IFEXISTS=TRUE;";

    @Override
    protected void before() throws SQLException {
        System.out.println("ExternalResource->before");
        connection = DriverManager.getConnection(DB_CONNECTION_IF_EXISTS, TestContext.INSTANCE.DB_USER(), TestContext.INSTANCE.DB_PASSWORD());
    }

    @Override
    protected void after() {
        System.out.println("ExternalResource->after");
        try{
            System.out.println("ExternalResource->after: isClosed=" + connection.isClosed());
            if (connection != null) {
                connection.close();
            }
        }catch (SQLException e) {
            LOG.error(e, e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String getDbConnectionUrl() {
        return DB_CONNECTION_IF_EXISTS;
    }
}
