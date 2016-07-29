package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 06/06/2016
 * Time: 11:33
 */
public class TriggerTaxChild extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.TAX_CHILD.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.TAX_CHILD.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {

    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.TAX_CHILD.TAX_ID.toString(), (Long) oldRow[Table.TAX_CHILD.TAX_ID.getColumnIndex()]);
        rowJson.addProperty(Table.TAX_CHILD.CHILD_TAX_ID.toString(), (Long) oldRow[Table.TAX_CHILD.CHILD_TAX_ID.getColumnIndex()]);
        rowJson.addProperty(Table.TAX_CHILD.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.TAX_CHILD.T_CREATEDON.getColumnIndex()]));
    }
}
