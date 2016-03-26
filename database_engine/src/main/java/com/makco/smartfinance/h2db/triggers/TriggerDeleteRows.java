package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonObject;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.tables.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public class TriggerDeleteRows extends AbstractTrigger {
    private final static Logger LOG = LogManager.getLogger(TriggerDeleteRows.class);

    @Override
    protected String logTriggerName() {
        return Table.Names._DELETED_ROWS.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table._DELETED_ROWS.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
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
}
