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
 * Time: 11:34
 */
public class TriggerAccountGroup extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.ACCOUNT_GROUP.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ACCOUNT_GROUP.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.ACCOUNT_GROUP.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ACCOUNT_GROUP.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.ACCOUNT_GROUP.ID.toString(), (Long) oldRow[Table.ACCOUNT_GROUP.ID.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT_GROUP.TYPE.toString(),
                (String) oldRow[Table.ACCOUNT_GROUP.TYPE.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT_GROUP.NAME.toString(),
                (String) oldRow[Table.ACCOUNT_GROUP.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT_GROUP.DESCRIPTION.toString(),
                (String) oldRow[Table.ACCOUNT_GROUP.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.ACCOUNT_GROUP.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.ACCOUNT_GROUP.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.ACCOUNT_GROUP.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.ACCOUNT_GROUP.T_UPDATEDON.getColumnIndex()]));
    }
}
