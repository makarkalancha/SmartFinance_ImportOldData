package com.makco.smartfinance.h2db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.rules.ExternalResource;

/**
 * Created by mcalancea on 2016-03-10.
 */
public class DBConnectionResource extends ExternalResource {
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
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}