package com.makco.smartfinance.h2db.triggers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.makco.smartfinance.h2db.utils.tables.Table;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.api.Trigger;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public class TriggerFamilyMember implements Trigger {

    private final static Logger LOG = LogManager.getLogger(TriggerFamilyMember.class);
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
        LOG.debug("operation type:" + type);
        LOG.debug("before oldRow:" + Arrays.toString(oldRow));
        LOG.debug("before newRow:" + Arrays.toString(newRow));
        java.util.Date now = new java.util.Date();

        if (Trigger.INSERT == type) {
            newRow[Table.FAMILY_MEMBER.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
            newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        } else if (Trigger.UPDATE == type) {
            newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        } else if (Trigger.DELETE == type) {
            Gson gson = new Gson();
            JsonElement
            gson.toJson()
            newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        }
        LOG.debug("after oldRow:" + Arrays.toString(oldRow));
        LOG.debug("after newRow:" + Arrays.toString(newRow));
    }

    @Override
    public void remove() throws SQLException {

    }
}
