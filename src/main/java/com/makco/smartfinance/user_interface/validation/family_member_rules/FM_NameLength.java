package com.makco.smartfinance.user_interface.validation.family_member_rules;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class FM_NameLength implements Rule<FamilyMember> {

    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (familyMember.getName().length() > 64) {
                errors.add(ErrorEnum.FM_NAME_LGTH);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}