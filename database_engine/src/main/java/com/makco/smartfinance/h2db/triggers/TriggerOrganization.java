package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 22/04/2016
 * Time: 18:01
 */
public class TriggerOrganization extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.ORGANIZATION.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ORGANIZATION.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.ORGANIZATION.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ORGANIZATION.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.ORGANIZATION.ID.toString(), (Long) oldRow[Table.ORGANIZATION.ID.getColumnIndex()]);
        rowJson.addProperty(Table.ORGANIZATION.NAME.toString(), (String) oldRow[Table.ORGANIZATION.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.ORGANIZATION.DESCRIPTION.toString(), (String) oldRow[Table.ORGANIZATION.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.ORGANIZATION.T_CREATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.ORGANIZATION.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.ORGANIZATION.T_UPDATEDON.toString(), JsonUtils.getSimpleDateFormat()
                .format((Date) oldRow[Table.ORGANIZATION.T_UPDATEDON.getColumnIndex()]));
    }
}
