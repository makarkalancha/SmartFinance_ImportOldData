package com.makco.smartfinance.h2db.utils.schema_constants;

/**
 * Created by mcalancea on 2016-03-24.
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
        TAX_CHILD,
        CURRENCY,
        DATEUNIT,
        FAMILY_MEMBER,
        ORGANIZATION,
        TAX;
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

}
