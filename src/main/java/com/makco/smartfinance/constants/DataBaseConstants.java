package com.makco.smartfinance.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class DataBaseConstants {
    private final static Logger LOG = LogManager.getLogger(DataBaseConstants.class);

    public final static String URL = "jdbc:h2:~/smart_finance/finance;IFEXISTS=TRUE";
    public final static String USERNAME = "client";
    public final static String PASSWORD = "qwerty1234";
//    public final static String USERNAME = "root";
//    public final static String PASSWORD = "root";
    public final static String SCHEMA = "FINANCE";
    public final static Integer BATCH_SIZE = 50;

    //family_memeber
    public final static int FM_NAME_MAX_LGTH = 32;
    public final static int FM_DESCRIPTION_MAX_LGTH = 512;

    //currency
    public final static int CUR_CODE_MAX_LGTH = 3;
    public final static int CUR_NAME_MAX_LGTH = 32;
    public final static int CUR_DESCRIPTION_MAX_LGTH = 512;

    //organization
    public final static int ORG_NAME_MAX_LGTH = 32;
    public final static int ORG_DESCRIPTION_MAX_LGTH = 512;

    //category_group
    public final static int CG_NAME_MAX_LGTH = 32;
    public final static int CG_DESCRIPTION_MAX_LGTH = 512;
    public static final String CATEGORY_GROUP_TYPE_DEBIT = "D";
    public static final String CATEGORY_GROUP_TYPE_CREDIT = "C";

    //category
    public final static int CAT_NAME_MAX_LGTH = 32;
    public final static int CAT_DESCRIPTION_MAX_LGTH = 512;

}
