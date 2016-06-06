package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.javafx.control.AutoCompleteComboBox;
import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.decorator.account_management.AccountManagementDecorator;
import com.makco.smartfinance.user_interface.decorator.account_management.AccountManagementDecoratorAccount;
import com.makco.smartfinance.user_interface.decorator.account_management.AccountManagementDecoratorRoot;
import com.makco.smartfinance.user_interface.models.AccountManagementModel;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.undoredo.Memento;
import com.makco.smartfinance.user_interface.undoredo.UndoRedoScreen;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.utility_screens.forms.ProgressIndicatorForm;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by mcalancea on 2016-06-06.
 */
public class AccountManagementController implements Initializable, ControlledScreen, UndoRedoScreen {
    private final static Logger LOG = LogManager.getLogger(AccountManagementController.class);
    private final static int ACCOUNT_GROUP_TAB_INDEX = 0;
    private final static int ACCOUNT_TAB_INDEX = 1;
    private final static String ACCOUNT_TAB_NON_ACCOUNT_BGCOLOR = "-fx-background-color: rgb(201,201,201)";
    private ScreensController screensController;
    private AccountManagementModel accountManagementModel = new AccountManagementModel();

    private ActionEvent actionEvent;

    private Worker<Void> onDeleteAccountGroupWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveAccountGroupWorker;
    private Worker<Void> onRefreshAccountGroupWorker;
    private List<String> accountGroupTypeStringList = new ArrayList<>();

    private Worker<Void> onDeleteAccountWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveAccountWorker;
    private Worker<Void> onRefreshAccountWorker;

    private ProgressIndicatorForm pForm = new ProgressIndicatorForm();

    private int selectedTabIndex;
    private CareTaker careTaker;
    private BooleanProperty isNotUndo = new SimpleBooleanProperty(true);

    @FXML
    private TabPane tabPane;
    @FXML
    private TableView<AccountGroup> agTable;
    @FXML
    private AutoCompleteComboBox agTypeACCB;
    @FXML
    private TextField agNameTF;
    @FXML
    private TextArea agDescTA;
    @FXML
    private Button agClearBtn;
    @FXML
    private Button agSaveBtn;
    @FXML
    private Button agDeleteBtn;

    @FXML
    private TreeTableView<AccountManagementDecorator> aTable;
    @FXML
    private AutoCompleteComboBox aAccountGroupACCB;
    @FXML
    private TextField aNameTF;
    @FXML
    private TextArea aDescTA;
    @FXML
    private Button aClearBtn;
    @FXML
    private Button aSaveBtn;
    @FXML
    private Button aDeleteBtn;

