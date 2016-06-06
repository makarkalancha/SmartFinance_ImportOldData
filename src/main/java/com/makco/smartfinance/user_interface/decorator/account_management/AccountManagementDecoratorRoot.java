package com.makco.smartfinance.user_interface.decorator.account_management;

import com.makco.smartfinance.persistence.entity.Account;

/**
 * User: Makar Kalancha
 * Date: 05/06/2016
 * Time: 17:53
 */
public class AccountManagementDecoratorRoot implements AccountManagementDecorator {
    private String name;

    public AccountManagementDecoratorRoot(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAccountGroupType() {
        return null;
    }

    @Override
    public String getCreatedOn() {
        return null;
    }

    @Override
    public String getUpdatedOn() {
        return null;
    }

    @Override
    public Account getAccount() {
        return null;
    }
}
