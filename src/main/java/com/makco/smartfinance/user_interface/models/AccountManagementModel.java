package com.makco.smartfinance.user_interface.models;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.persistence.entity.AccountFactory;
import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.persistence.entity.AccountGroupFactory;
import com.makco.smartfinance.services.AccountGroupService;
import com.makco.smartfinance.services.AccountGroupServiceImpl;
import com.makco.smartfinance.services.AccountService;
import com.makco.smartfinance.services.AccountServiceImpl;
import com.makco.smartfinance.user_interface.decorator.account_management.AccountManagementDecorator;
import com.makco.smartfinance.user_interface.decorator.account_management.AccountManagementDecoratorAccount;
import com.makco.smartfinance.user_interface.decorator.account_management.AccountManagementDecoratorAccountGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public class AccountManagementModel {
    private final static Logger LOG = LogManager.getLogger(AccountManagementModel.class);
    private AccountGroupService accountGroupService = new AccountGroupServiceImpl();
    private AccountService accountService = new AccountServiceImpl();
    private ObservableList<AccountGroup> accountGroupsWithoutCategories = FXCollections.observableArrayList();
//    private ObservableList<Account> categories = FXCollections.observableArrayList();
    private AccountGroup pendingAccountGroup;
    private Account pendingAccount;
    private AccountGroupFactory accountGroupFactory = new AccountGroupFactory();
    private AccountFactory accountFactory = new AccountFactory();
    private Map<String, AccountGroup> accountGroupUINameToAccountGroup = new HashMap<>();
    private ObservableList<String> accountGroupsUINames = FXCollections.observableArrayList();
    private Multimap<AccountManagementDecorator, AccountManagementDecorator> accountManagementDecoratorMultimap = ArrayListMultimap.create();

    public AccountManagementModel() {
//        accountGroupsWithoutCategories.addListener(new ListChangeListener<AccountGroup>() {
//            @Override
//            public void onChanged(Change<? extends AccountGroup> c) {
//                accountGroupsWithoutCategories.forEach(cg -> {
//                    accountGroupUINameToAccountGroup.put(convertAccountGroupFromBackendToUI(cg), cg);
//                });
//            }
//        });
    }

    public void refreshAccountGroupTab() throws Exception {
        try {
            LOG.debug("model->refreshAccountGroupTab");
            accountGroupsWithoutCategories = FXCollections.observableArrayList(accountGroupService.accountGroupListWithoutCategories());
            accountGroupsWithoutCategories.forEach(cg -> {
                accountGroupUINameToAccountGroup.put(convertAccountGroupFromBackendToUI(cg), cg);
            });
            accountGroupsUINames = FXCollections.observableArrayList(accountGroupUINameToAccountGroup.keySet());
            LOG.debug("accountGroupsWithoutCategories.size: " + accountGroupsWithoutCategories.size());
        } catch (Exception e) {
            throw e;
        }
    }

    public void refreshAccountTab() throws Exception {
        try {
            LOG.debug("model->refreshAccountTab");
//            if (!categories.isEmpty()) {
//                categories.clear();
//            }
//            categories = FXCollections.observableArrayList(accountService.accountList());
//            LOG.debug("categories.size: " + categories.size());
//            refreshAccountGroupTab();
//            accountGroupsWithoutCategories.forEach(cg -> {
//                accountGroupUINameToAccountGroup.put(convertAccountGroupFromBackendToUI(cg), cg);
//            });

            if (!accountManagementDecoratorMultimap.isEmpty()) {
                accountManagementDecoratorMultimap.clear();
            }
            List<AccountGroup> accountGroupsWithCategoriesList = FXCollections.observableArrayList(accountGroupService.accountGroupListWithCategories());
            accountGroupsWithCategoriesList.forEach(ag -> {
                Collection<Account> categories = ag.getAccounts();
                List<AccountManagementDecoratorAccount> accountManagementDecoratorCategories = categories.stream()
                    .map(cat -> new AccountManagementDecoratorAccount((Account) cat))
                    .collect(Collectors.toList());
                accountManagementDecoratorMultimap.putAll(new AccountManagementDecoratorAccountGroup(ag), accountManagementDecoratorCategories);
            });
        } catch (Exception e) {
            throw e;
        }
    }

    public String convertAccountGroupFromBackendToUI(AccountGroup accountGroup){
        if(accountGroup != null) {
            StringBuilder result = new StringBuilder();
            result.append(accountGroup.getName());
            result.append(" (");
            result.append(accountGroup.getAccountGroupType().getDiscriminator());
            result.append(")");

            return result.toString();
        }

        return "";
    }

    public ObservableList<String> getAccountGroupUIName(){
        return accountGroupsUINames;
    }

    public AccountGroup convertAccountGroupFromUIToBackendTo(String uiText){
        return accountGroupUINameToAccountGroup.get(uiText);
    }

    public ObservableList<AccountGroup> getAccountGroupsWithoutCategories() throws Exception {
        return accountGroupsWithoutCategories;
    }

    public Multimap<AccountManagementDecorator, AccountManagementDecorator> getAccountManagementDecoratorMultimap(){
        return accountManagementDecoratorMultimap;
    }

    public EnumSet<ErrorEnum> savePendingAccountGroup(DataBaseConstants.ACCOUNT_GROUP_TYPE type, String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            AccountGroup tmpAccountGroup;
            if (pendingAccountGroup != null) {
                pendingAccountGroup.setName(name);
                pendingAccountGroup.setDescription(description);
                tmpAccountGroup = pendingAccountGroup;
            } else {
                tmpAccountGroup = accountGroupFactory.getAccountGroup(type, name, description);
            }

            errors = accountGroupService.validate(tmpAccountGroup);
            if (errors.isEmpty()) {
                accountGroupService.saveOrUpdateAccountGroup(tmpAccountGroup);
                pendingAccountGroup = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshAccountGroupTab();
        }
        return errors;
    }

    public EnumSet<ErrorEnum> savePendingAccount(AccountGroup accountGroup, String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            Account tmpAccount;
            if (pendingAccount != null) {
                pendingAccount.setAccountGroup(accountGroup);
                pendingAccount.setName(name);
                pendingAccount.setDescription(description);
                tmpAccount = pendingAccount;
            } else {
                tmpAccount = accountFactory.getAccount(accountGroup, name, description);
            }

            errors = accountService.validate(tmpAccount);
            if (errors.isEmpty()) {
                accountService.saveOrUpdateAccount(tmpAccount);
                pendingAccount = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshAccountTab();
        }
        return errors;
    }

    public void deletePendingAccountGroup() throws Exception {
        try {
            if (pendingAccountGroup != null && pendingAccountGroup.getId() != null) {
                accountGroupService.removeAccountGroup(pendingAccountGroup.getId());
                pendingAccountGroup = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshAccountGroupTab();
        }
    }

    public void deletePendingAccount() throws Exception {
        try {
            if (pendingAccount != null && pendingAccount.getId() != null) {
                accountService.removeAccount(pendingAccount.getId());
                pendingAccount = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshAccountTab();
        }
    }

    public AccountGroup getPendingAccountGroup() throws Exception {
        return pendingAccountGroup;
    }

    public void setPendingAccountGroupProperty(AccountGroup accountGroup) throws Exception {
        pendingAccountGroup = accountGroup;
    }

    public Account getPendingAccount() throws Exception {
        return pendingAccount;
    }

    public void setPendingAccountProperty(Account account) throws Exception {
        pendingAccount = account;
    }
}