    public AccountManagementController(){
        try{
            onDeleteAccountGroupWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            accountManagementModel.deletePendingAccountGroup();
                            return null;
                        }
                    };
                }
            };
            onSaveAccountGroupWorker = new Service<EnumSet<ErrorEnum>>() {
                @Override
                protected Task<EnumSet<ErrorEnum>> createTask() {
                    return new Task<EnumSet<ErrorEnum>>() {
                        @Override
                        protected EnumSet<ErrorEnum> call() throws Exception {
                            return accountManagementModel.savePendingAccountGroup(
                                    UserInterfaceConstants.convertAccountGroupTypeFromUIToBackend(agTypeACCB.getValue()),
                                    agNameTF.getText(),
                                    agDescTA.getText());
                        }
                    };
                }
            };
            onRefreshAccountGroupWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            accountManagementModel.refreshAccountGroupTab();
                            return null;
                        }
                    };
                }
            };
        }catch (Exception e){
            //not in finally because refreshAccountGroup must run before populateAccountGroupTable
            startService(onRefreshAccountGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }

        try{
            onDeleteAccountWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            accountManagementModel.deletePendingAccount();
                            return null;
                        }
                    };
                }
            };
            onSaveAccountWorker = new Service<EnumSet<ErrorEnum>>() {
                @Override
                protected Task<EnumSet<ErrorEnum>> createTask() {
                    return new Task<EnumSet<ErrorEnum>>() {
                        @Override
                        protected EnumSet<ErrorEnum> call() throws Exception {
                            return accountManagementModel.savePendingAccount(
                                    accountManagementModel.convertAccountGroupFromUIToBackendTo(aAccountGroupACCB.getValue().toString()),
                                    aNameTF.getText(),
                                    aDescTA.getText());
                        }
                    };
                }
            };
            onRefreshAccountWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            accountManagementModel.refreshAccountTab();
                            return null;
                        }
                    };
                }
            };
        }catch (Exception e){
            //not in finally because refreshAccount must run before populateAccountGroupTable
            startService(onRefreshAccountWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    public void initializeServices(){
        try{
            ((Service<Void>) onDeleteAccountGroupWorker).setOnSucceeded(event -> {
                LOG.debug("onDeleteAccountGroupWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onDeleteAccountGroupWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountGroupTable();
            });
            ((Service<Void>) onDeleteAccountGroupWorker).setOnFailed(event -> {
                LOG.debug("onDeleteAccountGroupWorker->setOnFailed");
                LOG.debug(">>>>>>>>onDeleteAccountGroupWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountGroupTable();
                DialogMessages.showExceptionAlert(onDeleteAccountGroupWorker.getException());
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveAccountGroupWorker).setOnSucceeded(event -> {
                LOG.debug("onSaveAccountGroupWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onSaveAccountGroupWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountGroupTable();
                EnumSet<ErrorEnum> errors = onSaveAccountGroupWorker.getValue();
                if(!errors.isEmpty()) {
                    DialogMessages.showErrorDialog("Error while saving Account Group: type "
                                    + agTypeACCB.getValue() + ", with name " + agNameTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveAccountGroupWorker).getValue(), null);
                }
                onClearAccountGroup(actionEvent);
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveAccountGroupWorker).setOnFailed(event -> {
                LOG.debug("onSaveAccountGroupWorker->setOnFailed");
                LOG.debug(">>>>>>>>onSaveAccountGroupWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountGroupTable();
                DialogMessages.showExceptionAlert(onSaveAccountGroupWorker.getException());
            });
            ((Service<Void>) onRefreshAccountGroupWorker).setOnSucceeded(event -> {
                LOG.debug("onRefreshAccountGroupWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onRefreshAccountGroupWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountGroupTable();
            });
            ((Service<Void>) onRefreshAccountGroupWorker).setOnFailed(event -> {
                LOG.debug("onRefreshAccountGroupWorker->setOnFailed");
                LOG.debug(">>>>>>>>onRefreshAccountGroupWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountGroupTable();
                DialogMessages.showExceptionAlert(onRefreshAccountGroupWorker.getException());
            });
        }catch (Exception e){
            //not in finally because refreshAccountGroup must run before populateAccountGroupTable
            startService(onRefreshAccountGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }

        try{
            ((Service<Void>) onDeleteAccountWorker).setOnSucceeded(event -> {
                LOG.debug("onDeleteAccountWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onDeleteAccountWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountTable();
            });
            ((Service<Void>) onDeleteAccountWorker).setOnFailed(event -> {
                LOG.debug("onDeleteAccountWorker->setOnFailed");
                LOG.debug(">>>>>>>>onDeleteAccountWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountTable();
                DialogMessages.showExceptionAlert(onDeleteAccountWorker.getException());
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveAccountWorker).setOnSucceeded(event -> {
                LOG.debug("onSaveAccountWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onSaveAccountWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountTable();
                EnumSet<ErrorEnum> errors = onSaveAccountWorker.getValue();
                if(!errors.isEmpty()) {
                    DialogMessages.showErrorDialog("Error while saving Account: account group "
                                    + aAccountGroupACCB.getValue() + ", with name " + aNameTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveAccountWorker).getValue(), null);
                }
                onClearAccount(actionEvent);
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveAccountWorker).setOnFailed(event -> {
                LOG.debug("onSaveAccountWorker->setOnFailed");
                LOG.debug(">>>>>>>>onSaveAccountWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountTable();
                DialogMessages.showExceptionAlert(onSaveAccountWorker.getException());
            });
            ((Service<Void>) onRefreshAccountWorker).setOnSucceeded(event -> {
                LOG.debug("onRefreshAccountWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onRefreshAccountWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountTable();
                aAccountGroupACCB.setItems(accountManagementModel.getAccountGroupUIName());
            });
            ((Service<Void>) onRefreshAccountWorker).setOnFailed(event -> {
                LOG.debug("onRefreshAccountWorker->setOnFailed");
                LOG.debug(">>>>>>>>onRefreshAccountWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateAccountTable();
                DialogMessages.showExceptionAlert(onRefreshAccountWorker.getException());
                aAccountGroupACCB.setItems(accountManagementModel.getAccountGroupUIName());
            });
        }catch (Exception e){
            //not in finally because refreshAccountGroup must run before populateAccountGroupTable
            startService(onRefreshAccountWorker,null);
            DialogMessages.showExceptionAlert(e);
        }

//        getsc
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
            screensController = screenPage;
            careTaker = screensController.getCareTaker();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void refresh() {
        try{
            LOG.debug("contoller->refresh");
            careTaker.clear();
            initializeServices();
            startService(onRefreshAccountGroupWorker, null);
            startService(onRefreshAccountWorker, null);
            agTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFormAccountGroup();
                }
            });
            agTypeACCB.setItems(FXCollections.observableList(getAccountGroupTypeStringList()));
//            aAccountGroupACCB.setItems(accountManagementModel.getAccountGroupUIName());
            aTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null && newSelection.getValue() instanceof AccountManagementDecoratorAccount) {
                    populateFormAccount();
                } else {
                    onClearAccount(null);
                }
            });
            screensController.setToolbar_Save(new Command() {
                @Override
                public void execute() {
                    try {
                        if(tabPane.getSelectionModel().getSelectedIndex() == ACCOUNT_GROUP_TAB_INDEX) {
                            LOG.debug("AccountGroup->onSave");
                            startService(onSaveAccountGroupWorker, new ActionEvent());
                        } else if(tabPane.getSelectionModel().getSelectedIndex() == ACCOUNT_TAB_INDEX) {
                            LOG.debug("Account->onSave");
                            startService(onSaveAccountWorker, new ActionEvent());
                        }
                    } catch (Exception e) {
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
        }catch (Exception e){
            startService(onRefreshAccountGroupWorker,null);
            //todo throw exception here to see if startService for account is needed
//            startService(onRefreshAccountWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private List<String> getAccountGroupTypeStringList(){
        if(accountGroupTypeStringList.size() == 0){
            for(DataBaseConstants.ACCOUNT_GROUP_TYPE account_group_type : DataBaseConstants.ACCOUNT_GROUP_TYPE.values()){
                accountGroupTypeStringList.add(
                        UserInterfaceConstants.convertAccountGroupTypeFromBackendToUI(account_group_type.toString()));
            }
        }

        return accountGroupTypeStringList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try{
            tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                selectedTabIndex = (int) newValue;
                careTaker.clear();
                if(oldValue != newValue){
                    onClearAccountGroup(null);
                    onClearAccount(null);

                    if((int)newValue == ACCOUNT_TAB_INDEX){
                        startService(onRefreshAccountWorker, null);
                    }
                }

            });
        }catch (Exception e){
            //not in finally because refreshAccountGroup must run before populateAccountGroupTable
            DialogMessages.showExceptionAlert(e);
        }

        try {
            //http://javafx-and-me.blogspot.ca/2013/10/custom-combobox.html
            //to show string instead of classes
//            agTypeACCB.setItems(FXCollections.observableList(getAccountGroupTypeStringList()));
//            agTypeACCB.setPrefWidth(DataBaseConstants.Cgtype_CODE_MAX_LGTH);
            agTypeACCB.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            agNameTF.setPrefColumnCount(DataBaseConstants.CG_NAME_MAX_LGTH);
            agNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            agDescTA.setPrefColumnCount(DataBaseConstants.CG_DESCRIPTION_MAX_LGTH);
            agDescTA.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });
            agClearBtn.setDisable(true);
            agSaveBtn.setDisable(false);
            agDeleteBtn.setDisable(true);
        } catch (Exception e) {
            //not in finally because refreshAccountGroup must run before populateAccountGroupTable
            startService(onRefreshAccountGroupWorker, null);
            DialogMessages.showExceptionAlert(e);
        }

        try {
//            aAccountGroupACCB.setItems(accountManagementModel.getAccountGroupsWithoutCategories());
            aAccountGroupACCB.setEditable(true);
//            aAccountGroupACCB.setConverter(new StringConverter<AccountGroup>() {
//
//                @Override
//                public String toString(AccountGroup object) {
//                    if (object == null) {
//                        return null;
//                    }
//                    return accountManagementModel.convertAccountGroupFromBackendToUI(object);
//                }
//
//                @Override
//                public AccountGroup fromString(String string) {
//                    if(StringUtils.isEmpty(string)) {
//                        return null;
//                    }
//                    return accountManagementModel.convertAccountGroupFromUIToBackendTo(string);
//                }
//            });
            aAccountGroupACCB.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            aNameTF.setPrefColumnCount(DataBaseConstants.CAT_NAME_MAX_LGTH);
            aNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            aDescTA.setPrefColumnCount(DataBaseConstants.CAT_DESCRIPTION_MAX_LGTH);
            aDescTA.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });
            aClearBtn.setDisable(true);
            aSaveBtn.setDisable(false);
            aDeleteBtn.setDisable(true);
        } catch (Exception e) {
            //not in finally because onRefreshAccountWorker must run before populateAccountTable
            startService(onRefreshAccountWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClearAccountGroup(ActionEvent event){
        try{
            agTypeACCB.setValue(getAccountGroupTypeStringList().get(0));
            agNameTF.clear();
            agDescTA.clear();
            agClearBtn.setDisable(false);
            agSaveBtn.setDisable(false);
            agDeleteBtn.setDisable(true);
            careTaker.clear();
        }catch (Exception e){
            startService(onRefreshAccountGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSaveAccountGroup(ActionEvent event){
        try {
            LOG.debug("Account Group->onSave");
            startService(onSaveAccountGroupWorker, event);
            careTaker.clear();
        } catch (Exception e) {
            //no refreshAccountGroupTab() because there are in deletePendingAccountGroup, populateAccountGroupTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onDeleteAccountGroup(ActionEvent event){
        try {
            String title = UserInterfaceConstants.ACCOUNT_MANAGEMENT_WINDOW_TITLE;
            String headerText = "Account Group Deletion";
            StringBuilder contentText = new StringBuilder("Are you sure you want to delete account group with type");
            contentText.append("\"");
            contentText.append(agTypeACCB.getValue());
            contentText.append("\" and name \"");
            contentText.append(agNameTF.getText());
            contentText.append("\"?");
            if(DialogMessages.showConfirmationDialog(title,headerText,contentText.toString(),null)) {
                startService(onDeleteAccountGroupWorker, event);
                populateAccountGroupTable();
                onClearAccountGroup(actionEvent);
            }
        } catch (Exception e) {
            //no refreshAccountGroupTab() because there are in deletePendingAccountGroup, populateAccountGroupTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClearAccount(ActionEvent event){
        try{
            aAccountGroupACCB.setValue(accountManagementModel.getAccountGroupUIName().get(0));
            aNameTF.clear();
            aDescTA.clear();
            aClearBtn.setDisable(false);
            aSaveBtn.setDisable(false);
            aDeleteBtn.setDisable(true);
            careTaker.clear();
        }catch (Exception e){
            startService(onRefreshAccountWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSaveAccount(ActionEvent event){
        try {
            LOG.debug("Account->onSave");
            startService(onSaveAccountWorker, event);
            careTaker.clear();
        } catch (Exception e) {
            //no refreshAccountTab() because there are in deletePendingAccount, populateAccountTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onDeleteAccount(ActionEvent event){
        try {
            String title = UserInterfaceConstants.ACCOUNT_MANAGEMENT_WINDOW_TITLE;
            String headerText = "Account Deletion";
            StringBuilder contentText = new StringBuilder("Are you sure you want to delete account with account group");
            contentText.append("\"");
            contentText.append(aAccountGroupACCB.getValue());
            contentText.append("\" and name \"");
            contentText.append(aNameTF.getText());
            contentText.append("\"?");
            if(DialogMessages.showConfirmationDialog(title,headerText,contentText.toString(),null)) {
                startService(onDeleteAccountWorker, event);
                populateAccountTable();
                onClearAccount(actionEvent);
            }
        } catch (Exception e) {
            //no refreshAccountTab() because there are in deletePendingAccount, populateAccountTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateFormAccountGroup(){
        try{
            agClearBtn.setDisable(false);
            agSaveBtn.setDisable(false);
            agDeleteBtn.setDisable(false);
            accountManagementModel.setPendingAccountGroupProperty(agTable.getSelectionModel().getSelectedItem());

            agTypeACCB.setValue(
                    UserInterfaceConstants.convertAccountGroupTypeFromBackendToUI(
                            accountManagementModel.getPendingAccountGroup().getAccountGroupType().getDiscriminator())
            );
            agNameTF.setText(accountManagementModel.getPendingAccountGroup().getName());
            agDescTA.setText(accountManagementModel.getPendingAccountGroup().getDescription());
        }catch (Exception e){
            startService(onRefreshAccountGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateFormAccount(){
        try{
            aClearBtn.setDisable(false);
            aSaveBtn.setDisable(false);
            aDeleteBtn.setDisable(false);
            accountManagementModel.setPendingAccountProperty(aTable.getSelectionModel().getSelectedItem().getValue().getAccount());

//            aAccountGroupACCB.setValue(
//                    accountManagementModel.convertAccountGroupFromBackendToUI(
//                            accountManagementModel.getPendingAccount().getAccountGroup())
//            );
            aAccountGroupACCB.setValue(
                accountManagementModel.convertAccountGroupFromBackendToUI(accountManagementModel.getPendingAccount().getAccountGroup())
            );
            aNameTF.setText(accountManagementModel.getPendingAccount().getName());
            aDescTA.setText(accountManagementModel.getPendingAccount().getDescription());
        }catch (Exception e){
            startService(onRefreshAccountWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateAccountGroupTable(){
        try{
            LOG.debug("accountManagementModel.getAccountGroupsWithoutCategories().size():" + accountManagementModel.getAccountGroupsWithoutCategories().size());
            agTable.getItems().clear();
            agTable.setItems(accountManagementModel.getAccountGroupsWithoutCategories());

            TableColumn<AccountGroup, Long> accountGroupIdCol = new TableColumn<>("ID");
            accountGroupIdCol.setCellValueFactory(new PropertyValueFactory<AccountGroup, Long>("id"));

            TableColumn<AccountGroup, String> accountGroupTypeCol = new TableColumn<>("Type");
            accountGroupTypeCol.setCellValueFactory(cg -> new SimpleStringProperty(
                    UserInterfaceConstants.convertAccountGroupTypeFromBackendToUI(
                            cg.getValue().getAccountGroupType().getDiscriminator())));

            TableColumn<AccountGroup, String> accountGroupNameCol = new TableColumn<>("Name");
            accountGroupNameCol.setCellValueFactory(new PropertyValueFactory<AccountGroup, String>("name"));

            TableColumn<AccountGroup, String> accountGroupDescCol = new TableColumn<>("Description");
            accountGroupDescCol.setCellValueFactory(new PropertyValueFactory<AccountGroup, String>("description"));

            TableColumn<AccountGroup, Calendar> accountGroupCreatedCol = new TableColumn<>("Created on");
            accountGroupCreatedCol.setCellValueFactory(new PropertyValueFactory<AccountGroup, Calendar>("createdOn"));

            TableColumn<AccountGroup, Calendar> accountGroupUpdatedCol = new TableColumn<>("Updated on");
            accountGroupUpdatedCol.setCellValueFactory(new PropertyValueFactory<AccountGroup, Calendar>("updatedOn"));

            agTable.getColumns().setAll(accountGroupIdCol, accountGroupTypeCol, accountGroupNameCol,
                    accountGroupDescCol, accountGroupCreatedCol, accountGroupUpdatedCol);

        }catch (Exception e){
            startService(onRefreshAccountGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    /**
     * todo after save table is not updated
     */

    private void populateAccountTable(){
        try{
            LOG.debug("accountManagementModel.getCategories().size():" + accountManagementModel.getAccountManagementDecoratorMultimap().size());
//            aTable.getItems().clear();
            aTable.setRoot(null);
//            aTable.setItems(accountManagementModel.getCategories());

            TreeItem<AccountManagementDecorator> rootNode = new TreeItem<>(new AccountManagementDecoratorRoot(UserInterfaceConstants.ACCOUNT_ROOT_NODE));
            for(Map.Entry<AccountManagementDecorator, Collection<AccountManagementDecorator>>  accountGroupEntry :
                accountManagementModel.getAccountManagementDecoratorMultimap().asMap().entrySet()){
                TreeItem<AccountManagementDecorator> accountGroupNode = new TreeItem<>(accountGroupEntry.getKey());
                for(AccountManagementDecorator accountDecorators : accountGroupEntry.getValue()){
                    TreeItem<AccountManagementDecorator> accountNode = new TreeItem<>(accountDecorators);
                    accountGroupNode.getChildren().add(accountNode);
                }
                rootNode.getChildren().add(accountGroupNode);
            }
            aTable.setRoot(rootNode);

            TreeTableColumn<AccountManagementDecorator, Long> accountIdCol = new TreeTableColumn<>("ID");
            accountIdCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<AccountManagementDecorator,Long> param) ->
                            new ReadOnlyObjectWrapper<Long>(param.getValue().getValue().getId())
            );
            accountIdCol.setCellFactory(column -> {
                return new CustomTreeTableCell<Long>();
            });
//                return new TreeTableCell<AccountManagementDecorator, Long>(){
//                    @Override
//                    protected void updateItem(Long item, boolean empty) {
//                        if(getTreeTableRow().getItem() instanceof AccountManagementDecoratorAccount){
//                            setText(item.toString());
//                            setStyle("");
//                        } else {
//                            setStyle(ACCOUNT_TAB_NON_ACCOUNT_BGCOLOR);
//                        }
//                    }
//
//
//                };
//            });

            TreeTableColumn<AccountManagementDecorator, String> accountTypeCol = new TreeTableColumn<>("Type");
            accountTypeCol.setCellValueFactory(c -> new SimpleStringProperty(
                UserInterfaceConstants.convertAccountGroupTypeFromBackendToUI(
                        c.getValue().getValue().getAccountGroupType()
                )
            ));
            accountTypeCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

//            TreeTableColumn<AccountManagementDecorator, String> accountAccountGroupCol = new TreeTableColumn<>("Account Group");
//            accountAccountGroupCol.setCellValueFactory(c -> new SimpleStringProperty(
//                    accountManagementModel.convertAccountGroupFromBackendToUI(
//                            c.getValue().getValue().getAccountGroup()
//                    )
//            ));

            TreeTableColumn<AccountManagementDecorator, String> accountNameCol = new TreeTableColumn<>("Name");
            accountNameCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<AccountManagementDecorator, String> param) ->
                            new ReadOnlyStringWrapper(param.getValue().getValue().getName())
            );
            accountNameCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });
//            accountNameCol.setCellFactory(column -> {
//                return new TreeTableCell<AccountManagementDecorator, String>(){
//                    @Override
//                    protected void updateItem(String item, boolean empty) {
//                        setText(item);
//                        if(getTreeTableRow().getItem() instanceof AccountManagementDecoratorAccount){
//                            setStyle("");
//                        } else {
//                            setStyle(ACCOUNT_TAB_NON_ACCOUNT_BGCOLOR);
//                        }
//                    }
//
//
//                };
//            });

            TreeTableColumn<AccountManagementDecorator, String> accountDescCol = new TreeTableColumn<>("Description");
            accountDescCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<AccountManagementDecorator, String> param) ->
                            new ReadOnlyStringWrapper(param.getValue().getValue().getDescription())
            );
            accountDescCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

            TreeTableColumn<AccountManagementDecorator, String> accountCreatedCol = new TreeTableColumn<>("Created on");
            accountCreatedCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<AccountManagementDecorator, String> param) -> {
                    return new ReadOnlyObjectWrapper<String>(param.getValue().getValue().getCreatedOn());
                }
            );
            accountCreatedCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

            TreeTableColumn<AccountManagementDecorator, String> accountUpdatedCol = new TreeTableColumn<>("Updated on");
            accountUpdatedCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<AccountManagementDecorator, String> param) -> {
                        return new ReadOnlyObjectWrapper<String>(param.getValue().getValue().getUpdatedOn());
                    }
            );
            accountUpdatedCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

            aTable.getColumns().setAll(accountIdCol, accountTypeCol, accountNameCol,
                    accountDescCol, accountCreatedCol, accountUpdatedCol);
        }catch (Exception e){
            startService(onRefreshAccountWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void saveForm() {
        try {
            if(selectedTabIndex == ACCOUNT_GROUP_TAB_INDEX) {
                careTaker.saveState(new AccountGroupFormState(agTypeACCB.getValue(), agNameTF.getText(), agDescTA.getText()));
            }else if(selectedTabIndex == ACCOUNT_TAB_INDEX) {
                careTaker.saveState(new AccountFormState(aAccountGroupACCB.getValue(), aNameTF.getText(), aDescTA.getText()));
            }
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void restoreFormState(Memento memento) {
        try {
            if(selectedTabIndex == ACCOUNT_GROUP_TAB_INDEX) {
                AccountGroupFormState formState = (AccountGroupFormState) memento;
                agTypeACCB.setValue(formState.getCgTypeACCBStr());
                agNameTF.setText(formState.getCgNameTFStr());
                agDescTA.setText(formState.getCgDescTAStr());
            }else if(selectedTabIndex == ACCOUNT_TAB_INDEX) {
                AccountFormState formState = (AccountFormState) memento;
//                aAccountGroupACCB.setValue(formState.getCAccountGroupACCBStr());
//                aAccountGroupACCB.setValue(accountManagementModel.convertAccountGroupFromUIToBackendTo(formState.getCAccountGroupACCBStr()));
                aAccountGroupACCB.setValue(formState.getCAccountGroupACCB());
                aNameTF.setText(formState.getCNameTFStr());
                aDescTA.setText(formState.getCDescTAStr());
            }
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void close() {
        try {
            onClearAccountGroup(new ActionEvent());
            onClearAccount(new ActionEvent());
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
            if(!StringUtils.isBlank(agNameTF.getText())) {
                contentText.append("Account Group Name (");
                contentText.append(agNameTF.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(agDescTA.getText())) {
                contentText.append("Account Group Description (");
                contentText.append(agDescTA.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(aNameTF.getText())) {
                contentText.append("Account Name (");
                contentText.append(aNameTF.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(aDescTA.getText())) {
                contentText.append("Account Description (");
                contentText.append(aDescTA.getText());
                contentText.append(") is not saved. ");
            }

            if(contentText.length() > 0){
                contentText.append("Are you sure you want to close this window?");
                result = DialogMessages.showConfirmationDialog(UserInterfaceConstants.ACCOUNT_MANAGEMENT_WINDOW_TITLE,
                        "Not all fields are empty", contentText.toString(), null);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return result;
    }

    private static class AccountGroupFormState implements Memento{
        private final String cgTypeACCBStr;
        private final String cgNameTFStr;
        private final String cgDescTAStr;

        public AccountGroupFormState(String cgTypeACCB, String cgNameTF, String cgDescTA){
            this.cgTypeACCBStr = cgTypeACCB;
            this.cgNameTFStr = cgNameTF;
            this.cgDescTAStr = cgDescTA;
        }

        public String getCgTypeACCBStr() {
            return cgTypeACCBStr;
        }

        public String getCgNameTFStr() {
            return cgNameTFStr;
        }

        public String getCgDescTAStr() {
            return cgDescTAStr;
        }

        @Override
        public String toString() {
            return "AccountGroupFormState{" +
                    "cgTypeACCBStr='" + cgTypeACCBStr + '\'' +
                    ", cgNameTFStr='" + cgNameTFStr + '\'' +
                    ", cgDescTAStr='" + cgDescTAStr + '\'' +
                    '}';
        }
    }

    private static class AccountFormState implements Memento{
        private final String cAccountGroupACCBStr;
        private final String cNameTFStr;
        private final String cDescTAStr;

        public AccountFormState(String cAccountGroupACCB, String cNameTF, String cDescTA){
            this.cAccountGroupACCBStr = cAccountGroupACCB;
            this.cNameTFStr = cNameTF;
            this.cDescTAStr = cDescTA;
        }

        public String getCAccountGroupACCB() {
            return cAccountGroupACCBStr;
        }

        public String getCNameTFStr() {
            return cNameTFStr;
        }

        public String getCDescTAStr() {
            return cDescTAStr;
        }

        @Override
        public String toString() {
            return "AccountFormState{" +
                    "cAccountGroupACCBStr='" + cAccountGroupACCBStr + '\'' +
                    ", cNameTFStr='" + cNameTFStr + '\'' +
                    ", cDescTAStr='" + cDescTAStr + '\'' +
                    '}';
        }
    }

    private static class CustomTreeTableCell <T> extends TreeTableCell<AccountManagementDecorator, T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            if(item != null) {
                setText(item.toString());
            }
            if (getTreeTableRow().getItem() instanceof AccountManagementDecoratorAccount) {
                setStyle("");
            } else {
                setStyle(ACCOUNT_TAB_NON_ACCOUNT_BGCOLOR);
            }
        }
    }
}