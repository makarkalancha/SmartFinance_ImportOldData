package com.makco.smartfinance.h2db.utils.tables;

/**
 * Created by mcalancea on 2016-03-24.
 */
public class Table {
    public enum Elements{
        tableName, row;
    }

    public enum Names{
        _DELETED_ROWS, FAMILY_MEMBER;
    }

    public enum _DELETED_ROWS implements IEnumTable{
        ID(0, false),
        SCHEMA_NAME(1, false),
        TABLE_NAME(2, false),
        JSON_ROW(3, false),
        T_CREATEDON(4, true);

        private int columnIndex;
        private boolean isNullable;
        private _DELETED_ROWS(int columnIndex, boolean isNullable){
            this.columnIndex = columnIndex;
            this.isNullable = isNullable;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public boolean isNullable() {
            return isNullable;
        }

        @Override
        public _DELETED_ROWS getColumnNameByIndex(){
            return this;
        }
    };

    public enum FAMILY_MEMBER implements IEnumTable{
        ID(0, false),
        NAME(1, false),
        DESCRIPTION(2, true),
        T_CREATEDON(3, true),
        T_UPDATEDON(4, true);

        private int columnIndex;
        private boolean isNullable;
        private FAMILY_MEMBER(int columnIndex, boolean isNullable){
            this.columnIndex = columnIndex;
            this.isNullable = isNullable;
        }

        @Override
        public int getColumnIndex(){
            return this.columnIndex;
        }

        @Override
        public boolean isNullable() {
            return isNullable;
        }

        @Override
        public FAMILY_MEMBER getColumnNameByIndex(){
            return this;
        }


    };

}
