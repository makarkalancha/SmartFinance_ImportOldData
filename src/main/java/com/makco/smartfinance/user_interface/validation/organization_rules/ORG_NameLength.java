package com.makco.smartfinance.user_interface.validation.organization_rules;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public class ORG_NameLength implements Rule<Organization> {

    @Override
    public EnumSet<ErrorEnum> validate(Organization organization) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (organization.getName().length() > DataBaseConstants.ORG_NAME_MAX_LGTH) {
                errors.add(ErrorEnum.ORG_NAME_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
