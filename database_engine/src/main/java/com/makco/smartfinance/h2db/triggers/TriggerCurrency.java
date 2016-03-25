package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonObject;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.tables.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public class TriggerCurrency extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.CURRENCY.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.CURRENCY.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        newRow[Table.CURRENCY.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.CURRENCY.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
    }

    @Override
    protected void prepareJsonForDeletion(Object[] newRow) {
        rowJson.addProperty(Table.CURRENCY.ID.toString(), (Long) newRow[Table.CURRENCY.ID.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.CODE.toString(), (String) newRow[Table.CURRENCY.CODE.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.NAME.toString(), (String) newRow[Table.CURRENCY.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.DESCRIPTION.toString(), (String) newRow[Table.CURRENCY.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.T_CREATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) newRow[Table.CURRENCY.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.CURRENCY.T_UPDATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) newRow[Table.CURRENCY.T_UPDATEDON.getColumnIndex()]));
    }
}
