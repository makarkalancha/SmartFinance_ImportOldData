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
 * Created by Makar Kalancha on 12 Jul 2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableTransactionTest {
    private static final Logger LOG = LogManager.getLogger(TableTransactionTest.class);
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TableAccountGroupTest tableAccountGroupTest = new TableAccountGroupTest();
    private TableAccountTest tableAccountTest= new TableAccountTest();
    private TableOrganizationTest tableOrganizationTest = new TableOrganizationTest();
    private TableInvoiceTest tableInvoiceTest = new TableInvoiceTest();

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerTransactionTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TRANSACTION);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ACCOUNT);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ACCOUNT_GROUP);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ORGANIZATION);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerTransactionTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerTransactionTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerTransactionTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    public Long insert(String transactionNumber, Long accountId, Long invoiceId,
                       Long dateunitUnitday, String comment, BigDecimal subTotal, BigDecimal total) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.TRANSACTION +
                " (" + Table.TRANSACTION.TRANSACTION_NUMBER + ", " + Table.TRANSACTION.ACCOUNT_ID + ", " +
                Table.TRANSACTION.INVOICE_ID + ", " + Table.TRANSACTION.DATEUNIT_UNITDAY + ", " +
                Table.TRANSACTION.COMMENT + ", " + Table.TRANSACTION.DEBIT_AMOUNT + ", " +
                Table.TRANSACTION.CREDIT_AMOUNT + ") " +
                "VALUES('" + transactionNumber + "'," + accountId + ", " + invoiceId + ", " +
                dateunitUnitday + ", '" + comment + "', " + subTotal + ", " + total + ")";

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
    public void testTransaction_11_insert() throws Exception {
        LOG.debug("testTransaction_11_insert");
        String queryDates = "SELECT " + Table.TRANSACTION.ID + " FROM " + Table.Names.TRANSACTION +
                " WHERE " + Table.TRANSACTION.ID + " = ?" +
                " AND " + Table.TRANSACTION.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.TRANSACTION.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.TRANSACTION.T_CREATEDON + " = " + Table.TRANSACTION.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                    Date.from(LocalDate.of(2016, Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            String accountGroupType = "C";
            long accountGroupId = tableAccountGroupTest.insert(accountGroupType, "account group name",
                    "account group description from  TableTransactionTest #testTransaction_11_insert");
            long accountId = tableAccountTest.insert(accountGroupId, accountGroupType, "account name",
                    "account description from  TableTransactionTest #testTransaction_11_insert");

            long organizationId = tableOrganizationTest.insert("TableTransactionTest",
                    "Organization Description From TableTransactionTest #testItem_11_insert");
            long invoiceId = tableInvoiceTest.insert("11_insert", organizationId,
                    dateunitUnitday, "invoice comment TableTransactionTest #testItem_11_insert", new BigDecimal("3"),
                    new BigDecimal("4"));

            long idJustInserted = insert("11_insert", accountId, invoiceId, dateunitUnitday,
                    "transaction TableTransactionTest #testItem_11_insert comment 11", new BigDecimal("5.0"),
                    new BigDecimal("6.0"));
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
    //org.h2.jdbc.JdbcSQLException: Unique index or primary key violation: "IDX_UNQ_TRNSCTN_TRNSCTNNMBR ON TEST.TRANSACTION(TRANSACTION_NUMBER) VALUES ('12_insert', 3)"; SQL statement:
    public void testTransaction_12_insert_duplicate() throws Exception {
        LOG.debug("testTransaction_11_insert");
        long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                Date.from(LocalDate.of(2016, Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        String accountGroupType = "C";
        long accountGroupId = tableAccountGroupTest.insert(accountGroupType, "account group name12",
                "account group description from  TableTransactionTest #testTransaction_11_insert");
        long accountId = tableAccountTest.insert(accountGroupId, accountGroupType, "account name12",
                "account description from  TableTransactionTest #testTransaction_11_insert");

        long organizationId = tableOrganizationTest.insert("TableTransactionTest12",
                "Organization Description From TableTransactionTest #testItem_11_insert");
        long invoiceId = tableInvoiceTest.insert("12_insert", organizationId,
                dateunitUnitday, "invoice comment TableTransactionTest #testItem_11_insert", new BigDecimal("3"),
                new BigDecimal("4"));

        insert("12_insert", accountId, invoiceId, dateunitUnitday,
                "transaction TableTransactionTest #testItem_11_insert comment 11", new BigDecimal("5.0"),
                new BigDecimal("6.0"));

        insert("12_insert", accountId, invoiceId, dateunitUnitday,
                "transaction TableTransactionTest #testItem_11_insert comment 11", new BigDecimal("5.0"),
                new BigDecimal("6.0"));
    }

    public void updateComment(Long id, String comment) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.TRANSACTION + " SET " + Table.TRANSACTION.COMMENT + " = ? " +
                " WHERE " + Table.TRANSACTION.ID + " = ?";
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

    public void updateTransactionNumber(Long id, String transactionNumber) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.TRANSACTION + " SET " + Table.TRANSACTION.TRANSACTION_NUMBER + " = ? " +
                " WHERE " + Table.TRANSACTION.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setString(1, transactionNumber);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testTransaction_21_update() throws Exception {
        LOG.debug("testTransaction_21_update");
        String querySelect = "SELECT MAX(" + Table.TRANSACTION.ID + ") FROM " + Table.Names.TRANSACTION;
        String queryDates = "SELECT " + Table.TRANSACTION.ID + " FROM " + Table.Names.TRANSACTION +
                " WHERE " + Table.TRANSACTION.ID + " = ?" +
                " AND " + Table.TRANSACTION.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.TRANSACTION.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.TRANSACTION.T_CREATEDON + " != " + Table.TRANSACTION.T_UPDATEDON;
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
    //org.h2.jdbc.JdbcSQLException: Unique index or primary key violation: "IDX_UNQ_TRNSCTN_TRNSCTNNMBR ON TEST.TRANSACTION(TRANSACTION_NUMBER) VALUES ('22_update-2', 6)"; SQL statement:
    public void testTransaction_22_update_duplicate() throws Exception {
        LOG.debug("testTransaction_22_update_duplicate");
        long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                Date.from(LocalDate.of(2016, Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        String accountGroupType = "C";
        long accountGroupId = tableAccountGroupTest.insert(accountGroupType, "account group name22",
                "account group description from  TableTransactionTest #testTransaction_11_insert");
        long accountId = tableAccountTest.insert(accountGroupId, accountGroupType, "account name22",
                "account description from  TableTransactionTest #testTransaction_11_insert");

        long organizationId = tableOrganizationTest.insert("TableTransactionTest22",
                "Organization Description From TableTransactionTest #testItem_11_insert");
        long invoiceId = tableInvoiceTest.insert("22_update", organizationId,
                dateunitUnitday, "invoice comment TableTransactionTest #testItem_11_insert", new BigDecimal("3"),
                new BigDecimal("4"));

        String transactionNumber1 = "22_update-1";
        String transactionNumber2 = "22_update-2";
        long idJustInserted = insert(transactionNumber1, accountId, invoiceId, dateunitUnitday,
                "transaction TableTransactionTest #testItem_11_insert comment 11", new BigDecimal("5.0"),
                new BigDecimal("6.0"));
        insert(transactionNumber2, accountId, invoiceId, dateunitUnitday,
                "transaction TableTransactionTest #testItem_11_insert comment 11", new BigDecimal("5.0"),
                new BigDecimal("6.0"));

        updateTransactionNumber(idJustInserted, transactionNumber2);

    }

    @Test
    public void testTransaction_31_delete() throws Exception {
        LOG.debug("testTransaction_31_delete");
        String querySelect = "SELECT MIN(" + Table.TRANSACTION.ID + ") FROM " + Table.Names.TRANSACTION;
        String queryDelete = "DELETE FROM " + Table.Names.TRANSACTION + " WHERE " +
                Table.TRANSACTION.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = '" + Table.Names.TRANSACTION + "')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.TRANSACTION.ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[Table.TRANSACTION.values().length];
        row[0] = 1L;
        row[1] = "trans number";
        row[2] = 2L;
        row[3] = 3L;
        row[4] = 4L;
        row[5] = "invoice comment";
        row[6] = new BigDecimal("2.0");
        row[7] = new BigDecimal("5.0");
        row[8] = SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06");
        row[9] = SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.TRANSACTION.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.TRANSACTION.TRANSACTION_NUMBER.toString(), (String) row[1]);
        rowJson.addProperty(Table.TRANSACTION.ACCOUNT_ID.toString(), (Long) row[2]);
        rowJson.addProperty(Table.TRANSACTION.INVOICE_ID.toString(), (Long) row[3]);
        rowJson.addProperty(Table.TRANSACTION.DATEUNIT_UNITDAY.toString(), (Long) row[4]);
        rowJson.addProperty(Table.TRANSACTION.COMMENT.toString(), (String) row[5]);
        rowJson.addProperty(Table.TRANSACTION.DEBIT_AMOUNT.toString(), (BigDecimal) row[6]);
        rowJson.addProperty(Table.TRANSACTION.CREDIT_AMOUNT.toString(), (BigDecimal) row[7]);
        rowJson.addProperty(Table.TRANSACTION.T_CREATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[8]));
        rowJson.addProperty(Table.TRANSACTION.T_UPDATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[9]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.TRANSACTION);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.TRANSACTION\",\"row\":" +
                "{\"ID\":1,\"TRANSACTION_NUMBER\":\"trans number\",\"ACCOUNT_ID\":2,"+
                "\"INVOICE_ID\":3,\"DATEUNIT_UNITDAY\":4,"+
                "\"COMMENT\":\"invoice comment\","+
                "\"DEBIT_AMOUNT\":2.0,\"CREDIT_AMOUNT\":5.0," +
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
        assertEquals("TEST.TRANSACTION", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.TRANSACTION.ID.toString()).getAsLong();
        assertEquals(1L, id);
        JsonElement jsonElementAccGr = rowJsonObject.get(Table.TRANSACTION.TRANSACTION_NUMBER.toString());
        String accountGroup = JsonUtils.getNullableFromJsonElementAsString(jsonElementAccGr);
        assertEquals("trans number", accountGroup);
        long accountId = rowJsonObject.get(Table.TRANSACTION.ACCOUNT_ID.toString()).getAsLong();
        assertEquals(2L, accountId);
        long invoiceId = rowJsonObject.get(Table.TRANSACTION.INVOICE_ID.toString()).getAsLong();
        assertEquals(3L, invoiceId);
        long dateunitUnitday = rowJsonObject.get(Table.TRANSACTION.DATEUNIT_UNITDAY.toString()).getAsLong();
        assertEquals(4L, dateunitUnitday);

        JsonElement jsonElementComm = rowJsonObject.get(Table.TRANSACTION.COMMENT.toString());
        String comment = JsonUtils.getNullableFromJsonElementAsString(jsonElementComm);
        assertEquals("invoice comment", comment);

        JsonElement jsonElementSubTotal = rowJsonObject.get(Table.TRANSACTION.DEBIT_AMOUNT.toString());
        BigDecimal subTotal = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementSubTotal);
        assertEquals(new BigDecimal("2.0"), subTotal);
        JsonElement jsonElementTotal = rowJsonObject.get(Table.TRANSACTION.CREDIT_AMOUNT.toString());
        BigDecimal total = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementTotal);
        assertEquals(new BigDecimal("5.0"), total);

        Date createdOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.TRANSACTION.T_CREATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.TRANSACTION.T_UPDATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01"), updatedOn);
    }
}