package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
public interface FamilyMemberDAO {
    void addFamilyMember(FamilyMember familyMember);
    List<FamilyMember> listFamilyMembers();
    FamilyMember getFamilyMemberById(Long id);
    void removeFamilyMember(Long id);
    void updateFamilyMember(FamilyMember familyMember);
}
