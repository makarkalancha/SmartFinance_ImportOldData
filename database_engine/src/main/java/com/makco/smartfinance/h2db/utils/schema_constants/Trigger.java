package com.makco.smartfinance.h2db.utils.schema_constants;

/**
 * User: Makar Kalancha
 * Date: 27/03/2016
 * Time: 01:21
 */
public class Trigger {
    public enum CURRENCY{
        T_CURRENCY_INS,
        T_CURRENCY_UPD,
        T_CURRENCY_DEL;
    };

    public enum DATEUNIT{
        T_DATEUNIT_INS;
    };

    public enum FAMILY_MEMBER{
        T_FAMILY_MEMBER_INS,
        T_FAMILY_MEMBER_UPD,
        T_FAMILY_MEMBER_DEL;
    };
}
