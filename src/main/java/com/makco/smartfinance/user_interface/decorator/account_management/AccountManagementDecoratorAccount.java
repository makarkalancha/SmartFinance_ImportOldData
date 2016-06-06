package com.makco.smartfinance.user_interface.decorator.account_management;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;

/**
 * Created by mcalancea on 2016-06-03.
 */
public class AccountManagementDecoratorAccount implements AccountManagementDecorator {
    private Long id;
    private DataBaseConstants.ACCOUNT_GROUP_TYPE type;
    private String name;
    private String description;
    private String createdOn;
    private String updatedOn;
    private Account account;

    public AccountManagementDecoratorAccount(Account account){
        this.id = account.getId();
        this.type = account.getAccountGroupType();
        this.name = account.getName();
        this.description = account.getDescription();

        this.createdOn = UserInterfaceConstants.FULL_DATE_FORMAT.format(account.getCreatedOn());
        this.updatedOn = UserInterfaceConstants.FULL_DATE_FORMAT.format(account.getUpdatedOn());

        this.account = account;
    }

    @Override
    public Long getId() {
        return id;
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
        return createdOn;
    }

    @Override
    public String getUpdatedOn() {
        return updatedOn;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AccountManagementDecoratorAccount) {
            AccountManagementDecoratorAccount that = (AccountManagementDecoratorAccount) other;
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
