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
public class TriggerFamilyMember extends AbstractTrigger {

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.FAMILY_MEMBER.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.FAMILY_MEMBER.ID.toString(), (Long) oldRow[Table.FAMILY_MEMBER.ID.getColumnIndex()]);
        rowJson.addProperty(Table.FAMILY_MEMBER.NAME.toString(), (String) oldRow[Table.FAMILY_MEMBER.NAME.getColumnIndex()]);
        rowJson.addProperty(Table.FAMILY_MEMBER.DESCRIPTION.toString(), (String) oldRow[Table.FAMILY_MEMBER.DESCRIPTION.getColumnIndex()]);
        rowJson.addProperty(Table.FAMILY_MEMBER.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.FAMILY_MEMBER.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.FAMILY_MEMBER.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.FAMILY_MEMBER.T_UPDATEDON.getColumnIndex()]));
    }

    @Override
    protected String logTriggerName() {
        return Table.Names.FAMILY_MEMBER.toString();
    }
}
