package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
//http://programmers.stackexchange.com/questions/162399/how-essential-is-it-to-make-a-service-layer
public interface FamilyMemberService {
    List<FamilyMember> familyMemberList();
    FamilyMember getFamilyMemberById(Long id);
    List<FamilyMember> getFamilyMemberByName(String name);

    void removeFamilyMember(Long id);
    void saveOrUpdateFamilyMember(FamilyMember familyMember);
    EnumSet<ErrorEnum> validate(FamilyMember familyMember);
}
