package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 24/04/2016
 * Time: 00:54
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableCategoryGroupTest {
    private static final Logger LOG = LogManager.getLogger(TableCategoryGroupTest.class);
    private static final SimpleDateFormat sdfJson = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerCategoryGroupTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
//        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.CATEGORY_GROUP);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerCategoryGroupTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerCategoryGroupTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerCategoryGroupTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    public Long insert(String type, String name, String desc) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.CATEGORY_GROUP +
                " (" + Table.CATEGORY_GROUP.TYPE + ", " + Table.CATEGORY_GROUP.NAME + ", " + Table.CATEGORY_GROUP.DESCRIPTION + ") " +
                "VALUES('" + type + "','" + name + "','" + desc + "')";
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
    public void testCategoryGroup_11_insert() throws Exception {
        LOG.debug("testCategoryGroup_11_insert");
        String queryDates = "SELECT " + Table.CATEGORY_GROUP.ID + " FROM " + Table.Names.CATEGORY_GROUP +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " = " + Table.CATEGORY_GROUP.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long idJustInserted = insert("D", "income", "income from different resources");
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

    @Test
    public void testCategoryGroup_12_insertDuplicate_Name() throws Exception {
        LOG.debug("testCategoryGroup_12_insertDuplicate_Name");
        String queryDates = "SELECT " + Table.CATEGORY_GROUP.ID + " FROM " + Table.Names.CATEGORY_GROUP +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " = " + Table.CATEGORY_GROUP.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long idJustInserted = insert("C", "income", "income from different resources");
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

    @Test
    public void testCategoryGroup_13_insertDuplicate_Type() throws Exception {
        LOG.debug("testCategoryGroup_13_insertDuplicate_Type");
        String queryDates = "SELECT " + Table.CATEGORY_GROUP.ID + " FROM " + Table.Names.CATEGORY_GROUP +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " = " + Table.CATEGORY_GROUP.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long idJustInserted = insert("D", "gift", null);
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
    public void testCategoryGroup_14_insertDuplicate_NameAndType() throws Exception {
        LOG.debug("testCategoryGroup_14_insertDuplicate_NameAndType");
        //Unique index or primary key violation: "IDX_UNQ_CTGRGRP_TPNM ON TEST.CATEGORY_GROUP(TYPE, NAME) VALUES ('D', 'income', 7)"
        testCategoryGroup_11_insert();
    }

    public void update(Long id, String type, String name) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.CATEGORY_GROUP + " SET " +
                Table.CATEGORY_GROUP.TYPE + " = ?, " +
                Table.CATEGORY_GROUP.NAME + " = ? " +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setString(1, type);
            updatePS.setString(2, name);
            updatePS.setLong(3, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testCategoryGroup_21_update() throws Exception {
        LOG.debug("testCategoryGroup_21_update");
        String querySelect = "SELECT MAX(" + Table.CATEGORY_GROUP.ID + ") FROM " + Table.Names.CATEGORY_GROUP;
        String queryDates = "SELECT " + Table.CATEGORY_GROUP.ID + " FROM " + Table.Names.CATEGORY_GROUP +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " != " + Table.CATEGORY_GROUP.T_UPDATEDON;
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

            update(idMax, "C", "personal charges");

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
    public void testCategoryGroup_22_update_Name() throws Exception {
        LOG.debug("testCategoryGroup_22_update_Name");
        String querySelect = "SELECT MAX(" + Table.CATEGORY_GROUP.ID + ") FROM " + Table.Names.CATEGORY_GROUP;
        String queryDates = "SELECT " + Table.CATEGORY_GROUP.ID + " FROM " + Table.Names.CATEGORY_GROUP +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " != " + Table.CATEGORY_GROUP.T_UPDATEDON;
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

            update(idMax, "D", "personal charges");

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
    public void testCategoryGroup_23_update_Type() throws Exception {
        LOG.debug("testCategoryGroup_23_update_Type");
        String querySelect = "SELECT MAX(" + Table.CATEGORY_GROUP.ID + ") FROM " + Table.Names.CATEGORY_GROUP;
        String queryDates = "SELECT " + Table.CATEGORY_GROUP.ID + " FROM " + Table.Names.CATEGORY_GROUP +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " != " + Table.CATEGORY_GROUP.T_UPDATEDON;
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

            update(idMax, "C", "house");

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

    //Unique index or primary key violation: "IDX_UNQ_CTGRGRP_TPNM ON TEST.CATEGORY_GROUP(TYPE, NAME) VALUES ('C', 'house', 9)"
    @Test(expected=JdbcSQLException.class)
    public void testCategoryGroup_24_updateDuplicate_NameAndType() throws Exception {
        LOG.debug("testCategoryGroup_24_updateDuplicate_NameAndType");
        String queryDates = "SELECT " + Table.CATEGORY_GROUP.ID + " FROM " + Table.Names.CATEGORY_GROUP +
                " WHERE " + Table.CATEGORY_GROUP.ID + " = ?" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY_GROUP.T_CREATEDON + " != " + Table.CATEGORY_GROUP.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try {
            long idJustInserted = insert("C", "transport","bus, taxi");
            //"C","house" duplicate update, 24th update in method  testCategoryGroup_3_update
            update(idJustInserted, "C","house");
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testCategoryGroup_35_delete() throws Exception {
        LOG.debug("testCategoryGroup_35_delete");
        String querySelect = "SELECT MIN(" + Table.CATEGORY_GROUP.ID + ") FROM " + Table.Names.CATEGORY_GROUP;
        String queryDelete = "DELETE FROM " + Table.Names.CATEGORY_GROUP + " WHERE " +
                Table.CATEGORY_GROUP.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = 'CATEGORY_GROUP')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.CATEGORY_GROUP.ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[6];
        row[0] = 1L;
        row[1] = "C";
        row[2] = "health";
        row[3] = null;
        row[4] = sdfJson.parse("2001-02-03 14:05:06");
        row[5] = sdfJson.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.CATEGORY_GROUP.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.CATEGORY_GROUP.TYPE.toString(), (String) row[1]);
        rowJson.addProperty(Table.CATEGORY_GROUP.NAME.toString(), (String) row[2]);
        rowJson.addProperty(Table.CATEGORY_GROUP.DESCRIPTION.toString(), (String) row[3]);
        rowJson.addProperty(Table.CATEGORY_GROUP.T_CREATEDON.toString(), sdfJson.format((Date) row[4]));
        rowJson.addProperty(Table.CATEGORY_GROUP.T_UPDATEDON.toString(), sdfJson.format((Date) row[5]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.CATEGORY_GROUP);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.CATEGORY_GROUP\",\"row\":{\"ID\":1,\"TYPE\":\"C\",\"NAME\":\"health\",\"DESCRIPTION\":null,\"T_CREATEDON\":\"2001-02-03 14:05:06\",\"T_UPDATEDON\":\"2006-05-04 03:02:01\"}}";

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
        assertEquals("TEST.CATEGORY_GROUP", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.CATEGORY_GROUP.ID.toString()).getAsLong();
        assertEquals(1L, id);
        String name = rowJsonObject.get(Table.CATEGORY_GROUP.NAME.toString()).getAsString();
        assertEquals("health", name);
        JsonElement jsonElement = rowJsonObject.get(Table.CATEGORY_GROUP.DESCRIPTION.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElement);
        assertEquals(null, description);
        Date createdOn = sdfJson.parse(rowJsonObject.get(Table.CATEGORY_GROUP.T_CREATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = sdfJson.parse(rowJsonObject.get(Table.CATEGORY_GROUP.T_UPDATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2006-05-04 03:02:01"), updatedOn);
    }
}