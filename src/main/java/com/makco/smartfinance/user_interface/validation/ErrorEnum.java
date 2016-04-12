package com.makco.smartfinance.user_interface.validation;

/**
 * User: Makar Kalancha
 * Date: 08/04/2016
 * Time: 00:37
 */
public enum ErrorEnum {
    Cur_CODE_DUPLICATE("Currency with this code already exists")
    , Cur_CODE_NULL("Currency code cannot be empty.")
    , Cur_CODE_LGTH("Currency code length must be 3 characters.")
    , Cur_NAME_LGTH("Currency name cannot be greater than 64 characters.")
    , Cur_DESC_LGTH("Currency description cannot be greater than 128 characters.")
    , FM_NAME_DUPLICATE("Family Member with this name already exists")
    , FM_NAME_NULL("Family Member name cannot be empty.")
    , FM_NAME_LGTH("Family Member name cannot be greater than 64 characters.")
    , FM_DESC_LGTH("Family Member description cannot be greater than 128 characters.")
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
