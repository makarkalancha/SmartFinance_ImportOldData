package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.FamilyMemberDAO;
import com.makco.smartfinance.persistence.dao.FamilyMemberDAOImpl;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class FamilyMemberServiceImpl implements FamilyMemberService {

    private FamilyMemberDAO familyMemberDAO = new FamilyMemberDAOImpl();

    @Override
    public void addFamilyMember(FamilyMember familyMember) {
        familyMemberDAO.addFamilyMember(familyMember);
    }

    @Override
    public List<FamilyMember> listFamilyMembers() {
        return familyMemberDAO.listFamilyMembers();
    }

    @Override
    public void removeFamilyMember(Long id) {
        familyMemberDAO.removeFamilyMember(id);
    }

    @Override
    public void updateFamilyMember(FamilyMember familyMember) {
        familyMemberDAO.updateFamilyMember(familyMember);
    }

    @Override
    public List<FamilyMember> getFamilyMemberByName(String name) {
        return familyMemberDAO.getFamilyMemberByName(name);
    }

    @Override
    public FamilyMember getFamilyMemberById(Long id) {
        return familyMemberDAO.getFamilyMemberById(id);
    }

    @Override
    public void saveOrUpdateFamilyMember(FamilyMember familyMember) {
        familyMemberDAO.saveOrUpdateFamilyMember(familyMember);
    }

    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember){
        EnumSet<ErrorEnum> errorsList = EnumSet.noneOf(ErrorEnum.class);
//        if(StringUtils.isBlank(familyMember.getName())
        return errorsList;
    }
}
