package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonObject;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.api.Trigger;

/**
 * User: Makar Kalancha
 * Date: 06/03/2016
 * Time: 18:05
 */
public abstract class AbstractTrigger implements Trigger {

    private static final Logger LOG = LogManager.getLogger(AbstractTrigger.class);
    private static final String SCHEMA_NAME_PLACEHOLDER = "{{schemaName}}";

    private StringBuilder schemaNameAndTable = new StringBuilder();
    private final StringBuilder queryStrB = new StringBuilder();
    {
        queryStrB.append("INSERT INTO ");
        queryStrB.append(SCHEMA_NAME_PLACEHOLDER);
        queryStrB.append(".");
        queryStrB.append(Table.Names._DELETED_ROWS);
        queryStrB.append(" (");
        queryStrB.append(Table._DELETED_ROWS.SCHEMA_NAME);
        queryStrB.append(", ");
        queryStrB.append(Table._DELETED_ROWS.TABLE_NAME);
        queryStrB.append(", ");
        queryStrB.append(Table._DELETED_ROWS.JSON_ROW);
        queryStrB.append(") VALUES(?,?,?)");
    }

    protected java.util.Date now;
    private int type;
    private String schemaName;
    private String tableName;
    protected JsonObject rowJson = new JsonObject();

    private final JsonObject tableJson = new JsonObject();

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void init(Connection conn, String schemaName, String triggerName,
                     String tableName, boolean before, int type) throws SQLException {
        this.type = type;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.schemaNameAndTable.append(schemaName);
        this.schemaNameAndTable.append(".");
        this.schemaNameAndTable.append(tableName);
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        LOG.debug(logTriggerName());
        LOG.debug("before oldRow:" + Arrays.toString(oldRow));
        LOG.debug("before newRow:" + Arrays.toString(newRow));
        now = new java.util.Date();
        LOG.debug("now: " + now);
        if (Trigger.INSERT == type) {
            LOG.debug("operation type: Trigger.INSERT." + type);
            insert(connection, oldRow, newRow);
        } else if (Trigger.UPDATE == type) {
            LOG.debug("operation type: Trigger.UPDATE." + type);
            update(connection, oldRow, newRow);
        } else if (Trigger.DELETE == type) {
            LOG.debug("operation type: Trigger.DELETE." + type);
            delete(connection, oldRow, newRow);
        }
        LOG.debug("after oldRow:" + Arrays.toString(oldRow));
        LOG.debug("after newRow:" + Arrays.toString(newRow));
    }

    @Override
    public void remove() throws SQLException {

    }

    private String fillTableJson(JsonObject rowJson) {
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaNameAndTable.toString());
        tableJson.add(Table.Elements.row.toString(), rowJson);
        LOG.debug("tableJson: " + tableJson.toString());
        return tableJson.toString();
    }

    protected int insertIntoDeletedRowsTable(Connection connection) throws SQLException{
        String query = queryStrB.toString().replace(SCHEMA_NAME_PLACEHOLDER, schemaName);
        LOG.debug("query:" + query);
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, schemaName);
            preparedStatement.setString(2, tableName);
            preparedStatement.setString(3, fillTableJson(rowJson));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e, e);
            throw e;
        }
    }

    protected abstract String logTriggerName();
    protected abstract void insert(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException;
    protected abstract void update(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException;
    protected abstract void prepareJsonForDeletion(Object[] oldRow);

    protected void delete(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException{
        prepareJsonForDeletion(oldRow);
        LOG.debug("rowJson: " + rowJson);
        insertIntoDeletedRowsTable(connection);
    }
}
