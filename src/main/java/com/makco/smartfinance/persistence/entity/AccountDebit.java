package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
@Entity
@DiscriminatorValue(DataBaseConstants.ACCOUNT_GROUP_TYPE.Values.DEBIT)
public class AccountDebit extends Account implements Comparable<AccountDebit>{
    private final static Logger LOG = LogManager.getLogger(AccountDebit.class);
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_GROUP_ID", referencedColumnName = "ID", nullable = false)
    private AccountGroup accountGroup;

    public AccountDebit(){

    }

    public AccountDebit(AccountGroup accountGroup, String name, String description) {
        this.accountGroup = (AccountGroupDebit) accountGroup;
        this.name = name;
        this.description = description;
    }

    @Override
    public AccountGroup getAccountGroup() {
        return accountGroup;
    }

    @Override
    public void setAccountGroup(AccountGroup accountGroup) {
        this.accountGroup = (AccountGroupDebit) accountGroup;
    }

    @Override
    public DataBaseConstants.ACCOUNT_GROUP_TYPE getAccountGroupType() {
        return DataBaseConstants.ACCOUNT_GROUP_TYPE.DEBIT;
    }

    /**
     * when entity is transient id == null, so it's impossible to put it in Map or Set
     * redundant check getAccountGroupType(), because there's already check for instance of AccountDebit
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof AccountDebit) {
            AccountDebit that = (AccountDebit) other;
            return Objects.equal(getAccountGroupType(), that.getAccountGroupType())
                    && Objects.equal(getName(), that.getName());
        }
        return false;
    }


    //when entity is transient id == null, so it's impossible to put it in Map or Set
    @Override
    public int hashCode() {
        return Objects.hashCode(getAccountGroupType(), getName());
    }

    @Override
    public String toString() {
        return "AccountDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getAccountGroupType='" + getAccountGroupType() + '\'' +
                ", AccountGroupDebit='" + accountGroup + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
            '}';
    }


    //because AccountDebit is used in AccountGroupDebit SortedSet<AccountDebit> and this collection puts only Comparable
    @Override
    public int compareTo(AccountDebit that) {
        return this.getName().compareTo(that.getName());
    }
}
