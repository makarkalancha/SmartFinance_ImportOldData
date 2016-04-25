package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.utils.H2DbUtilsTest;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 24/04/2016
 * Time: 00:55
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableCategoryTest {
    private static final Logger LOG = LogManager.getLogger(TableCategoryTest.class);
    private static final SimpleDateFormat sdfJson = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String categoryGroupType = "C";
    private static Long categoryGroupIdMain;
    private static Long categoryGroupIdAnother;
    

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerCategoryTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.CATEGORY);
        categoryGroupIdMain = insertCategoryGroup(categoryGroupType, "Main", "main category group");
        categoryGroupIdAnother = insertCategoryGroup(categoryGroupType, "Another", "another category group");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerCategoryTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerCategoryTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerCategoryTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    public static Long insertCategoryGroup(String type, String name, String desc) throws Exception {
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
    
    public Long insertCategory(Long categoryGroupId, String type, String name, String desc) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.CATEGORY +
                " (" + Table.CATEGORY.CATEGORY_GROUP_ID + ", " + Table.CATEGORY.CATEGORY_GROUP_TYPE + 
                ", " + Table.CATEGORY.NAME + ", " + Table.CATEGORY.DESCRIPTION + ") " +
                "VALUES(" + categoryGroupId + ",'" + type + "','" + name + "','" + desc + "')";
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
    public void testCategory_11_insertCategory_MainCat1() throws Exception {
        LOG.debug("testCategory_11_insert");
        String queryDates = "SELECT " + Table.CATEGORY.ID + " FROM " + Table.Names.CATEGORY +
                " WHERE " + Table.CATEGORY.ID + " = ?" +
                " AND " + Table.CATEGORY.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_CREATEDON + " = " + Table.CATEGORY.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long idJustInserted = insertCategory(categoryGroupIdMain, categoryGroupType, "CG:Main->cat1", "category 1");
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
    public void testCategory_12_insertDuplicate() throws Exception {
        LOG.debug("testCategory_12_insertDuplicate");
        //Unique index or primary key violation: "IDX_UNQ_CTGR_CGIDNM ON TEST.CATEGORY(CATEGORY_GROUP_ID, NAME) VALUES (1, 'CG:Main->cat1', 1)"
        testCategory_11_insertCategory_MainCat1();
    }

    @Test
    public void testCategory_13_insertCategory_AnotherCat1() throws Exception {
        LOG.debug("testCategory_11_insert");
        String queryDates = "SELECT " + Table.CATEGORY.ID + " FROM " + Table.Names.CATEGORY +
                " WHERE " + Table.CATEGORY.ID + " = ?" +
                " AND " + Table.CATEGORY.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_CREATEDON + " = " + Table.CATEGORY.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long idJustInserted = insertCategory(categoryGroupIdAnother, categoryGroupType, "CG:Main->cat1", "category 1");
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

    //Referential integrity constraint violation: "CONSTRAINT_31: TEST.CATEGORY FOREIGN KEY(CATEGORY_GROUP_ID, CATEGORY_GROUP_TYPE) REFERENCES TEST.CATEGORY_GROUP(ID, TYPE) (1, 'D')"
    @Test(expected=JdbcSQLException.class)
    public void testCategory_14_insertCategory_DifferentType() throws Exception {
        LOG.debug("testCategory_11_insert");
        String queryDates = "SELECT " + Table.CATEGORY.ID + " FROM " + Table.Names.CATEGORY +
                " WHERE " + Table.CATEGORY.ID + " = ?" +
                " AND " + Table.CATEGORY.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_CREATEDON + " = " + Table.CATEGORY.T_UPDATEDON;
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
        ){
            long idJustInserted = insertCategory(categoryGroupIdMain, "D", "CG:Main->cat4", "category 4");
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

    public void update(Long id, String name) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.CATEGORY + " SET " + Table.CATEGORY.NAME + " = ? " +
                " WHERE " + Table.CATEGORY.ID + " = ?";
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
    public void testCategory_21_update() throws Exception {
        LOG.debug("testCategory_21_update");
        String querySelect =
                "SELECT MAX(" + Table.CATEGORY.ID + ") FROM " + Table.Names.CATEGORY + " WHERE " + Table.CATEGORY.CATEGORY_GROUP_ID + " = " +
                        categoryGroupIdMain;
        String queryDates = "SELECT " + Table.CATEGORY.ID + " FROM " + Table.Names.CATEGORY +
                " WHERE " + Table.CATEGORY.ID + " = ?" +
                " AND " + Table.CATEGORY.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.CATEGORY.T_CREATEDON + " != " + Table.CATEGORY.T_UPDATEDON;
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

            update(idMax, "cat1_v1");

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

    //Unique index or primary key violation: "IDX_UNQ_CTGR_CGIDNM ON TEST.CATEGORY(CATEGORY_GROUP_ID, NAME) VALUES (1, 'cat1_v1', 1)"
    @Test(expected=JdbcSQLException.class)
    public void testCategory_22_updateDuplicate() throws Exception {
        LOG.debug("testCategory_22_updateDuplicate");
        ResultSet rs = null;
        try {
            long idJustInserted = insertCategory(categoryGroupIdMain, categoryGroupType, "CG:Main->cat2", "category 2");
            //"cat1_v1" duplicate update, 1st update in method  testCategory_21_update
            update(idJustInserted, "cat1_v1");
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testCategory_23_update_sameName_AnotherCG() throws Exception {
        LOG.debug("testCategory_23_update_sameName_AnotherCG");
        ResultSet rs = null;
        try {
            long idJustInserted = insertCategory(categoryGroupIdAnother, categoryGroupType, "CG:Another->cat3", "category 3");
            //"cat1_v1" duplicate update, 1st update in method  testCategory_21_update
            update(idJustInserted, "cat1_v1");
        } finally {
            if (rs != null) rs.close();
        }
    }

//    @Test
    public void testCategory_31_delete() throws Exception {
        LOG.debug("testCategory_31_delete");
        String querySelect = "SELECT MIN(" + Table.CATEGORY.ID + ") FROM " + Table.Names.CATEGORY;
        String queryDelete = "DELETE FROM " + Table.Names.CATEGORY + " WHERE " +
                Table.CATEGORY.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = 'CATEGORY')";

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

            long idFromDeletedRows = rowJsonObject.get(Table.CATEGORY.ID.toString()).getAsLong();
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
        rowJson.addProperty(Table.CATEGORY.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.CATEGORY.NAME.toString(), (String) row[1]);
        rowJson.addProperty(Table.CATEGORY.DESCRIPTION.toString(), (String) row[2]);
        rowJson.addProperty(Table.CATEGORY.T_CREATEDON.toString(), sdfJson.format((Date) row[3]));
        rowJson.addProperty(Table.CATEGORY.T_UPDATEDON.toString(), sdfJson.format((Date) row[4]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.CATEGORY);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.CATEGORY\",\"row\":{\"ID\":1,\"NAME\":\"Fred\",\"DESCRIPTION\":null,\"T_CREATEDON\":\"2001-02-03 14:05:06\",\"T_UPDATEDON\":\"2006-05-04 03:02:01\"}}";

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
        assertEquals("TEST.CATEGORY", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.CATEGORY.ID.toString()).getAsLong();
        assertEquals(1L, id);
        String name = rowJsonObject.get(Table.CATEGORY.NAME.toString()).getAsString();
        assertEquals("Fred", name);
        JsonElement jsonElement = rowJsonObject.get(Table.CATEGORY.DESCRIPTION.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElement);
        assertEquals(null, description);
        Date createdOn = sdfJson.parse(rowJsonObject.get(Table.CATEGORY.T_CREATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = sdfJson.parse(rowJsonObject.get(Table.CATEGORY.T_UPDATEDON.toString()).getAsString());
        assertEquals(sdfJson.parse("2006-05-04 03:02:01"), updatedOn);
    }
}