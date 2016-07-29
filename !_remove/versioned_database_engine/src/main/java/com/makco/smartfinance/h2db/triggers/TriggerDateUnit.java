package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public class TriggerDateUnit extends AbstractTrigger {

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.DATEUNIT.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        //no update
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
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

//    public static void createDateUnitInsertUpdateTriggers(Connection connection) throws SQLException{
//        final StringBuilder createDateUnitTrigger = new StringBuilder();
//        createDateUnitTrigger.append("CREATE TRIGGER T_DATEUNIT_INS ");
//        createDateUnitTrigger.append("BEFORE INSERT ");
//        createDateUnitTrigger.append("ON DATEUNIT ");
//        createDateUnitTrigger.append("FOR EACH ROW ");
//        createDateUnitTrigger.append("CALL \""+TriggerDateUnit.class.getName()+"\";");
//
//        createDateUnitTrigger.append("CREATE TRIGGER T_DATEUNIT_UPD ");
//        createDateUnitTrigger.append("BEFORE UPDATE ");
//        createDateUnitTrigger.append("ON DATEUNIT ");
//        createDateUnitTrigger.append("FOR EACH ROW ");
//        createDateUnitTrigger.append("CALL \""+TriggerDateUnit.class.getName()+"\";");
//
//        LOG.debug(createDateUnitTrigger.toString());
//        try (Statement statement = connection.createStatement()){
//            statement.execute(createDateUnitTrigger.toString());
//        }
//    }
}
