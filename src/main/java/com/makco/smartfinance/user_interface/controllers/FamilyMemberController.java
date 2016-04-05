package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
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
    private ScreensController myController;
    private FamilyMemberService familyMemberService = new FamilyMemberServiceImpl();
    private ObservableList<FamilyMember> familyMembers = FXCollections.observableArrayList();
    private int index;
    @FXML
    private TableView<FamilyMember> table;
    @FXML
    private TextField idTx;
    @FXML
    private TextField nameTx;
    @FXML
    private TextArea descTx;
    @FXML
    private TextField createdOnTx;
    @FXML
    private TextField updatedOnTx;

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTable();
        populateForm(0);
    }

    @FXML
    public void addNew(ActionEvent event){
        FamilyMember familyMember = new FamilyMember();
        familyMember.setName(nameTx.getText());
        familyMember.setDescription(descTx.getText());
        familyMemberService.addFamilyMember(familyMember);
        populateTable();
    }

    @FXML
    public void udpate(ActionEvent event) {
        FamilyMember familyMember = familyMemberService.getFamilyMemberById(Long.parseLong(idTx.getId()));
        familyMember.setName(nameTx.getText());
        familyMember.setDescription(descTx.getText());
        familyMemberService.updateFamilyMember(familyMember);
        populateTable();
    }

    @FXML
    public void delete(ActionEvent event) {
        familyMemberService.removeFamilyMember(Long.parseLong(idTx.getId()));
        populateTable();
    }

    @FXML
    public void first(ActionEvent event){
        index = 0;
        populateForm(index);
    }

    @FXML
    public void oneUp(ActionEvent event){
        if(index > 0){
            index--;
        }else {
            event.consume();
        }
        populateForm(index);
    }

    @FXML
    public void oneDown(ActionEvent event){
        if(index < (familyMembers.size() - 1) ){
            index++;
        }else {
            event.consume();
        }
        populateForm(index);
    }

    @FXML
    public void last(ActionEvent event){
        index = familyMembers.size() - 1;
        populateForm(index);
    }

    public ObservableList<FamilyMember> getFamilyMembers() {
        if(!familyMembers.isEmpty()){
            familyMembers.clear();
        }
        familyMembers = FXCollections.observableArrayList((List<FamilyMember>) familyMemberService.listFamilyMembers());
        return familyMembers;
    }

    private void populateForm(int index){
        if(familyMembers.isEmpty()){
            return;
        }

        FamilyMember familyMember = familyMembers.get(index);
        idTx.setText(familyMember.getId().toString());
        nameTx.setText(familyMember.getName());
        descTx.setText(familyMember.getDescription());
        createdOnTx.setText(familyMember.getCreatedOn().toString());
        updatedOnTx.setText(familyMember.getUpdatedOn().toString());
    }

    private void populateTable(){
        table.getItems().clear();
        table.setItems(familyMembers);

        TableColumn<FamilyMember, Integer> familyMemberIdCol = new TableColumn<>("ID");
        familyMemberIdCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, Integer>("id"));

        TableColumn<FamilyMember, String> familyMemberNameCol = new TableColumn<>("Name");
        familyMemberNameCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, String>("name"));

        TableColumn<FamilyMember, String> familyMemberDescCol = new TableColumn<>("Description");
        familyMemberDescCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, String>("description"));

        TableColumn<FamilyMember, Calendar> familyMemberCreatedCol = new TableColumn<>("Created on");
        familyMemberCreatedCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, Calendar>("createdOn"));

        TableColumn<FamilyMember, Calendar> familyMemberUpdatedCol = new TableColumn<>("ID");
        familyMemberUpdatedCol.setCellValueFactory(new PropertyValueFactory<FamilyMember, Calendar>("updatedOn"));
    }

//    public void removeFamilyMember(Long id) {
//        familyMemberService.removeFamilyMember(id);
//    }
//
//    public void updateFamilyMember(FamilyMember familyMember) {
//        familyMemberService.updateFamilyMember(familyMember);
//    }
}
