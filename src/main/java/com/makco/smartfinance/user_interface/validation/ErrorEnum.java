package com.makco.smartfinance.user_interface.validation;

import com.makco.smartfinance.constants.DataBaseConstants;

/**
 * User: Makar Kalancha
 * Date: 08/04/2016
 * Time: 00:37
 */
public enum ErrorEnum {
    Cat_DESC_LGTH("Category description cannot be greater than " + DataBaseConstants.CAT_DESCRIPTION_MAX_LGTH + " characters.")
    , Cat_NAME_DUPLICATE("Category with this name and type already exists.")
    , Cat_NAME_LGTH("Category name cannot be greater than " + DataBaseConstants.CAT_NAME_MAX_LGTH + " characters.")
    , Cat_NULL_NAME("Category name cannot be empty.")
    , CatGr_DESC_LGTH("Category Group description cannot be greater than " + DataBaseConstants.CG_DESCRIPTION_MAX_LGTH + " characters.")
    , CatGr_NAME_DUPLICATE("Category Group with this name already exists.")
    , CatGr_NAME_LGTH("Category Group name cannot be greater than " + DataBaseConstants.CG_NAME_MAX_LGTH + " characters.")
    , CatGr_NULL_NAME("Category Group name cannot be empty.")
    , CatGr_NULL_CG_TYPE("Category Group type cannot be empty.")
    , Cur_CODE_DUPLICATE("Currency with this code already exists.")
    , Cur_DESC_LGTH("Currency description cannot be greater than " + DataBaseConstants.CUR_DESCRIPTION_MAX_LGTH + " characters.")
    , Cur_CODE_LGTH("Currency code length must be " + DataBaseConstants.CUR_CODE_MAX_LGTH + " characters.")
    , Cur_CODE_NULL("Currency code cannot be empty.")
    , Cur_NAME_LGTH("Currency name cannot be greater than " + DataBaseConstants.CUR_NAME_MAX_LGTH + " characters.")
    , FM_DESC_LGTH("Family Member description cannot be greater than " + DataBaseConstants.FM_DESCRIPTION_MAX_LGTH + " characters.")
    , FM_NAME_DUPLICATE("Family Member with this name already exists.")
    , FM_NAME_LGTH("Family Member name cannot be greater than " + DataBaseConstants.FM_NAME_MAX_LGTH + " characters.")
    , FM_NAME_NULL("Family Member name cannot be empty.")
    , MAIN_ERROR("Error while opening the application")
    , ORG_DESC_LGTH("Organization description cannot be greater than " + DataBaseConstants.ORG_DESCRIPTION_MAX_LGTH + " characters.")
    , ORG_NAME_DUPLICATE("Organization with this name already exists.")
    , ORG_NAME_LGTH("Organization name cannot be greater than " + DataBaseConstants.ORG_NAME_MAX_LGTH + " characters.")
    , ORG_NAME_NULL("Organization name cannot be empty.")

    ;
    String message;
    private ErrorEnum(String mess){
        this.message = mess;
    }

    public String getMessage(){
        return message;
    }
}
