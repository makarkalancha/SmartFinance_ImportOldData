package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class FamilyMemberModel {
    private final static Logger LOG = LogManager.getLogger(FamilyMemberModel.class);
    private FamilyMemberService familyMemberService = new FamilyMemberServiceImpl();
    private ObservableList<FamilyMember> familyMembers = FXCollections.observableArrayList();
    private FamilyMember pendingFamilyMember;

    public FamilyMemberModel(){

    }

    public void refreshFamilyMembers(){
        try{
            if (!familyMembers.isEmpty()) {
                familyMembers.clear();
            }
            familyMembers = FXCollections.observableArrayList((List<FamilyMember>) familyMemberService.listFamilyMembers());
            LOG.debug("familyMember.size: " + familyMembers.size());
        }catch (Exception e){
            DialogMessages.showAlert(e);
        }
    }

    public ObservableList<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void savePendingFamilyMember(String name, String description){
        try{
            if(pendingFamilyMember != null){
                pendingFamilyMember.setName(name);
                pendingFamilyMember.setDescription(description);
                familyMemberService.saveOrUpdateFamilyMember(pendingFamilyMember);
                pendingFamilyMember = null;
            } else {
                FamilyMember tmpFamilyMember = new FamilyMember(name, description);
                familyMemberService.saveOrUpdateFamilyMember(tmpFamilyMember);
            }
        } catch (Exception e){
            DialogMessages.showAlert(e);
        }finally {
            refreshFamilyMembers();
        }
    }

    public void deletePendingFamilyMember(){
        try{
            if (pendingFamilyMember != null && pendingFamilyMember.getId() != null) {
                familyMemberService.removeFamilyMember(pendingFamilyMember.getId());
                pendingFamilyMember = null;
            }
            refreshFamilyMembers();
        } catch (Exception e){
            DialogMessages.showAlert(e);
        }finally {
            refreshFamilyMembers();
        }
    }

    public FamilyMember getPendingFamilyMember() {
        return pendingFamilyMember;
    }

    public void setPendingFamilyMemberProperty(FamilyMember familyMember) {
        pendingFamilyMember = familyMember;
    }
}
