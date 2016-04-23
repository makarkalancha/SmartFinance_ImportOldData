package com.makco.smartfinance.user_interface.validation.family_member_rules;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import java.util.EnumSet;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class FM_EmptyName implements Rule<FamilyMember> {

    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (StringUtils.isBlank(familyMember.getName())) {
                errors.add(ErrorEnum.FM_NAME_NULL);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
