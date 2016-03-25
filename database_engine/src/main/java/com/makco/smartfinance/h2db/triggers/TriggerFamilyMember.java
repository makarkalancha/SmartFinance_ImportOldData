package com.makco.smartfinance.h2db.triggers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.tables.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.StringBuilders;
import org.h2.api.Trigger;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public class TriggerFamilyMember implements Trigger {

    private final static Logger LOG = LogManager.getLogger(TriggerFamilyMember.class);
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
        LOG.debug("before oldRow:" + Arrays.toString(oldRow));
        LOG.debug("before newRow:" + Arrays.toString(newRow));
        java.util.Date now = new java.util.Date();

        if (Trigger.INSERT == type) {
            LOG.debug("operation type:" + Trigger.INSERT);
            newRow[Table.FAMILY_MEMBER.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
            newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        } else if (Trigger.UPDATE == type) {
            LOG.debug("operation type:" + Trigger.UPDATE);
            newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        } else if (Trigger.DELETE == type) {
            LOG.debug("operation type:" + Trigger.DELETE);
            JsonObject rowJson = new JsonObject();
            rowJson.addProperty(Table.FAMILY_MEMBER.ID.toString(), (Long) newRow[0]);
            rowJson.addProperty(Table.FAMILY_MEMBER.NAME.toString(), (String) newRow[1]);
            rowJson.addProperty(Table.FAMILY_MEMBER.DESCRIPTION.toString(), (String) newRow[2]);
            rowJson.addProperty(Table.FAMILY_MEMBER.T_CREATEDON.toString(), JsonUtils.getSimpleDateFormat()
                    .format((Date) newRow[3]));
            rowJson.addProperty(Table.FAMILY_MEMBER.T_UPDATEDON.toString(), JsonUtils.getSimpleDateFormat()
                    .format((Date) newRow[4]));

            JsonObject tableJson = new JsonObject();
            tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.FAMILY_MEMBER);
            tableJson.add(Table.Elements.row.toString(), rowJson);
//
            StringBuilder querySt = new StringBuilder();
            querySt.append("INSERT INTO ");
            querySt.append(schemaName);
            querySt.append(".");
            querySt.append(Table.Names._DELETED_ROWS);
            querySt.append(" (");
            querySt.append(Table._DELETED_ROWS.SCHEMA_NAME);
            querySt.append(", ");
            querySt.append(Table._DELETED_ROWS.TABLE_NAME);
            querySt.append(", ");
            querySt.append(Table._DELETED_ROWS.JSON_ROW);
            querySt.append(") VALUES(?,?,?)");
            PreparedStatement preparedStatement = connection.prepareStatement(querySt.toString());
            preparedStatement.setString(1,schemaName);
            preparedStatement.setString(2, Table.Names.FAMILY_MEMBER.toString());
            preparedStatement.setString(3, tableJson.toString());
            preparedStatement.executeUpdate();
        }
        LOG.debug("after oldRow:" + Arrays.toString(oldRow));
        LOG.debug("after newRow:" + Arrays.toString(newRow));
    }

    @Override
    public void remove() throws SQLException {

    }
}
