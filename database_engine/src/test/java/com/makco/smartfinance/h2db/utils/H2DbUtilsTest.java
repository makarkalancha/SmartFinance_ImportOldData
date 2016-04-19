package com.makco.smartfinance.h2db.utils;

import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.TestContext;
import com.makco.smartfinance.h2db.tables.DateUnit;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * Created by mcalancea on 2016-03-08.
 */
public class H2DbUtilsTest {
    private static final Logger LOG = LogManager.getLogger(H2DbUtilsTest.class);

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    //comment or remove this method from test suite
//    @Test
    public void testDateUnitTable() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime start;
        LocalDateTime end;

        H2DbUtils.setSchema(dbConnectionResource.getConnection(), TestContext.INSTANCE.DB_SCHEMA());
        start = LocalDateTime.now();
        LOG.debug("method start at: " + start);
        StringBuilder deleteAllDU = new StringBuilder();
        deleteAllDU.append("DELETE FROM ");
        deleteAllDU.append(Table.Names.DATEUNIT);
        deleteAllDU.append(";");


        StringBuilder insertDU = new StringBuilder();
        insertDU.append("INSERT INTO ");
        insertDU.append(Table.Names.DATEUNIT);
        insertDU.append("(");
        insertDU.append(Table.DATEUNIT.UNITTIMESTAMP + ", ");
        insertDU.append(Table.DATEUNIT.UNITYEAR + ", ");
        insertDU.append(Table.DATEUNIT.UNITMONTHOFYEAR + ", ");
        insertDU.append(Table.DATEUNIT.UNITMONTH + ", ");
        insertDU.append(Table.DATEUNIT.UNITDATE + ", ");
        insertDU.append(Table.DATEUNIT.UNITDAYOFWEEK + ", ");
        insertDU.append(Table.DATEUNIT.WEEKDAY + ", ");
        insertDU.append(Table.DATEUNIT.UNITDATEOFMONTH + ") ");
        insertDU.append("VALUES (?,?,?,?,?,?,?,?); ");

        Date todaysDate = new Date();
        Date lastDate = sdf.parse("2000-01-01");

        int batchSize = 1_000;
        int printCount = 100;
        int count = 0;
        int limit = 10_000;
        start = LocalDateTime.now();
        LOG.debug("batchSize: " + batchSize);
        LOG.debug("printCount: " + printCount);
        LOG.debug("limit: " + limit);
        LOG.debug("batch start: " + start);

        try (
                PreparedStatement deletePS = dbConnectionResource.getConnection().prepareStatement(deleteAllDU.toString());
                PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(insertDU.toString());
        ) {
            deletePS.executeUpdate();
            while (count < limit) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(todaysDate);
                cal.add(Calendar.DATE, -count);
                DateUnit du = new DateUnit(cal.getTime());
                insertPS.setDate(1, du.getUnitTimeStamp());
                insertPS.setInt(2, du.getUnitYear());
                insertPS.setInt(3, du.getUnitMonthOfYear());
                insertPS.setLong(4, du.getUnitMonth());
                insertPS.setLong(5, du.getUnitDate());
                insertPS.setInt(6, du.getUnitDayOfWeek());
                insertPS.setBoolean(7, du.getWeekDay());
                insertPS.setLong(8, du.getUnitDateOfMonth());
                insertPS.addBatch();

                ++count;

                if(count % printCount == 0){
                    LOG.debug(sdf.format(du.getUnitTimeStamp()));
                }

                if(count % batchSize == 0){
                    insertPS.executeBatch();
                }
            }
            insertPS.executeBatch();
        }
        end = LocalDateTime.now();
        LOG.debug("batch end: " + end);
        LocalDateTime tmp = LocalDateTime.from(start);
        long hours = tmp.until(end, ChronoUnit.HOURS);
        long minutes = tmp.until(end, ChronoUnit.MINUTES);
        long seconds = tmp.until(end, ChronoUnit.SECONDS);
        long millisSeconds = tmp.until(end, ChronoUnit.MILLIS);
        LOG.debug("duration (H:M:S.m): " + hours + ":" + minutes + ":" + seconds + "." + millisSeconds);

        long hours1 = Duration.between(start,end).toHours();
        long minutes1 = Duration.between(start,end).toMinutes();
        long seconds1 = (Duration.between(start,end).toMillis() / 1_000);
        long millisSeconds1 = Duration.between(start,end).toMillis();
        LOG.debug("duration1 (H:M:S.m): " + hours1 + ":" + minutes1 + ":" + seconds1 + "." + millisSeconds1);


        end = LocalDateTime.now();
        LOG.debug("method end at: " + end);
    }

    @Test
    public void testBatchQty() {
        LocalDate start = LocalDate.of(2010, Month.JANUARY, 01);
        LocalDate today = LocalDate.now();
        LocalDate end = today.plus(5, ChronoUnit.YEARS);
        System.out.println("start:" + start.toString());
        System.out.println("today:" + today.toString());
        System.out.println("end:" + end.toString());

        long daysBetween = ChronoUnit.DAYS.between(start, end);
        System.out.println("difference (in days) between start and end: " + daysBetween);
//        Duration diffStartEnd = Duration.between(start, end);
//        System.out.println("difference (in days) between start and end: " + diffStartEnd.toDays());

    }
}