package com.makco.smartfinance.user_interface.validation;

/**
 * User: Makar Kalancha
 * Date: 08/04/2016
 * Time: 00:37
 */
public enum ErrorEnum {
    FM_NAME_NULL("Family Member <b>name</b> cannot be empty"),
    FM_NAME_DUPLICATE("Family Member with name <b>%s</b> already exists")


    ;
    String message;
    private ErrorEnum(String mess){
        this.message = mess;
    }

    public String getMessage() {
        return message;
    }
}
