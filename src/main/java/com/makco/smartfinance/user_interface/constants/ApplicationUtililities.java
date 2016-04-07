package com.makco.smartfinance.user_interface.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Platform;
import javafx.event.Event;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class ApplicationUtililities {
    private final static Logger LOG = LogManager.getLogger(ApplicationUtililities.class);

    public static final String DB_SCHEMA_NAME = "FINANCE";

    public static void quit(Event event){
        try {
            LOG.debug("Closing the applicatoin.");
            Platform.exit();
            System.exit(0);
        } catch (Throwable t) {
            LOG.error(t, t);
        }

    }
}
