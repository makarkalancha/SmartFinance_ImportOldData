package com.makco.smartfinance.h2db.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Makar Kalancha
 * Date: 25/03/2016
 * Time: 13:47
 */
public class JsonUtils {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";


    public static SimpleDateFormat getSimpleDateTimeFormat(){
        return new SimpleDateFormat(DATE_TIME_PATTERN);
    }

    public static SimpleDateFormat getSimpleDateFormat(){
        return new SimpleDateFormat(DATE_PATTERN);
    }

    public static String getStringOfNullableDate(Date date){
        return (date == null) ? null : new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    public static String getNullableFromJsonElementAsString(JsonElement jsonElement){
        return (jsonElement == JsonNull.INSTANCE) ? null : jsonElement.getAsString();
    }

    public static BigDecimal getNullableFromJsonElementAsBigDecimal(JsonElement jsonElement){
        return (jsonElement == JsonNull.INSTANCE) ? null : jsonElement.getAsBigDecimal();
    }
}
