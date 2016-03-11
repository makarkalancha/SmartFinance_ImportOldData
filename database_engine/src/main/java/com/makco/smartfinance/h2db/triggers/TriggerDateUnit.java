package com.makco.smartfinance.h2db.triggers;

import java.sql.Connection;
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

        if(Trigger.INSERT == type) {
            newRow[8] = new java.sql.Timestamp(now.getTime());
            newRow[9] = new java.sql.Timestamp(now.getTime());
        } else if(Trigger.UPDATE == type) {
            newRow[9] = new java.sql.Timestamp(now.getTime());
        }
    }

    @Override
    public void remove() throws SQLException {

    }

    public static void createDateUnitInsertUpdateTriggers(Connection connection) throws SQLException{
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
