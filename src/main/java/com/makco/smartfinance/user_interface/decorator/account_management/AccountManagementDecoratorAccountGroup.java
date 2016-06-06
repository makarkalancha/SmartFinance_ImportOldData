package com.makco.smartfinance.user_interface.decorator.account_management;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.persistence.entity.AccountGroup;

/**
 * Created by mcalancea on 2016-06-03.
 */
public class AccountManagementDecoratorAccountGroup implements AccountManagementDecorator {
    private String name;
    private String description;
    private DataBaseConstants.ACCOUNT_GROUP_TYPE type;

    public AccountManagementDecoratorAccountGroup(AccountGroup accountGroup){
        this.name = accountGroup.getName();
        this.description = accountGroup.getDescription();
        this.type = accountGroup.getAccountGroupType();
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAccountGroupType() {
        return type.getDiscriminator();
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof AccountManagementDecoratorAccountGroup) {
            AccountManagementDecoratorAccountGroup that = (AccountManagementDecoratorAccountGroup) other;
            return Objects.equal(getId(), that.getId())
                    && Objects.equal(getName(), that.getName())
                    && Objects.equal(getDescription(), that.getDescription())
                    && Objects.equal(getAccountGroupType(), that.getAccountGroupType())
                    && Objects.equal(getCreatedOn(), that.getCreatedOn())
                    && Objects.equal(getUpdatedOn(), that.getUpdatedOn())
                    && Objects.equal(getAccount(), that.getAccount());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                getId(),
                getName(),
                getDescription(),
                getAccountGroupType(),
                getCreatedOn(),
                getUpdatedOn(),
                getAccount()
        );
    }
}
