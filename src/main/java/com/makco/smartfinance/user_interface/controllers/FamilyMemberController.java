package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationUtililities;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.models.FamilyMemberModel;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Override
    public void setScreenParent(ScreensController screenPage) {
        try{
            myController = screenPage;
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            familyMemberModel.refreshFamilyMembers();
            populateTable();
            table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateForm();
                }
            });
            clearBtn.setDisable(true);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        }catch (Exception e){
            //not in finally because refreshFamilyMembers must run before populateTable
            familyMemberModel.refreshFamilyMembers();
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
            familyMemberModel.refreshFamilyMembers();
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onSave(ActionEvent event){
        try {
            familyMemberModel.savePendingFamilyMember(nameTF.getText(), descTA.getText());
            populateTable();
            onClear(event);
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
            Task taskToDelete = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String title = ApplicationUtililities.FAMILY_MEMBER_WINDOW_TITLE;
                    String headerText = "Family Member Deletion";
                    StringBuilder contentText = new StringBuilder("Are you sure you want to delete family member ");
                    contentText.append("\"");
                    contentText.append(nameTF.getText());
                    contentText.append("\"?");
                    if(DialogMessages.showConfirmationDialog(title,headerText,contentText.toString(),null))

                    {
                        familyMemberModel.deletePendingFamilyMember();
                        populateTable();
                        onClear(event);
                    }
                    return null;
                }
            };
            Thread backGroundT = new Thread(taskToDelete);
            backGroundT.setDaemon(true);
            backGroundT.start();
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
            familyMemberModel.refreshFamilyMembers();
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
            familyMemberModel.refreshFamilyMembers();
            DialogMessages.showExceptionAlert(e);
        }
    }
}
