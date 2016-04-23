package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
//https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
public interface FamilyMemberDAO {
    List<FamilyMember> familyMemberList() throws Exception;
    FamilyMember getFamilyMemberById(Long id) throws Exception;
    List<FamilyMember> getFamilyMemberByName(String name) throws Exception;

    void removeFamilyMember(Long id) throws Exception;
    void saveOrUpdateFamilyMember(FamilyMember familyMember) throws Exception;
}
