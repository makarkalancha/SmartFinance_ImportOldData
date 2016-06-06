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
public class TriggerAccount extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.ACCOUNT.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ACCOUNT.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.ACCOUNT.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ACCOUNT.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.ACCOUNT.ID.toString(), (Long) oldRow[Table.ACCOUNT.ID.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT.ACCOUNT_GROUP_ID.toString(),
                (Long) oldRow[Table.ACCOUNT.ACCOUNT_GROUP_ID.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT.ACCOUNT_GROUP_TYPE.toString(),
                (String) oldRow[Table.ACCOUNT.ACCOUNT_GROUP_TYPE.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT.NAME.toString(), (String) oldRow[Table.ACCOUNT.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT.DESCRIPTION.toString(),
                (String) oldRow[Table.ACCOUNT.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT.T_CREATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.ACCOUNT.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.ACCOUNT.T_UPDATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.ACCOUNT.T_UPDATEDON.getColumnIndex()]));
    }
}
