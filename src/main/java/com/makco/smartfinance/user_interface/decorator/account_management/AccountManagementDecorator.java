package com.makco.smartfinance.user_interface.decorator.account_management;

import com.makco.smartfinance.persistence.entity.Account;

/**
 * Created by Makar Kalancha on 2016-06-03.
 */
public interface AccountManagementDecorator {
    Long getId();
    String getDescription();
    String getName();
    String getAccountGroupType();
    String getCreatedOn();
    String getUpdatedOn();
    Account getAccount();
}
