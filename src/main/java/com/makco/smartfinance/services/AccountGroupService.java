package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-06.
 */
public interface AccountGroupService {
    AccountGroup getAccountGroupById(Long id, boolean initializeCategories) throws Exception;
    List<AccountGroup> accountGroupListWithoutCategories() throws Exception;
    List<AccountGroup> accountGroupListWithCategories()throws Exception;
    void saveOrUpdateAccountGroup(AccountGroup accountGroup) throws Exception;
    void removeAccountGroup(Long id) throws Exception;
    List<AccountGroup> getAccountGroupByName(String accountGroupName) throws Exception;

    EnumSet<ErrorEnum> validate(AccountGroup accountGroup) throws Exception;
}
