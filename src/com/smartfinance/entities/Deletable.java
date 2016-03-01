package com.smartfinance.entities;

/**
 * Created by mcalancea on 2016-03-01.
 */
public interface Deletable {
    default boolean isDeleted(){
        return false;
    }
}
