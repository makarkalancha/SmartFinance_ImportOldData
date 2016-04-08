package com.makco.smartfinance.utils;

/**
 * User: Makar Kalancha
 * Date: 08/04/2016
 * Time: 00:37
 */
public enum Errors {
    FM_NAME_NULL("Family Member name cannot be empty"),
    FM_NAME_DUPLICATE("Family Member with name %s already exists")


    ;
    String message;
    private Errors(String mess){
        this.message = mess;
    }
}
