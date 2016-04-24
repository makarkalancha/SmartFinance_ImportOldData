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
public class TriggerCategoryGroup extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.CATEGORY_GROUP.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
//        newRow[Table.CATEGORY_GROUP.T_CREATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
//        newRow[Table.CATEGORY_GROUP.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        newRow[Table.CATEGORY_GROUP.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.CATEGORY_GROUP.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
//        newRow[Table.CATEGORY_GROUP.T_UPDATEDON.getColumnIndex()] = new java.sql.Timestamp(now.getTime());
        newRow[Table.CATEGORY_GROUP.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.CATEGORY_GROUP.ID.toString(), (Long) oldRow[Table.CATEGORY_GROUP.ID.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY_GROUP.TYPE.toString(),
                (String) oldRow[Table.CATEGORY_GROUP.TYPE.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY_GROUP.NAME.toString(),
                (String) oldRow[Table.CATEGORY_GROUP.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY_GROUP.DESCRIPTION.toString(),
                (String) oldRow[Table.CATEGORY_GROUP.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.CATEGORY_GROUP.T_CREATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.CATEGORY_GROUP.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.CATEGORY_GROUP.T_UPDATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.CATEGORY_GROUP.T_UPDATEDON.getColumnIndex()]));
    }
}
