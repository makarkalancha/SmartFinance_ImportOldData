package com.makco.smartfinance.user_interface.validation.account_group;

import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public class AccGr_EmptyAccountGroupType implements Rule<AccountGroup> {

    @Override
    public EnumSet<ErrorEnum> validate(AccountGroup accountGroup) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (accountGroup == null || StringUtils.isBlank(accountGroup.getAccountGroupType().getDiscriminator())) {
                errors.add(ErrorEnum.AccGr_NULL_AG_TYPE);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
