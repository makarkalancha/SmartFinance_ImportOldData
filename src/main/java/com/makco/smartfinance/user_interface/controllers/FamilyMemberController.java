package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ErrorMessage;
import com.makco.smartfinance.user_interface.models.FamilyMemberModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

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
            ErrorMessage.showAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            familyMemberModel.refreshFamilyMembers();
            populateTable();
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if(newSelection != null){
                    populateForm();
                }
            });
            clearBtn.setDisable(true);
            saveBtn.setDisable(false);
            deleteBtn.setDisable(true);
        }catch (Exception e){
            ErrorMessage.showAlert(e);
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
            ErrorMessage.showAlert(e);
        }
    }

    @FXML
    public void onSave(ActionEvent event){
        try {
            familyMemberModel.savePendingFamilyMember(nameTF.getText(), descTA.getText());
            populateTable();
            onClear(event);
        } catch (Exception e) {
            ErrorMessage.showAlert(e);
        }
    }

    @FXML
    public void onDelete(ActionEvent event){
        try {
            familyMemberModel.deletePendingFamilyMember();
            populateTable();
            onClear(event);
        } catch (Exception e) {
            ErrorMessage.showAlert(e);
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
            ErrorMessage.showAlert(e);
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
            ErrorMessage.showAlert(e);
        }
    }
}
