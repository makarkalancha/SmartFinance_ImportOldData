package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryCredit;
import com.makco.smartfinance.persistence.entity.CategoryDebit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 30/04/2016
 * Time: 23:52
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//category_group & category DAO
public class CategoriesManagementDAOImplTest {
    private final static Logger LOG = LogManager.getLogger(CategoriesManagementDAOImplTest.class);
    private static final String debitCategoryGroupDebitName = "categoryGroupDebit_name";
    private static final String debitCategoryGroupDebitDesc = "categoryGroupDebit_description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();

    @Test
    public void test_11_saveOrUpdateCategoryGroupDebit() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = debitCategoryGroupDebitName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, debitCategoryGroupDebitDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(name, categoryGroup.getName());
        assertEquals(debitCategoryGroupDebitDesc, categoryGroup.getDescription());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
    }

    @Test
    public void test_12_saveCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = debitCategoryGroupDebitName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, debitCategoryGroupDebitDesc);

        List<Category> categories = new ArrayList<>();

        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category category = new CategoryDebit(categoryGroup, "cat deb " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroup.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(name, categoryGroup.getName());
        assertEquals(debitCategoryGroupDebitDesc, categoryGroup.getDescription());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        for (Category category : categories){
            LOG.debug("category: " + category);
            assertEquals(true, category.getId() != null);
            assertEquals(true, category.getCreatedOn() != null);
            assertEquals(true, category.getUpdatedOn() != null);
        }
    }

    //java.lang.ClassCastException: com.makco.smartfinance.persistence.entity.CategoryGroupDebit cannot be cast to com.makco.smartfinance.persistence.entity.CategoryGroupCredit
    @Test(expected = ClassCastException.class)
//    @Test
    public void test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = debitCategoryGroupDebitName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, debitCategoryGroupDebitDesc);

        List<Category> categories = new ArrayList<>();

        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category category = new CategoryCredit(categoryGroup, "cat cred " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroup.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);
//        java.lang.ClassCastException: com.makco.smartfinance.persistence.entity.CategoryGroupDebit cannot be cast to com.makco.smartfinance.persistence.entity.CategoryGroupCredit
//        at com.makco.smartfinance.persistence.entity.CategoryCredit.<init>(CategoryCredit.java:25)
//        at com.makco.smartfinance.persistence.dao.CategoryGroupDAOImplTest.test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType(CategoryGroupDAOImplTest.java:97)
    }

    @Test
    public void test_21_updateCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();
//        http://stackoverflow.com/questions/15957441/jpql-with-subquery-to-select-max-count
//        List<CategoryGroup> categoryGroupList = session
//                .createQuery("SELECT cg FROM CategoryGroup cg WHERE cg.comments.size = (SELECT c.categoryGroupCredit.id COUNT(u2.comments.size) FROM Category c)")
//                .list();

        List<Object[]> catGrCrWithQtyOfCatCr = session
