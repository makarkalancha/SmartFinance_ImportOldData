package com.makco.smartfinance.h2db.utils.tables;

/**
 * Created by mcalancea on 2016-03-24.
 */
public class Table {
    public enum FAMILY_MEMBER{
        ID(0),
        NAME(1),
        DESCRIPTION(2),
        T_CREATEDON(3),
        T_UPDATEDON(4);

        int columnIndex;
        private FAMILY_MEMBER(int columnIndex){
            this.columnIndex = columnIndex;
        }

        public int getColumnIndex(){
            return this.columnIndex;
        }

        public FAMILY_MEMBER getColumnNameByIndex(){
            return this;
        }
    };

}
