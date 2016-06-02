package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryFactory;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupFactory;
import com.makco.smartfinance.services.CategoryGroupService;
import com.makco.smartfinance.services.CategoryGroupServiceImpl;
import com.makco.smartfinance.services.CategoryService;
import com.makco.smartfinance.services.CategoryServiceImpl;
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
    private CategoryService categoryService = new CategoryServiceImpl();
    private ObservableList<CategoryGroup> categoryGroupsWithoutCategories = FXCollections.observableArrayList();
    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private CategoryGroup pendingGroupCategory;
    private Category pendingCategory;
    private CategoryGroupFactory categoryGroupFactory = new CategoryGroupFactory();
    private CategoryFactory categoryFactory = new CategoryFactory();

    public CategoryManagementModel() {

    }

    public void refresh() throws Exception {
        try {
            if (!categoryGroupsWithoutCategories.isEmpty()) {
                categoryGroupsWithoutCategories.clear();
            }
            categoryGroupsWithoutCategories = FXCollections.observableArrayList(categoryGroupService.categoryGroupListWithoutCategories());
            LOG.debug("categoryGroupsWithoutCategories.size: " + categoryGroupsWithoutCategories.size());
        } catch (Exception e) {
            throw e;
        }
    }

    public ObservableList<CategoryGroup> getCategoryGroupsWithoutCategories() throws Exception {
        return categoryGroupsWithoutCategories;
    }

    public ObservableList<Category> getCategories() throws Exception {
        return categories;
    }

    public EnumSet<ErrorEnum> savePendingCategoryGroup(DataBaseConstants.CATEGORY_GROUP_TYPE type, String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            CategoryGroup tmpCategoryGroup;
            if (pendingGroupCategory != null) {
                pendingGroupCategory.setName(name);
                pendingGroupCategory.setDescription(description);
                tmpCategoryGroup = pendingGroupCategory;
                pendingGroupCategory = null;
            } else {
                tmpCategoryGroup = categoryGroupFactory.getCategoryGroup(type, name, description);
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

    public EnumSet<ErrorEnum> savePendingCategory(CategoryGroup categoryGroup, String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            Category tmpCategory;
            if (pendingCategory != null) {
                pendingCategory.setCategoryGroup(categoryGroup);
                pendingCategory.setName(name);
                pendingCategory.setDescription(description);
                tmpCategory = pendingCategory;
                pendingCategory = null;
            } else {
                tmpCategory = categoryFactory.getCategory(categoryGroup, name, description);
            }

            errors = categoryService.validate(tmpCategory);
            if (errors.isEmpty()) {
                categoryGroupService.saveOrUpdateCategoryGroup(tmpCategory);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refresh();
        }
        return errors;
    }

    public void deletePendingCategoryGroup() throws Exception {
        try {
            if (pendingGroupCategory != null && pendingGroupCategory.getId() != null) {
                categoryGroupService.removeCategoryGroup(pendingGroupCategory.getId());
                pendingGroupCategory = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refresh();
        }
    }

    public void deletePendingCategory() throws Exception {
        try {
            if (pendingCategory != null && pendingCategory.getId() != null) {
                categoryService.removeCategory(pendingCategory.getId());
                pendingCategory = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refresh();
        }
    }

    public CategoryGroup getPendingGroupCategory() throws Exception {
        return pendingGroupCategory;
    }

    public void setPendingCategoryGroupProperty(CategoryGroup categoryGroup) throws Exception {
        pendingGroupCategory = categoryGroup;
    }
}