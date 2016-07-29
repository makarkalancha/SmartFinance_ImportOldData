package com.makco.smartfinance.persistence.entity;

/**
 * Created by Makar Kalancha on 2016-06-02.
 */
public class AccountFactory {

    public Account getAccount(AccountGroup accountGroup, String name, String description) throws Exception {
        Account accountResult = null;
        if (accountGroup != null && accountGroup.getAccountGroupType() != null) {
            switch (accountGroup.getAccountGroupType()) {
                case CREDIT:
                    accountResult = new AccountCredit(accountGroup, name, description);
                    break;
                case DEBIT:
                    accountResult = new AccountDebit(accountGroup, name, description);
                    break;
                default:
                    throw new Exception("This type of Account Group is not supported!");
            }
        }
        return accountResult;
    }

}
