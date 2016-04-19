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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class FamilyMemberServiceImpl implements FamilyMemberService {
    private final static Logger LOG = LogManager.getLogger(FamilyMemberServiceImpl.class);
    private FamilyMemberDAO familyMemberDAO = new FamilyMemberDAOImpl();

    private int tmp = 0;
    
    @Override
    public List<FamilyMember> familyMemberList() {
        List<FamilyMember> familyMemberList = new ArrayList<>();
        try {
            familyMemberList = familyMemberDAO.familyMemberList();
            LOG.debug("familyMemberList: before " + tmp + " sec");
            Thread.sleep(tmp);
            LOG.debug("familyMemberList: after " + tmp + " sec");
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return familyMemberList;
    }

    @Override
    public void removeFamilyMember(Long id) {
        try{
            familyMemberDAO.removeFamilyMember(id);
            LOG.debug("removeFamilyMember: before " + tmp + " sec");
            Thread.sleep(tmp);
            LOG.debug("removeFamilyMember: after " + tmp + " sec");
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public List<FamilyMember> getFamilyMemberByName(String name) {
        List<FamilyMember> familyMemberList = new ArrayList<>();
        try {
            familyMemberList = familyMemberDAO.getFamilyMemberByName(name);
            LOG.debug("getFamilyMemberByName: before " + tmp + " sec");
            Thread.sleep(tmp);
            LOG.debug("getFamilyMemberByName: after " + tmp + " sec");
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
            LOG.debug("getFamilyMemberById: before " + tmp + " sec");
            Thread.sleep(tmp);
            LOG.debug("getFamilyMemberById: after " + tmp + " sec");
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return familyMember;
    }

    @Override
    public void saveOrUpdateFamilyMember(FamilyMember familyMember) {
        try{
            familyMemberDAO.saveOrUpdateFamilyMember(familyMember);
            LOG.debug("saveOrUpdateFamilyMember: before " + tmp + " sec");
            Thread.sleep(tmp);
            LOG.debug("saveOrUpdateFamilyMember: after " + tmp + " sec");
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
            LOG.debug("validate: before " + tmp + " sec");
            Thread.sleep(tmp);
            LOG.debug("validate: after " + tmp + " sec");
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
