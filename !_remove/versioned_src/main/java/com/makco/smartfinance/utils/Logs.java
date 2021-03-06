package com.makco.smartfinance.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Makar Kalancha on 2016-04-05.
 */
public class Logs {
    private final static Logger LOG = LogManager.getLogger(Logs.class);
    public static PrintStream createLoggingErrors(final PrintStream printStream){
        http://jcgonzalez.com/java-system-err-system-out-examples
        return new PrintStream(printStream){
            @Override
            public void print(String s) {
                LOG.error(s);
            }

            @Override
            public void println(String x) {
                LOG.error(x);
            }
        };
    }

    public static PrintStream createLoggingDebugs(final PrintStream printStream){
        http://jcgonzalez.com/java-system-err-system-out-examples
        return new PrintStream(printStream){
            @Override
            public void print(String s) {
                LOG.debug(s);
            }

            @Override
            public void println(String x) {
                LOG.debug(x);
            }
        };
    }

    public static String benchmarkCalcultaion(long startNanoTime, long endNanoTime){
        long elapsed_string = endNanoTime - startNanoTime;
        long minutes = TimeUnit.NANOSECONDS.toMinutes(elapsed_string);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(elapsed_string - TimeUnit.MINUTES.toNanos(minutes));
        long millis = TimeUnit.NANOSECONDS.toMillis(elapsed_string - TimeUnit.MINUTES.toNanos(minutes) - TimeUnit.SECONDS.toNanos(seconds));
        long nanos = elapsed_string - TimeUnit.MINUTES.toNanos(minutes) - TimeUnit.SECONDS.toNanos(seconds) - TimeUnit.MILLISECONDS.toNanos(millis);
        return String.format("[mm:ss:millis.nanos]: %s:%s:%s.%s", minutes, seconds, millis, nanos);
    }

}