//                .createQuery("SELECT TYPE(c) as type, COUNT(c) as count FROM Category c where TYPE(c) <> 'E' group by TYPE(c)")
                .createQuery("SELECT c.categoryGroup, COUNT(c) FROM CategoryCredit c group by c.categoryGroup")
                .list();

        LOG.debug(">>>categoryGroupList: "+catGrCrWithQtyOfCatCr);

        CategoryGroupCredit minCategoryGroupCredit = (CategoryGroupCredit)catGrCrWithQtyOfCatCr
                .stream()
                .min(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(min -> min[0])
                .orElse(null);
        LOG.debug(">>>minCategoryGroupCredit: "+minCategoryGroupCredit);
        session.getTransaction().commit();
        session.close();

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(minCategoryGroupCredit.getId(), false);

        String newName = categoryGroup.getName()+"_changed_v"+randomInt;

        newName = (newName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ? newName.substring(newName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) : newName;
        categoryGroup.setName(newName);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(newName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != categoryGroup.getCreatedOn());
    }

    @Test
    //with CategoryGroup EAGER loading of Categories it works
    //with CategoryGroup LAZY loading of Categories it works: if CategoryGroup and Category classes have @DiscriminatorOptions(force = true)
    ////http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
    ////http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
    public void test_22_updateCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();

        List<Object[]> catGrCrWithQtyOfCatCr = session
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroupList: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Category category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category category = iteratorCheck.next();
            LOG.debug(">>>category: " + category);
            assertEquals(true, categoryNames.contains(category.getName()));
            categoryNames.remove(category.getName());
            assertEquals(true, category.getCreatedOn() != null);
            assertEquals(true, category.getUpdatedOn() != null);
            assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        }
    }

    @Test
    public void test_23_updateCategoryGroupWithCategoriesWithMerge() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();

        List<Object[]> catGrCrWithQtyOfCatCr = session
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroupList: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Category category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateWithMergeCategoryGroup(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category category = iteratorCheck.next();
            LOG.debug(">>>category: " + category);
            assertEquals(true, categoryNames.contains(category.getName()));
            categoryNames.remove(category.getName());
            assertEquals(true, category.getCreatedOn() != null);
            assertEquals(true, category.getUpdatedOn() != null);
            assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        }
    }

    @Test
    public void test_24_updateCategoryGroupWithOneCategoryUpdate() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();

        List<Object[]> catGrCrWithQtyOfCatCr = session
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroupList: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        if (iterator.hasNext()) {
            Category category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category category = iteratorCheck.next();
            LOG.debug(">>>category: " + category);
            if(categoryNames.contains(category.getName())) {
                assertEquals(true, categoryNames.contains(category.getName()));
                categoryNames.remove(category.getName());
                assertEquals(true, category.getCreatedOn() != null);
                assertEquals(true, category.getUpdatedOn() != null);
                assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
                break;
            }
        }
    }

    @Test
    public void test_25_updateCategoryGroupWithOneCategoryWithMerge() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();

        List<Object[]> catGrCrWithQtyOfCatCr = session
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroupList: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        if (iterator.hasNext()) {
            Category category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateWithMergeCategoryGroup(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category category = iteratorCheck.next();
            LOG.debug(">>>category: " + category);
            if(categoryNames.contains(category.getName())) {
                assertEquals(true, categoryNames.contains(category.getName()));
                categoryNames.remove(category.getName());
                assertEquals(true, category.getCreatedOn() != null);
                assertEquals(true, category.getUpdatedOn() != null);
                assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
                break;
            }
        }
    }

    @Test
    public void test_31_removeCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        CategoryGroupCredit categoryGroupCredit = new CategoryGroupCredit("catGr to del v" + randomInt,
                "category group to delete " + randomInt);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);
        LOG.debug(">>>category group to delete: " + categoryGroupCredit.getId());

        categoryGroupDAOImplForTest.removeCategoryGroup(categoryGroupCredit.getId());

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(categoryGroupCredit.getId(), false);
        assertEquals(true, categoryGroup == null);
    }

    @Test
    public void test_32_removeCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("catGr to del " + randomInt,
                "category group to delete " + randomInt);

        List<Category> categories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category category = new CategoryCredit(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroupCredit.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);
        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + categories);

        categoryGroupDAOImplForTest.removeCategoryGroup(categoryGroupCredit.getId());

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(categoryGroupCredit.getId(), false);
        assertEquals(true, categoryGroup == null);
        for(Category category : categories){
            Category categoryDeleted = categoryGroupDAOImplForTest.getCategoryById(category.getId());
            assertEquals(true, categoryDeleted == null);
        }
    }

    @Test
    public void test_33_removeCategoryFromCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 5;
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("catGr to del " + randomInt,
                "category group to delete " + randomInt);

        List<Category> categories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryCredit(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroupCredit.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);
        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + categories);

        Category categoryToDelete = categories.get(0);
        categoryGroupDAOImplForTest.removeCategory(categoryToDelete.getId());
        ////////////////////////////1//////////////////////////////////
        CategoryGroup categoryGroup1 = categoryGroupDAOImplForTest.getCategoryGroupById(categoryGroupCredit.getId(), false);
        assertEquals(true, categoryGroup1 != null);
        for(Category category : categories){
            Category categoryDeleted = categoryGroupDAOImplForTest.getCategoryById(category.getId());
            if(category.getId().equals(categoryToDelete.getId())) {
                assertEquals(true, categoryDeleted == null);
            } else {
                assertEquals(true, categoryDeleted != null);
            }
        }
        ////////////////////////////2//////////////////////////////////
        CategoryGroup categoryGroup2 = categoryGroupDAOImplForTest.getCategoryGroupById(categoryGroupCredit.getId(), true);
        assertEquals(true, categoryGroup2 != null);
        assertEquals(true, !categoryGroup2.getCategories().contains(categoryToDelete));
        assertEquals(true, categoryGroup2.getCategories().size() == (categoriesQty - 1));
    }

    @Test
    public void test_41_seleteAllCategoryGroups() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryCredit(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);

        LOG.debug(">>>category group credit to delete: " + categoryGroupCredit);
        LOG.debug(">>>credit categories to delete: " + creditCategories);

        CategoryGroup categoryGroupDebit = new CategoryGroupDebit("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryDebit(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        LOG.debug(">>>category group debit to delete: " + categoryGroupDebit);
        LOG.debug(">>>debit categories to delete: " + debitCategories);

        List<CategoryGroup> categoryGroups = categoryGroupDAOImplForTest.categoryGroupList(false);
        LOG.debug(">>>categoryGroupList: " + categoryGroups);
        assertEquals(true, categoryGroups.size() > 0);
    }

    @Test
    public void test_42_seleteCategoryGroupById() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryCredit(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        LOG.debug(">>>category group id: " + categoryGroupCredit);
        LOG.debug(">>>categories of category group: " + creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);

        long categoryGroupId = categoryGroupCredit.getId();
        LOG.debug(">>>category group id: " + categoryGroupId);

        CategoryGroup categoryGroupById = categoryGroupDAOImplForTest.getCategoryGroupById(categoryGroupId, true);

        LOG.debug(">>>category group select by id: " + categoryGroupById);

        assertEquals(true, categoryGroupId == categoryGroupById.getId());

        LOG.debug(">>>categoryGroupList: " + categoryGroupById.getCategories());
        assertEquals(true, categoryGroupById.getCategories().size() > 0);
    }

    @Test
    public void test_43_seleteCategoryGroupByName() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        String categoryGroupDebitName = "categoryGroup debit name " + randomInt;
        CategoryGroup categoryGroupDebit = new CategoryGroupDebit(categoryGroupDebitName,
                "debit category group to select " + randomInt);
        List<Category> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryDebit(categoryGroupDebit, "cat db " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        LOG.debug(">>>category group id: " + categoryGroupDebit);
        LOG.debug(">>>categories of category group: " + debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        List<CategoryGroup> categoryGroupByNameList = categoryGroupDAOImplForTest.getCategoryGroupByName(categoryGroupDebitName, true);

        for(CategoryGroup categoryGroupByName : categoryGroupByNameList){
            LOG.debug(">>>category group select by name: " + categoryGroupByName);

            assertEquals(true, categoryGroupDebitName.equals(categoryGroupByName.getName()));

            LOG.debug(">>>categoryGroupList: " + categoryGroupByName.getCategories());
            assertEquals(true, categoryGroupByName.getCategories().size() > 0);
        }
    }

    @Test
    public void test_44_seleteAllCategoryGroupsWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup categoryGroupDebit = new CategoryGroupDebit("dt catGr to sel All " + randomInt,
                "debit category group to select all " + randomInt);
        List<Category> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryDebit(categoryGroupDebit, "cat dt All" + i + "_v" + randomInt,
                    "debit category #" + i + " 'description' select all categoryGroups with categories");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        List<CategoryGroup> categoryGroups = categoryGroupDAOImplForTest.categoryGroupList(true);
        LOG.debug(">>>categoryGroups: " + categoryGroups);
        assertEquals(true, categoryGroups.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup categoryGroup : categoryGroups){
            if(categoryGroup.getCategories().size() > 0){
                ++qtyCategoryGroupsWithCategories;
            } else {
                ++qtyCategoryGroupsWithoutCategories;
            }
        }
        LOG.debug(">>>qtyCategoryGroupsWithCategories: " + qtyCategoryGroupsWithCategories);
        LOG.debug(">>>qtyCategoryGroupsWithoutCategories: " + qtyCategoryGroupsWithoutCategories);
        assertEquals(true, qtyCategoryGroupsWithCategories > 0);
    }

    @Test
    public void test_45_seleteDebitCategoryGroupsWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup categoryGroupDebit = new CategoryGroupDebit("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryDebit(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        LOG.debug(">>>category group to delete: " + categoryGroupDebit);
        LOG.debug(">>>categories to delete: " + debitCategories);

//        List<CategoryGroupDebit> categoryGroupDebits = categoryGroupDAOImplForTest.categoryGroupDebitList_old(true);
        List<CategoryGroupDebit> categoryGroupDebits = categoryGroupDAOImplForTest.categoryGroupByType(CategoryGroupDebit.class, true);
        LOG.debug(">>>categoryGroupDebits: " + categoryGroupDebits);
        assertEquals(true, categoryGroupDebits.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup categoryGroup : categoryGroupDebits){
            if(categoryGroup.getCategories().size() > 0){
                ++qtyCategoryGroupsWithCategories;
            } else {
                ++qtyCategoryGroupsWithoutCategories;
            }
        }
        LOG.debug(">>>qtyCategoryGroupsWithCategories: " + qtyCategoryGroupsWithCategories);
        LOG.debug(">>>qtyCategoryGroupsWithoutCategories: " + qtyCategoryGroupsWithoutCategories);
        assertEquals(true, qtyCategoryGroupsWithCategories > 0);
    }

    @Test
    public void test_46_seleteCreditCategoryGroupsWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryCredit(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);

        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + creditCategories);

