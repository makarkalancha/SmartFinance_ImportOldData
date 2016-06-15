package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.javafx.control.AutoCompleteComboBox;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.decorator.category_management.CategoryManagementDecorator;
import com.makco.smartfinance.user_interface.decorator.category_management.CategoryManagementDecoratorCategory;
import com.makco.smartfinance.user_interface.decorator.category_management.CategoryManagementDecoratorRoot;
import com.makco.smartfinance.user_interface.models.CategoryManagementModel;
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
 * Created by mcalancea on 2016-05-13.
 */
public class CategoryManagementController extends AbstractControlledScreen {
    private final static Logger LOG = LogManager.getLogger(CategoryManagementController.class);
    private final static int CATEGORY_GROUP_TAB_INDEX = 0;
    private final static int CATEGORY_TAB_INDEX = 1;
    private CategoryManagementModel categoryManagementModel = new CategoryManagementModel();

    private Worker<Void> onDeleteCategoryGroupWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveCategoryGroupWorker;
    private Worker<Void> onRefreshCategoryGroupWorker;
    private List<String> categoryGroupTypeStringList = new ArrayList<>();

    private Worker<Void> onDeleteCategoryWorker;
    private Worker<EnumSet<ErrorEnum>> onSaveCategoryWorker;
    private Worker<Void> onRefreshCategoryWorker;

    private ProgressIndicatorForm pForm = new ProgressIndicatorForm();

    private int selectedTabIndex;

    @FXML
    private TabPane tabPane;
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

    @FXML
    private TreeTableView<CategoryManagementDecorator> cTable;
    @FXML
    private AutoCompleteComboBox cCategoryGroupACCB;
    @FXML
    private TextField cNameTF;
    @FXML
    private TextArea cDescTA;
    @FXML
    private Button cClearBtn;
    @FXML
    private Button cSaveBtn;
    @FXML
    private Button cDeleteBtn;

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
                            categoryManagementModel.refreshCategoryGroupTab();
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

