package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.FamilyMemberRuleSet;
import java.util.EnumSet;
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
            DialogMessages.showExceptionAlert(e);
        }
    }

    public ObservableList<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void savePendingFamilyMember(String name, String description){
        try{
            FamilyMember tmpFamilyMember;
            if(pendingFamilyMember != null){
                pendingFamilyMember.setName(name);
                pendingFamilyMember.setDescription(description);
                tmpFamilyMember = pendingFamilyMember;
                pendingFamilyMember = null;
            } else {
                tmpFamilyMember = new FamilyMember(name, description);
            }

            RuleSet ruleSet = new FamilyMemberRuleSet();
            EnumSet<ErrorEnum> errors = ruleSet.validate(tmpFamilyMember);
            if(!errors.isEmpty()) {
                DialogMessages.showErrorDialog("Error while saving Family Member: " + tmpFamilyMember.getName(), errors, null);
            } else {
                familyMemberService.saveOrUpdateFamilyMember(tmpFamilyMember);
            }
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
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
            DialogMessages.showExceptionAlert(e);
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
