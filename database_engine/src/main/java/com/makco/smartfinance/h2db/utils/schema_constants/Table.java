package com.makco.smartfinance.h2db.utils.schema_constants;

/**
 * Created by mcalancea on 2016-03-24.
 */
public class Table {
    public enum Elements{
        tableName, row;
    }

    public enum Names{
        _DELETED_ROWS,
        CURRENCY,
        DATEUNIT,
        FAMILY_MEMBER;
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
        UNITDATE(0),
        UNITDATEOFMONTH(1),
        UNITMONTH(2),
        UNITMONTHOFYEAR(3),
        UNITYEAR(4),
        UNITDAYOFWEEK(5),
        WEEKDAY(6),
        UNITTIMESTAMP(7),
        T_CREATEDON(8);

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

}
