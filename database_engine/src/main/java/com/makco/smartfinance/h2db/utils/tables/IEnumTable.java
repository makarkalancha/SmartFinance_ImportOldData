package com.makco.smartfinance.h2db.utils.tables;

/**
 * User: Makar Kalancha
 * Date: 25/03/2016
 * Time: 12:26
 */
public interface IEnumTable {
    int getColumnIndex();
    <T> T getColumnNameByIndex();
    boolean isNullable();
}
