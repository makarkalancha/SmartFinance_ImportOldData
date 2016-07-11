package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.AccountGroup;

import java.util.List;

/**
 * Created by Makar Kalancha on 2016-05-12.
 */
public interface AccountGroupDAO {
    AccountGroup getAccountGroupById(Long id, boolean initializeCategories) throws Exception;
    List<AccountGroup> accountGroupListWithoutCategories() throws Exception;
    List<AccountGroup> accountGroupListWithCategories()throws Exception;
    void saveOrUpdateAccountGroup(AccountGroup accountGroup) throws Exception;
    void removeAccountGroup(Long id) throws Exception;

    List<AccountGroup> getAccountGroupByName(String accountGroupName) throws Exception;
}
