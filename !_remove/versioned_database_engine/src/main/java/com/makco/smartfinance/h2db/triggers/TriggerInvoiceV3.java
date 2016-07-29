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
 * \SmartFinance\database_engine\src\main\java\com\makco\smartfinance\h2db\triggers\
 * v3
 */
public class TriggerInvoiceV3 extends AbstractTrigger {
    private static final String UPDATE_ITEM_DATEUNIT = new StringBuilder()
            .append("UPDATE ")
            .append(SCHEMA_NAME_PLACEHOLDER)
            .append(".")
            .append(Table.Names.ITEM_V3)
            .append(" SET ")
            .append(Table.ITEM.DATEUNIT_UNITDAY)
            .append(" = ? ")
            .append(" WHERE ")
            .append(Table.ITEM.INVOICE_ID)
            .append(" = ? ")
            .toString();

    @Override
    protected String logTriggerName() {
        return Table.Names.INVOICE_V3.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.INVOICE_V3.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.INVOICE_V3.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.INVOICE.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);

        if (!((Long) oldRow[Table.INVOICE.DATEUNIT_UNITDAY.getColumnIndex()]).equals(
                (Long) newRow[Table.INVOICE.DATEUNIT_UNITDAY.getColumnIndex()])) {
            String query = UPDATE_ITEM_DATEUNIT.replace(SCHEMA_NAME_PLACEHOLDER, schemaName);
            try (
                    PreparedStatement updateItemDate = connection.prepareStatement(query);
            ) {

                updateItemDate.setLong(1, (Long) newRow[Table.INVOICE.DATEUNIT_UNITDAY.getColumnIndex()]);
                updateItemDate.setLong(2, (Long) newRow[Table.INVOICE.ID.getColumnIndex()]);
                updateItemDate.execute();
            }
        }
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.INVOICE_V3.ID.toString(), (Long) oldRow[Table.INVOICE_V3.ID.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE_V3.INVOICE_NUMBER.toString(), (String) oldRow[Table.INVOICE_V3.INVOICE_NUMBER.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE_V3.ORGANIZATION_ID.toString(), (Long) oldRow[Table.INVOICE_V3.ORGANIZATION_ID.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE_V3.COMMENT.toString(),
                (String) oldRow[Table.INVOICE_V3.COMMENT.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE_V3.DEBIT_TOTAL.toString(),
                (BigDecimal) oldRow[Table.INVOICE_V3.DEBIT_TOTAL.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE_V3.CREDIT_TOTAL.toString(),
                (BigDecimal) oldRow[Table.INVOICE_V3.CREDIT_TOTAL.getColumnIndex()]);
        rowJson.addProperty(Table.INVOICE_V3.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.INVOICE_V3.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.INVOICE_V3.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.INVOICE_V3.T_UPDATEDON.getColumnIndex()]));
    }
}