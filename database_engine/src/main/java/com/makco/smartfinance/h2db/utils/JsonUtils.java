package com.makco.smartfinance.h2db.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import java.text.SimpleDateFormat;

/**
 * User: Makar Kalancha
 * Date: 25/03/2016
 * Time: 13:47
 */
public class JsonUtils {

    public static SimpleDateFormat getSimpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String getNullableFromJsonElementAsString(JsonElement jsonElement){
        return (jsonElement == JsonNull.INSTANCE) ? null : jsonElement.getAsString();
    }
}
