package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        newRow[Table.CURRENCY.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.CURRENCY.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.CURRENCY.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.CURRENCY.ID.toString(), (Long) oldRow[Table.CURRENCY.ID.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.CODE.toString(), (String) oldRow[Table.CURRENCY.CODE.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.NAME.toString(), (String) oldRow[Table.CURRENCY.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.DESCRIPTION.toString(), (String) oldRow[Table.CURRENCY.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.CURRENCY.T_CREATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.CURRENCY.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.CURRENCY.T_UPDATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.CURRENCY.T_UPDATEDON.getColumnIndex()]));
    }
}
