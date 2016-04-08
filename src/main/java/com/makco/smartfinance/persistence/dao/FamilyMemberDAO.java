package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
//https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
public interface FamilyMemberDAO {
    List<FamilyMember> listFamilyMembers();
    FamilyMember getFamilyMemberById(Long id);
    List<FamilyMember> getFamilyMemberByName(String name);

    void addFamilyMember(FamilyMember familyMember);
    void removeFamilyMember(Long id);
    void saveOrUpdateFamilyMember(FamilyMember familyMember);
    void updateFamilyMember(FamilyMember familyMember);
}
