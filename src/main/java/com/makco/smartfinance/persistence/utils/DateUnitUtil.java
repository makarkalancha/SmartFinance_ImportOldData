package com.makco.smartfinance.persistence.utils;

import com.google.common.base.Objects;
import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by mcalancea on 2016-04-20.
 */
public class DateUnitUtil {
    public static final int DEFAULT_YEARS_IN_FUTURE = 5;

    public static LocalDate getLocaDateInFutureSinceNow(){
        return LocalDate.now().plus(DEFAULT_YEARS_IN_FUTURE)
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
