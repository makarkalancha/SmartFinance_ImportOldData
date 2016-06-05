package com.makco.smartfinance.user_interface.models;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryFactory;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.entity.CategoryGroupFactory;
import com.makco.smartfinance.services.CategoryGroupService;
import com.makco.smartfinance.services.CategoryGroupServiceImpl;
import com.makco.smartfinance.services.CategoryService;
import com.makco.smartfinance.services.CategoryServiceImpl;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.decorator.CategoryManagementDecoratorCategory;
import com.makco.smartfinance.user_interface.decorator.CategoryManagementDecoratorCategoryGroup;
import com.makco.smartfinance.user_interface.decorator.CategoryManagmentDecorator;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mcalancea on 2016-05-13.
 */
public class CategoryManagementModel {
    private final static Logger LOG = LogManager.getLogger(CategoryManagementModel.class);
    private CategoryGroupService categoryGroupService = new CategoryGroupServiceImpl();
    private CategoryService categoryService = new CategoryServiceImpl();
    private ObservableList<CategoryGroup> categoryGroupsWithoutCategories = FXCollections.observableArrayList();
//    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private CategoryGroup pendingCategoryGroup;
    private Category pendingCategory;
    private CategoryGroupFactory categoryGroupFactory = new CategoryGroupFactory();
    private CategoryFactory categoryFactory = new CategoryFactory();
    private Map<String, CategoryGroup> categoryGroupUINameToCategoryGroup = new HashMap<>();
    private Multimap<CategoryManagmentDecorator, CategoryManagmentDecorator> categoryManagmentDecoratorMultimap = ArrayListMultimap.create();

    public CategoryManagementModel() {
//        categoryGroupsWithoutCategories.addListener(new ListChangeListener<CategoryGroup>() {
//            @Override
//            public void onChanged(Change<? extends CategoryGroup> c) {
//                categoryGroupsWithoutCategories.forEach(cg -> {
//                    categoryGroupUINameToCategoryGroup.put(convertCategoryGroupFromBackendToUI(cg), cg);
//                });
//            }
//        });
    }

    public void refreshCategoryGroupTab() throws Exception {
        try {
            LOG.debug("model->refreshCategoryGroupTab");
            if (!categoryGroupsWithoutCategories.isEmpty()) {
                categoryGroupsWithoutCategories.clear();
            }
            categoryGroupsWithoutCategories = FXCollections.observableArrayList(categoryGroupService.categoryGroupListWithoutCategories());
            LOG.debug("categoryGroupsWithoutCategories.size: " + categoryGroupsWithoutCategories.size());
        } catch (Exception e) {
            throw e;
        }
    }

    public void refreshCategoryTab() throws Exception {
        try {
            LOG.debug("model->refreshCategoryTab");
//            if (!categories.isEmpty()) {
//                categories.clear();
//            }
//            categories = FXCollections.observableArrayList(categoryService.categoryList());
//            LOG.debug("categories.size: " + categories.size());
//            refreshCategoryGroupTab();
//            categoryGroupsWithoutCategories.forEach(cg -> {
//                categoryGroupUINameToCategoryGroup.put(convertCategoryGroupFromBackendToUI(cg), cg);
//            });

            if (!categoryManagmentDecoratorMultimap.isEmpty()) {
                categoryManagmentDecoratorMultimap.clear();
            }
            List<CategoryGroup> categoryGroupsWithCategoriesList = FXCollections.observableArrayList(categoryGroupService.categoryGroupListWithCategories());
            categoryGroupsWithCategoriesList.forEach(cg -> {
                Collection<Category> categories = cg.getCategories();
                List<CategoryManagementDecoratorCategory> categoryManagementDecoratorCategories = categories.stream()
                    .map(cat -> new CategoryManagementDecoratorCategory((Category) cat))
                    .collect(Collectors.toList());
                categoryManagmentDecoratorMultimap.putAll(new CategoryManagementDecoratorCategoryGroup(cg), categoryManagementDecoratorCategories);
            });
        } catch (Exception e) {
            throw e;
        }
    }

    //category
    public String convertCategoryGroupFromBackendToUI(CategoryGroup categoryGroup){
        if(categoryGroup != null) {
            StringBuilder result = new StringBuilder();
            result.append(categoryGroup.getName());
            result.append(" (");
            result.append(categoryGroup.getCategoryGroupType().getDiscriminator());
            result.append(")");

            return result.toString();
        }

        return "";
    }

    public CategoryGroup convertCategoryGroupFromUIToBackendTo(String uiText){
        return categoryGroupUINameToCategoryGroup.get(uiText);
    }

    public ObservableList<CategoryGroup> getCategoryGroupsWithoutCategories() throws Exception {
        return categoryGroupsWithoutCategories;
    }

//    public ObservableList<CategoryGroup> getCategoryGroupsWithCategories() throws Exception {
//        return categoryGroupsWithCategoriesList;
//    }

    public Multimap<CategoryManagmentDecorator, CategoryManagmentDecorator> getCategoryManagmentDecoratorMultimap(){
        return categoryManagmentDecoratorMultimap;
    }


//    public ObservableList<Category> getCategories() throws Exception {
//        return categories;
//    }

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
                tmpCategoryGroup = categoryGroupFactory.getCategoryGroup(type, name, description);
            }

            errors = categoryGroupService.validate(tmpCategoryGroup);
            if (errors.isEmpty()) {
                categoryGroupService.saveOrUpdateCategoryGroup(tmpCategoryGroup);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshCategoryGroupTab();
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
                categoryService.saveOrUpdateCategory(tmpCategory);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshCategoryTab();
        }
        return errors;
    }

    public void deletePendingCategoryGroup() throws Exception {
        try {
            if (pendingCategoryGroup != null && pendingCategoryGroup.getId() != null) {
                categoryGroupService.removeCategoryGroup(pendingCategoryGroup.getId());
                pendingCategoryGroup = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshCategoryGroupTab();
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
            refreshCategoryTab();
        }
    }

    public CategoryGroup getPendingCategoryGroup() throws Exception {
        return pendingCategoryGroup;
    }

    public void setPendingCategoryGroupProperty(CategoryGroup categoryGroup) throws Exception {
        pendingCategoryGroup = categoryGroup;
    }

    public Category getPendingCategory() throws Exception {
        return pendingCategory;
    }

    public void setPendingCategoryProperty(Category category) throws Exception {
        pendingCategory = category;
    }
}