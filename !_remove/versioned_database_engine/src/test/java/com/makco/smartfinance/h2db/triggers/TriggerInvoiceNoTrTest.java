package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.functions.TestDateUnitFunctions;
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

import static org.junit.Assert.assertEquals;

/**
 * Created by Makar Kalancha on 11 Jul 2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TriggerInvoiceNoTrTest {
    private static final Logger LOG = LogManager.getLogger(TriggerInvoiceNoTrTest.class);
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TriggerOrganizationTest triggerOrganizationTest = new TriggerOrganizationTest();

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerInvoiceTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ITEM_V3);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ITEM_NOTR);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ITEM);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE_V3);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE_NOTR);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ORGANIZATION);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TRANSACTION_V3);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TRANSACTION_NOTR);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TRANSACTION);
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

    public Long insert(String invoiceNumber, Long organizationId, Long dateunitUnitday, String comment, BigDecimal subTotal, BigDecimal total)
            throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.INVOICE_NOTR +
                " (" + Table.INVOICE.INVOICE_NUMBER + ", " + Table.INVOICE.ORGANIZATION_ID + ", " + Table.INVOICE.DATEUNIT_UNITDAY + ", " +
                Table.INVOICE.COMMENT + ", " + Table.INVOICE.SUB_TOTAL + ", " + Table.INVOICE.TOTAL + ") " +
                "VALUES('" + invoiceNumber + "'," + organizationId + "," + dateunitUnitday + ",'" + comment + "'," + subTotal + "," +
                total + ")";
        LOG.debug(queryInsert);
        ResultSet rs = null;
        Long result = -1L;
        try (
                PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
        ) {
            insertPS.executeUpdate();
            rs = insertPS.getGeneratedKeys();
            rs.next();
            result = rs.getLong(1);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public Long insert(String invoiceNumber, Long organizationId, Long dateunitUnitday, String comment)
            throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.INVOICE_NOTR +
                " (" + Table.INVOICE.INVOICE_NUMBER + ", " + Table.INVOICE.ORGANIZATION_ID + ", " +
                Table.INVOICE.DATEUNIT_UNITDAY + ", " + Table.INVOICE.COMMENT + ") " +
                "VALUES('" + invoiceNumber + "'," + organizationId + "," + dateunitUnitday + ",'" + comment + "')";
        LOG.debug(queryInsert);
        ResultSet rs = null;
        Long result = -1L;
        try (
                PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
        ) {
            insertPS.executeUpdate();
            rs = insertPS.getGeneratedKeys();
            rs.next();
            result = rs.getLong(1);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    @Test
    public void testInvoice_11_insert() throws Exception {
        LOG.debug("testInvoice_11_insert");
        String queryDates = "SELECT " + Table.INVOICE.ID + " FROM " + Table.Names.INVOICE_NOTR +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_CREATEDON + " = " + Table.INVOICE.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                    Date.from(LocalDate.of(2016, Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            long organizationId = triggerOrganizationTest.insert("Organization TableInvoiceTest", "Organization Description From TableInvoiceTest #testInvoice_11_insert");
            long idJustInserted = insert("11_insert", organizationId, dateunitUnitday, "invoice comment 11", new BigDecimal("5.0"), new BigDecimal("6.0"));
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

    @Test(expected=JdbcSQLException.class)
    //org.h2.jdbc.JdbcSQLException: Unique index or primary key violation: "IDX_UNQ_NVC_NVCNMBR ON TEST.INVOICE(INVOICE_NUMBER) VALUES ('20160715112027', 4)"; SQL statement:
    public void testInvoice_12_insert_duplicate() throws Exception {
        LOG.debug("testInvoice_12_insert_duplicate");
        long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                Date.from(LocalDate.of(2016, Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        long organizationId = triggerOrganizationTest.insert("Organization TableInvoiceTest", "Organization Description From TableInvoiceTest #testInvoice_11_insert");
        String invoiceNumber = "Invoice_12_insert";
        long idJustInserted1 = insert(invoiceNumber, organizationId, dateunitUnitday, "invoice comment 11", new BigDecimal("5.0"), new BigDecimal("6.0"));
        long idJustInserted2 = insert(invoiceNumber, organizationId, dateunitUnitday, "invoice comment 12", new BigDecimal("5.0"), new BigDecimal("6.0"));
    }

    public void updateComment(Long id, String comment) throws Exception {
        String queryUpdate = "UPDATE " + Table.Names.INVOICE_NOTR + " SET " + Table.INVOICE.COMMENT + " = ? " +
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

    public void updateInvoiceNumber(Long id, String invoiceNumber) throws Exception {
        String queryUpdate = "UPDATE " + Table.Names.INVOICE_NOTR + " SET " + Table.INVOICE.INVOICE_NUMBER + " = ? " +
                " WHERE " + Table.INVOICE.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setString(1, invoiceNumber);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    public void updateDate(Long id, Long dateunit) throws Exception {
        String queryUpdate = "UPDATE " + Table.Names.INVOICE_NOTR + " SET " + Table.INVOICE.DATEUNIT_UNITDAY + " = ? " +
                " WHERE " + Table.INVOICE.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setLong(1, dateunit);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testInvoice_21_update() throws Exception {
        LOG.debug("testInvoice_21_update");
        String querySelect = "SELECT MAX(" + Table.INVOICE.ID + ") FROM " + Table.Names.INVOICE_NOTR;
        String queryDates = "SELECT " + Table.INVOICE.ID + " FROM " + Table.Names.INVOICE_NOTR +
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

            updateComment(idMax, "_new comment");

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

    @Test(expected=JdbcSQLException.class)
    //org.h2.jdbc.JdbcSQLException: Unique index or primary key violation: "IDX_UNQ_NVC_NVCNMBR ON TEST.INVOICE(INVOICE_NUMBER) VALUES ('201607151144aa', 12)"; SQL statement:
    public void testInvoice_22_update_duplicate() throws Exception {
        LOG.debug("testInvoice_22_update_duplicate");

        long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                Date.from(LocalDate.of(2016, Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        long organizationId = triggerOrganizationTest.insert("Organization TableInvoiceTest", "Organization Description From TableInvoiceTest #testInvoice_11_insert");
        String invoiceNumber1 = "22_update";
        String invoiceNumber2 = invoiceNumber1.substring(0, invoiceNumber1.length() - 2) + "aa";
        LOG.debug(String.format(">>>>invoiceNumber1=%s; invoiceNumber2=%s", invoiceNumber1, invoiceNumber2));
        assert (!invoiceNumber1.equals(invoiceNumber2));
        long idJustInserted1 = insert(invoiceNumber1, organizationId, dateunitUnitday, "invoice comment 11", new BigDecimal("5.0"), new BigDecimal("6.0"));
        long idJustInserted2 = insert(invoiceNumber2, organizationId, dateunitUnitday, "invoice comment 11", new BigDecimal("5.0"), new BigDecimal("6.0"));

        updateInvoiceNumber(idJustInserted1, invoiceNumber2);

    }

    @Test
    public void testInvoice_31_delete() throws Exception {
        LOG.debug("testInvoice_31_delete");
        String querySelect = "SELECT MIN(" + Table.INVOICE.ID + ") FROM " + Table.Names.INVOICE_NOTR;
        String queryDelete = "DELETE FROM " + Table.Names.INVOICE_NOTR + " WHERE " +
                Table.INVOICE.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = '" + Table.Names.INVOICE_NOTR + "')";

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

        Object[] row = new Object[Table.INVOICE.values().length];
        row[0] = 1L;
        row[1] = "20160715114901";
        row[2] = 2L;
        row[3] = "invoice comment";
        row[4] = new BigDecimal("2.0");
        row[5] = new BigDecimal("5.0");
        row[6] = SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06");
        row[7] = SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.INVOICE.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.INVOICE.INVOICE_NUMBER.toString(), (String) row[1]);
        rowJson.addProperty(Table.INVOICE.ORGANIZATION_ID.toString(), (Long) row[2]);
        rowJson.addProperty(Table.INVOICE.COMMENT.toString(), (String) row[3]);
        rowJson.addProperty(Table.INVOICE.SUB_TOTAL.toString(), (BigDecimal) row[4]);
        rowJson.addProperty(Table.INVOICE.TOTAL.toString(), (BigDecimal) row[5]);
        rowJson.addProperty(Table.INVOICE.T_CREATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[6]));
        rowJson.addProperty(Table.INVOICE.T_UPDATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[7]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.INVOICE_NOTR);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.INVOICE\",\"row\":" +
                "{\"ID\":1,\"INVOICE_NUMBER\":\"20160715114901\",\"ORGANIZATION_ID\":2,\"COMMENT\":\"invoice comment\","+
                "\"SUB_TOTAL\":2.0,\"TOTAL\":5.0," +
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

        JsonElement jsonElementInvoiceNumber = rowJsonObject.get(Table.INVOICE.INVOICE_NUMBER.toString());
        String invoiceNumber= JsonUtils.getNullableFromJsonElementAsString(jsonElementInvoiceNumber);
        assertEquals("20160715114901", invoiceNumber);

        long organizationId = rowJsonObject.get(Table.INVOICE.ORGANIZATION_ID.toString()).getAsLong();
        assertEquals(2L, organizationId);
        JsonElement jsonElementDesc = rowJsonObject.get(Table.INVOICE.COMMENT.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElementDesc);
        assertEquals("invoice comment", description);

        JsonElement jsonElementSubTotal = rowJsonObject.get(Table.INVOICE.SUB_TOTAL.toString());
        BigDecimal subTotal = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementSubTotal);
        assertEquals(new BigDecimal("2.0"), subTotal);
        JsonElement jsonElementTotal = rowJsonObject.get(Table.INVOICE.TOTAL.toString());
        BigDecimal total = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementTotal);
        assertEquals(new BigDecimal("5.0"), total);

        Date createdOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.INVOICE.T_CREATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.INVOICE.T_UPDATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01"), updatedOn);
    }
}