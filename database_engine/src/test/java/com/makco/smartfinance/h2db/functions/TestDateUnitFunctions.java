package com.makco.smartfinance.h2db.functions;

import com.makco.smartfinance.h2db.utils.TestUtilDateUnit;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mcalancea on 2016-02-16.
 */
public class TestDateUnitFunctions {
    private static final Logger LOG = LogManager.getLogger(TestDateUnitFunctions.class);
    public static void createDateUnitTable(Connection connection) throws SQLException{
        final StringBuilder createDateUnitTable = new StringBuilder();
        createDateUnitTable.append("CREATE SEQUENCE IF NOT EXISTS SEQ_DATEUNIT;");
        createDateUnitTable.append("CREATE TABLE IF NOT EXISTS DATEUNIT(");
            createDateUnitTable.append("ID BIGINT DEFAULT SEQ_DATEUNIT.NEXTVAL,");
            createDateUnitTable.append("UNITTIMESTAMP DATE UNIQUE NOT NULL,");
            createDateUnitTable.append("UNITYEAR INT NOT NULL,");
            createDateUnitTable.append("UNITMONTHOFYEAR INT NOT NULL,");
            createDateUnitTable.append("UNITMONTH BIGINT NOT NULL,");
            createDateUnitTable.append("UNITDAY BIGINT NOT NULL,");
            createDateUnitTable.append("UNITDAYOFWEEK INT NOT NULL,");
            createDateUnitTable.append("WEEKDAY BOOLEAN NOT NULL,");//if weekend false
            createDateUnitTable.append("T_CREATEDON TIMESTAMP,");
            createDateUnitTable.append("T_UPDATEDON TIMESTAMP,");
            createDateUnitTable.append("PRIMARY KEY(ID,UNITTIMESTAMP),");
        //there is no weekofyear, because if you change first day of the week, week of year is changed and all reports need to re-calculated
        //there is no unitweek, because if you change first day of the week, unit week is changed and all reports need to re-calculated
        createDateUnitTable.append(");");
        createDateUnitTable.append("CREATE INDEX IF NOT EXISTS IDXUNITYEAR ON DATEUNIT(UNITYEAR);");
        createDateUnitTable.append("CREATE INDEX IF NOT EXISTS IDXUNITMONTHOFYEAR ON DATEUNIT(UNITMONTHOFYEAR);");
        createDateUnitTable.append("CREATE INDEX IF NOT EXISTS IDXUNITMONTH ON DATEUNIT(UNITMONTH);");
        createDateUnitTable.append("CREATE INDEX IF NOT EXISTS IDXUNITDATE ON DATEUNIT(UNITDAY);");
        createDateUnitTable.append("CREATE INDEX IF NOT EXISTS IDXUNITDAYOFWEEK ON DATEUNIT(UNITDAYOFWEEK);");
        createDateUnitTable.append("CREATE INDEX IF NOT EXISTS IDXWEEKDAY ON DATEUNIT(WEEKDAY);");

        LOG.debug(createDateUnitTable.toString());
        try (Statement statement = connection.createStatement()){
            statement.execute(createDateUnitTable.toString());
        }
    }



    //h2 accepts java.UTIL.Date, not only java.SQL.Date
    public static Long insertSelectDate(Connection connection, Date date) throws SQLException {
        try {
            Long result = null;
            TestUtilDateUnit du = new TestUtilDateUnit(date);

            StringBuilder selectQuery = new StringBuilder();
            selectQuery.append("SELECT " + Table.DATEUNIT.UNITDAY + " FROM " + Table.Names.DATEUNIT);
            selectQuery.append(" WHERE " + Table.DATEUNIT.UNITTIMESTAMP + " = ?");

            StringBuilder insertQuery = new StringBuilder();
            insertQuery.append("INSERT INTO ");
            insertQuery.append(Table.Names.DATEUNIT);
            insertQuery.append("(");
            insertQuery.append(Table.DATEUNIT.UNITTIMESTAMP + ", ");
            insertQuery.append(Table.DATEUNIT.UNITYEAR + ", ");
            insertQuery.append(Table.DATEUNIT.UNITMONTHOFYEAR + ", ");
            insertQuery.append(Table.DATEUNIT.UNITMONTH + ", ");
            insertQuery.append(Table.DATEUNIT.UNITDAY + ", ");
            insertQuery.append(Table.DATEUNIT.UNITDAYOFWEEK + ", ");
            insertQuery.append(Table.DATEUNIT.WEEKDAY + ", ");
            insertQuery.append(Table.DATEUNIT.UNITDAYOFYEAR + ", ");
            insertQuery.append(Table.DATEUNIT.UNITDAYOFMONTH + ") ");
            insertQuery.append("VALUES (?,?,?,?,?,?,?,?,?); ");

            LOG.debug(selectQuery.toString());
            LOG.debug(insertQuery.toString());
            ResultSet rs = null;
            try (
                    PreparedStatement selectPS = connection.prepareStatement(selectQuery.toString());
                    PreparedStatement insertPS = connection.prepareStatement(insertQuery.toString(), Statement.RETURN_GENERATED_KEYS);
            ) {
                selectPS.setDate(1, du.getUnitTimeStamp());
                selectPS.execute();
                rs = selectPS.getResultSet();
                if (rs.next()) {
                    result = rs.getLong(1);
                } else {
                    insertPS.setDate(1, du.getUnitTimeStamp());
                    insertPS.setInt(2, du.getUnitYear());
                    insertPS.setInt(3, du.getUnitMonthOfYear());
                    insertPS.setLong(4, du.getUnitMonth());
                    insertPS.setLong(5, du.getUnitDay());
                    insertPS.setInt(6, du.getUnitDayOfWeek());
                    insertPS.setBoolean(7, du.getWeekDay());
                    insertPS.setInt(8, du.getUnitDayOfYear());
                    insertPS.setLong(9, du.getUnitDayOfMonth());

                    insertPS.executeUpdate();
//                rs = insertPS.getGeneratedKeys();
//                rs.next();
//                result = rs.getLong(1);
                    result = du.getUnitDay();
                }

                return result;
            } finally {
                if (rs != null) rs.close();
            }
        }catch (SQLException e) {
            LOG.error(e, e);
            throw e;
        }
    }
}
