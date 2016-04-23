package com.makco.smartfinance.user_interface.validation.organization_rules;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-22.
 */
public class ORG_DescLength implements Rule<Organization> {

    @Override
    public EnumSet<ErrorEnum> validate(Organization organization) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (organization.getDescription().length() > DataBaseConstants.ORG_DESCRIPTION_MAX_LGTH) {
                errors.add(ErrorEnum.ORG_DESC_LGTH);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
