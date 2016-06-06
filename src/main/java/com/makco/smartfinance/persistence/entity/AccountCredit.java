package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by mcalancea on 2016-06-06.
 */
@Entity
@DiscriminatorValue(DataBaseConstants.ACCOUNT_GROUP_TYPE.Values.CREDIT)
public class AccountCredit extends Account implements Comparable<AccountCredit>{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_GROUP_ID", referencedColumnName = "ID", nullable = false)
    private AccountGroup accountGroup;

    public AccountCredit(){

    }

    public AccountCredit(AccountGroup accountGroup, String name, String description) {
        this.accountGroup = (AccountGroupCredit) accountGroup;
        this.description = description;
        this.name = name;
    }

    @Override
    public AccountGroup getAccountGroup() {
        return accountGroup;
    }

    @Override
    public void setAccountGroup(AccountGroup accountGroup) {
        this.accountGroup = accountGroup;
    }

    @Override
    public DataBaseConstants.ACCOUNT_GROUP_TYPE getAccountGroupType() {
        return DataBaseConstants.ACCOUNT_GROUP_TYPE.CREDIT;
    }

    /**
     * when entity is transient id == null, so it's impossible to put it in Map or Set
     * redundant check getAccountGroupType(), because there's already check for instance of AccountDebit
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof AccountCredit) {
            AccountCredit that = (AccountCredit) other;
            return Objects.equal(getAccountGroupType(), that.getAccountGroupType())
                    && Objects.equal(getName(), that.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAccountGroupType(), getName());
    }

    @Override
    public String toString() {
        return "AccountCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getAccountGroupType='" + getAccountGroupType() + '\'' +
                ", accountGroup='" + accountGroup + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }

    @Override
    public int compareTo(AccountCredit that) {
        return this.getName().compareTo(that.getName());
    }
}
