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
public class TriggerFamilyMember extends AbstractTrigger {
    private final static Logger LOG = LogManager.getLogger(TriggerFamilyMember.class);

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.FAMILY_MEMBER.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
    }

    @Override
    protected void prepareJsonForDeletion(Object[] newRow) {
        rowJson.addProperty(Table.FAMILY_MEMBER.ID.toString(), (Long) newRow[Table.FAMILY_MEMBER.ID.getColumnIndex()]);
        rowJson.addProperty(Table.FAMILY_MEMBER.NAME.toString(), (String) newRow[Table.FAMILY_MEMBER.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.FAMILY_MEMBER.DESCRIPTION.toString(), (String) newRow[Table.FAMILY_MEMBER.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.FAMILY_MEMBER.T_CREATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) newRow[Table.FAMILY_MEMBER.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.FAMILY_MEMBER.T_UPDATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()]));
    }

    @Override
    protected String logTriggerName() {
        return Table.Names.FAMILY_MEMBER.toString();
    }
}
