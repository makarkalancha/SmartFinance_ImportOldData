package com.makco.smartfinance.persistence.utils;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.DateUnit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-20.
 */
public class DateUnitUtil {
    public static final Chrono DEFAULT_5_YEARS_IN_FUTURE = new Chrono(5L,ChronoUnit.YEARS);

    public static LocalDate getLocaDateInFutureSinceDate(LocalDate localDate){
        return localDate.plus(DEFAULT_5_YEARS_IN_FUTURE.getChronoNumber(), DEFAULT_5_YEARS_IN_FUTURE.getChronoUnit());
    }

    public static long getNumberOfBatchesInRange(LocalDate start, LocalDate end) throws Exception{
        long result = 0L;

        long daysBetween = ChronoUnit.DAYS.between(start, end);
        //floor rounds to the least value, so
        //4000 days / 50 row per batch = 80.02 => floor = 80
        //-4000 days/ 50 row per batch = -80.02 => floor = -81 => -81 x -1 = 81
        result = Math.floorDiv(daysBetween * (-1), DataBaseConstants.BATCH_SIZE) * (-1);
        return result;
    }

    public static List<DateUnit> getListOfDateUnitEntities(LocalDate start, LocalDate end) throws Exception{
        List<DateUnit> result = new ArrayList<>();
        if(start.compareTo(end) < 0) {
            LocalDate tmp = start;
            while (tmp.compareTo(end) <= 0) {
                DateUnit du = new DateUnit(tmp);
                System.out.println(du);
                result.add(du);
                tmp = tmp.plus(1, ChronoUnit.DAYS);
            }
        }
        return result;
    }

    public static class Chrono{
        private final long chronoNumber;
        private final ChronoUnit chronoUnit;

        public Chrono(long chronoNumber, ChronoUnit chronoUnit) {
            this.chronoNumber = chronoNumber;
            this.chronoUnit = chronoUnit;
        }

        public long getChronoNumber() {
            return chronoNumber;
        }

        public ChronoUnit getChronoUnit() {
            return chronoUnit;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof Chrono){
                Chrono that = (Chrono) o;
                return Objects.equal(chronoNumber, that.chronoNumber)
                        && Objects.equal(chronoUnit, that.chronoUnit);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(chronoNumber, chronoUnit);
        }
    }

}
