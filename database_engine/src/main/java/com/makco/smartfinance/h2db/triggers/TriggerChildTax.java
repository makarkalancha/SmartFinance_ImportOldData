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
public class TriggerChildTax extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.CHILD_TAX.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.CHILD_TAX.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.CHILD_TAX.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.CHILD_TAX.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.CHILD_TAX.TAX_ID.toString(), (Long) oldRow[Table.CHILD_TAX.TAX_ID.getColumnIndex()]);
        rowJson.addProperty(Table.CHILD_TAX.CHILD_TAX_ID.toString(), (Long) oldRow[Table.CHILD_TAX.CHILD_TAX_ID.getColumnIndex()]);
        rowJson.addProperty(Table.CHILD_TAX.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.CHILD_TAX.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.CHILD_TAX.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.CHILD_TAX.T_UPDATEDON.getColumnIndex()]));
    }
}
