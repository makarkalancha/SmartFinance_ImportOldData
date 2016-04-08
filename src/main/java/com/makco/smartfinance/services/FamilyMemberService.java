package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.utils.Errors;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
public interface FamilyMemberService {
    List<FamilyMember> listFamilyMembers();
    FamilyMember getFamilyMemberById(Long id);

    void addFamilyMember(FamilyMember familyMember);
    void removeFamilyMember(Long id);
    void saveOrUpdateFamilyMember(FamilyMember familyMember);
    void updateFamilyMember(FamilyMember familyMember);
    EnumSet<Errors> validate(FamilyMember familyMember);
}
