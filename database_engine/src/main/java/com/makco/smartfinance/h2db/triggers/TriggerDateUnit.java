package com.makco.smartfinance.h2db.triggers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public class TriggerDateUnit implements Trigger {
    private int type;

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        this.type = i;
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        java.util.Date now = new java.util.Date();
        Long dateUnitId = (Long) newRow[1];

        PreparedStatement preparedStatement = null;
        if(Trigger.INSERT == type) {
            preparedStatement = connection.prepareStatement(
                    "UPDATE DATEUNIT SET T_CREATEDON = ?, T_UPDATEDON = ? WHERE ID = ?"
            );
            preparedStatement.setDate(1,new java.sql.Date(now.getTime()));
            preparedStatement.setDate(2,new java.sql.Date(now.getTime()));
            preparedStatement.setLong(3, dateUnitId);
            preparedStatement.executeUpdate();
        } else if(Trigger.UPDATE == type) {
            preparedStatement = connection.prepareStatement(
                    "UPDATE DATEUNIT SET T_UPDATEDON = ? WHERE ID = ?"
            );
            preparedStatement.setDate(1,new java.sql.Date(now.getTime()));
            preparedStatement.setLong(2, dateUnitId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void remove() throws SQLException {

    }

    public static void createDateUnitTable(Connection connection) throws SQLException{
        final StringBuilder createDateUnitTrigger = new StringBuilder();
        createDateUnitTrigger.append("CREATE TRIGGER T_DATEUNIT_INS ");
        createDateUnitTrigger.append("BEFORE INSERT ");
        createDateUnitTrigger.append("ON DATEUNIT ");
        createDateUnitTrigger.append("FOR EACH ROW ");
        createDateUnitTrigger.append("CALL \"com.makco.smartfinance.h2db.triggers.TriggerDateUnit\"; ");

        createDateUnitTrigger.append("CREATE TRIGGER T_DATEUNIT_UPD ");
        createDateUnitTrigger.append("BEFORE UPDATE ");
        createDateUnitTrigger.append("ON DATEUNIT ");
        createDateUnitTrigger.append("FOR EACH ROW ");
        createDateUnitTrigger.append("CALL \"com.makco.smartfinance.h2db.triggers.TriggerDateUnit\";");
        try (Statement statement = connection.createStatement()){
            statement.execute(createDateUnitTrigger.toString());
        }
    }
}
