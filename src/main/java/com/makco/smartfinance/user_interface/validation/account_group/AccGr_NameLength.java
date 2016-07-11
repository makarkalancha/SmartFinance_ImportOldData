package com.makco.smartfinance.user_interface.validation.account_group;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public class AccGr_NameLength implements Rule<AccountGroup> {

    @Override
    public EnumSet<ErrorEnum> validate(AccountGroup accountGroup) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (accountGroup != null && accountGroup.getName().length() > DataBaseConstants.AG_NAME_MAX_LGTH) {
                errors.add(ErrorEnum.AccGr_NAME_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
