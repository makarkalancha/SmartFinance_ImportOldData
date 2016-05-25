package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.javafx.control.AutoCompleteComboBox;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.models.CategoryManagementModel;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.undoredo.Memento;
import com.makco.smartfinance.user_interface.undoredo.UndoRedoScreen;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.utility_screens.forms.ProgressIndicatorForm;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.beans.property.BooleanProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by mcalancea on 2016-05-13.
 */
public class CategoryManagementController implements Initializable, ControlledScreen, UndoRedoScreen {
    private final static Logger LOG = LogManager.getLogger(CategoryManagementController.class);
    private ScreensController myController;
    private CategoryManagementModel categoryManagementModel = new CategoryManagementModel();

    private ActionEvent actionEvent;
    private Worker<Void> onDeleteCategoryGroupWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveCategoryGroupWorker;
    private Worker<Void> onRefreshCategoryGroupWorker;
    private List<String> categoryGroupTypeStringList = new ArrayList<>();

    private ProgressIndicatorForm pForm = new ProgressIndicatorForm();

    private CareTaker careTaker;
    private BooleanProperty isNotUndo = new SimpleBooleanProperty(true);

    @FXML
    private TableView<CategoryGroup> cgTable;
    @FXML
    private AutoCompleteComboBox cgTypeACCB;
    @FXML
    private TextField cgNameTF;
    @FXML
    private TextArea cgDescTA;
    @FXML
    private Button cgClearBtn;
    @FXML
    private Button cgSaveBtn;
    @FXML
    private Button cgDeleteBtn;

