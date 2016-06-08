package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 06/06/2016
 * Time: 11:33
 */
public class TriggerTax extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.TAX.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.TAX.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.TAX.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.TAX.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.TAX.ID.toString(), (Long) oldRow[Table.TAX.ID.getColumnIndex()]);
        rowJson.addProperty(Table.TAX.NAME.toString(), (String) oldRow[Table.TAX.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.TAX.DESCRIPTION.toString(),
                (String) oldRow[Table.TAX.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.TAX.RATE.toString(),
                (BigDecimal) oldRow[Table.TAX.RATE.getColumnIndex()]);
        rowJson.addProperty(Table.TAX.FORMULA.toString(),
                (String) oldRow[Table.TAX.FORMULA.getColumnIndex()]);
        rowJson.addProperty(Table.TAX.STARTDATE.toString(),
                JsonUtils.getStringOfNullableDate((Date) oldRow[Table.TAX.STARTDATE.getColumnIndex()]));
        rowJson.addProperty(Table.TAX.ENDDATE.toString(),
                JsonUtils.getStringOfNullableDate((Date) oldRow[Table.TAX.ENDDATE.getColumnIndex()]));
        rowJson.addProperty(Table.TAX.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.TAX.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.TAX.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.TAX.T_UPDATEDON.getColumnIndex()]));
    }
}
