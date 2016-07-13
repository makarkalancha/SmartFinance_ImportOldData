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
public class TriggerItem extends AbstractTrigger {
    private static final String UPDATE_INVOICE = new StringBuilder()
            .append("UPDATE ")
            .append(Table.Names.INVOICE)
            .append(" SET ")
            .append(Table.INVOICE.SUB_TOTAL)
            .append(" = ")
            .append(Table.INVOICE.SUB_TOTAL)
            .append("+ ?, ")
            .append(Table.INVOICE.TOTAL)
            .append(" = ")
            .append(Table.INVOICE.TOTAL)
            .append("+ ? where ")
            .append(Table.INVOICE.ID)
            .append(" = ?")
            .toString();

    @Override
    protected String logTriggerName() {
        return Table.Names.ITEM.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ITEM.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.ITEM.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);

        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INVOICE);
        preparedStatement.setBigDecimal(1, (BigDecimal) newRow[Table.ITEM.SUB_TOTAL.getColumnIndex()]);
        preparedStatement.setBigDecimal(2, (BigDecimal) newRow[Table.ITEM.TOTAL.getColumnIndex()]);
        preparedStatement.setLong(3, (Long) newRow[Table.ITEM.INVOICE_ID.getColumnIndex()]);
        preparedStatement.execute();
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.ITEM.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);

        BigDecimal diffSubtotal = (BigDecimal) newRow[Table.ITEM.SUB_TOTAL.getColumnIndex()];
        diffSubtotal = diffSubtotal.subtract((BigDecimal) oldRow[Table.ITEM.SUB_TOTAL.getColumnIndex()]);

        BigDecimal diffTotal = (BigDecimal) newRow[Table.ITEM.TOTAL.getColumnIndex()];
        diffTotal = diffTotal.subtract((BigDecimal) oldRow[Table.ITEM.TOTAL.getColumnIndex()]);


        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INVOICE);
        preparedStatement.setBigDecimal(1, diffSubtotal);
        preparedStatement.setBigDecimal(2, diffTotal);
        preparedStatement.setLong(3, (Long) newRow[Table.ITEM.INVOICE_ID.getColumnIndex()]);
        preparedStatement.execute();
    }

    @Override
    protected void delete(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        super.delete(connection, oldRow, newRow);

        BigDecimal diffSubtotal = ((BigDecimal) oldRow[Table.ITEM.SUB_TOTAL.getColumnIndex()]).negate();

        BigDecimal diffTotal = ((BigDecimal) oldRow[Table.ITEM.TOTAL.getColumnIndex()]).negate();


        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INVOICE);
        preparedStatement.setBigDecimal(1, diffSubtotal);
        preparedStatement.setBigDecimal(2, diffTotal);
        preparedStatement.setLong(3, (Long) oldRow[Table.ITEM.INVOICE_ID.getColumnIndex()]);
        preparedStatement.execute();
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.ITEM.ID.toString(), (Long) oldRow[Table.ITEM.ID.getColumnIndex()]);
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
