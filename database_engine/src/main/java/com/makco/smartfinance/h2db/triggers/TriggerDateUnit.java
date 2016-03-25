package com.makco.smartfinance.h2db.triggers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.makco.smartfinance.h2db.utils.tables.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.api.Trigger;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public class TriggerDateUnit extends AbstractTrigger {
    private final static Logger LOG = LogManager.getLogger(TriggerDateUnit.class);

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.DATEUNIT.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        newRow[Table.DATEUNIT.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.DATEUNIT.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
    }

    @Override
    protected void prepareJsonForDeletion(Object[] newRow) {
        //no delete
    }

    @Override
    protected void delete(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        //no delete
    }

    @Override
    protected String logTriggerName() {
        return Table.Names.DATEUNIT.toString();
    }

    public static void createDateUnitInsertUpdateTriggers(Connection connection) throws SQLException{
        final StringBuilder createDateUnitTrigger = new StringBuilder();
        createDateUnitTrigger.append("CREATE TRIGGER T_DATEUNIT_INS ");
        createDateUnitTrigger.append("BEFORE INSERT ");
        createDateUnitTrigger.append("ON DATEUNIT ");
        createDateUnitTrigger.append("FOR EACH ROW ");
        createDateUnitTrigger.append("CALL \""+TriggerDateUnit.class.getName()+"\";");

        createDateUnitTrigger.append("CREATE TRIGGER T_DATEUNIT_UPD ");
        createDateUnitTrigger.append("BEFORE UPDATE ");
        createDateUnitTrigger.append("ON DATEUNIT ");
        createDateUnitTrigger.append("FOR EACH ROW ");
        createDateUnitTrigger.append("CALL \""+TriggerDateUnit.class.getName()+"\";");

        LOG.debug(createDateUnitTrigger.toString());
        try (Statement statement = connection.createStatement()){
            statement.execute(createDateUnitTrigger.toString());
        }
    }
}
