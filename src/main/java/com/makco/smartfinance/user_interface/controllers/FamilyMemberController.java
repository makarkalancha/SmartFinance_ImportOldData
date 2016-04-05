package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class FamilyMemberController implements Initializable, ControlledScreen {
    private ScreensController myController;
    private FamilyMemberService familyMemberService = new FamilyMemberServiceImpl();
    private ObservableList<FamilyMember> familyMembers = FXCollections.observableArrayList();

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }

//    @FXML
    public void addFamilyMember(FamilyMember familyMember){
        familyMemberService.addFamilyMember(familyMember);
    }

    public ObservableList<FamilyMember> getFamilyMembers() {
        if(!familyMembers.isEmpty()){
            familyMembers.clear();
        }
        familyMembers = FXCollections.observableArrayList((List<FamilyMember>) familyMemberService.listFamilyMembers());
        return familyMembers;
    }

    public void removeFamilyMember(Long id) {
        familyMemberService.removeFamilyMember(id);
    }

    public void updateFamilyMember(FamilyMember familyMember) {
        familyMemberService.updateFamilyMember(familyMember);
    }
}
