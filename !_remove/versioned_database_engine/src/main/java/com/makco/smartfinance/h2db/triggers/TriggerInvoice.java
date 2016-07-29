package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
 * \SmartFinance\database_engine\src\main\java\com\makco\smartfinance\h2db\triggers\
 * v1
 */
public class TriggerInvoice extends AbstractTrigger {
    private static final Logger LOG = LogManager.getLogger(TriggerInvoice.class);
    private static final String UPDATE_ITEM_DATEUNIT = new StringBuilder()
            .append("UPDATE ")
            .append(SCHEMA_NAME_PLACEHOLDER)
            .append(".")
            .append(Table.Names.ITEM)
            .append(" SET ")
            .append(Table.ITEM.DATEUNIT_UNITDAY)
            .append(" = ? ")
            .append(" WHERE ")
            .append(Table.ITEM.INVOICE_ID)
            .append(" = ? ")
            .toString();

    @Override
    protected String logTriggerName() {
        return Table.Names.INVOICE.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.INVOICE.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.INVOICE.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);

//        LOG.debug(String.format(">>>>TriggerInvoice->insert: dateunit=%s, subtotal=%s, total=%s",
//                newRow[Table.INVOICE.DATEUNIT_UNITDAY.getColumnIndex()],
//                newRow[Table.INVOICE.SUB_TOTAL.getColumnIndex()],
//                newRow[Table.INVOICE.TOTAL.getColumnIndex()]));
//        LOG.debug(String.format(">>>>TriggerInvoice->update: create=%s, update=%s",
//                newRow[Table.INVOICE.T_CREATEDON.getColumnIndex()],
//                newRow[Table.INVOICE.T_UPDATEDON.getColumnIndex()]));
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
//        LOG.debug(String.format(">>>>TriggerInvoice->update: dateunit=%s, subtotal=%s, total=%s",
//                newRow[Table.INVOICE.DATEUNIT_UNITDAY.getColumnIndex()],
//                newRow[Table.INVOICE.SUB_TOTAL.getColumnIndex()],
//                newRow[Table.INVOICE.TOTAL.getColumnIndex()]));
//        LOG.debug(String.format(">>>>TriggerInvoice->update: create=%s, update=%s",
//                newRow[Table.INVOICE.T_CREATEDON.getColumnIndex()],
//                newRow[Table.INVOICE.T_UPDATEDON.getColumnIndex()]));
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
