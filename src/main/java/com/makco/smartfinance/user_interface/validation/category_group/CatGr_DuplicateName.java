package com.makco.smartfinance.user_interface.validation.category_group;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.services.FamilyMemberService;
import com.makco.smartfinance.services.FamilyMemberServiceImpl;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-08.
 */
//TODO CHECK ALL RULES
public class CatGr_DuplicateName implements Rule<CategoryGroup> {
    private FamilyMemberService familyMemberService = new FamilyMemberServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            List<FamilyMember> familyMembers = familyMemberService.getFamilyMemberByName(familyMember.getName());
            familyMembers.removeIf(f -> f.getId().equals(familyMember.getId()));
            if (!familyMembers.isEmpty()) {
                errors.add(ErrorEnum.FM_NAME_DUPLICATE);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
