package com.makco.smartfinance.user_interface.workers;

import com.makco.smartfinance.h2db.utils.H2DbUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: Makar Kalancha
 * Date: 21/04/2016
 * Time: 23:53
 */
public class QuitWorker extends Service<Void>{
    private final static Logger LOG = LogManager.getLogger(QuitWorker.class);
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LOG.debug("1-Main:backup");
                H2DbUtils.backup("quit");
                return null;
            }
        };
    }
}
