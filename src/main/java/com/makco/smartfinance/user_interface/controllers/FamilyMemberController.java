package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ErrorMessage;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private FamilyMemberService familyMemberService = new FamilyMemberServiceImpl();
    private ObservableList<FamilyMember> familyMembers = FXCollections.observableArrayList();
//    private int index;
    @FXML
    private TableView<FamilyMember> table;
//    @FXML
//    private TextField idTx;
    @FXML
    private TextField nameTF;
    @FXML
    private TextArea descTA;
//    @FXML
//    private TextField createdOnTx;
//    @FXML
//    private TextField updatedOnTx;

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
            table.setOnMouseClicked(event -> {
                populateForm();
            });
            getFamilyMembers();
            populateTable();
            populateForm();
        }catch (Exception e){
            ErrorMessage.showAlert(e);
        }
    }

    @FXML
    public void onNew(ActionEvent event){
        nameTF.clear();
        descTA.clear();
//        try{
//            FamilyMember familyMember = new FamilyMember();
//            familyMember.setName(nameTF.getText());
//            familyMember.setDescription(descTA.getText());
//            familyMemberService.addFamilyMember(familyMember);
//            getFamilyMembers();
//            populateTable();
//        }catch (Exception e){
//            ErrorMessage.showAlert(e);
//        }
    }
    @FXML
    public void onSave(ActionEvent event){

    }

//    @FXML
//    public void udpate(ActionEvent event){
//        try{
////            FamilyMember familyMember = familyMemberService.getFamilyMemberById(Long.parseLong(idTx.getText()));
////            familyMember.setName(nameTF.getText());
////            familyMember.setDescription(descTA.getText());
////            familyMemberService.updateFamilyMember(familyMember);
//            getFamilyMembers();
//            populateTable();
//        }catch (Exception e){
//            ErrorMessage.showAlert(e);
//        }
//    }

//    @FXML
//    public void delete(ActionEvent event){
//        try{
////            familyMemberService.removeFamilyMember(Long.parseLong(idTx.getText()));
//            getFamilyMembers();
//            populateTable();
//        }catch (Exception e){
//            ErrorMessage.showAlert(e);
//        }
//    }

//    @FXML
//    public void first(ActionEvent event){
//        try{
//            index = 0;
//            populateForm(index);
//            populateTable();
//        }catch (Exception e){
//            ErrorMessage.showAlert(e);
//        }
//    }

//    @FXML
//    public void oneUp(ActionEvent event){
//        try{
//            if(index > 0){
//                index--;
//            }else {
//                event.consume();
//            }
//            populateForm(index);
//            populateTable();
//        }catch (Exception e){
//            ErrorMessage.showAlert(e);
//        }
//    }

//    @FXML
//    public void oneDown(ActionEvent event){
//        try{
//            if(index < (familyMembers.size() - 1) ){
//                index++;
//            }else {
//                event.consume();
//            }
//            populateForm(index);
//            populateTable();
//        }catch (Exception e){
//            ErrorMessage.showAlert(e);
//        }
//    }
//
//    @FXML
//    public void last(ActionEvent event){
//        try{
//            index = familyMembers.size() - 1;
//            populateForm(index);
//            populateTable();
//        }catch (Exception e){
//            ErrorMessage.showAlert(e);
//        }
//    }

    public ObservableList<FamilyMember> getFamilyMembers(){
        try{
            if (!familyMembers.isEmpty()) {
                familyMembers.clear();
            }
            familyMembers = FXCollections.observableArrayList((List<FamilyMember>) familyMemberService.listFamilyMembers());
            LOG.debug("familyMember.size: " + familyMembers.size());
        }catch (Exception e){
            ErrorMessage.showAlert(e);
        }
        return familyMembers;
    }

    private void populateForm(){
        try{
            FamilyMember familyMember = table.getSelectionModel().getSelectedItem();

//            if(familyMembers.isEmpty()){
//                return;
//            }
//
//            FamilyMember familyMember = table.getSelectionModel().getSelectedItem().get;
////            idTx.setText(familyMember.getId().toString());
            nameTF.setText(familyMember.getName());
            descTA.setText(familyMember.getDescription());
////            createdOnTx.setText(familyMember.getCreatedOn().toString());
////            updatedOnTx.setText(familyMember.getUpdatedOn().toString());
        }catch (Exception e){
            ErrorMessage.showAlert(e);
        }
    }

    private void populateTable(){
        try{
            table.getItems().clear();
            table.setItems(familyMembers);

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

//    public void removeFamilyMember(Long id){
//        familyMemberService.removeFamilyMember(id);
//    }
//
//    public void updateFamilyMember(FamilyMember familyMember){
//        familyMemberService.updateFamilyMember(familyMember);
//    }
}
