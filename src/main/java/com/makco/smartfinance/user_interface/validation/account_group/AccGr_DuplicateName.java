package com.makco.smartfinance.user_interface.validation.account_group;

import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.services.AccountGroupService;
import com.makco.smartfinance.services.AccountGroupServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-07.
 */
public class AccGr_DuplicateName implements Rule<AccountGroup> {
    private AccountGroupService accountGroupService = new AccountGroupServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(AccountGroup accountGroup) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (accountGroup != null) {
                List<AccountGroup> accountGroupsByName = accountGroupService.getAccountGroupByName(accountGroup.getName());
                accountGroupsByName.removeIf(cg -> cg.getId().equals(accountGroup.getId()));
                accountGroupsByName.removeIf(cg -> !cg.getAccountGroupType().equals(accountGroup.getAccountGroupType()));
                if (!accountGroupsByName.isEmpty()) {
                    errors.add(ErrorEnum.AccGr_NAME_DUPLICATE);
                }
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
