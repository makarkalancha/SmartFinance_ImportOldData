package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by mcalancea on 2016-06-06.
 */
@Entity
@DiscriminatorValue(DataBaseConstants.ACCOUNT_GROUP_TYPE.Values.DEBIT)
public class AccountGroupDebit extends AccountGroup<AccountDebit>{
    @OneToMany(mappedBy = "accountGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @javax.persistence.OrderBy("name")
    private SortedSet<AccountDebit> accounts = new TreeSet<>();

    public AccountGroupDebit() {
    }

    public AccountGroupDebit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public Collection<AccountDebit> getAccounts() {
        return this.accounts;
    }

    @Override
    public void setAccounts(Collection<AccountDebit> accounts) {
        this.accounts = new TreeSet<>(accounts);
    }

    @Override
    public DataBaseConstants.ACCOUNT_GROUP_TYPE getAccountGroupType() {
        return DataBaseConstants.ACCOUNT_GROUP_TYPE.DEBIT;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AccountGroupDebit) {
            AccountGroupDebit that = (AccountGroupDebit) other;
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
        return "AccountGroupDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getAccountGroupType='" + getAccountGroupType() + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
            '}';
    }
}
