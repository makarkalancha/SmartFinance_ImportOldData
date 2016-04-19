package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
//https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
public interface FamilyMemberDAO {
    List<FamilyMember> familyMemberList();
    FamilyMember getFamilyMemberById(Long id);
    List<FamilyMember> getFamilyMemberByName(String name);

    void removeFamilyMember(Long id);
    void saveOrUpdateFamilyMember(FamilyMember familyMember);
}