//        List<CategoryGroupCredit> categoryGroupCredits = categoryGroupDAOImplForTest.categoryGroupCreditList_old(true);
        List<CategoryGroupCredit> categoryGroupCredits = categoryGroupDAOImplForTest.categoryGroupByType(CategoryGroupCredit.class
                ,true);
        LOG.debug(">>>categoryGroupList: " + categoryGroupCredits);
        assertEquals(true, categoryGroupCredits.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup categoryGroup : categoryGroupCredits){
            if(categoryGroup.getCategories().size() > 0){
                ++qtyCategoryGroupsWithCategories;
            } else {
                ++qtyCategoryGroupsWithoutCategories;
            }
        }
        LOG.debug(">>>qtyCategoryGroupsWithCategories: " + qtyCategoryGroupsWithCategories);
        LOG.debug(">>>qtyCategoryGroupsWithoutCategories: " + qtyCategoryGroupsWithoutCategories);
        assertEquals(true, qtyCategoryGroupsWithCategories > 0);
    }

    @Test
    public void test_47_seleteCategoryByNameAndItsCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        String categoryName = "cat cr 47_v" + randomInt;
        Category category = new CategoryCredit(categoryGroupCredit, categoryName,
                "credit category test #47 'description'");
        creditCategories.add(category);
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);

        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + creditCategories);

        List<Category> categoryByNameList = categoryGroupDAOImplForTest.getCategoryByName(categoryName);
        LOG.debug(">>>categoryByNameList: " + categoryByNameList);
        assertEquals(true, categoryByNameList.size() > 0);
        for(Category cat : categoryByNameList) {
            assertEquals(true, categoryName.equals(cat.getName()));
        }
    }

    @Test
    public void test_48_seleteDifferentCategoryGroupBySameName() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        String categoryGroupSameName = "same categoryGroup name " + randomInt;
        CategoryGroup categoryGroupDebit = new CategoryGroupDebit(categoryGroupSameName,
                "debit category group to select " + randomInt);
        List<Category> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryDebit(categoryGroupDebit, "cat db same name " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        LOG.debug(">>>category group id: " + categoryGroupDebit);
        LOG.debug(">>>categories of category group: " + debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        CategoryGroup categoryGroupCredit = new CategoryGroupCredit(categoryGroupSameName,
                "credit category group to select " + randomInt);
        List<Category> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryCredit(categoryGroupCredit, "cat cr same name " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        LOG.debug(">>>category group by same name: " + categoryGroupCredit);
        LOG.debug(">>>categories of category group: " + creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);

        //byName return list as it might be debit or credit and return categories
        List<CategoryGroup> categoryGroupWithSameName = categoryGroupDAOImplForTest.getCategoryGroupByName(categoryGroupSameName, true);

        for(CategoryGroup categoryGroup : categoryGroupWithSameName){
            LOG.debug(">>>category group select by id: " + categoryGroup);

            assertEquals(true, categoryGroup.getName().equals(categoryGroupSameName));

            LOG.debug(">>>categoryGroupList: " + categoryGroup.getCategories());
            assertEquals(true, categoryGroup.getCategories().size() > 0);
        }
    }

    @Test
    public void test_49_seleteCategoryGroupByNameWithType() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        String categoryGroupName = "categoryGroup nameAndType " + randomInt;
        CategoryGroup categoryGroupDebit = new CategoryGroupDebit(categoryGroupName,
                "debit category group to select " + randomInt);
        List<Category> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryDebit(categoryGroupDebit, "cat db same name " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        LOG.debug(">>>category group name and type: " + categoryGroupDebit);
        LOG.debug(">>>categories of category group: " + debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        //byName return list as it might be debit or credit and return categories
        CategoryGroup categoryGroupByNameAndTypeResult = categoryGroupDAOImplForTest
                .getCategoryGroupByNameAndType(categoryGroupName, DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT, true);

        LOG.debug(">>>category group select by id: " + categoryGroupByNameAndTypeResult);

        assertEquals(true, categoryGroupByNameAndTypeResult.getName().equals(categoryGroupName));
        assertEquals(true, categoryGroupByNameAndTypeResult.getCategoryGroupType().equals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator()));

        LOG.debug(">>>categoryGroupList: " + categoryGroupByNameAndTypeResult.getCategories());
        assertEquals(true, categoryGroupByNameAndTypeResult.getCategories().size() > 0);
    }
}