        try{
            onDeleteCategoryWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            categoryManagementModel.deletePendingCategory();
                            return null;
                        }
                    };
                }
            };
            onSaveCategoryWorker = new Service<EnumSet<ErrorEnum>>() {
                @Override
                protected Task<EnumSet<ErrorEnum>> createTask() {
                    return new Task<EnumSet<ErrorEnum>>() {
                        @Override
                        protected EnumSet<ErrorEnum> call() throws Exception {
                            return categoryManagementModel.savePendingCategory(
                                    categoryManagementModel.convertCategoryGroupFromUIToBackendTo(cCategoryGroupACCB.getValue().toString()),
                                    cNameTF.getText(),
                                    cDescTA.getText());
                        }
                    };
                }
            };
            onRefreshCategoryWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            categoryManagementModel.refreshCategoryTab();
                            return null;
                        }
                    };
                }
            };
        }catch (Exception e){
            //not in finally because refreshCategory must run before populateCategoryGroupTable
            startService(onRefreshCategoryWorker,null);
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
                    highlightInvalidFields(errors);
                    DialogMessages.showErrorDialog("Error while saving Category Group: type "
                                    + cgTypeACCB.getValue() + ", with name " + cgNameTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveCategoryGroupWorker).getValue(), null);
                } else{
                    onClearCategoryGroup(actionEvent);
                }
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

        try{
            ((Service<Void>) onDeleteCategoryWorker).setOnSucceeded(event -> {
                LOG.debug("onDeleteCategoryWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onDeleteCategoryWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryTable();
            });
            ((Service<Void>) onDeleteCategoryWorker).setOnFailed(event -> {
                LOG.debug("onDeleteCategoryWorker->setOnFailed");
                LOG.debug(">>>>>>>>onDeleteCategoryWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryTable();
                DialogMessages.showExceptionAlert(onDeleteCategoryWorker.getException());
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveCategoryWorker).setOnSucceeded(event -> {
                LOG.debug("onSaveCategoryWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onSaveCategoryWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryTable();
                EnumSet<ErrorEnum> errors = onSaveCategoryWorker.getValue();
                if(!errors.isEmpty()) {
                    highlightInvalidFields(errors);
                    DialogMessages.showErrorDialog("Error while saving Category: category group "
                                    + cCategoryGroupACCB.getValue() + ", with name " + cNameTF.getText(),
                            (EnumSet<ErrorEnum>) ((Service) onSaveCategoryWorker).getValue(), null);
                } else {
                    onClearCategory(actionEvent);
                }
            });
            ((Service<EnumSet<ErrorEnum>>) onSaveCategoryWorker).setOnFailed(event -> {
                LOG.debug("onSaveCategoryWorker->setOnFailed");
                LOG.debug(">>>>>>>>onSaveCategoryWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryTable();
                DialogMessages.showExceptionAlert(onSaveCategoryWorker.getException());
            });
            ((Service<Void>) onRefreshCategoryWorker).setOnSucceeded(event -> {
                LOG.debug("onRefreshCategoryWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>onRefreshCategoryWorker->setOnSucceeded: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryTable();
                cCategoryGroupACCB.setItems(categoryManagementModel.getCategoryGroupUIName());
            });
            ((Service<Void>) onRefreshCategoryWorker).setOnFailed(event -> {
                LOG.debug("onRefreshCategoryWorker->setOnFailed");
                LOG.debug(">>>>>>>>onRefreshCategoryWorker->setOnFailed: pForm.getDialogStage().close()");
                pForm.close();
                populateCategoryTable();
                DialogMessages.showExceptionAlert(onRefreshCategoryWorker.getException());
                cCategoryGroupACCB.setItems(categoryManagementModel.getCategoryGroupUIName());
            });
        }catch (Exception e){
            //not in finally because refreshCategoryGroup must run before populateCategoryGroupTable
            startService(onRefreshCategoryWorker,null);
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
    public void refresh() {
        try{
            LOG.debug("contoller->refresh");
            careTaker.clear();
            initializeServices();
            startService(onRefreshCategoryGroupWorker, null);
            startService(onRefreshCategoryWorker, null);
            cgTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFormCategoryGroup();
                }
            });
            cgTypeACCB.setItems(FXCollections.observableList(getCategoryGroupTypeStringList()));
//            cCategoryGroupACCB.setItems(categoryManagementModel.getCategoryGroupUIName());
            cTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null && newSelection.getValue() instanceof CategoryManagementDecoratorCategory) {
                    populateFormCategory();
                } else {
                    onClearCategory(null);
                }
            });
            screensController.setToolbar_Save(new Command() {
                @Override
                public void execute() {
                    try {
                        if(tabPane.getSelectionModel().getSelectedIndex() == CATEGORY_GROUP_TAB_INDEX) {
                            LOG.debug("CategoryGroup->onSave");
                            startService(onSaveCategoryGroupWorker, new ActionEvent());
                        } else if(tabPane.getSelectionModel().getSelectedIndex() == CATEGORY_TAB_INDEX) {
                            LOG.debug("Category->onSave");
                            startService(onSaveCategoryWorker, new ActionEvent());
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
            startService(onRefreshCategoryGroupWorker,null);
            /**
             * startService(onRefreshCategoryWorker,null);
             * is not needed there were no changes in behavior with / without it
             */
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
        try{
            tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                selectedTabIndex = (int) newValue;
                careTaker.clear();
                if(oldValue != newValue){
                    onClearCategoryGroup(null);
                    onClearCategory(null);

                    if((int)newValue == CATEGORY_TAB_INDEX){
                        startService(onRefreshCategoryWorker, null);
                    }
                }

            });
        }catch (Exception e){
            //not in finally because refreshCategoryGroup must run before populateCategoryGroupTable
            DialogMessages.showExceptionAlert(e);
        }

        try {
            //http://javafx-and-me.blogspot.ca/2013/10/custom-combobox.html
            //to show string instead of classes
//            cgTypeACCB.setItems(FXCollections.observableList(getCategoryGroupTypeStringList()));
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
            cgClearBtn.setDisable(false);
            cgSaveBtn.setDisable(false);
            cgDeleteBtn.setDisable(true);

            errorControlDictionary.put(ErrorEnum.CatGr_DESC_LGTH, cgDescTA);
            errorControlDictionary.put(ErrorEnum.CatGr_NAME_DUPLICATE, cgNameTF);
            errorControlDictionary.put(ErrorEnum.CatGr_NAME_LGTH, cgNameTF);
            errorControlDictionary.put(ErrorEnum.CatGr_NULL_NAME, cgNameTF);
            errorControlDictionary.put(ErrorEnum.CatGr_NULL_CG_TYPE, cgTypeACCB);

            erroneousControlSet.add(cgTypeACCB);
            erroneousControlSet.add(cgNameTF);
            erroneousControlSet.add(cgDescTA);
        } catch (Exception e) {
            //not in finally because refreshCategoryGroup must run before populateCategoryGroupTable
            startService(onRefreshCategoryGroupWorker, null);
            DialogMessages.showExceptionAlert(e);
        }

        try {
//            cCategoryGroupACCB.setItems(categoryManagementModel.getCategoryGroupsWithoutCategories());
            cCategoryGroupACCB.setEditable(true);
//            cCategoryGroupACCB.setConverter(new StringConverter<CategoryGroup>() {
//
//                @Override
//                public String toString(CategoryGroup object) {
//                    if (object == null) {
//                        return null;
//                    }
//                    return categoryManagementModel.convertCategoryGroupFromBackendToUI(object);
//                }
//
//                @Override
//                public CategoryGroup fromString(String string) {
//                    if(StringUtils.isEmpty(string)) {
//                        return null;
//                    }
//                    return categoryManagementModel.convertCategoryGroupFromUIToBackendTo(string);
//                }
//            });
            cCategoryGroupACCB.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            cNameTF.setPrefColumnCount(DataBaseConstants.CAT_NAME_MAX_LGTH);
            cNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });

            cDescTA.setPrefColumnCount(DataBaseConstants.CAT_DESCRIPTION_MAX_LGTH);
            cDescTA.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isNotUndo.getValue()) {
                    saveForm();
                } else {
                    isNotUndo.setValue(true);
                }
            });
            cClearBtn.setDisable(false);
            cSaveBtn.setDisable(false);
            cDeleteBtn.setDisable(true);

            errorControlDictionary.put(ErrorEnum.Cat_CG_EMPTY, cCategoryGroupACCB);
            errorControlDictionary.put(ErrorEnum.Cat_DESC_LGTH, cDescTA);
            errorControlDictionary.put(ErrorEnum.Cat_NAME_DUPLICATE, cNameTF);
            errorControlDictionary.put(ErrorEnum.Cat_NAME_LGTH, cNameTF);
            errorControlDictionary.put(ErrorEnum.Cat_NULL_NAME, cNameTF);

            erroneousControlSet.add(cCategoryGroupACCB);
            erroneousControlSet.add(cNameTF);
            erroneousControlSet.add(cDescTA);
        } catch (Exception e) {
            //not in finally because onRefreshCategoryWorker must run before populateCategoryTable
            startService(onRefreshCategoryWorker, null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClearCategoryGroup(ActionEvent event){
        try{
            clearErrorHighlight();

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
            LOG.debug("Category Group->onSave");
            startService(onSaveCategoryGroupWorker, event);
            careTaker.clear();
        } catch (Exception e) {
            //no refreshCategoryGroupTab() because there are in deletePendingCategoryGroup, populateCategoryGroupTable, onClear
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
            //no refreshCategoryGroupTab() because there are in deletePendingCategoryGroup, populateCategoryGroupTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onClearCategory(ActionEvent event){
        try{
            clearErrorHighlight();

            if(categoryManagementModel.getCategoryGroupUIName().size() > 0){
                cCategoryGroupACCB.setValue(categoryManagementModel.getCategoryGroupUIName().get(0));
            }else{
                cCategoryGroupACCB.setValue("");
            }
            cNameTF.clear();
            cDescTA.clear();
            cClearBtn.setDisable(false);
            cSaveBtn.setDisable(false);
            cDeleteBtn.setDisable(true);
            careTaker.clear();
        }catch (Exception e){
            startService(onRefreshCategoryWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSaveCategory(ActionEvent event){
        try {
            LOG.debug("Category->onSave");
            startService(onSaveCategoryWorker, event);
            careTaker.clear();
        } catch (Exception e) {
            //no refreshCategoryTab() because there are in deletePendingCategory, populateCategoryTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onDeleteCategory(ActionEvent event){
        try {
            String title = UserInterfaceConstants.CATEGORY_MANAGEMENT_WINDOW_TITLE;
            String headerText = "Category Deletion";
            StringBuilder contentText = new StringBuilder("Are you sure you want to delete category with category group");
            contentText.append("\"");
            contentText.append(cCategoryGroupACCB.getValue());
            contentText.append("\" and name \"");
            contentText.append(cNameTF.getText());
            contentText.append("\"?");
            if(DialogMessages.showConfirmationDialog(title,headerText,contentText.toString(),null)) {
                startService(onDeleteCategoryWorker, event);
                populateCategoryTable();
                onClearCategory(actionEvent);
            }
        } catch (Exception e) {
            //no refreshCategoryTab() because there are in deletePendingCategory, populateCategoryTable, onClear
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateFormCategoryGroup(){
        try{
            cgClearBtn.setDisable(false);
            cgSaveBtn.setDisable(false);
            cgDeleteBtn.setDisable(false);
            categoryManagementModel.setPendingCategoryGroupProperty(cgTable.getSelectionModel().getSelectedItem());

            cgTypeACCB.setValue(
                    UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(
                            categoryManagementModel.getPendingCategoryGroup().getCategoryGroupType().getDiscriminator())
            );
            cgNameTF.setText(categoryManagementModel.getPendingCategoryGroup().getName());
            cgDescTA.setText(categoryManagementModel.getPendingCategoryGroup().getDescription());
        }catch (Exception e){
            startService(onRefreshCategoryGroupWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateFormCategory(){
        try{
            cClearBtn.setDisable(false);
            cSaveBtn.setDisable(false);
            cDeleteBtn.setDisable(false);
            categoryManagementModel.setPendingCategoryProperty(cTable.getSelectionModel().getSelectedItem().getValue().getCategory());

//            cCategoryGroupACCB.setValue(
//                    categoryManagementModel.convertCategoryGroupFromBackendToUI(
//                            categoryManagementModel.getPendingCategory().getCategoryGroup())
//            );
            cCategoryGroupACCB.setValue(
                categoryManagementModel.convertCategoryGroupFromBackendToUI(categoryManagementModel.getPendingCategory().getCategoryGroup())
            );
            cNameTF.setText(categoryManagementModel.getPendingCategory().getName());
            cDescTA.setText(categoryManagementModel.getPendingCategory().getDescription());
        }catch (Exception e){
            startService(onRefreshCategoryWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void populateCategoryGroupTable(){
        try{
            LOG.debug("categoryManagementModel.getCategoryGroupsWithoutCategories().size():" + categoryManagementModel.getCategoryGroupsWithoutCategories().size());
            cgTable.getItems().clear();
            cgTable.setItems(categoryManagementModel.getCategoryGroupsWithoutCategories());

            TableColumn<CategoryGroup, Long> categoryGroupIdCol = new TableColumn<>("ID");
            categoryGroupIdCol.setCellValueFactory(new PropertyValueFactory<CategoryGroup, Long>("id"));

            TableColumn<CategoryGroup, String> categoryGroupTypeCol = new TableColumn<>("Type");
            categoryGroupTypeCol.setCellValueFactory(cg -> new SimpleStringProperty(
                    UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(
                            cg.getValue().getCategoryGroupType().getDiscriminator())));

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

    private void populateCategoryTable(){
        try{
            LOG.debug("categoryManagementModel.getCategories().size():" + categoryManagementModel.getCategoryManagementDecoratorMultimap().size());
//            cTable.getItems().clear();
            cTable.setRoot(null);
//            cTable.setItems(categoryManagementModel.getCategories());

            TreeItem<CategoryManagementDecorator> rootNode = new TreeItem<>(new CategoryManagementDecoratorRoot(UserInterfaceConstants.CATEGORY_ROOT_NODE));
            rootNode.setExpanded(true);
            for(Map.Entry<CategoryManagementDecorator, Collection<CategoryManagementDecorator>>  categoryGroupEntry :
                categoryManagementModel.getCategoryManagementDecoratorMultimap().asMap().entrySet()){
                TreeItem<CategoryManagementDecorator> categoryGroupNode = new TreeItem<>(categoryGroupEntry.getKey());
                categoryGroupNode.setExpanded(true);
                for(CategoryManagementDecorator categoryDecorators : categoryGroupEntry.getValue()){
                    TreeItem<CategoryManagementDecorator> categoryNode = new TreeItem<>(categoryDecorators);
                    categoryGroupNode.getChildren().add(categoryNode);
                }
                rootNode.getChildren().add(categoryGroupNode);
            }
            cTable.setRoot(rootNode);

            TreeTableColumn<CategoryManagementDecorator, Long> categoryIdCol = new TreeTableColumn<>("ID");
            categoryIdCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<CategoryManagementDecorator,Long> param) ->
                            new ReadOnlyObjectWrapper<Long>(param.getValue().getValue().getId())
            );
            categoryIdCol.setCellFactory(column -> {
                return new CustomTreeTableCell<Long>();
            });
//                return new TreeTableCell<CategoryManagmentDecorator, Long>(){
//                    @Override
//                    protected void updateItem(Long item, boolean empty) {
//                        if(getTreeTableRow().getItem() instanceof CategoryManagementDecoratorCategory){
//                            setText(item.toString());
//                            setStyle("");
//                        } else {
//                            setStyle(CATEGORY_TAB_NON_CATEGORY_BGCOLOR);
//                        }
//                    }
//
//
//                };
//            });

            TreeTableColumn<CategoryManagementDecorator, String> categoryTypeCol = new TreeTableColumn<>("Type");
            categoryTypeCol.setCellValueFactory(c -> new SimpleStringProperty(
                UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(
                        c.getValue().getValue().getCategoryGroupType()
                )
            ));
            categoryTypeCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

//            TreeTableColumn<CategoryManagmentDecorator, String> categoryCategoryGroupCol = new TreeTableColumn<>("Category Group");
//            categoryCategoryGroupCol.setCellValueFactory(c -> new SimpleStringProperty(
//                    categoryManagementModel.convertCategoryGroupFromBackendToUI(
//                            c.getValue().getValue().getCategoryGroup()
//                    )
//            ));

            TreeTableColumn<CategoryManagementDecorator, String> categoryNameCol = new TreeTableColumn<>("Name");
            categoryNameCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<CategoryManagementDecorator, String> param) ->
                            new ReadOnlyStringWrapper(param.getValue().getValue().getName())
            );
            categoryNameCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });
//            categoryNameCol.setCellFactory(column -> {
//                return new TreeTableCell<CategoryManagmentDecorator, String>(){
//                    @Override
//                    protected void updateItem(String item, boolean empty) {
//                        setText(item);
//                        if(getTreeTableRow().getItem() instanceof CategoryManagementDecoratorCategory){
//                            setStyle("");
//                        } else {
//                            setStyle(CATEGORY_TAB_NON_CATEGORY_BGCOLOR);
//                        }
//                    }
//
//
//                };
//            });

            TreeTableColumn<CategoryManagementDecorator, String> categoryDescCol = new TreeTableColumn<>("Description");
            categoryDescCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<CategoryManagementDecorator, String> param) ->
                            new ReadOnlyStringWrapper(param.getValue().getValue().getDescription())
            );
            categoryDescCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

            TreeTableColumn<CategoryManagementDecorator, String> categoryCreatedCol = new TreeTableColumn<>("Created on");
            categoryCreatedCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CategoryManagementDecorator, String> param) -> {
                    return new ReadOnlyObjectWrapper<String>(param.getValue().getValue().getCreatedOn());
                }
            );
            categoryCreatedCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

            TreeTableColumn<CategoryManagementDecorator, String> categoryUpdatedCol = new TreeTableColumn<>("Updated on");
            categoryUpdatedCol.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<CategoryManagementDecorator, String> param) -> {
                        return new ReadOnlyObjectWrapper<String>(param.getValue().getValue().getUpdatedOn());
                    }
            );
            categoryUpdatedCol.setCellFactory(column -> {
                return new CustomTreeTableCell<String>();
            });

            cTable.getColumns().setAll(categoryIdCol, categoryTypeCol, categoryNameCol,
                    categoryDescCol, categoryCreatedCol, categoryUpdatedCol);
        }catch (Exception e){
            startService(onRefreshCategoryWorker,null);
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void saveForm() {
        try {
            if(selectedTabIndex == CATEGORY_GROUP_TAB_INDEX) {
                careTaker.saveState(new CategoryGroupFormState(cgTypeACCB.getValue(), cgNameTF.getText(), cgDescTA.getText()));
            }else if(selectedTabIndex == CATEGORY_TAB_INDEX) {
                careTaker.saveState(new CategoryFormState(cCategoryGroupACCB.getValue(), cNameTF.getText(), cDescTA.getText()));
            }
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void restoreFormState(Memento memento) {
        try {
            if(selectedTabIndex == CATEGORY_GROUP_TAB_INDEX) {
                CategoryGroupFormState formState = (CategoryGroupFormState) memento;
                cgTypeACCB.setValue(formState.getCgTypeACCBStr());
                cgNameTF.setText(formState.getCgNameTFStr());
                cgDescTA.setText(formState.getCgDescTAStr());
            }else if(selectedTabIndex == CATEGORY_TAB_INDEX) {
                CategoryFormState formState = (CategoryFormState) memento;
//                cCategoryGroupACCB.setValue(formState.getCCategoryGroupACCBStr());
//                cCategoryGroupACCB.setValue(categoryManagementModel.convertCategoryGroupFromUIToBackendTo(formState.getCCategoryGroupACCBStr()));
                cCategoryGroupACCB.setValue(formState.getCCategoryGroupACCB());
                cNameTF.setText(formState.getCNameTFStr());
                cDescTA.setText(formState.getCDescTAStr());
            }
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void close() {
        try {
            onClearCategoryGroup(new ActionEvent());
            onClearCategory(new ActionEvent());
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
            if(!StringUtils.isBlank(cgNameTF.getText())) {
                contentText.append("Category Group Name (");
                contentText.append(cgNameTF.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(cgDescTA.getText())) {
                contentText.append("Category Group Description (");
                contentText.append(cgDescTA.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(cNameTF.getText())) {
                contentText.append("Category Name (");
                contentText.append(cNameTF.getText());
                contentText.append(") is not saved. ");
            }

            if(!StringUtils.isBlank(cDescTA.getText())) {
                contentText.append("Category Description (");
                contentText.append(cDescTA.getText());
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

    private static class CategoryFormState implements Memento{
        private final String cCategoryGroupACCBStr;
        private final String cNameTFStr;
        private final String cDescTAStr;

        public CategoryFormState(String cCategoryGroupACCB, String cNameTF, String cDescTA){
            this.cCategoryGroupACCBStr = cCategoryGroupACCB;
            this.cNameTFStr = cNameTF;
            this.cDescTAStr = cDescTA;
        }

        public String getCCategoryGroupACCB() {
            return cCategoryGroupACCBStr;
        }

        public String getCNameTFStr() {
            return cNameTFStr;
        }

        public String getCDescTAStr() {
            return cDescTAStr;
        }

        @Override
        public String toString() {
            return "CategoryFormState{" +
                    "cCategoryGroupACCBStr='" + cCategoryGroupACCBStr + '\'' +
                    ", cNameTFStr='" + cNameTFStr + '\'' +
                    ", cDescTAStr='" + cDescTAStr + '\'' +
                    '}';
        }
    }

    private static class CustomTreeTableCell <T> extends TreeTableCell<CategoryManagementDecorator, T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            if(item != null) {
                setText(item.toString());
            }
            if (getTreeTableRow().getItem() instanceof CategoryManagementDecoratorCategory) {
                setStyle("");
            } else {
                setStyle(UserInterfaceConstants.CATEGORY_TAB_NON_CATEGORY_BGCOLOR);
            }
        }
    }
}