package com.makco.smartfinance.h2db.triggers;

import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 12/07/2016
 * Time: 11:11
 */
public class TriggerTransaction extends AbstractTrigger {
    @Override
    protected String logTriggerName() {
        return Table.Names.TRANSACTION.toString();
    }

    @Override
    protected void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.TRANSACTION.T_CREATEDON.getColumnIndex()] = Timestamp.valueOf(now);
        newRow[Table.TRANSACTION.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        newRow[Table.TRANSACTION.T_UPDATEDON.getColumnIndex()] = Timestamp.valueOf(now);
    }

    @Override
    protected void prepareJsonForDeletion(Object[] oldRow) {
        rowJson.addProperty(Table.TRANSACTION.ID.toString(), (Long) oldRow[Table.TRANSACTION.ID.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.TRANSACTION_NUMBER.toString(), (String) oldRow[Table.TRANSACTION.TRANSACTION_NUMBER.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.ACCOUNT_ID.toString(), (Long) oldRow[Table.TRANSACTION.ACCOUNT_ID.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.ACCOUNT_GROUP_TYPE.toString(), (String) oldRow[Table.TRANSACTION.ACCOUNT_GROUP_TYPE.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.INVOICE_ID.toString(), (Long) oldRow[Table.TRANSACTION.INVOICE_ID.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.DATEUNIT_UNITDAY.toString(), (Long) oldRow[Table.TRANSACTION.DATEUNIT_UNITDAY.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.COMMENT.toString(), (String) oldRow[Table.TRANSACTION.COMMENT.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.DEBIT_AMOUNT.toString(), (BigDecimal) oldRow[Table.TRANSACTION.DEBIT_AMOUNT.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.CREDIT_AMOUNT.toString(), (BigDecimal) oldRow[Table.TRANSACTION.CREDIT_AMOUNT.getColumnIndex()]);
        rowJson.addProperty(Table.TRANSACTION.T_CREATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.TRANSACTION.T_CREATEDON.getColumnIndex()]));
        rowJson.addProperty(Table.TRANSACTION.T_UPDATEDON.toString(), JsonUtils.getSimpleDateTimeFormat()
                .format((Date) oldRow[Table.TRANSACTION.T_UPDATEDON.getColumnIndex()]));
    }
}
