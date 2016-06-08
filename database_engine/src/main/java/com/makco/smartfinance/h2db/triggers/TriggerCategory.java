package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 24/04/2016
 * Time: 00:36
 */
public class TriggerCategory extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.CATEGORY.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.CATEGORY.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.CATEGORY.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.CATEGORY.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.CATEGORY.ID.toString(), (Long) oldRow[Table.CATEGORY.ID.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY.CATEGORY_GROUP_ID.toString(),
                (Long) oldRow[Table.CATEGORY.CATEGORY_GROUP_ID.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY.CATEGORY_GROUP_TYPE.toString(),
                (String) oldRow[Table.CATEGORY.CATEGORY_GROUP_TYPE.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY.NAME.toString(), (String) oldRow[Table.CATEGORY.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY.DESCRIPTION.toString(),
                (String) oldRow[Table.CATEGORY.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.CATEGORY.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.CATEGORY.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.CATEGORY.T_UPDATEDON.getColumnIndex()]));
    }
}
