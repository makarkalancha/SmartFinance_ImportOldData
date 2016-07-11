package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public class AccountGroupFactory {

    public AccountGroup getAccountGroup(DataBaseConstants.ACCOUNT_GROUP_TYPE type, String name, String description) throws Exception {
        AccountGroup accountGroupResult = null;
        if (type != null) {
            switch (type) {
                case CREDIT:
                    accountGroupResult = new AccountGroupCredit(name, description);
                    break;
                case DEBIT:
                    accountGroupResult = new AccountGroupDebit(name, description);
                    break;
                default:
                    throw new Exception("This type of Account Group is not supported!");
            }
        }
        return accountGroupResult;
    }

}
