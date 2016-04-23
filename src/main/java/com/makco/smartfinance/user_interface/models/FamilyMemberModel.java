package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
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

    public void refreshFamilyMembers() throws Exception {
        try{
            if (!familyMembers.isEmpty()) {
                familyMembers.clear();
            }
            familyMembers = FXCollections.observableArrayList((List<FamilyMember>) familyMemberService.familyMemberList());
            LOG.debug("familyMember.size: " + familyMembers.size());
        }catch (Exception e){
            throw e;
        }
    }

    public ObservableList<FamilyMember> getFamilyMembers() throws Exception {
        return familyMembers;
    }

    public EnumSet<ErrorEnum> savePendingFamilyMember(String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            FamilyMember tmpFamilyMember;
            if (pendingFamilyMember != null) {
                pendingFamilyMember.setName(name);
                pendingFamilyMember.setDescription(description);
                tmpFamilyMember = pendingFamilyMember;
                pendingFamilyMember = null;
            } else {
                tmpFamilyMember = new FamilyMember(name, description);
            }

            errors = familyMemberService.validate(tmpFamilyMember);
            if (!errors.isEmpty()) {

            } else {
                familyMemberService.saveOrUpdateFamilyMember(tmpFamilyMember);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshFamilyMembers();
        }
        return errors;
    }

    public void deletePendingFamilyMember() throws Exception {
        try{
            if (pendingFamilyMember != null && pendingFamilyMember.getId() != null) {
                familyMemberService.removeFamilyMember(pendingFamilyMember.getId());
                pendingFamilyMember = null;
            }
        } catch (Exception e){
            throw e;
        }finally {
            refreshFamilyMembers();
        }
    }

    public FamilyMember getPendingFamilyMember() throws Exception {
        return pendingFamilyMember;
    }

    public void setPendingFamilyMemberProperty(FamilyMember familyMember) throws Exception {
        pendingFamilyMember = familyMember;
    }
}
