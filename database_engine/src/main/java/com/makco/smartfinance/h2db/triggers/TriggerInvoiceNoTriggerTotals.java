package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 06/06/2016
 * Time: 11:33
 */
public class TriggerInvoiceNoTriggerTotals extends AbstractTrigger {

    @Override
    protected String logTriggerName() {
        return Table.Names.INVOICE.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.INVOICE.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.INVOICE.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.INVOICE.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.INVOICE.ID.toString(), (Long) oldRow[Table.INVOICE.ID.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE.INVOICE_NUMBER.toString(), (String) oldRow[Table.INVOICE.INVOICE_NUMBER.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE.ORGANIZATION_ID.toString(), (Long) oldRow[Table.INVOICE.ORGANIZATION_ID.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE.COMMENT.toString(),
                (String) oldRow[Table.INVOICE.COMMENT.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE.SUB_TOTAL.toString(),
                (BigDecimal) oldRow[Table.INVOICE.SUB_TOTAL.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE.TOTAL.toString(),
                (BigDecimal) oldRow[Table.INVOICE.TOTAL.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.INVOICE.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.INVOICE.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.INVOICE.T_UPDATEDON.getColumnIndex()]));
    }
}
