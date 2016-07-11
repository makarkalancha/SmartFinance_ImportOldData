package com.makco.smartfinance.user_interface.validation.family_member_rules;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-08.
 */
public class FM_NameLength implements Rule<FamilyMember> {

    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (familyMember.getName().length() > DataBaseConstants.FM_NAME_MAX_LGTH) {
                errors.add(ErrorEnum.FM_NAME_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
