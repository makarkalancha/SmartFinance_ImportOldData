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

import static org.junit.Assert.*;

/**
 * Created by Makar Kalancha on 11 Jul 2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableItemTest {
    private static final Logger LOG = LogManager.getLogger(TableItemTest.class);
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String ORGANIZATION_NAME_PREFIX = "Organization From TableItemTest #";
    private static final String ORGANIZATION_DESC = "Organization DESC";

    private TableOrganizationTest tableOrganizationTest = new TableOrganizationTest();
    private TableInvoiceTest tableInvoiceTest = new TableInvoiceTest();
    private TableCategoryGroupTest tableCategoryGroupTest = new TableCategoryGroupTest();
    private TableCategoryTest tableCategoryTest = new TableCategoryTest();
    private TableTaxTest tableTaxTest = new TableTaxTest();
    private TableFamilyMemberTest tableFamilyMemberTest = new TableFamilyMemberTest();

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerItemTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ITEM);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ORGANIZATION);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.FAMILY_MEMBER);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TAX);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.CATEGORY);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.CATEGORY_GROUP);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerItemTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerItemTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerItemTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    public Long insert(Long invoiceId, Long categoryId, Long taxId,
                       Long familyMemberId, String description1, String description2,
                       String comment, BigDecimal grossAmount, BigDecimal netAmount) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.ITEM +
                " (" + Table.ITEM.INVOICE_ID + ", " + Table.ITEM.CATEGORY_ID + ", " + Table.ITEM.TAX_ID + ", " +
                Table.ITEM.FAMILY_MEMBER_ID + ", " + Table.ITEM.DESCRIPTION1 + ", " + Table.ITEM.DESCRIPTION2 + ", " +
                Table.ITEM.COMMENT + ", " + Table.ITEM.GROSS_AMOUNT + ", " + Table.ITEM.NET_AMOUNT + ") " +
                "VALUES(" + invoiceId + ", " + categoryId + ", " + taxId + ", " +
                familyMemberId + ", '" + description1 + "', '" + description2 + "', '" + comment + "', " +
                grossAmount + ", " + netAmount + ")";

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
    public void testItem_11_insert() throws Exception {
        LOG.debug("testItem_11_insert");
        String queryDates = "SELECT " + Table.ITEM.ID + " FROM " + Table.Names.ITEM +
                " WHERE " + Table.ITEM.ID + " = ?" +
                " AND " + Table.ITEM.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_CREATEDON + " = " + Table.ITEM.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long organizationId = tableOrganizationTest.insert("Organization TableItemTest", "Organization Description From TableItemTest #testItem_11_insert");
            long invoiceId = tableInvoiceTest.insert(organizationId, "invoice comment", new BigDecimal("3"));

            long categoryGroupId = tableCategoryGroupTest.insert("D", "debit category group", "debit category group desc");
            long categoryId = tableCategoryTest.insertCategory(categoryGroupId, "D", "debit category", "debit category desc");

            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.of(2010, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            long taxId = tableTaxTest.insert("tax name", "tax desc", new BigDecimal("10"), "1+2", startDate, endDate);

            long familyMemberId = tableFamilyMemberTest.insert("family member #1", "family member desc");

            long idJustInserted = insert(invoiceId, categoryId, taxId, familyMemberId,
                    "product desc1", "product desc2", "comment",
                    new BigDecimal("5.0"), new BigDecimal("15.0"));
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
        String queryUpdate = "UPDATE " + Table.Names.ITEM + " SET " + Table.ITEM.COMMENT + " = ? " +
                " WHERE " + Table.ITEM.ID + " = ?";
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
    public void testItem_21_update() throws Exception {
        LOG.debug("testItem_21_update");
        String querySelect = "SELECT MAX(" + Table.ITEM.ID + ") FROM " + Table.Names.ITEM;
        String queryDates = "SELECT " + Table.ITEM.ID + " FROM " + Table.Names.ITEM +
                " WHERE " + Table.ITEM.ID + " = ?" +
                " AND " + Table.ITEM.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_CREATEDON + " != " + Table.ITEM.T_UPDATEDON;
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
    public void testItem_31_delete() throws Exception {
        LOG.debug("testItem_31_delete");
        String querySelect = "SELECT MIN(" + Table.ITEM.ID + ") FROM " + Table.Names.ITEM;
        String queryDelete = "DELETE FROM " + Table.Names.ITEM + " WHERE " +
                Table.ITEM.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = '" + Table.Names.ITEM + "')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.ITEM.ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[12];
        row[0] = 1L;
        row[1] = 2L;
        row[2] = 3L;
        row[3] = 4L;
        row[4] = 5L;
        row[5] = "desc1";
        row[6] = "desc2";
        row[7] = "comm";
        row[8] = new BigDecimal("2.0");
        row[9] = new BigDecimal("4.0");
        row[10] = SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06");
        row[11] = SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.ITEM.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.ITEM.INVOICE_ID.toString(), (Long) row[1]);
        rowJson.addProperty(Table.ITEM.CATEGORY_ID.toString(), (Long) row[2]);
        rowJson.addProperty(Table.ITEM.TAX_ID.toString(), (Long) row[3]);
        rowJson.addProperty(Table.ITEM.FAMILY_MEMBER_ID.toString(), (Long) row[4]);
        rowJson.addProperty(Table.ITEM.DESCRIPTION1.toString(), (String) row[5]);
        rowJson.addProperty(Table.ITEM.DESCRIPTION2.toString(), (String) row[6]);
        rowJson.addProperty(Table.ITEM.COMMENT.toString(), (String) row[7]);
        rowJson.addProperty(Table.ITEM.GROSS_AMOUNT.toString(), (BigDecimal) row[8]);
        rowJson.addProperty(Table.ITEM.NET_AMOUNT.toString(), (BigDecimal) row[9]);
        rowJson.addProperty(Table.ITEM.T_CREATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[10]));
        rowJson.addProperty(Table.ITEM.T_UPDATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[11]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.ITEM);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.ITEM\",\"row\":" +
                "{\"ID\":1,\"INVOICE_ID\":2,\"CATEGORY_ID\":3,\"TAX_ID\":4," +
                "\"FAMILY_MEMBER_ID\":5,\"DESCRIPTION1\":\"desc1\",\"DESCRIPTION2\":\"desc2\"," +
                "\"COMMENT\":\"comm\",\"GROSS_AMOUNT\":2.0,\"NET_AMOUNT\":4.0," +
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
        assertEquals("TEST.ITEM", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.ITEM.ID.toString()).getAsLong();
        assertEquals(1L, id);
        long invoiceId = rowJsonObject.get(Table.ITEM.INVOICE_ID.toString()).getAsLong();
        assertEquals(2L, invoiceId);
        long categoryId = rowJsonObject.get(Table.ITEM.CATEGORY_ID.toString()).getAsLong();
        assertEquals(3L, categoryId);
        long taxId = rowJsonObject.get(Table.ITEM.TAX_ID.toString()).getAsLong();
        assertEquals(4L, taxId);
        long familyMemberId = rowJsonObject.get(Table.ITEM.FAMILY_MEMBER_ID.toString()).getAsLong();
        assertEquals(5L, familyMemberId);

        JsonElement jsonElementDesc1 = rowJsonObject.get(Table.ITEM.DESCRIPTION1.toString());
        String description1 = JsonUtils.getNullableFromJsonElementAsString(jsonElementDesc1);
        assertEquals("desc1", description1);
        JsonElement jsonElementDesc2 = rowJsonObject.get(Table.ITEM.DESCRIPTION2.toString());
        String description2 = JsonUtils.getNullableFromJsonElementAsString(jsonElementDesc2);
        assertEquals("desc2", description2);
        JsonElement jsonElementComm = rowJsonObject.get(Table.ITEM.COMMENT.toString());
        String comment = JsonUtils.getNullableFromJsonElementAsString(jsonElementComm);
        assertEquals("comm", comment);

        JsonElement jsonElementGrAmt = rowJsonObject.get(Table.ITEM.GROSS_AMOUNT.toString());
        BigDecimal grAmt = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementGrAmt);
        assertEquals(new BigDecimal("2.0"), grAmt);
        JsonElement jsonElementNtAmt = rowJsonObject.get(Table.ITEM.NET_AMOUNT.toString());
        BigDecimal ntAmt = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementNtAmt);
        assertEquals(new BigDecimal("4.0"), ntAmt);
        Date createdOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.ITEM.T_CREATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.ITEM.T_UPDATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01"), updatedOn);
    }
}