package com.makco.smartfinance.user_interface.workers.prestart;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.services.DateUnitService;
import com.makco.smartfinance.services.DateUnitServiceImpl;
import com.makco.smartfinance.user_interface.Command;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: Makar Kalancha
 * Date: 18/04/2016
 * Time: 22:44
 */
public class PreStartWorker extends Service<Void>{
    private final static Logger LOG = LogManager.getLogger(PreStartWorker.class);
    private BooleanProperty isEmpty = new SimpleBooleanProperty();

    public PreStartWorker() {
        isEmpty.addListener((observable, oldValue, newValue) -> {
            LOG.debug(String.format("PreStartWorker->oldValue: %b; newValue: %b", oldValue, newValue));
//            if(newValue){
//                DialogMessages.showConfirmationDialog("Create", "Create", "insert or not", null);
//            }
        });
    }

    public BooleanProperty isEmptyProperty() {
        return isEmpty;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int interval = 100;

                Command[] commands = {
                        new DBMigrate(),
                        new DBBackup(),
                        new DateUnitTableCheck(),

                };
                int total = commands.length;
                updateProgress(0, total);
                updateTitle("Start Application");
                updateMessage("Starting...");
                for (int i = 1; i <= total; i++) {
                    updateTitle("Example Service (" + i + ")");
                    updateMessage("Processed " + i + " of " + total + " items: " + commands[i - 1].toString());
                    commands[i - 1].execute();
                    updateProgress(i, total);
                    Thread.sleep(interval);
                }
                //                Thread.sleep(250);
                return null;
            }
        };
    }

    private class DateUnitTableCheck implements Command {
        @Override
        public void execute() throws Exception {
            LOG.debug("Main: is date unit table empty");
            DateUnitService dateUnitService = new DateUnitServiceImpl();
            PreStartWorker.this.isEmpty.set(dateUnitService.isEmpty());
        }

        @Override
        public String toString(){
            return "Date Unit Table Check";
        }
    }

    private class DBBackup implements Command {
        @Override
        public void execute() throws Exception {
            LOG.debug("Main: backup");
            H2DbUtils.backup("start");
        }

        @Override
        public String toString(){
            return "Data Base Backup";
        }
    }

    private class DBMigrate implements Command {
        @Override
        public void execute() throws Exception {
            LOG.debug("Main: migrate");
            H2DbUtils.migrate(DataBaseConstants.SCHEMA);
        }

        @Override
        public String toString(){
            return "Data Base Migration";
        }
    }
}
