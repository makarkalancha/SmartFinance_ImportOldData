package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.utils.H2DbUtilsTest;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbc.JdbcSQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Makar Kalancha on 11 Jul 2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableInvoiceTest {
    private static final Logger LOG = LogManager.getLogger(TableInvoiceTest.class);
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String ORGANIZATION_NAME_PREFIX = "Organization TableInvoiceTest";
    private static final String ORGANIZATION_DESC = "Organization TableInvoiceTest DESC";

    private TableOrganizationTest tableOrganizationTest = new TableOrganizationTest();

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerInvoiceTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ORGANIZATION);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerInvoiceTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerInvoiceTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerInvoiceTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    public Long insert(Long organizationId, String comment, BigDecimal balance) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.INVOICE +
                " (" + Table.INVOICE.ORGANIZATION_ID + ", " + Table.INVOICE.COMMENT + ", " + Table.INVOICE.BALANCE + ") " +
                "VALUES(" + organizationId + ",'" + comment + "'," + balance + ")";
        LOG.debug(queryInsert);
        ResultSet rs = null;
        Long result = -1L;
        try (
                PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
        ){
            insertPS.executeUpdate();
            rs = insertPS.getGeneratedKeys();
            rs.next();
            result = rs.getLong(1);
            return result;
        } finally {
            if(rs != null){
                rs.close();
            }
        }
    }

    @Test
    public void testInvoice_11_insert() throws Exception {
        LOG.debug("testInvoice_11_insert");
        String queryDates = "SELECT " + Table.INVOICE.ID + " FROM " + Table.Names.INVOICE +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_CREATEDON + " = " + Table.INVOICE.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long organizationId = tableOrganizationTest.insert("Organization TableInvoiceTest", "Organization Description From TableInvoiceTest #testInvoice_11_insert");
            long idJustInserted = insert(organizationId, "invoice comment 11", new BigDecimal("5.0"));
            LOG.debug("idJustInserted > 0: idJustInserted=" + idJustInserted);
            assert (idJustInserted > 0);
            selectDatesPS.setLong(1, idJustInserted);
            rs = selectDatesPS.executeQuery();
            rs.next();
            long idWithDates = rs.getLong(1);
            LOG.debug("idWithDates > 0: idWithDates=" + idWithDates);
            LOG.debug("idJustInserted == idWithDates: " + (idJustInserted == idWithDates) +
                    "; idJustInserted=" + idJustInserted +
                    "; idWithDates=" + idWithDates);
            assert (idWithDates > 0);
            assert (idJustInserted == idWithDates);
        } finally {
            if (rs != null) rs.close();
        }
    }

    public void update(Long id, String comment) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.INVOICE + " SET " + Table.INVOICE.COMMENT + " = ? " +
                " WHERE " + Table.INVOICE.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setString(1, comment);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testInvoice_21_update() throws Exception {
        LOG.debug("testInvoice_21_update");
        String querySelect = "SELECT MAX(" + Table.INVOICE.ID + ") FROM " + Table.Names.INVOICE;
        String queryDates = "SELECT " + Table.INVOICE.ID + " FROM " + Table.Names.INVOICE +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_CREATEDON + " != " + Table.INVOICE.T_UPDATEDON;
        LOG.debug(querySelect);
        LOG.debug(queryDates);
        ResultSet rs = null;
        long idMax = 0L;
        try (
                PreparedStatement selectPS = dbConnectionResource.getConnection().prepareStatement(querySelect);
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            rs = selectPS.executeQuery();
            rs.next();
            idMax = rs.getLong(1);

            update(idMax, "_new comment");

            selectDatesPS.setLong(1, idMax);
            rs = selectDatesPS.executeQuery();
            rs.next();
            long idWithDates = rs.getLong(1);
            assert (idWithDates > 0);
            assert (idMax == idWithDates);
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testInvoice_31_delete() throws Exception {
        LOG.debug("testInvoice_31_delete");
        String querySelect = "SELECT MIN(" + Table.INVOICE.ID + ") FROM " + Table.Names.INVOICE;
        String queryDelete = "DELETE FROM " + Table.Names.INVOICE + " WHERE " +
                Table.INVOICE.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = '" + Table.Names.INVOICE + "')";

        LOG.debug(querySelect);
        LOG.debug(queryDelete);
        LOG.debug(querySelectDeletedRow);
        ResultSet rs = null;
        long idMin = 0L;
        long idNewMin = 0L;
        try (
                PreparedStatement selectPS = dbConnectionResource.getConnection().prepareStatement(querySelect);
                PreparedStatement deletePS = dbConnectionResource.getConnection().prepareStatement(queryDelete);
                PreparedStatement selectDeletedRowsPS = dbConnectionResource.getConnection().prepareStatement(querySelectDeletedRow);
        ){
            rs = selectPS.executeQuery();
            rs.next();
            idMin = rs.getLong(1);

            deletePS.setLong(1, idMin);
            deletePS.executeUpdate();

            rs = selectPS.executeQuery();
            rs.next();
            idNewMin = rs.getLong(1);

            assertEquals(true, idMin != idNewMin);

            rs = selectDeletedRowsPS.executeQuery();
            rs.next();
            String jsonRow = rs.getString(1);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(jsonRow).getAsJsonObject();
            JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

            long idFromDeletedRows = rowJsonObject.get(Table.INVOICE.ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[6];
        row[0] = 1L;
        row[1] = 2L;
        row[2] = "invoice comment";
        row[3] = new BigDecimal("2.0");
        row[4] = SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06");
        row[5] = SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.INVOICE.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.INVOICE.ORGANIZATION_ID.toString(), (Long) row[1]);
        rowJson.addProperty(Table.INVOICE.COMMENT.toString(), (String) row[2]);
        rowJson.addProperty(Table.INVOICE.BALANCE.toString(), (BigDecimal) row[3]);
        rowJson.addProperty(Table.INVOICE.T_CREATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[4]));
        rowJson.addProperty(Table.INVOICE.T_UPDATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[5]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.INVOICE);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.INVOICE\",\"row\":" +
                "{\"ID\":1,\"ORGANIZATION_ID\":2,\"COMMENT\":\"invoice comment\",\"BALANCE\":2.0," +
                "\"T_CREATEDON\":\"2001-02-03 14:05:06\",\"T_UPDATEDON\":\"2006-05-04 03:02:01\"}}";

        LOG.debug("testDeleteToJsonObject.expectedJsonString:" + expectedJsonString);
        LOG.debug("testDeleteToJsonObject.actualJsonString:" + tableJson.toString());
        assertEquals(expectedJsonString, tableJson.toString());
    }

    @Test
    public void testDeleteReadJsonObject() throws Exception{
        String tableJsonString = createJsonObject().toString();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(tableJsonString).getAsJsonObject();

        String schemaTableName = jsonObject.get(Table.Elements.tableName.toString()).getAsString();
        assertEquals("TEST.INVOICE", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.INVOICE.ID.toString()).getAsLong();
        assertEquals(1L, id);
        long organizationId = rowJsonObject.get(Table.INVOICE.ORGANIZATION_ID.toString()).getAsLong();
        assertEquals(2L, organizationId);
        JsonElement jsonElementDesc = rowJsonObject.get(Table.INVOICE.COMMENT.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElementDesc);
        assertEquals("invoice comment", description);
        JsonElement jsonElementRate = rowJsonObject.get(Table.INVOICE.BALANCE.toString());
        BigDecimal rate = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementRate);
        assertEquals(new BigDecimal("2.0"), rate);
        Date createdOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.INVOICE.T_CREATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.INVOICE.T_UPDATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01"), updatedOn);
    }
}