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
public class TriggerDeleteRows implements Trigger {

    private final static Logger LOG = LogManager.getLogger(TriggerDeleteRows.class);
    private int type;
    private String schemaName;

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        this.type = i;
        this.schemaName = s;
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        LOG.debug("operation type:" + type);
        LOG.debug("before oldRow:" + Arrays.toString(oldRow));
        LOG.debug("before newRow:" + Arrays.toString(newRow));
        Date now = new Date();

        if (Trigger.INSERT == type) {
            newRow[Table._DELETED_ROWS.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        }
        LOG.debug("after oldRow:" + Arrays.toString(oldRow));
        LOG.debug("after newRow:" + Arrays.toString(newRow));
    }

    @Override
    public void remove() throws SQLException {

    }
}
