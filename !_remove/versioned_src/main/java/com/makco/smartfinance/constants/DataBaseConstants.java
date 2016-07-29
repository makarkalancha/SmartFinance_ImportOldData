package com.makco.smartfinance.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Makar Kalancha on 2016-04-05.
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
    public enum CATEGORY_GROUP_TYPE{
        CREDIT(Values.CREDIT),
        DEBIT(Values.DEBIT);

        private final String discriminator;
        private CATEGORY_GROUP_TYPE(String discriminator){
            this.discriminator = discriminator;
        }

        public String getDiscriminator() {
            return discriminator;
        }

        public static class Values{
            public static final String DEBIT = "D";
            public static final String CREDIT = "C";
        }
    }

    //category
    public final static int CAT_NAME_MAX_LGTH = 32;
    public final static int CAT_DESCRIPTION_MAX_LGTH = 512;

    //account_group
    public final static int AG_NAME_MAX_LGTH = 32;
    public final static int AG_DESCRIPTION_MAX_LGTH = 512;
    public enum ACCOUNT_GROUP_TYPE{
        CREDIT(Values.CREDIT),
        DEBIT(Values.DEBIT);

        private final String discriminator;
        private ACCOUNT_GROUP_TYPE(String discriminator){
            this.discriminator = discriminator;
        }

        public String getDiscriminator() {
            return discriminator;
        }

        public static class Values{
            public static final String DEBIT = "D";
            public static final String CREDIT = "C";
        }
    }

    //account
    public final static int ACC_NAME_MAX_LGTH = 32;
    public final static int ACC_DESCRIPTION_MAX_LGTH = 512;

    //tax
    public final static int TAX_NAME_MAX_LGTH = 32;
    public final static int TAX_DESCRIPTION_MAX_LGTH = 512;
    public final static int TAX_FORMULA_MAX_LGTH = 512;
    public final static int TAX_DENORMALIZED_FORMULA_MAX_LGTH = 512;
    public final static String TAX_RATE_PLACEHOLDER = "{RATE}";
    public final static String TAX_NUMBER_PLACEHOLDER = "{NUM}";
    //http://stackoverflow.com/questions/513600/should-i-use-javas-string-format-if-performance-is-important
    public final static String getTaxChildIdPlaceholder(long taxId){
        StringBuilder placeholder = new StringBuilder();
        placeholder.append("{TAX");
        placeholder.append(taxId);
        placeholder.append("}");
        return placeholder.toString();
    }
    public final static long getTaxChildId(String taxIdPlaceholder) throws Exception {
        Pattern patternTaxId = Pattern.compile("\\d+");
        Matcher matcherTaxId = patternTaxId.matcher(taxIdPlaceholder);
        if (matcherTaxId.find()) {
            return Long.valueOf(matcherTaxId.group());
        } else {
            throw new Exception("Match (with number) is not found in " + taxIdPlaceholder);
        }
    }
    public final static String TAX_CHILD_ID_PLACEHOLDER_PATTERN = "\\{TAX\\d+\\}";

    //invoice
    public final static int INVOICE_COMMENT_MAX_LGTH = 512;
    public final static int INVOICE_NUMBER_MAX_LGTH = 14;
    public static final DateTimeFormatter INVOICE_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static String generateInvoiceNumber(){
        return LocalDateTime.now().format(INVOICE_NUMBER_FORMAT);
    }

    //item
    public final static int ITEM_DESCRIPTION1_MAX_LGTH = 512;
    public final static int ITEM_DESCRIPTION2_MAX_LGTH = 512;
    public final static int ITEM_COMMENT_MAX_LGTH = 512;

    //transaction
    public final static int TRANSACTION_NUMBER_MAX_LGTH = 14;
    public final static int TRANSACTION_COMMENT_MAX_LGTH = 512;
}
