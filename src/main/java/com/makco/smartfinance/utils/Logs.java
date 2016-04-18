package com.makco.smartfinance.utils;

import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mcalancea on 2016-04-05.
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

}
