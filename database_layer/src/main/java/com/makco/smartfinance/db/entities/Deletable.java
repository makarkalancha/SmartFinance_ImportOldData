package com.makco.smartfinance.db.entities;

/**
 * Created by mcalancea on 2016-03-01.
 */
public interface Deletable {
    default boolean isDeleted(){
        return false;
    }

    void setIsDeleted(boolean isDeleted);
}
