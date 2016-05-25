package com.makco.smartfinance.user_interface.validation.organization_rules;

import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-22.
 */
public class ORG_EmptyName implements Rule<Organization> {

    @Override
    public EnumSet<ErrorEnum> validate(Organization organization) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (StringUtils.isBlank(organization.getName())) {
                errors.add(ErrorEnum.ORG_NAME_NULL);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
