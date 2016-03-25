package com.makco.smartfinance.h2db.utils.tables;

import com.google.gson.JsonObject;

/**
 * User: Makar Kalancha
 * Date: 25/03/2016
 * Time: 12:26
 */
public interface IEnumRow {
    int getColumnIndex();
    <T> T getColumnNameByIndex();
}
