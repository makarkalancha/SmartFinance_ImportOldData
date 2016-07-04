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

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 24/04/2016
 * Time: 00:54
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableChildTaxTest {
    private static final Logger LOG = LogManager.getLogger(TableChildTaxTest.class);
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerChildTaxTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.CHILD_TAX);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerChildTaxTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerChildTaxTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerChildTaxTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    
    public void insert(Long taxParend, Long taxChild) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.CHILD_TAX +
                " (" + Table.CHILD_TAX.TAX_ID + ", " + Table.CHILD_TAX.CHILD_TAX_ID+ ") " +
                "VALUES(" + taxParend + "," + taxChild + ")";
        LOG.debug(queryInsert);
        try (
                PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(queryInsert);
        ){
            insertPS.executeUpdate();
        }
    }

    @Test
    public void testChildTax_11_insert() throws Exception {
        LOG.debug("testChildTax_11_insert");
        String queryDates = "SELECT " + Table.CHILD_TAX.ID + " FROM " + Table.Names.CHILD_TAX +
                " WHERE " + Table.CHILD_TAX.ID + " = ?" +
                " AND " + Table.CHILD_TAX.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CHILD_TAX.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CHILD_TAX.T_CREATEDON + " = " + Table.CHILD_TAX.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            //https://en.wikipedia.org/wiki/Goods_and_services_tax_%28Canada%29
            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            TableTaxTest tableTaxTest = new TableTaxTest();
            long idTax1 = tableTaxTest.insert("GST1 (2008)", "#1 the goods and services tax", new BigDecimal("5.0"), "{num} * tax / 100", startDate, null);
            long idTax2 = tableTaxTest.insert("GST2 (2008)", "#2 the goods and services tax", new BigDecimal("7.0"), "{num} * tax / 100", startDate, null);

            insert(idTax1, idTax2);
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
    public void testChildTax_12_insertDuplicate() throws Exception {
        LOG.debug("testChildTax_12_insertDuplicate");
        //Unique index or primary key violation: "IDX_UNQ_CRRNC_CD ON TEST.TAX(CODE) VALUES ('CAD', 7)"
        testChildTax_11_insert();
    }

    public void update(Long id, String code) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.CHILD_TAX + " SET " + Table.CHILD_TAX.NAME + " = ? " +
                " WHERE " + Table.CHILD_TAX.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setString(1, code);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testChildTax_21_update() throws Exception {
        LOG.debug("testChildTax_21_update");
        String querySelect = "SELECT MAX(" + Table.CHILD_TAX.ID + ") FROM " + Table.Names.CHILD_TAX;
        String queryDates = "SELECT " + Table.CHILD_TAX.ID + " FROM " + Table.Names.CHILD_TAX +
                " WHERE " + Table.CHILD_TAX.ID + " = ?" +
                " AND " + Table.CHILD_TAX.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CHILD_TAX.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CHILD_TAX.T_CREATEDON + " != " + Table.CHILD_TAX.T_UPDATEDON;
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

            update(idMax, "QST");

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
    public void testChildTax_22_updateDuplicate() throws Exception {
        LOG.debug("testChildTax_22_updateDuplicate");
        ResultSet rs = null;
        try {
            //https://en.wikipedia.org/wiki/Goods_and_services_tax_%28Canada%29
            Date startDate = Date.from(LocalDate.of(2006, Month.JULY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.of(2007, Month.DECEMBER, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
            long idJustInserted = insert("HST (2006)", "The harmonized sales tax", new BigDecimal("14.0"), "{num} * tax / 100", startDate, endDate);
            //"QST" duplicate update, 1st update in method  testChildTax_21_update
            //Unique index or primary key violation: "IDX_UNQ_TX_NM ON TEST.TAX(NAME)"
            update(idJustInserted, "QST");
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testChildTax_31_delete() throws Exception {
        LOG.debug("testChildTax_31_delete");
        String querySelect = "SELECT MIN(" + Table.CHILD_TAX.ID + ") FROM " + Table.Names.CHILD_TAX;
        String queryDelete = "DELETE FROM " + Table.Names.CHILD_TAX + " WHERE " +
                Table.CHILD_TAX.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = 'TAX')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.CHILD_TAX.ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[9];
        row[0] = 1L;
        row[1] = "name of a tax";
        row[2] = "desc of a tax";
        row[3] = new BigDecimal("1.0");
        row[4] = "formula";
        row[5] = SIMPLE_DATE_FORMAT.parse("2001-02-03");
        row[6] = SIMPLE_DATE_FORMAT.parse("2003-02-02");
        row[7] = SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06");
        row[8] = SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.CHILD_TAX.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.CHILD_TAX.NAME.toString(), (String) row[1]);
        rowJson.addProperty(Table.CHILD_TAX.DESCRIPTION.toString(), (String) row[2]);
        rowJson.addProperty(Table.CHILD_TAX.RATE.toString(), (BigDecimal) row[3]);
        rowJson.addProperty(Table.CHILD_TAX.FORMULA.toString(), (String) row[4]);
        rowJson.addProperty(Table.CHILD_TAX.STARTDATE.toString(), SIMPLE_DATE_FORMAT.format((Date) row[5]));
        rowJson.addProperty(Table.CHILD_TAX.ENDDATE.toString(), SIMPLE_DATE_FORMAT.format((Date) row[6]));
        rowJson.addProperty(Table.CHILD_TAX.T_CREATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[7]));
        rowJson.addProperty(Table.CHILD_TAX.T_UPDATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[8]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.CHILD_TAX);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.TAX\",\"row\":" +
                "{\"ID\":1,\"NAME\":\"name of a tax\",\"DESCRIPTION\":\"desc of a tax\",\"RATE\":1," +
                "\"FORMULA\":\"formula\",\"STARTDATE\":\"2001-02-03\",\"ENDDATE\":\"2003-02-02\"," +
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
        assertEquals("TEST.TAX", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.CHILD_TAX.ID.toString()).getAsLong();
        assertEquals(1L, id);
        String name = rowJsonObject.get(Table.CHILD_TAX.NAME.toString()).getAsString();
        assertEquals("name of a tax", name);
        JsonElement jsonElementDesc = rowJsonObject.get(Table.CHILD_TAX.DESCRIPTION.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElementDesc);
        assertEquals("desc of a tax", description);
        JsonElement jsonElementRate = rowJsonObject.get(Table.CHILD_TAX.RATE.toString());
        BigDecimal rate = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementRate);
        assertEquals(new BigDecimal("1.0"), rate);
        Date startDate = SIMPLE_DATE_FORMAT.parse(rowJsonObject.get(Table.CHILD_TAX.STARTDATE.toString()).getAsString());
        assertEquals(SIMPLE_DATE_FORMAT.parse("2001-02-03"), startDate);
        Date endDate = SIMPLE_DATE_FORMAT.parse(rowJsonObject.get(Table.CHILD_TAX.ENDDATE.toString()).getAsString());
        assertEquals(SIMPLE_DATE_FORMAT.parse("2003-02-02"), endDate);
        Date createdOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.CHILD_TAX.T_CREATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.CHILD_TAX.T_UPDATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01"), updatedOn);
    }
}