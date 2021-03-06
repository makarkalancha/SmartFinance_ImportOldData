package com.makco.smartfinance.user_interface.validation.family_member_rules;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-08.
 */
public class FM_DuplicateName implements Rule<FamilyMember> {
    private FamilyMemberService familyMemberService = new FamilyMemberServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            List<FamilyMember> familyMembers = familyMemberService.getFamilyMemberByName(familyMember.getName());
            familyMembers.removeIf(f -> f.getId().equals(familyMember.getId()));
            if (!familyMembers.isEmpty()) {
                errors.add(ErrorEnum.FM_NAME_DUPLICATE);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
