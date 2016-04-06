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
        FamilyMember familyMember = familyMemberService.getFamilyMemberById(Long.parseLong(idTx.getText()));
        familyMember.setName(nameTx.getText());
        familyMember.setDescription(descTx.getText());
        familyMemberService.updateFamilyMember(familyMember);
        populateTable();
    }

    @FXML
    public void delete(ActionEvent event) {
        familyMemberService.removeFamilyMember(Long.parseLong(idTx.getText()));
        populateTable();
    }

    @FXML
    public void first(ActionEvent event){
        index = 0;
        populateForm(index);
        populateTable();
    }

    @FXML
    public void oneUp(ActionEvent event){
        if(index > 0){
            index--;
        }else {
            event.consume();
        }
        populateForm(index);
        populateTable();
    }

    @FXML
    public void oneDown(ActionEvent event){
        if(index < (getFamilyMembers().size() - 1) ){
            index++;
        }else {
            event.consume();
        }
        populateForm(index);
        populateTable();
    }

    @FXML
    public void last(ActionEvent event){
        index = getFamilyMembers().size() - 1;
        populateForm(index);
        populateTable();
    }

    public ObservableList<FamilyMember> getFamilyMembers() {
        if(!familyMembers.isEmpty()){
            familyMembers.clear();
        }
        familyMembers = FXCollections.observableArrayList((List<FamilyMember>) familyMemberService.listFamilyMembers());
        LOG.debug("familyMember.size: " + familyMembers.size());
        return familyMembers;
    }

    private void populateForm(int index){
        if(getFamilyMembers().isEmpty()){
            return;
        }

        FamilyMember familyMember = getFamilyMembers().get(index);
        idTx.setText(familyMember.getId().toString());
        nameTx.setText(familyMember.getName());
        descTx.setText(familyMember.getDescription());
        createdOnTx.setText(familyMember.getCreatedOn().toString());
        updatedOnTx.setText(familyMember.getUpdatedOn().toString());
    }

    private void populateTable(){
        table.getItems().clear();
        table.setItems(getFamilyMembers());

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

    }

//    public void removeFamilyMember(Long id) {
//        familyMemberService.removeFamilyMember(id);
//    }
//
//    public void updateFamilyMember(FamilyMember familyMember) {
//        familyMemberService.updateFamilyMember(familyMember);
//    }
}
