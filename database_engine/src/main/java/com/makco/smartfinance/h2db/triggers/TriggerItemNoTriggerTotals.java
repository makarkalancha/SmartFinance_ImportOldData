package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 06/06/2016
 * Time: 11:33
 */
public class TriggerItemNoTriggerTotals extends AbstractTrigger {

    @Override
    protected String logTriggerName() {
        return Table.Names.ITEM.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ITEM.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.ITEM.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ITEM.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.ITEM.ID.toString(), (Long) oldRow[Table.ITEM.ID.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.ORDER_NUMBER.toString(), (Integer) oldRow[Table.ITEM.ORDER_NUMBER.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.INVOICE_ID.toString(), (Long) oldRow[Table.ITEM.INVOICE_ID.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.CATEGORY_ID.toString(), (Long) oldRow[Table.ITEM.CATEGORY_ID.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.TAX_ID.toString(), (Long) oldRow[Table.ITEM.TAX_ID.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.FAMILY_MEMBER_ID.toString(),
                (Long) oldRow[Table.ITEM.FAMILY_MEMBER_ID.getColumnIndex()]);

        rowJson.addProperty(Table.ITEM.DESCRIPTION1.toString(),
                (String) oldRow[Table.ITEM.DESCRIPTION1.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.DESCRIPTION2.toString(),
                (String) oldRow[Table.ITEM.DESCRIPTION2.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.COMMENT.toString(), (String) oldRow[Table.ITEM.COMMENT.getColumnIndex()]);

        rowJson.addProperty(Table.ITEM.SUB_TOTAL.toString(),
                (BigDecimal) oldRow[Table.ITEM.SUB_TOTAL.getColumnIndex()]);
        rowJson.addProperty(Table.ITEM.TOTAL.toString(),
                (BigDecimal) oldRow[Table.ITEM.TOTAL.getColumnIndex()]);

        rowJson.addProperty(Table.ITEM.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.ITEM.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.ITEM.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.ITEM.T_UPDATEDON.getColumnIndex()]));
    }
}
