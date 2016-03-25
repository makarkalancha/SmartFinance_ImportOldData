package com.makco.smartfinance.h2db.triggers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.tables.Table;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 25/03/2016
 * Time: 12:05
 */
public class TriggerFamilyMemberTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[5];
        row[0] = 1L;
        row[1] = "Fred";
        row[2] = null;
        row[3] = sdf.parse("2001-02-03 04:05:06");
        row[4] = sdf.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.FAMILY_MEMBER.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.FAMILY_MEMBER.NAME.toString(), (String) row[1]);
        rowJson.addProperty(Table.FAMILY_MEMBER.DESCRIPTION.toString(), (String) row[2]);
        rowJson.addProperty(Table.FAMILY_MEMBER.T_CREATEDON.toString(), sdf.format((Date) row[3]));
        rowJson.addProperty(Table.FAMILY_MEMBER.T_UPDATEDON.toString(), sdf.format((Date) row[4]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.FAMILY_MEMBER);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.FAMILY_MEMBER\",\"row\":{\"ID\":1,\"NAME\":\"Fred\",\"DESCRIPTION\":null,\"T_CREATEDON\":\"2001-02-03 04:05:06\",\"T_UPDATEDON\":\"2006-05-04 03:02:01\"}}";

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

        long id = rowJsonObject.get(Table.FAMILY_MEMBER.ID.toString()).getAsLong();
        assertEquals(1L, id);
        String name = rowJsonObject.get(Table.FAMILY_MEMBER.NAME.toString()).getAsString();
        assertEquals("Fred", name);
        JsonElement jsonElement = rowJsonObject.get(Table.FAMILY_MEMBER.DESCRIPTION.toString());
        String description = JsonUtils.getNullableFromJsonElementAsString(jsonElement);
        assertEquals(null, description);
        Date createdOn = sdf.parse(rowJsonObject.get(Table.FAMILY_MEMBER.T_CREATEDON.toString()).getAsString());
        assertEquals(sdf.parse("2001-02-03 04:05:06"), createdOn);
        Date updatedOn = sdf.parse(rowJsonObject.get(Table.FAMILY_MEMBER.T_UPDATEDON.toString()).getAsString());
        assertEquals(sdf.parse("2006-05-04 03:02:01"), updatedOn);
    }

}