package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.models.OrganizationModel;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.undoredo.Memento;
import com.makco.smartfinance.user_interface.undoredo.UndoRedoScreen;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.utility_screens.forms.ProgressIndicatorForm;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.ResourceBundle;

/**
 * Created by mcalancea on 2016-04-01.
 */
//http://www.devx.com/Java/Article/48193/0/page/2
public class OrganizationController implements Initializable, ControlledScreen, UndoRedoScreen {
    private final static Logger LOG = LogManager.getLogger(OrganizationController.class);
    private ScreensController screensController;
    private OrganizationModel organizationModel = new OrganizationModel();

    private ActionEvent actionEvent;
    private Worker<Void> onDeleteWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveWorker;
    private Worker<Void> onRefreshWorker;
    private ProgressIndicatorForm pForm = new ProgressIndicatorForm();

    private CareTaker careTaker;
    private BooleanProperty isNotUndo = new SimpleBooleanProperty(true);

    @FXML
    private TableView<Organization> table;
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

    public OrganizationController() {
        try {
            onDeleteWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            organizationModel.deletePendingOrganization();
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
                            return organizationModel.savePendingOrganization(nameTF.getText(), descTA.getText());
                        }
                    };
                }
            };
            onRefreshWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            organizationModel.refreshOrganization();
                            return null;
                        }
                    };
                }
            };
        } catch (Exception e) {
            //not in finally because refreshOrganization must run before populateTable
            startService(onRefreshWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    public void initializeServices() {
        try {
            ((Service<Void>) onDeleteWorker).setOnSucceeded(event -> {
                LOG.debug("onDeleteWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onDeleteWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
            });
            ((Service<Void>) onDeleteWorker).setOnFailed(event -> {
                LOG.debug("onDeleteWorker->setOnFailed");
                LOG.debug(">>>>>>>>onDeleteWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
                DialogMessages.showExceptionAlert(onDeleteWorker.getException());
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveWorker).setOnSucceeded(event -> {
                LOG.debug("onSaveWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onSaveWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
                EnumSet<ErrorEnum> errors = ((Service<EnumSet<ErrorEnum>>) onSaveWorker).getValue();
                if (!errors.isEmpty()) {
                    DialogMessages.showErrorDialog("Error while saving Organization: " + nameTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveWorker).getValue(), null);
                }
                onClear(actionEvent);
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveWorker).setOnFailed(event -> {
                LOG.debug("onSaveWorker->setOnFailed");
                LOG.debug(">>>>>>>>onSaveWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
                DialogMessages.showExceptionAlert(onSaveWorker.getException());
            });
            ((Service<Void>) onRefreshWorker).setOnSucceeded(event -> {
                LOG.debug("onRefreshWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onRefreshWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
            });
            ((Service<Void>) onRefreshWorker).setOnFailed(event -> {
                LOG.debug("onRefreshWorker->setOnFailed");
                LOG.debug(">>>>>>>>onRefreshWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateTable();
                DialogMessages.showExceptionAlert(onRefreshWorker.getException());
            });
        } catch (Exception e) {
            //not in finally because refreshOrganization must run before populateTable
            startService(onRefreshWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private <V> void startService(Worker<V> worker, ActionEvent event) {
        pForm.activateProgressBar(worker);
        LOG.debug(">>>>>>>>pForm.getDialogStage().show()");
        pForm.show();
        actionEvent = event;

        Service<V> service = ((Service<V>) worker);
        if (service == null) {
            LOG.debug("service IS NULL");
        } else {
            LOG.debug("service IS NOT NULL");
            ((Service<V>) worker).restart();
        }
    }

    @Override
    public void setScreenPage(ScreensController screenPage) {
        try {
            screensController = screenPage;
            careTaker = screensController.getCareTaker();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void refresh() {
        try {
            careTaker.clear();
            initializeServices();
            startService(onRefreshWorker, null);
            table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateForm();
                }
            });

            screensController.setToolbar_Save(new Command() {
                @Override
                public void execute() {
                    try {
                        LOG.debug("OrganizationController->onSave");
                        startService(onSaveWorker, new ActionEvent());
                    } catch (Exception e) {
                        //no refreshOrganization() because there are in deletePendingOrganization, populateTable, onClear
                        DialogMessages.showExceptionAlert(e);
                    }
                }
            });
            screensController.setToolbar_Undo(new Command() {
                @Override
                public void execute() {
                    isNotUndo.setValue(false);
                    restoreFormState(careTaker.undoState());
                }
            });
            screensController.setToolbar_Redo(() -> {
                        isNotUndo.setValue(false);
                        restoreFormState(careTaker.redoState());
                    }
            );
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            nameTF.setPrefColumnCount(DataBaseConstants.ORG_NAME_MAX_LGTH);
            nameTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            descTA.setPrefColumnCount(DataBaseConstants.ORG_DESCRIPTION_MAX_LGTH);
            descTA.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });
            clearBtn.setDisable(true);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        } catch (Exception e) {
            //not in finally because refreshOrganization must run before populateTable
            startService(onRefreshWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClear(ActionEvent event) {
        try {
            nameTF.clear();
            descTA.clear();
            clearBtn.setDisable(false);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
            careTaker.clear();
        } catch (Exception e) {
            startService(onRefreshWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSave(ActionEvent event) {
        try {
            LOG.debug("OrganizationController->onSave");
            startService(onSaveWorker, event);
            careTaker.clear();
        } catch (Exception e) {
            //no refreshOrganization() because there are in deletePendingOrganization, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onDelete(ActionEvent event) {
        try {
            String title = UserInterfaceConstants.ORGANIZATION_WINDOW_TITLE;
            String headerText = "Organization Deletion";
            StringBuilder contentText = new StringBuilder("Are you sure you want to delete organization ");
            contentText.append("\"");
            contentText.append(nameTF.getText());
            contentText.append("\"?");
            if (DialogMessages.showConfirmationDialog(title, headerText, contentText.toString(), null)) {
                startService(onDeleteWorker, event);
                populateTable();
                onClear(actionEvent);
            }
        } catch (Exception e) {
            //no refreshOrganization() because there are in deletePendingOrganization, populateTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateForm() {
        try {
            clearBtn.setDisable(false);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(false);
            organizationModel.setPendingOrganizationProperty(table.getSelectionModel().getSelectedItem());

            nameTF.setText(organizationModel.getPendingOrganization().getName());
            descTA.setText(organizationModel.getPendingOrganization().getDescription());
        } catch (Exception e) {
            startService(onRefreshWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateTable() {
        try {
            LOG.debug("organizationModel.getOrganizations().size():" + organizationModel.getOrganizations().size());
            table.getItems().clear();
            table.setItems(organizationModel.getOrganizations());

            TableColumn<Organization, Long> organizationIdCol = new TableColumn<>("ID");
            organizationIdCol.setCellValueFactory(new PropertyValueFactory<Organization, Long>("id"));

            TableColumn<Organization, String> organizationNameCol = new TableColumn<>("Name");
            organizationNameCol.setCellValueFactory(new PropertyValueFactory<Organization, String>("name"));

            TableColumn<Organization, String> organizationDescCol = new TableColumn<>("Description");
            organizationDescCol.setCellValueFactory(new PropertyValueFactory<Organization, String>("description"));

            TableColumn<Organization, Calendar> organizationCreatedCol = new TableColumn<>("Created on");
            organizationCreatedCol.setCellValueFactory(new PropertyValueFactory<Organization, Calendar>("createdOn"));

            TableColumn<Organization, Calendar> organizationUpdatedCol = new TableColumn<>("Updated on");
            organizationUpdatedCol.setCellValueFactory(new PropertyValueFactory<Organization, Calendar>("updatedOn"));

            table.getColumns().setAll(organizationIdCol, organizationNameCol, organizationDescCol, organizationCreatedCol, organizationUpdatedCol);
        } catch (Exception e) {
            startService(onRefreshWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void saveForm() {
        try {
            careTaker.saveState(new OrganizationFormState(nameTF.getText(), descTA.getText()));
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void restoreFormState(Memento memento) {
        try {
            OrganizationFormState formState = (OrganizationFormState) memento;
            nameTF.setText(formState.getNameTFStr());
            descTA.setText(formState.getDescTAStr());
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void close(){
        try {
            onClear(new ActionEvent());
            screensController.setToolbar_Save(null);
            screensController.setToolbar_Undo(null);
            screensController.setToolbar_Redo(null);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public boolean askPermissionToClose() {
        boolean result = true;
        try{
            StringBuilder contentText = new StringBuilder();
            if(!StringUtils.isBlank(nameTF.getText())) {
                contentText.append("Name (");
                contentText.append(nameTF.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(descTA.getText())) {
                contentText.append("Description (");
                contentText.append(descTA.getText());
                contentText.append(") is not saved. ");
            }

            if(contentText.length() > 0){
                contentText.append("Are you sure you want to close this window?");
                result = DialogMessages.showConfirmationDialog(UserInterfaceConstants.ORGANIZATION_WINDOW_TITLE,
                        "Not all fields are empty", contentText.toString(), null);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return result;
    }

    private static class OrganizationFormState implements Memento{
        private final String nameTFStr;
        private final String descTAStr;

        public OrganizationFormState(String nameTF, String descTA){
            this.nameTFStr = nameTF;
            this.descTAStr = descTA;
        }

        public String getNameTFStr() {
            return nameTFStr;
        }

        public String getDescTAStr() {
            return descTAStr;
        }

        @Override
        public String toString() {
            return "OrganizationFormState{" +
                    "nameTF='" + nameTFStr + '\'' +
                    ", descTA='" + descTAStr + '\'' +
                    '}';
        }
    }
}
