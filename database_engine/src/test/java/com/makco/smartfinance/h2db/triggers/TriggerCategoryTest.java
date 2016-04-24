package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.TestContext;
import com.makco.smartfinance.h2db.utils.DBObjectType;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 24/04/2016
 * Time: 00:55
 */
public class TriggerCategoryTest {
    private static final Logger LOG = LogManager.getLogger(TriggerCategoryTest.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final SimpleDateFormat sdfJson = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerFamilyMemberTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerFamilyMemberTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerFamilyMemberTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerFamilyMemberTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Test
    public void testCRUDWithFamilyMemberTable() throws Exception {
        if(H2DbUtils.checkIfDBObjectExists(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA(),
                DBObjectType.TABLE, Table.Names.FAMILY_MEMBER.toString())){
            H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());

            testFamilyMemeber_insert();
            testFamilyMemeber_update();
            testFamilyMemeber_delete();

        } else {
            assert (false);
        }
    }

    //    @Test: junit doesn't support order in test (http://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4)
    public void testFamilyMemeber_insert() throws Exception {
        String queryInsert = "INSERT INTO " + Table.Names.FAMILY_MEMBER +
                " (" + Table.FAMILY_MEMBER.NAME + ", " + Table.FAMILY_MEMBER.DESCRIPTION + ") " +
                "VALUES('the Flintstones" + (sdf.format(new Date())) + "','family')";
        String queryDates = "SELECT " + Table.FAMILY_MEMBER.ID + " FROM " + Table.Names.FAMILY_MEMBER +
                " WHERE " + Table.FAMILY_MEMBER.ID + " = ?" +
                " AND " + Table.FAMILY_MEMBER.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.FAMILY_MEMBER.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.FAMILY_MEMBER.T_CREATEDON + " = " + Table.FAMILY_MEMBER.T_UPDATEDON;
        LOG.debug(queryInsert);
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
            PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            insertPS.executeUpdate();
            rs = insertPS.getGeneratedKeys();
            rs.next();
            long idJustInserted = rs.getLong(1);
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

    public void testFamilyMemeber_update() throws Exception {
        String querySelect = "SELECT MAX(" + Table.FAMILY_MEMBER.ID + ") FROM " + Table.Names.FAMILY_MEMBER;
        String queryUpdate = "UPDATE " + Table.Names.FAMILY_MEMBER + " SET " + Table.FAMILY_MEMBER.NAME + " = ? " +
                " WHERE " + Table.FAMILY_MEMBER.ID + " = ?";
        String queryDates = "SELECT " + Table.FAMILY_MEMBER.ID + " FROM " + Table.Names.FAMILY_MEMBER +
                " WHERE " + Table.FAMILY_MEMBER.ID + " = ?" +
                " AND " + Table.FAMILY_MEMBER.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.FAMILY_MEMBER.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.FAMILY_MEMBER.T_CREATEDON + " != " + Table.FAMILY_MEMBER.T_UPDATEDON;
        LOG.debug(querySelect);
        LOG.debug(queryUpdate);
        LOG.debug(queryDates);
        ResultSet rs = null;
        long idMax = 0L;
        long idNewMin = 0L;
        try (
                PreparedStatement selectPS = dbConnectionResource.getConnection().prepareStatement(querySelect);
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            rs = selectPS.executeQuery();
            rs.next();
            idMax = rs.getLong(1);

            updatePS.setString(1, "Barney" + (sdf.format(new Date())));
            updatePS.setLong(2, idMax);
            updatePS.executeUpdate();

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

    public void testFamilyMemeber_delete() throws Exception {
        String querySelect = "SELECT MIN(" + Table.FAMILY_MEMBER.ID + ") FROM " + Table.Names.FAMILY_MEMBER;
        String queryDelete = "DELETE FROM " + Table.Names.FAMILY_MEMBER + " WHERE " +
                Table.FAMILY_MEMBER.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = 'FAMILY_MEMBER')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.FAMILY_MEMBER.ID.toString()).getAsLong();
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
        rowJson.addProperty(Table.FAMILY_MEMBER.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.FAMILY_MEMBER.NAME.toString(), (String) row[1]);
        rowJson.addProperty(Table.FAMILY_MEMBER.DESCRIPTION.toString(), (String) row[2]);
        rowJson.addProperty(Table.FAMILY_MEMBER.T_CREATEDON.toString(), sdfJson.format((Date) row[3]));
        rowJson.addProperty(Table.FAMILY_MEMBER.T_UPDATEDON.toString(), sdfJson.format((Date) row[4]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.FAMILY_MEMBER);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.FAMILY_MEMBER\",\"row\":{\"ID\":1,\"NAME\":\"Fred\",\"DESCRIPTION\":null,\"T_CREATEDON\":\"2001-02-03 14:05:06\",\"T_UPDATEDON\":\"2006-05-04 03:02:01\"}}";

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
        assertEquals("TEST.FAMILY_MEMBER", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.FAMILY_MEMBER.ID.toString()).getAsLong();
        assertEquals(1L, id);
        String name = rowJsonObject.get(Table.FAMILY_MEMBER.NAME.toString()).getAsString();
        assertEquals("Fred", name);
        JsonElement jsonElement = rowJsonObject.get(Table.FAMILY_MEMBER.DESCRIPTION.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElement);
        assertEquals(null, description);
        Date createdOn = sdfJson.parse(rowJsonObject.get(Table.FAMILY_MEMBER.T_CREATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = sdfJson.parse(rowJsonObject.get(Table.FAMILY_MEMBER.T_UPDATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2006-05-04 03:02:01"), updatedOn);
    }

}