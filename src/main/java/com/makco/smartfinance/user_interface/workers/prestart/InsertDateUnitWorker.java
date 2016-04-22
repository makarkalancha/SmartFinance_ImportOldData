package com.makco.smartfinance.user_interface.workers.prestart;

import com.google.common.collect.Lists;
import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.utils.DateUnitUtil;
import com.makco.smartfinance.services.DateUnitService;
import com.makco.smartfinance.services.DateUnitServiceImpl;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

/**
 * User: Makar Kalancha
 * Date: 21/04/2016
 * Time: 23:04
 */
public class InsertDateUnitWorker extends Service<Void> {
    private final static Logger LOG = LogManager.getLogger(InsertDateUnitWorker.class);
    private LocalDate startLocalDate;

    public void setStartLocalDate(LocalDate startLocalDate) {
        this.startLocalDate = startLocalDate;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(startLocalDate == null){
                    throw new Exception("Start date is not set");
                }

                int interval = 100;
                LocalDate endLocalDate = DateUnitUtil.getLocaDateInFutureSinceDate(LocalDate.now());
                long totalBatches = DateUnitUtil.getNumberOfBatchesInRange(startLocalDate, endLocalDate);
                updateProgress(0, totalBatches);
                updateTitle("Start inserting dates");
                updateMessage("Starting...");
                DateUnitService dateUnitService = new DateUnitServiceImpl();
                List<DateUnit> dateUnitList = DateUnitUtil.getListOfDateUnitEntities(startLocalDate, endLocalDate);
                int i = 0;
                long totalSize = dateUnitList.size();
                long batchSize = 0;
                updateTitle("Inserting dates");
                for (List<DateUnit> dateUnitBatch : Lists.partition(dateUnitList, DataBaseConstants.BATCH_SIZE)) {
                    batchSize += dateUnitBatch.size();
                    int precent = new Double(((double) batchSize / totalSize) * 100).intValue();
                    dateUnitService.addDateUnitList(dateUnitBatch);
                    updateTitle("Inserting dates " + precent + "%");
                    updateMessage("Inserted " + batchSize + " out of " + totalSize);
                    updateProgress(++i, totalBatches);
                    Thread.sleep(interval);
                }

                return null;
            }
        };
    }
}
