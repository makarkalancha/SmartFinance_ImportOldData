package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.services.CategoryGroupService;
import com.makco.smartfinance.services.CategoryGroupServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-05-13.
 */
public class CategoryManagementModel {
    private final static Logger LOG = LogManager.getLogger(CategoryManagementModel.class);
    private CategoryGroupService categoryGroupService = new CategoryGroupServiceImpl();
    private ObservableList<CategoryGroup> categoryGroups = FXCollections.observableArrayList();
    private CategoryGroup pendingCategoryGroup;

    public CategoryManagementModel(){

    }

    public void refresh() throws Exception{
        try{
            if (!categoryGroups.isEmpty()) {
                categoryGroups.clear();
            }
            categoryGroups = FXCollections.observableArrayList(categoryGroupService.categoryGroupList(true));
            LOG.debug("categoryGroups.size: " + categoryGroups.size());
        }catch (Exception e) {
            throw e;
        }
    }

    public ObservableList<CategoryGroup> getCategoryGroups() throws Exception {
        return categoryGroups;
    }

    public EnumSet<ErrorEnum> savePendingCategoryGroup(DataBaseConstants.CATEGORY_GROUP_TYPE type, String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            CategoryGroup tmpCategoryGroup;
            if (pendingCategoryGroup != null) {
                pendingCategoryGroup.setName(name);
                pendingCategoryGroup.setDescription(description);
                tmpCategoryGroup = pendingCategoryGroup;
                pendingCategoryGroup = null;
            } else {
                switch (type){
                    case CREDIT:
                        tmpCategoryGroup = new CategoryGroupCredit(name, description);
                        break;
                    case DEBIT:
                        tmpCategoryGroup = new CategoryGroupDebit(name, description);
                        break;
                    default:
                        throw new Exception("This type of Category Group is not supported!");
                }
            }

            errors = categoryGroupService.validate(tmpCategoryGroup);
            if (errors.isEmpty()) {
                categoryGroupService.saveOrUpdateCategoryGroup(tmpCategoryGroup);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refresh();
        }
        return errors;
    }

    public void deletePendingCurrency() throws Exception {
        try {
            if (pendingCategoryGroup != null && pendingCategoryGroup.getId() != null) {
                categoryGroupService.removeCategoryGroup(pendingCategoryGroup.getId());
                pendingCategoryGroup = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refresh();
        }
    }

    public CategoryGroup getPendingCategoryGroup() throws Exception {
        return pendingCategoryGroup;
    }

    public void setPendingCategoryGroupProperty(CategoryGroup categoryGroup) throws Exception {
        pendingCategoryGroup = categoryGroup;
    }