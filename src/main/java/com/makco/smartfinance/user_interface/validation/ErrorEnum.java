package com.makco.smartfinance.user_interface.validation;

/**
 * User: Makar Kalancha
 * Date: 08/04/2016
 * Time: 00:37
 */
public enum ErrorEnum {
    FM_NAME_DUPLICATE("Family Member with this name already exists")
    , FM_NAME_NULL("Family Member name cannot be empty.")
    , MAIN_ERROR("Error while opening the application")
    ;
    String message;
    private ErrorEnum(String mess){
        this.message = mess;
    }

    public String getMessage(){
        return message;
    }
}