    public CategoryManagementController(){
        try{
            onDeleteCategoryGroupWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            categoryManagementModel.deletePendingCategoryGroup();
                            return null;
                        }
                    };
                }
            };
            onSaveCategoryGroupWorker = new Service<EnumSet<ErrorEnum>>() {
                @Override
                protected Task<EnumSet<ErrorEnum>> createTask() {
                    return new Task<EnumSet<ErrorEnum>>() {
                        @Override
                        protected EnumSet<ErrorEnum> call() throws Exception {
                            return categoryManagementModel.savePendingCategoryGroup(
                                    UserInterfaceConstants.convertCategoryGroupTypeFromUIToBackend(cgTypeACCB.getValue()),
                                    cgNameTF.getText(),
                                    cgDescTA.getText());
                        }
                    };
                }
            };
            onRefreshCategoryGroupWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            categoryManagementModel.refresh();
                            return null;
                        }
                    };
                }
            };
        }catch (Exception e){
            //not in finally because refreshCategoryGroup must run before populateCategoryGroupTable
            startService(onRefreshCategoryGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    public void initializeServices(){
        try{
            ((Service<Void>) onDeleteCategoryGroupWorker).setOnSucceeded(event -> {
                LOG.debug("onDeleteCategoryGroupWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onDeleteCategoryGroupWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryGroupTable();
            });
            ((Service<Void>) onDeleteCategoryGroupWorker).setOnFailed(event -> {
                LOG.debug("onDeleteCategoryGroupWorker->setOnFailed");
                LOG.debug(">>>>>>>>onDeleteCategoryGroupWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryGroupTable();
                DialogMessages.showExceptionAlert(onDeleteCategoryGroupWorker.getException());
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveCategoryGroupWorker).setOnSucceeded(event -> {
                LOG.debug("onSaveCategoryGroupWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onSaveCategoryGroupWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryGroupTable();
                EnumSet<ErrorEnum> errors = onSaveCategoryGroupWorker.getValue();
                if(!errors.isEmpty()) {
                    DialogMessages.showErrorDialog("Error while saving Category Group: type "
                                    + cgTypeACCB.getValue() + ", with name " + cgNameTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveCategoryGroupWorker).getValue(), null);
                }
                onClearCategoryGroup(actionEvent);
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveCategoryGroupWorker).setOnFailed(event -> {
                LOG.debug("onSaveCategoryGroupWorker->setOnFailed");
                LOG.debug(">>>>>>>>onSaveCategoryGroupWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryGroupTable();
                DialogMessages.showExceptionAlert(onSaveCategoryGroupWorker.getException());
            });
            ((Service<Void>) onRefreshCategoryGroupWorker).setOnSucceeded(event -> {
                LOG.debug("onRefreshCategoryGroupWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onRefreshCategoryGroupWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryGroupTable();
            });
            ((Service<Void>) onRefreshCategoryGroupWorker).setOnFailed(event -> {
                LOG.debug("onRefreshCategoryGroupWorker->setOnFailed");
                LOG.debug(">>>>>>>>onRefreshCategoryGroupWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryGroupTable();
                DialogMessages.showExceptionAlert(onRefreshCategoryGroupWorker.getException());
            });
        }catch (Exception e){
            //not in finally because refreshCategoryGroup must run before populateCategoryGroupTable
            startService(onRefreshCategoryGroupWorker,null);
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
            careTaker = myController.getCareTaker();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void refresh() {
        try{
            careTaker.clear();
            initializeServices();
            startService(onRefreshCategoryGroupWorker, null);
            cgTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFormCategoryGroup();
                }
            });
            myController.setToolbar_Save(new Command() {
                @Override
                public void execute() {
                    try {
                        LOG.debug("CategoryManagementController->onSave");
                        startService(onSaveCategoryGroupWorker, new ActionEvent());
                    } catch (Exception e) {
                        //no refresh() because there are in deletePendingCategoryGroup, populateCategoryGroupTable, onClear
                        DialogMessages.showExceptionAlert(e);
                    }
                }
            });
            myController.setToolbar_Undo(new Command() {
                @Override
                public void execute() {
                    isNotUndo.setValue(false);
                    restoreFormState(careTaker.undoState());
                }
            });
            myController.setToolbar_Redo(() -> {
                        isNotUndo.setValue(false);
                        restoreFormState(careTaker.redoState());
                    }
            );
        }catch (Exception e){
            startService(onRefreshCategoryGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private List<String> getCategoryGroupTypeStringList(){
        if(categoryGroupTypeStringList.size() == 0){
            for(DataBaseConstants.CATEGORY_GROUP_TYPE category_group_type : DataBaseConstants.CATEGORY_GROUP_TYPE.values()){
                categoryGroupTypeStringList.add(
                        UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(category_group_type.toString()));
            }
        }

        return categoryGroupTypeStringList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            //http://javafx-and-me.blogspot.ca/2013/10/custom-combobox.html
            //to show string instead of classes
            cgTypeACCB.setItems(FXCollections.observableList(getCategoryGroupTypeStringList()));
//            cgTypeACCB.setPrefWidth(DataBaseConstants.Cgtype_CODE_MAX_LGTH);
            cgTypeACCB.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            cgNameTF.setPrefColumnCount(DataBaseConstants.CG_NAME_MAX_LGTH);
            cgNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            cgDescTA.setPrefColumnCount(DataBaseConstants.CG_DESCRIPTION_MAX_LGTH);
            cgDescTA.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });
            cgClearBtn.setDisable(true);
            cgSaveBtn.setDisable(false);
            cgDeleteBtn.setDisable(true);
        } catch (Exception e) {
            //not in finally because refreshCategoryGroup must run before populateCategoryGroupTable
            startService(onRefreshCategoryGroupWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClearCategoryGroup(ActionEvent event){
        try{
            cgTypeACCB.setValue(getCategoryGroupTypeStringList().get(0));
            cgNameTF.clear();
            cgDescTA.clear();
            cgClearBtn.setDisable(false);
            cgSaveBtn.setDisable(false);
            cgDeleteBtn.setDisable(true);
            careTaker.clear();
        }catch (Exception e){
            startService(onRefreshCategoryGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSaveCategoryGroup(ActionEvent event){
        try {
            LOG.debug("CategoryManagementController->onSave");
            startService(onSaveCategoryGroupWorker, event);
            careTaker.clear();
        } catch (Exception e) {
            //no refresh() because there are in deletePendingCategoryGroup, populateCategoryGroupTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onDeleteCategoryGroup(ActionEvent event){
        try {
            String title = UserInterfaceConstants.CATEGORY_MANAGEMENT_WINDOW_TITLE;
            String headerText = "Category Group Deletion";
            StringBuilder contentText = new StringBuilder("Are you sure you want to delete category group with type");
            contentText.append("\"");
            contentText.append(cgTypeACCB.getValue());
            contentText.append("\" and name \"");
            contentText.append(cgNameTF.getText());
            contentText.append("\"?");
            if(DialogMessages.showConfirmationDialog(title,headerText,contentText.toString(),null)) {
                startService(onDeleteCategoryGroupWorker, event);
                populateCategoryGroupTable();
                onClearCategoryGroup(actionEvent);
            }
        } catch (Exception e) {
            //no refresh() because there are in deletePendingCategoryGroup, populateCategoryGroupTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClearCategory(ActionEvent event){

    }

    @FXML
    public void onSaveCategory(ActionEvent event){

    }

    @FXML
    public void onDeleteCategory(ActionEvent event){

    }

    private void populateFormCategoryGroup(){
        try{
            cgClearBtn.setDisable(false);
            cgSaveBtn.setDisable(false);
            cgDeleteBtn.setDisable(false);
            categoryManagementModel.setPendingCategoryGroupProperty(cgTable.getSelectionModel().getSelectedItem());

            cgTypeACCB.setValue(
                    UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(categoryManagementModel.getPendingCategoryGroup().getCategoryGroupType())
            );
            cgNameTF.setText(categoryManagementModel.getPendingCategoryGroup().getName());
            cgDescTA.setText(categoryManagementModel.getPendingCategoryGroup().getDescription());
        }catch (Exception e){
            startService(onRefreshCategoryGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateCategoryGroupTable(){
        try{
            LOG.debug("categoryManagementModel.getCategoryGroups().size():" + categoryManagementModel.getCategoryGroups().size());
            cgTable.getItems().clear();
            // TODO if add another method getCategoryGroup(boolean) to get categoryGroup with/without categories
            cgTable.setItems(categoryManagementModel.getCategoryGroups());

            TableColumn<CategoryGroup, Long> categoryGroupIdCol = new TableColumn<>("ID");
            categoryGroupIdCol.setCellValueFactory(new PropertyValueFactory<CategoryGroup, Long>("id"));

            TableColumn<CategoryGroup, String> categoryGroupTypeCol = new TableColumn<>("Type");
            categoryGroupTypeCol.setCellValueFactory(cg -> new SimpleStringProperty(
                    UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(cg.getValue().getCategoryGroupType())));

            TableColumn<CategoryGroup, String> categoryGroupNameCol = new TableColumn<>("Name");
            categoryGroupNameCol.setCellValueFactory(new PropertyValueFactory<CategoryGroup, String>("name"));

            TableColumn<CategoryGroup, String> categoryGroupDescCol = new TableColumn<>("Description");
            categoryGroupDescCol.setCellValueFactory(new PropertyValueFactory<CategoryGroup, String>("description"));

            TableColumn<CategoryGroup, Calendar> categoryGroupCreatedCol = new TableColumn<>("Created on");
            categoryGroupCreatedCol.setCellValueFactory(new PropertyValueFactory<CategoryGroup, Calendar>("createdOn"));

            TableColumn<CategoryGroup, Calendar> categoryGroupUpdatedCol = new TableColumn<>("Updated on");
            categoryGroupUpdatedCol.setCellValueFactory(new PropertyValueFactory<CategoryGroup, Calendar>("updatedOn"));

            cgTable.getColumns().setAll(categoryGroupIdCol, categoryGroupTypeCol, categoryGroupNameCol,
                    categoryGroupDescCol, categoryGroupCreatedCol, categoryGroupUpdatedCol);

        }catch (Exception e){
            startService(onRefreshCategoryGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void saveForm() {
        try {
            careTaker.saveState(new CategoryGroupFormState(cgTypeACCB.getValue(), cgNameTF.getText(), cgDescTA.getText()));
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void restoreFormState(Memento memento) {
        try {
            CategoryGroupFormState formState = (CategoryGroupFormState) memento;
            cgTypeACCB.setValue(formState.getCgTypeACCBStr());
            cgNameTF.setText(formState.getCgNameTFStr());
            cgDescTA.setText(formState.getCgDescTAStr());
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void close() {
        try {
            onClearCategoryGroup(new ActionEvent());
            myController.setToolbar_Save(null);
            myController.setToolbar_Undo(null);
            myController.setToolbar_Redo(null);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public boolean askPermissionToClose() {
        boolean result = true;
        try{
            StringBuilder contentText = new StringBuilder();
            if(!StringUtils.isBlank(cgNameTF.getText())) {
                contentText.append("Name (");
                contentText.append(cgNameTF.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(cgDescTA.getText())) {
                contentText.append("Description (");
                contentText.append(cgDescTA.getText());
                contentText.append(") is not saved. ");
            }

            if(contentText.length() > 0){
                contentText.append("Are you sure you want to close this window?");
                result = DialogMessages.showConfirmationDialog(UserInterfaceConstants.CATEGORY_MANAGEMENT_WINDOW_TITLE,
                        "Not all fields are empty", contentText.toString(), null);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return result;
    }

    private static class CategoryGroupFormState implements Memento{
        private final String cgTypeACCBStr;
        private final String cgNameTFStr;
        private final String cgDescTAStr;

        public CategoryGroupFormState(String cgTypeACCB, String cgNameTF, String cgDescTA){
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
            return "CategoryGroupFormState{" +
                    "cgTypeACCBStr='" + cgTypeACCBStr + '\'' +
                    ", cgNameTFStr='" + cgNameTFStr + '\'' +
                    ", cgDescTAStr='" + cgDescTAStr + '\'' +
                    '}';
        }
    }
}