package com.makco.smartfinance.h2db.utils.schema_constants;

/**
 * Created by Makar Kalancha on 2016-03-24.
 */
public class Table {
    public enum Elements{
        tableName, row;
    }

    public enum Names {
        _DELETED_ROWS,
        ACCOUNT,
        ACCOUNT_GROUP,
        CATEGORY,
        CATEGORY_GROUP,
        CURRENCY,
        DATEUNIT,
        FAMILY_MEMBER,
        INVOICE,
        ITEM,
        ORGANIZATION,
        TAX,
        TAX_CHILD,
        TRANSACTION;
    }

    public enum _DELETED_ROWS implements IEnumRow {
        ID(0),
        SCHEMA_NAME(1),
        TABLE_NAME(2),
        JSON_ROW(3),
        T_CREATEDON(4);

        private int columnIndex;
        private _DELETED_ROWS(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public _DELETED_ROWS getColumnNameByIndex(){
            return this;
        }
    };

    public enum CURRENCY implements IEnumRow {
        ID(0),
        CODE(1),
        NAME(2),
        DESCRIPTION(3),
        T_CREATEDON(4),
        T_UPDATEDON(5);

        private int columnIndex;
        private CURRENCY(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public CURRENCY getColumnNameByIndex(){
            return this;
        }
    };
    
    public enum DATEUNIT implements IEnumRow {
        UNITDAY(0),
        UNITDAYOFMONTH(1),
        UNITDAYOFYEAR(2),
        UNITMONTH(3),
        UNITMONTHOFYEAR(4),
        UNITYEAR(5),
        UNITDAYOFWEEK(6),
        WEEKDAY(7),
        UNITTIMESTAMP(8),
        T_CREATEDON(9);

        private int columnIndex;
        private DATEUNIT(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public DATEUNIT getColumnNameByIndex(){
            return this;
        }
    };
    
    public enum FAMILY_MEMBER implements IEnumRow {
        ID(0),
        NAME(1),
        DESCRIPTION(2),
        T_CREATEDON(3),
        T_UPDATEDON(4);

        private int columnIndex;
        private FAMILY_MEMBER(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public FAMILY_MEMBER getColumnNameByIndex(){
            return this;
        }
    };

    public enum ORGANIZATION implements IEnumRow {
        ID(0),
        NAME(1),
        DESCRIPTION(2),
        T_CREATEDON(3),
        T_UPDATEDON(4);

        private int columnIndex;
        private ORGANIZATION(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public ORGANIZATION getColumnNameByIndex(){
            return this;
        }
    };

    public enum CATEGORY_GROUP implements IEnumRow {
        ID(0),
        TYPE(1),
        NAME(2),
        DESCRIPTION(3),
        T_CREATEDON(4),
        T_UPDATEDON(5);

        private int columnIndex;
        private CATEGORY_GROUP(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public CATEGORY_GROUP getColumnNameByIndex(){
            return this;
        }
    };

    public enum CATEGORY implements IEnumRow {
        ID(0),
        CATEGORY_GROUP_ID(1),
        CATEGORY_GROUP_TYPE(2),
        NAME(3),
        DESCRIPTION(4),
        T_CREATEDON(5),
        T_UPDATEDON(6);

        private int columnIndex;
        private CATEGORY(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public CATEGORY getColumnNameByIndex(){
            return this;
        }
    };

    public enum ACCOUNT_GROUP implements IEnumRow {
        ID(0),
        TYPE(1),
        NAME(2),
        DESCRIPTION(3),
        T_CREATEDON(4),
        T_UPDATEDON(5);

        private int columnIndex;
        private ACCOUNT_GROUP(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public ACCOUNT_GROUP getColumnNameByIndex(){
            return this;
        }
    };

    public enum ACCOUNT implements IEnumRow {
        ID(0),
        ACCOUNT_GROUP_ID(1),
        ACCOUNT_GROUP_TYPE(2),
        NAME(3),
        DESCRIPTION(4),
        T_CREATEDON(5),
        T_UPDATEDON(6);

        private int columnIndex;
        private ACCOUNT(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public ACCOUNT getColumnNameByIndex(){
            return this;
        }
    };

    public enum TAX implements IEnumRow {
        ID(0),
        NAME(1),
        DESCRIPTION(2),
        RATE(3),
        FORMULA(4),
        DENORMALIZED_FORMULA(5),
        STARTDATE(6),
        ENDDATE(7),
        T_CREATEDON(8),
        T_UPDATEDON(9);

        private int columnIndex;
        private TAX(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public TAX getColumnNameByIndex(){
            return this;
        }
    };

    public enum TAX_CHILD implements IEnumRow {
        TAX_ID(0),
        CHILD_TAX_ID(1),
        T_CREATEDON(2);

        private int columnIndex;
        private TAX_CHILD(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public TAX_CHILD getColumnNameByIndex(){
            return this;
        }
    };

    public enum INVOICE implements IEnumRow {
        ID(0),
        INVOICE_NUMBER(1),
        ORGANIZATION_ID(2),
        DATEUNIT_UNITDAY(3),
        COMMENT(4),
        DEBIT_TOTAL(5),
        CREDIT_TOTAL(6),
        T_CREATEDON(7),
        T_UPDATEDON(8);

        private int columnIndex;
        private INVOICE(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public INVOICE getColumnNameByIndex(){
            return this;
        }
    };

    public enum ITEM implements IEnumRow {
        ID(0),
        ORDER_NUMBER(1),
        INVOICE_ID(2),
        CATEGORY_ID(3),
        TAX_ID(4),
        FAMILY_MEMBER_ID(5),
        DATEUNIT_UNITDAY(6),
        DESCRIPTION1(7),
        DESCRIPTION2(8),
        COMMENT(9),
        SUB_TOTAL(10),
        TOTAL(11),
        T_CREATEDON(12),
        T_UPDATEDON(13);

        private int columnIndex;
        private ITEM(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public ITEM getColumnNameByIndex(){
            return this;
        }
    };

    public enum TRANSACTION implements IEnumRow {
        ID(0),
        TRANSACTION_NUMBER(1),
        ACCOUNT_ID(2),
        INVOICE_ID(3),
        DATEUNIT_UNITDAY(4),
        COMMENT(5),
        DEBIT_AMOUNT(6),
        CREDIT_AMOUNT(7),
        T_CREATEDON(8),
        T_UPDATEDON(9);

        private int columnIndex;
        private TRANSACTION(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public TRANSACTION getColumnNameByIndex(){
            return this;
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
