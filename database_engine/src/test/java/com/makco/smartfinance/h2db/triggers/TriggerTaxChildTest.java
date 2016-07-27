package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.utils.H2DbUtilsTest;
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
public class TriggerTaxChildTest {
    private static final Logger LOG = LogManager.getLogger(TriggerTaxChildTest.class);
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerTaxChildTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TAX_CHILD);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TAX);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerTaxChildTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerTaxChildTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerTaxChildTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    
    public void insert(Long taxParend, Long taxChild) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.TAX_CHILD +
                " (" + Table.TAX_CHILD.TAX_ID + ", " + Table.TAX_CHILD.CHILD_TAX_ID + ") " +
                "VALUES(" + taxParend + "," + taxChild + ")";
        LOG.debug(queryInsert);
        try (
                PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(queryInsert);
        ){
            insertPS.executeUpdate();
        }
    }

    @Test
    public void testTaxChild_11_insert() throws Exception {
        LOG.debug("testTaxChild_11_insert");
        String queryDates = "SELECT " + Table.TAX_CHILD.TAX_ID + " FROM " + Table.Names.TAX_CHILD +
                " WHERE " + Table.TAX_CHILD.TAX_ID + " = ?" +
                " AND " + Table.TAX_CHILD.T_CREATEDON + " IS NOT NULL";
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            //https://en.wikipedia.org/wiki/Goods_and_services_tax_%28Canada%29
            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            TriggerTaxTest triggerTaxTest = new TriggerTaxTest();
            long idTax1 = triggerTaxTest.insert("GST1 (2008)", "#1 the goods and services tax", new BigDecimal("5.0"), "{num} * tax / 100", startDate, null);
            long idTax2 = triggerTaxTest.insert("GST2 (2008)", "#2 the goods and services tax", new BigDecimal("7.0"), "{num} * tax / 100", startDate, null);

            insert(idTax1, idTax2);
//            LOG.debug("idJustInserted > 0: idJustInserted=" + idJustInserted);
//            assert (idJustInserted > 0);
            selectDatesPS.setLong(1, idTax1);
            rs = selectDatesPS.executeQuery();
            rs.next();
            long idWithDates = rs.getLong(1);
            LOG.debug("idWithDates > 0: idWithDates=" + idWithDates);
            LOG.debug("idTax1 == idWithDates: " + (idTax1 == idWithDates) +
                    "; idTax1=" + idTax1 +
                    "; idWithDates=" + idWithDates);
            assert (idWithDates > 0);
            assert (idTax1 == idWithDates);
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test (expected=JdbcSQLException.class)
    public void testTaxChild_12_insertDuplicate() throws Exception {
        LOG.debug("testTaxChild_12_insertDuplicate");
        //Unique index or primary key violation: "PRIMARY_KEY_DB ON TEST.TAX_CHILD(TAX_ID, CHILD_TAX_ID) VALUES (10, 11, 2)"
        String queryDates = "SELECT " + Table.TAX_CHILD.TAX_ID + " FROM " + Table.Names.TAX_CHILD +
                " WHERE " + Table.TAX_CHILD.TAX_ID + " = ?" +
                " AND " + Table.TAX_CHILD.T_CREATEDON + " IS NOT NULL";
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            //https://en.wikipedia.org/wiki/Goods_and_services_tax_%28Canada%29
            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            TriggerTaxTest triggerTaxTest = new TriggerTaxTest();
            long idTax1 = triggerTaxTest.insert("GST3 (2008)", "#3 the goods and services tax", new BigDecimal("5.0"), "{num} * tax / 100", startDate, null);
            long idTax2 = triggerTaxTest.insert("GST4 (2008)", "#4 the goods and services tax", new BigDecimal("7.0"), "{num} * tax / 100", startDate, null);

            insert(idTax1, idTax2);
            insert(idTax1, idTax2);
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testTaxChild_31_delete() throws Exception {
        LOG.debug("testTaxChild_31_delete");
        String querySelect = "SELECT MIN(" + Table.TAX_CHILD.TAX_ID + ") FROM " + Table.Names.TAX_CHILD;
        String queryDelete = "DELETE FROM " + Table.Names.TAX_CHILD + " WHERE " +
                Table.TAX_CHILD.TAX_ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = '" + Table.Names.TAX_CHILD + "')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.TAX_CHILD.TAX_ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[Table.TAX_CHILD.values().length];
        row[0] = 1L;
        row[1] = 2L;
        row[2] = SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.TAX_CHILD.TAX_ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.TAX_CHILD.CHILD_TAX_ID.toString(), (Long) row[1]);
        rowJson.addProperty(Table.TAX_CHILD.T_CREATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[2]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.TAX_CHILD);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.TAX_CHILD\",\"row\":" +
                "{\"TAX_ID\":1,\"CHILD_TAX_ID\":2,\"T_CREATEDON\":\"2001-02-03 14:05:06\"}}";

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
        assertEquals("TEST.TAX_CHILD", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.TAX_CHILD.TAX_ID.toString()).getAsLong();
        assertEquals(1L, id);
        long childId = rowJsonObject.get(Table.TAX_CHILD.CHILD_TAX_ID.toString()).getAsLong();
        assertEquals(2L, childId);
        Date createdOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.TAX_CHILD.T_CREATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06"), createdOn);
    }
}