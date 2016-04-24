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
public class TableOrganizationTest {
    private static final Logger LOG = LogManager.getLogger(TableOrganizationTest.class);
    private static final SimpleDateFormat sdfJson = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerOrganizationTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ORGANIZATION);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerOrganizationTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerOrganizationTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerOrganizationTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    public Long insert(String name, String desc) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.ORGANIZATION +
                " (" + Table.ORGANIZATION.NAME + ", " + Table.ORGANIZATION.DESCRIPTION + ") " +
                "VALUES('" + name + "','" + desc + "')";
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
    public void testOrganization_11_insert() throws Exception {
        LOG.debug("testOrganization_11_insert");
        String queryDates = "SELECT " + Table.ORGANIZATION.ID + " FROM " + Table.Names.ORGANIZATION +
                " WHERE " + Table.ORGANIZATION.ID + " = ?" +
                " AND " + Table.ORGANIZATION.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.ORGANIZATION.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.ORGANIZATION.T_CREATEDON + " = " + Table.ORGANIZATION.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long idJustInserted = insert("Walmart", "shop");
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
    public void testOrganization_12_insertDuplicate() throws Exception {
        LOG.debug("testOrganization_12_insertDuplicate");
        //Unique index or primary key violation: "IDX_UNQ_ORGNZTN_NM ON TEST.ORGANIZATION(NAME) VALUES ('Walmart', 1)"
        testOrganization_11_insert();
    }

    public void update(Long id, String name) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.ORGANIZATION + " SET " + Table.ORGANIZATION.NAME + " = ? " +
                " WHERE " + Table.ORGANIZATION.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setString(1, name);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testOrganization_21_update() throws Exception {
        LOG.debug("testOrganization_21_update");
        String querySelect = "SELECT MAX(" + Table.ORGANIZATION.ID + ") FROM " + Table.Names.ORGANIZATION;
        String queryDates = "SELECT " + Table.ORGANIZATION.ID + " FROM " + Table.Names.ORGANIZATION +
                " WHERE " + Table.ORGANIZATION.ID + " = ?" +
                " AND " + Table.ORGANIZATION.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.ORGANIZATION.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.ORGANIZATION.T_CREATEDON + " != " + Table.ORGANIZATION.T_UPDATEDON;
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

            update(idMax, "IGA");

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
    public void testOrganization_22_updateDuplicate() throws Exception {
        LOG.debug("testOrganization_22_updateDuplicate");
        String queryDates = "SELECT " + Table.ORGANIZATION.ID + " FROM " + Table.Names.ORGANIZATION +
                " WHERE " + Table.ORGANIZATION.ID + " = ?" +
                " AND " + Table.ORGANIZATION.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.ORGANIZATION.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.ORGANIZATION.T_CREATEDON + " != " + Table.ORGANIZATION.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try {
            long idJustInserted = insert("Loblaw''s", "shop");
            //"IGA" duplicate update, 1st update in method  testOrganization_21_update
            //Unique index or primary key violation: "IDX_UNQ_ORGNZTN_NM ON TEST.ORGANIZATION(NAME) VALUES ('IGA', 1)"
            update(idJustInserted, "IGA");
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testOrganization_31_delete() throws Exception {
        String querySelect = "SELECT MIN(" + Table.ORGANIZATION.ID + ") FROM " + Table.Names.ORGANIZATION;
        String queryDelete = "DELETE FROM " + Table.Names.ORGANIZATION + " WHERE " +
                Table.ORGANIZATION.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = 'ORGANIZATION')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.ORGANIZATION.ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[5];
        row[0] = 1L;
        row[1] = "Fred";
        row[2] = null;
        row[3] = sdfJson.parse("2001-02-03 14:05:06");
        row[4] = sdfJson.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.ORGANIZATION.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.ORGANIZATION.NAME.toString(), (String) row[1]);
        rowJson.addProperty(Table.ORGANIZATION.DESCRIPTION.toString(), (String) row[2]);
        rowJson.addProperty(Table.ORGANIZATION.T_CREATEDON.toString(), sdfJson.format((Date) row[3]));
        rowJson.addProperty(Table.ORGANIZATION.T_UPDATEDON.toString(), sdfJson.format((Date) row[4]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.ORGANIZATION);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.ORGANIZATION\",\"row\":{\"ID\":1,\"NAME\":\"Fred\",\"DESCRIPTION\":null,\"T_CREATEDON\":\"2001-02-03 14:05:06\",\"T_UPDATEDON\":\"2006-05-04 03:02:01\"}}";

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
        assertEquals("TEST.ORGANIZATION", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.ORGANIZATION.ID.toString()).getAsLong();
        assertEquals(1L, id);
        String name = rowJsonObject.get(Table.ORGANIZATION.NAME.toString()).getAsString();
        assertEquals("Fred", name);
        JsonElement jsonElement = rowJsonObject.get(Table.ORGANIZATION.DESCRIPTION.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElement);
        assertEquals(null, description);
        Date createdOn = sdfJson.parse(rowJsonObject.get(Table.ORGANIZATION.T_CREATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = sdfJson.parse(rowJsonObject.get(Table.ORGANIZATION.T_UPDATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2006-05-04 03:02:01"), updatedOn);
    }

}