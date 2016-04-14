package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.constants.ProgressForm;
import com.makco.smartfinance.user_interface.models.FamilyMemberModel;
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
 * Created by mcalancea on 2016-04-01.
 */
//http://www.devx.com/Java/Article/48193/0/page/2
public class FamilyMemberController implements Initializable, ControlledScreen {
    private final static Logger LOG = LogManager.getLogger(FamilyMemberController.class);
    private ScreensController myController;
    private FamilyMemberModel familyMemberModel = new FamilyMemberModel();

//    private Executor executor;
    private ActionEvent actionEvent;
    private String globalTest;
    private Worker<Void> onDeleteWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveWorker;
    private Worker<Void> onRefreshFamilyMembersWorker;
    private ProgressForm pForm = new ProgressForm();

    @FXML
    private TableView<FamilyMember> table;
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

    public void initializeServices(){
        try{
//            executor = Executors.newCachedThreadPool(runnable -> {
//                Thread t = new Thread(runnable);
//                t.setDaemon(true);
//                return t;
//
//            });

            onDeleteWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            LOG.debug("globalTest:" + globalTest);
                            familyMemberModel.deletePendingFamilyMember();
                            return null;
                        }
                    };
                }
            };
            ((Service<Void>) onDeleteWorker).setOnSucceeded(event -> {
                LOG.debug("onDeleteWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onDeleteWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.getDialogStage().close();
                populateTable();
    //                startButton.setDisable(false);
            });
            ((Service<Void>) onDeleteWorker).setOnFailed(event -> {
                LOG.debug("onDeleteWorker->setOnFailed");
                LOG.debug(">>>>>>>>onDeleteWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.getDialogStage().close();
                populateTable();
//                onClear(actionEvent);
//                startButton.setDisable(false);
            });
            onSaveWorker = new Service<EnumSet<ErrorEnum>>() {
                @Override
                protected Task<EnumSet<ErrorEnum>> createTask() {
                    return new Task<EnumSet<ErrorEnum>>() {
                        @Override
                        protected EnumSet<ErrorEnum> call() throws Exception {
                            LOG.debug("globalTest:" + globalTest);
//                            familyMemberModel.savePendingFamilyMember(nameTF.getText(), descTA.getText());
//                            return null;
                            return familyMemberModel.savePendingFamilyMember(nameTF.getText(), descTA.getText());
                        }
                    };
                }
            };
            ((Service<EnumSet<ErrorEnum>>) onSaveWorker).setOnSucceeded(event -> {
                LOG.debug("onSaveWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onSaveWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.getDialogStage().close();
                populateTable();
                EnumSet<ErrorEnum> errors = ((Service<EnumSet<ErrorEnum>>) onSaveWorker).getValue();
                if(!errors.isEmpty()) {
                    DialogMessages.showErrorDialog("Error while saving Family Member: " + nameTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveWorker).getValue(), null);
                }
                onClear(actionEvent);
//                startButton.setDisable(false);
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveWorker).setOnFailed(event -> {
                LOG.debug("onSaveWorker->setOnFailed");
                LOG.debug(">>>>>>>>onSaveWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.getDialogStage().close();
                populateTable();
//                onClear(actionEvent);
//                startButton.setDisable(false);
            });
            onRefreshFamilyMembersWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            LOG.debug("globalTest:" + globalTest);
                            familyMemberModel.refreshFamilyMembers();
                            return null;
                        }
                    };
                }
            };
            ((Service<Void>) onRefreshFamilyMembersWorker).setOnSucceeded(event -> {
                LOG.debug("onRefreshFamilyMembersWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onRefreshFamilyMembersWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.getDialogStage().close();
                populateTable();
//                startButton.setDisable(false);
            });
            ((Service<Void>) onRefreshFamilyMembersWorker).setOnFailed(event -> {
                LOG.debug("onRefreshFamilyMembersWorker->setOnFailed");
                LOG.debug(">>>>>>>>onRefreshFamilyMembersWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.getDialogStage().close();
                populateTable();
//                onClear(actionEvent);
//                startButton.setDisable(false);
            });
        }catch (Exception e){
            //not in finally because refreshFamilyMembers must run before populateTable
            startService(onRefreshFamilyMembersWorker,null,"from initializeServices: catch exception");
            DialogMessages.showExceptionAlert(e);
        }
    }

    private <V> void startService(Worker<V> worker, ActionEvent event, String test){
        pForm.activateProgressBar(worker);
        LOG.debug(">>>>>>>>pForm.getDialogStage().show()");
        pForm.getDialogStage().show();
//        pForm.getDialogStage().toFront();
        pForm.getDialogStage().setAlwaysOnTop(true);
        actionEvent = event;
        globalTest = test;

//        ((Service<V>)worker).setOnSucceeded(succeededEevent -> {
//            pForm.getDialogStage().close();
//        });
        Service<V> service = ((Service<V>)worker);
        if(service == null){
            LOG.debug("service IS NULL");
        } else {
            LOG.debug("service IS NOT NULL");
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
            nameTF.setPrefColumnCount(64);
            descTA.setPrefColumnCount(128);
            initializeServices();
            startService(onRefreshFamilyMembersWorker, null, "from initialize");
            table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateForm();
                }
            });
            clearBtn.setDisable(true);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        } catch (Exception e) {
            //not in finally because refreshFamilyMembers must run before populateTable
            startService(onRefreshFamilyMembersWorker, null, "from initialize: catch exception");
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClear(ActionEvent event){
        try{
            nameTF.clear();
            descTA.clear();
            clearBtn.setDisable(false);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        }catch (Exception e){
            startService(onRefreshFamilyMembersWorker,null,"from onClear: catch exception");
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSave(ActionEvent event){
        try {
            LOG.debug("FamilyMemberController->onSave");
            startService(onSaveWorker, event, "from onSave");
        } catch (Exception e) {
            //no refreshFamilyMembers() because there are in deletePendingFamilyMember, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void save(ActionEvent event){
        try{

        } catch (Exception e) {
            //no refreshFamilyMembers() because there are in deletePendingFamilyMember, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onDelete(ActionEvent event){
        try {
//            http://stackoverflow.com/questions/22098288/javafx-how-can-i-use-correctly-a-progressindicator-in-javafx
//            http://stackoverflow.com/questions/30249493/using-threads-to-make-database-requests
//            http://stackoverflow.com/questions/29625170/display-popup-with-progressbar-in-javafx
            //http://www.jensd.de/wordpress/?p=1211
            //use service as it can be reused
//            Task taskToDelete = new Task<Void>() {
//                @Override
//                protected Void call() throws Exception {
//
//                }
//            };
//            Thread backGroundT = new Thread(taskToDelete);
//            backGroundT.setDaemon(true);
//            backGroundT.start();
            String title = ApplicationConstants.FAMILY_MEMBER_WINDOW_TITLE;
            String headerText = "Family Member Deletion";
            StringBuilder contentText = new StringBuilder("Are you sure you want to delete family member ");
            contentText.append("\"");
            contentText.append(nameTF.getText());
            contentText.append("\"?");
            if(DialogMessages.showConfirmationDialog(title,headerText,contentText.toString(),null)) {
                startService(onDeleteWorker, event, "from onDelete");
                populateTable();
                onClear(actionEvent);
            }


        } catch (Exception e) {
            //no refreshFamilyMembers() because there are in deletePendingFamilyMember, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void delete(ActionEvent event){
        try{

        } catch (Exception e) {
            //no refreshFamilyMembers() because there are in deletePendingFamilyMember, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateForm(){
        try{
            clearBtn.setDisable(false);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(false);
            familyMemberModel.setPendingFamilyMemberProperty(table.getSelectionModel().getSelectedItem());

            nameTF.setText(familyMemberModel.getPendingFamilyMember().getName());
            descTA.setText(familyMemberModel.getPendingFamilyMember().getDescription());
        }catch (Exception e){
            startService(onRefreshFamilyMembersWorker,null,"from populateForm: catch exception");
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateTable(){
        try{
            LOG.debug("familyMemberModel.getFamilyMembers().size():"+familyMemberModel.getFamilyMembers().size());
            table.getItems().clear();
            table.setItems(familyMemberModel.getFamilyMembers());

            TableColumn<FamilyMember, Long> familyMemberIdCol = new TableColumn<>("ID");
            familyMemberIdCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, Long>("id"));

            TableColumn<FamilyMember, String> familyMemberNameCol = new TableColumn<>("Name");
            familyMemberNameCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, String>("name"));

            TableColumn<FamilyMember, String> familyMemberDescCol = new TableColumn<>("Description");
            familyMemberDescCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, String>("description"));

            TableColumn<FamilyMember, Calendar> familyMemberCreatedCol = new TableColumn<>("Created on");
            familyMemberCreatedCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, Calendar>("createdOn"));

            TableColumn<FamilyMember, Calendar> familyMemberUpdatedCol = new TableColumn<>("Updated on");
            familyMemberUpdatedCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, Calendar>("updatedOn"));

            table.getColumns().setAll(familyMemberIdCol, familyMemberNameCol, familyMemberDescCol, familyMemberCreatedCol, familyMemberUpdatedCol);
        }catch (Exception e){
            startService(onRefreshFamilyMembersWorker,null,"from populateTable: catch exception");
            DialogMessages.showExceptionAlert(e);
        }
    }
}
