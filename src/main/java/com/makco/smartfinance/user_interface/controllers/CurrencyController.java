package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.constants.ProgressForm;
import com.makco.smartfinance.user_interface.models.CurrencyModel;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import java.net.URL;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by mcalancea on 2016-04-12.
 */
public class CurrencyController implements Initializable, ControlledScreen {
    private final static Logger LOG = LogManager.getLogger(CurrencyController.class);
    private ScreensController myController;
    private CurrencyModel currencyModel = new CurrencyModel();

    private ActionEvent actionEvent;
    private Worker<Void> onDeleteWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveWorker;
    private Worker<Void> onRefreshCurrencyWorker;
    private ProgressForm pForm = new ProgressForm();

    @FXML
    private TableView<Currency> table;
    @FXML
    private TextField codeTF;
    @FXML
    private TextField nameTF;
    @FXML
    private TextArea descTA;
    @FXML
    private Button clearBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button deleteBtn;

    public CurrencyController(){
        try{
            onDeleteWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            currencyModel.deletePendingCurrency();
                            return null;
                        }
                    };
                }
            };
            onSaveWorker = new Service<EnumSet<ErrorEnum>>() {
                @Override
                protected Task<EnumSet<ErrorEnum>> createTask() {
                    return new Task<EnumSet<ErrorEnum>>() {
                        @Override
                        protected EnumSet<ErrorEnum> call() throws Exception {
                            return currencyModel.savePendingCurrency(codeTF.getText(), nameTF.getText(), descTA.getText());
                        }
                    };
                }
            };
            onRefreshCurrencyWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            currencyModel.refreshCurrency();
                            return null;
                        }
                    };
                }
            };
        }catch (Exception e){
            //not in finally because refreshCurrency must run before populateTable
            startService(onRefreshCurrencyWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    public void initializeServices(){
        try{
            ((Service<Void>) onDeleteWorker).setOnSucceeded(event -> {
                LOG.debug("onDeleteWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onDeleteWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
            });
            ((Service<Void>) onDeleteWorker).setOnFailed(event -> {
                LOG.debug("onDeleteWorker->setOnFailed");
                LOG.debug(">>>>>>>>onDeleteWorker->setOnFailed: pForm.getDialogStage().close()");
                DialogMessages.showExceptionAlert(onDeleteWorker.getException());
                pForm.close();
                populateTable();
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveWorker).setOnSucceeded(event -> {
                LOG.debug("onSaveWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onSaveWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
                EnumSet<ErrorEnum> errors = onSaveWorker.getValue();
                if(!errors.isEmpty()) {
                    DialogMessages.showErrorDialog("Error while saving Currency: " + codeTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveWorker).getValue(), null);
                }
                onClear(actionEvent);
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveWorker).setOnFailed(event -> {
                LOG.debug("onSaveWorker->setOnFailed");
                LOG.debug(">>>>>>>>onSaveWorker->setOnFailed: pForm.getDialogStage().close()");
                DialogMessages.showExceptionAlert(onSaveWorker.getException());
                pForm.close();
                populateTable();
            });
            ((Service<Void>) onRefreshCurrencyWorker).setOnSucceeded(event -> {
                LOG.debug("onRefreshCurrencyWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onRefreshCurrencyWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
            });
            ((Service<Void>) onRefreshCurrencyWorker).setOnFailed(event -> {
                LOG.debug("onRefreshCurrencyWorker->setOnFailed");
                LOG.debug(">>>>>>>>onRefreshCurrencyWorker->setOnFailed: pForm.getDialogStage().close()");
                DialogMessages.showExceptionAlert(onRefreshCurrencyWorker.getException());
                pForm.close();
                populateTable();
            });
        }catch (Exception e){
            //not in finally because refreshCurrency must run before populateTable
            startService(onRefreshCurrencyWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private <V> void startService(Worker<V> worker, ActionEvent event){
        pForm.activateProgressBar(worker);
        LOG.debug(">>>>>>>>pForm.getDialogStage().show()");
        pForm.show();
        actionEvent = event;

        Service<V> service = ((Service<V>)worker);
        if(service != null){
            ((Service<V>)worker).restart();
        }
    }

    @Override
    public void setScreenPage(ScreensController screenPage) {
        try{
            myController = screenPage;
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            codeTF.setPrefColumnCount(3);
            nameTF.setPrefColumnCount(64);
            descTA.setPrefColumnCount(128);
//            initializeServices();
//            startService(onRefreshCurrencyWorker, null);
//            table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
//                if (newSelection != null) {
//                    populateForm();
//                }
//            });
            clearBtn.setDisable(true);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        } catch (Exception e) {
            //not in finally because refreshCurrency must run before populateTable
            startService(onRefreshCurrencyWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClear(ActionEvent event){
        try{
            codeTF.clear();
            nameTF.clear();
            descTA.clear();
            clearBtn.setDisable(false);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        }catch (Exception e){
            startService(onRefreshCurrencyWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSave(ActionEvent event){
        try {
            startService(onSaveWorker, event);
        } catch (Exception e) {
            //no refreshCurrency() because there are in deletePendingCurrency, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onDelete(ActionEvent event){
        try {
            String title = ApplicationConstants.CURRENCY_WINDOW_TITLE;
            String headerText = "Currency Deletion";
            StringBuilder contentText = new StringBuilder("Are you sure you want to delete currency ");
            contentText.append("\"");
            contentText.append(codeTF.getText());
            contentText.append("\"?");
            if(DialogMessages.showConfirmationDialog(title,headerText,contentText.toString(),null)) {
                startService(onDeleteWorker, event);
                populateTable();
                onClear(actionEvent);
            }
        } catch (Exception e) {
            //no refreshCurrency() because there are in deletePendingCurrency, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateForm(){
        try{
            clearBtn.setDisable(false);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(false);
            currencyModel.setPendingCurrencyProperty(table.getSelectionModel().getSelectedItem());

            codeTF.setText(currencyModel.getPendingCurrency().getCode());
            nameTF.setText(currencyModel.getPendingCurrency().getName());
            descTA.setText(currencyModel.getPendingCurrency().getDescription());
        }catch (Exception e){
            startService(onRefreshCurrencyWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateTable(){
        try{
            LOG.debug("currencyModel.getCurrencies().size():" + currencyModel.getCurrencies().size());
            table.getItems().clear();
            table.setItems(currencyModel.getCurrencies());

            TableColumn<Currency, Long> currencyIdCol = new TableColumn<>("ID");
            currencyIdCol.setCellValueFactory(new PropertyValueFactory<Currency, Long>("id"));

            TableColumn<Currency, String> currencyCodeCol = new TableColumn<>("Code");
            currencyCodeCol.setCellValueFactory(new PropertyValueFactory<Currency, String>("code"));
            
            TableColumn<Currency, String> currencyNameCol = new TableColumn<>("Name");
            currencyNameCol.setCellValueFactory(new PropertyValueFactory<Currency, String>("name"));

            TableColumn<Currency, String> currencyDescCol = new TableColumn<>("Description");
            currencyDescCol.setCellValueFactory(new PropertyValueFactory<Currency, String>("description"));

            TableColumn<Currency, Calendar> currencyCreatedCol = new TableColumn<>("Created on");
            currencyCreatedCol.setCellValueFactory(new PropertyValueFactory<Currency, Calendar>("createdOn"));

            TableColumn<Currency, Calendar> currencyUpdatedCol = new TableColumn<>("Updated on");
            currencyUpdatedCol.setCellValueFactory(new PropertyValueFactory<Currency, Calendar>("updatedOn"));

            table.getColumns().setAll(currencyIdCol, currencyCodeCol, currencyNameCol, currencyDescCol, currencyCreatedCol, currencyUpdatedCol);
        }catch (Exception e){
            startService(onRefreshCurrencyWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void refresh() {
        try{
            initializeServices();
            startService(onRefreshCurrencyWorker, null);
            table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateForm();
                }
            });
        }catch (Exception e){
            startService(onRefreshCurrencyWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public boolean askPermissionToClose() {
        return false;
    }

    @Override
    public void close() {

    }
}