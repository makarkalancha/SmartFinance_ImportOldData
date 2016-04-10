package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.FamilyMemberDAO;
import com.makco.smartfinance.persistence.dao.FamilyMemberDAOImpl;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.FamilyMemberRuleSet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class FamilyMemberServiceImpl implements FamilyMemberService {

    private FamilyMemberDAO familyMemberDAO = new FamilyMemberDAOImpl();

    @Override
    public void addFamilyMember(FamilyMember familyMember) {
        try {
            familyMemberDAO.addFamilyMember(familyMember);
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public List<FamilyMember> listFamilyMembers() {
        List<FamilyMember> familyMemberList = new ArrayList<>();
        try {
            familyMemberList = familyMemberDAO.listFamilyMembers();
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return familyMemberList;
    }

    @Override
    public void removeFamilyMember(Long id) {
        try{
            familyMemberDAO.removeFamilyMember(id);
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void updateFamilyMember(FamilyMember familyMember) {
        try{
            familyMemberDAO.updateFamilyMember(familyMember);
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public List<FamilyMember> getFamilyMemberByName(String name) {
        List<FamilyMember> familyMemberList = new ArrayList<>();
        try {
            familyMemberList = familyMemberDAO.getFamilyMemberByName(name);
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return familyMemberList;
    }

    @Override
    public FamilyMember getFamilyMemberById(Long id) {
        FamilyMember familyMember = null;
        try {
            familyMember = familyMemberDAO.getFamilyMemberById(id);
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return familyMember;
    }

    @Override
    public void saveOrUpdateFamilyMember(FamilyMember familyMember) {
        try{
            familyMemberDAO.saveOrUpdateFamilyMember(familyMember);
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new FamilyMemberRuleSet();
            errors = ruleSet.validate(familyMember);
            Thread.sleep(5000);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
