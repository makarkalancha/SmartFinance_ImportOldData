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
 * \SmartFinance\database_engine\src\main\java\com\makco\smartfinance\h2db\triggers\
 * v3
 */
public class TriggerItem extends AbstractTrigger {

    private static final String INVOICE_DATEUNIT = new StringBuilder()
            .append("SELECT ")
            .append(Table.INVOICE.DATEUNIT_UNITDAY)
            .append(" FROM ")
            .append(SCHEMA_NAME_PLACEHOLDER)
            .append(".")
            .append(Table.Names.INVOICE)
            .append(" WHERE ")
            .append(Table.INVOICE.ID)
            .append(" = ?")
            .toString();

    //no Item trigger, order number is in equals/hashcode
//    private static final String ITEM_COUNT_BY_INVOICE_ID = new StringBuilder()
//            .append("SELECT MAX(")
//            .append(Table.ITEM.ORDER_NUMBER)
//            .append(") FROM ")
//            .append(SCHEMA_NAME_PLACEHOLDER)
//            .append(".")
//            .append(Table.Names.ITEM)
//            .append(" WHERE ")
//            .append(Table.ITEM.INVOICE_ID)
//            .append(" = ?")
//            .toString();

    @Override
    protected String logTriggerName() {
        return Table.Names.ITEM.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        String invoiceDateunitQ = INVOICE_DATEUNIT.replace(SCHEMA_NAME_PLACEHOLDER, schemaName);
//        String qtyItemsQ = ITEM_COUNT_BY_INVOICE_ID.replace(SCHEMA_NAME_PLACEHOLDER, schemaName);
        ResultSet rs = null;
        try(
                PreparedStatement invoiceDate = connection.prepareStatement(invoiceDateunitQ);
//                PreparedStatement qtyItems = connection.prepareStatement(qtyItemsQ);
        ) {
            invoiceDate.setLong(1, (Long) newRow[Table.ITEM.INVOICE_ID.getColumnIndex()]);
            rs = invoiceDate.executeQuery();
            rs.next();
            newRow[Table.ITEM.DATEUNIT_UNITDAY.getColumnIndex()] = rs.getLong(1);

//            qtyItems.setLong(1, (Long) newRow[Table.ITEM.INVOICE_ID.getColumnIndex()]);
//            rs = qtyItems.executeQuery();
//            rs.next();
//            newRow[Table.ITEM.ORDER_NUMBER.getColumnIndex()] = rs.getInt(1) + 1;

            newRow[Table.ITEM.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
            newRow[Table.ITEM.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);

//            LOG.debug(String.format(">>>>TriggerItem->insert: dateunit=%s, subtotal=%s, total=%s",
//                    newRow[Table.ITEM.DATEUNIT_UNITDAY.getColumnIndex()],
//                    newRow[Table.ITEM.SUB_TOTAL.getColumnIndex()],
//                    newRow[Table.ITEM.TOTAL.getColumnIndex()]));
        }finally {
            if(rs != null){
                rs.close();
            }
        }
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
