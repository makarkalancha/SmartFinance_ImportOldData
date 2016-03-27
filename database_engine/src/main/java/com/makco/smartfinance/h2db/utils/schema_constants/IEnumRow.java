package com.makco.smartfinance.h2db.utils.schema_constants;

/**
 * User: Makar Kalancha
 * Date: 25/03/2016
 * Time: 12:26
 */
public interface IEnumRow {
    int getColumnIndex();
    <T> T getColumnNameByIndex();
}
