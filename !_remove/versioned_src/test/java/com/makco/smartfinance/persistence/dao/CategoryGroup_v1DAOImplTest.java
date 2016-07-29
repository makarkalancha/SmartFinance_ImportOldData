package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroup_v1DAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.Category_v1DAOImplForTest;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryCredit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryDebit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroupCredit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroupDebit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroup_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.Category_v1;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 30/04/2016
 * Time: 23:52
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryGroup_v1DAOImplTest {
    private static final Logger LOG = LogManager.getLogger(CategoryGroup_v1DAOImplTest.class);
    private static final String categoryGroupName = "categoryGroup_name";
    private static final String categoryGroupDesc = "categoryGroup_description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private CategoryGroup_v1DAOImplForTest categoryGroupDAOImplForTest = new CategoryGroup_v1DAOImplForTest();

    @Test
    public void test_11_saveOrUpdateCategoryGroupDebit_v1() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = categoryGroupName + randomInt;
        CategoryGroup_v1 categoryGroup = new CategoryGroupDebit_v1(name, categoryGroupDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(name, categoryGroup.getName());
        assertEquals(categoryGroupDesc, categoryGroup.getDescription());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
    }

    @Test
    public void test_12_saveCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = categoryGroupName + randomInt;
        CategoryGroup_v1 categoryGroup = new CategoryGroupDebit_v1(name, categoryGroupDesc);

        List<Category_v1> categories = new ArrayList<>();

        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroup, "cat deb " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroup.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(name, categoryGroup.getName());
        assertEquals(categoryGroupDesc, categoryGroup.getDescription());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        for (Category_v1 category : categories){
            LOG.debug("category: " + category);
            assertEquals(true, category.getId() != null);
            assertEquals(true, category.getCreatedOn() != null);
            assertEquals(true, category.getUpdatedOn() != null);
        }
    }

    //java.lang.ClassCastException: com.makco.smartfinance.persistence.entity.CategoryGroupDebit_v1 cannot be cast to com.makco.smartfinance.persistence.entity.CategoryGroupCredit
    @Test(expected = ClassCastException.class)
//    @Test
    public void test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = categoryGroupName + randomInt;
        CategoryGroup_v1 categoryGroup = new CategoryGroupDebit_v1(name, categoryGroupDesc);

        List<Category_v1> categories = new ArrayList<>();

        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroup, "cat cred " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroup.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup);
//        java.lang.ClassCastException: com.makco.smartfinance.persistence.entity.CategoryGroupDebit_v1 cannot be cast to com.makco.smartfinance.persistence.entity.CategoryGroupCredit
//        at com.makco.smartfinance.persistence.entity.CategoryCredit.<init>(CategoryCredit.java:25)
//        at com.makco.smartfinance.persistence.dao.CategoryGroupDAOImplTest.test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType(CategoryGroupDAOImplTest.java:97)
    }

    @Test
    public void test_21_updateCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();
//        http://stackoverflow.com/questions/15957441/jpql-with-subquery-to-select-max-count
//        List<CategoryGroup_v1> categoryGroup_v1List = session
//                .createQuery("SELECT cg FROM CategoryGroup_v1 cg WHERE cg.comments.size = (SELECT c.categoryGroupCredit.id COUNT(u2.comments.size) FROM Category_v1 c)")
//                .list();

        List<Object[]> catGrCrWithQtyOfCatCr = session
//                .createQuery("SELECT TYPE(c) as type, COUNT(c) as count FROM Category_v1 c where TYPE(c) <> 'E' group by TYPE(c)")
                .createQuery("SELECT c.categoryGroup, COUNT(c) FROM CategoryCredit_v1 c group by c.categoryGroup")
                .list();

        LOG.debug(">>>categoryGroup_v1List: "+catGrCrWithQtyOfCatCr);

        CategoryGroupCredit_v1 minCategoryGroupCredit_v1 = (CategoryGroupCredit_v1)catGrCrWithQtyOfCatCr
                .stream()
                .min(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(min -> min[0])
                .orElse(null);
        LOG.debug(">>>minCategoryGroupCredit: "+minCategoryGroupCredit_v1);
        session.getTransaction().commit();
        session.close();

        CategoryGroup_v1 categoryGroup = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(minCategoryGroupCredit_v1.getId(), false);

        String newName = categoryGroup.getName()+"_changed_v"+randomInt;

        newName = (newName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ? newName.substring(newName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) : newName;
        categoryGroup.setName(newName);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(newName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, !categoryGroup.getUpdatedOn().equals(categoryGroup.getCreatedOn()));
    }

    @Test
    //with CategoryGroup_v1 EAGER loading of Categories it works
    //with CategoryGroup_v1 LAZY loading of Categories it works: if CategoryGroup_v1 and Category_v1 classes have @DiscriminatorOptions(force = true)
    ////http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
    ////http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html
    public void test_22_updateCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();

        List<Object[]> catGrCrWithQtyOfCatCr = session
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit_v1 c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroup_v1List: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup_v1 categoryGroup = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category_v1> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Category_v1 category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category_v1> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category_v1 category = iteratorCheck.next();
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
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit_v1 c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroup_v1List: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup_v1 categoryGroup = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category_v1> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Category_v1 category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateWithMergeCategoryGroup_v1(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category_v1> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category_v1 category = iteratorCheck.next();
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
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit_v1 c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroup_v1List: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup_v1 categoryGroup = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category_v1> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        if (iterator.hasNext()) {
            Category_v1 category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category_v1> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category_v1 category = iteratorCheck.next();
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
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryDebit_v1 c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroup_v1List: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup_v1 categoryGroup = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(maxCategoryGroupCreditId, true);

        String catgoryGroupNewName = categoryGroup.getName()+"_changed_v"+randomInt;

        catgoryGroupNewName = (catgoryGroupNewName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ?
                catgoryGroupNewName.substring(catgoryGroupNewName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) :
                catgoryGroupNewName;
        categoryGroup.setName(catgoryGroupNewName);

        //put service: putting category_group in category and category in category_group
        Set<String> categoryNames = new HashSet<>();
        Iterator<Category_v1> iterator = categoryGroup.getCategories().iterator();
        int i = 0;
        if (iterator.hasNext()) {
            Category_v1 category = iterator.next();
            String categoryNewName = category.getName() + "_changed_v" + (++i) + "_v" + randomInt;
            categoryNewName = (categoryNewName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ?
                    categoryNewName.substring(categoryNewName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) :
                    categoryNewName;
            category.setName(categoryNewName);
            categoryNames.add(categoryNewName);
        }

        categoryGroupDAOImplForTest.saveOrUpdateWithMergeCategoryGroup_v1(categoryGroup);

        LOG.debug(">>>categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroup.getCategoryGroupType());
        assertEquals(catgoryGroupNewName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getCreatedOn() != categoryGroup.getUpdatedOn());
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        Iterator<Category_v1> iteratorCheck = categoryGroup.getCategories().iterator();
        while (iteratorCheck.hasNext()) {
            Category_v1 category = iteratorCheck.next();
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
    public void test_31_removeCategoryGroup_v1() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        CategoryGroupCredit_v1 categoryGroupCredit = new CategoryGroupCredit_v1("catGr to del v" + randomInt,
                "category group to delete " + randomInt);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);
        LOG.debug(">>>category group to delete: " + categoryGroupCredit.getId());

        categoryGroupDAOImplForTest.removeCategoryGroup_v1(categoryGroupCredit.getId());

        CategoryGroup_v1 categoryGroup = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(categoryGroupCredit.getId(), false);
        assertEquals(true, categoryGroup == null);
    }

    @Test
    public void test_32_removeCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("catGr to del " + randomInt,
                "category group to delete " + randomInt);

        List<Category_v1> categories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroupCredit.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);
        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + categories);

        categoryGroupDAOImplForTest.removeCategoryGroup_v1(categoryGroupCredit.getId());

        CategoryGroup_v1 categoryGroup = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(categoryGroupCredit.getId(), false);
        assertEquals(true, categoryGroup == null);
        Category_v1DAOImplForTest categoryDAOImplForTest = new Category_v1DAOImplForTest();
        for(Category_v1 category : categories){
            Category_v1 categoryDeleted = categoryDAOImplForTest.getCategoryById(category.getId());
            assertEquals(true, categoryDeleted == null);
        }
    }

    @Test
    public void test_41_1_seleteAllCategoryGroups() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category_v1> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);

        LOG.debug(">>>category group credit to delete: " + categoryGroupCredit);
        LOG.debug(">>>credit categories to delete: " + creditCategories);

        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        LOG.debug(">>>category group debit to delete: " + categoryGroupDebit);
        LOG.debug(">>>debit categories to delete: " + debitCategories);

        List<CategoryGroup_v1> categoryGroups = categoryGroupDAOImplForTest.categoryGroup_v1List(false);
        LOG.debug(">>>categoryGroup_v1List: " + categoryGroups);
        assertEquals(true, categoryGroups.size() > 0);
//        //lazy initialization exception
//        for(CategoryGroup_v1 categoryGroup : categoryGroups){
//            assertEquals(true, categoryGroup.getCategories().size() == 0);
//        }
    }

    @Test
    public void test_42_seleteCategoryGroupById() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category_v1> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        LOG.debug(">>>category group id: " + categoryGroupCredit);
        LOG.debug(">>>categories of category group: " + creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);

        long categoryGroupId = categoryGroupCredit.getId();
        LOG.debug(">>>category group id: " + categoryGroupId);

        CategoryGroup_v1 categoryGroupById = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(categoryGroupId, true);

        LOG.debug(">>>category group select by id: " + categoryGroupById);

        assertEquals(true, categoryGroupId == categoryGroupById.getId());

        LOG.debug(">>>categoryGroup_v1List: " + categoryGroupById.getCategories());
        assertEquals(true, categoryGroupById.getCategories().size() > 0);
    }

    @Test
    /**
     * 1+n select issue
     * see CategoryGroupSelectAll_cGWithCats_.log
     */
    public void test_44_1_seleteAllCategoryGroupsWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1("dt catGr to sel All " + randomInt,
                "debit category group to select all " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat dt All" + i + "_v" + randomInt,
                    "debit category #" + i + " 'description' select all categoryGroups with categories");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        List<CategoryGroup_v1> categoryGroups = categoryGroupDAOImplForTest.categoryGroup_v1List(true);
        LOG.debug(">>>categoryGroups: " + categoryGroups);
        assertEquals(true, categoryGroups.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup_v1 categoryGroup : categoryGroups){
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
    public void test_41_2_seleteAllCategoryGroups_noCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category_v1> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);

        LOG.debug(">>>category group credit to delete: " + categoryGroupCredit);
        LOG.debug(">>>credit categories to delete: " + creditCategories);

        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        LOG.debug(">>>category group debit to delete: " + categoryGroupDebit);
        LOG.debug(">>>debit categories to delete: " + debitCategories);

        List<CategoryGroup_v1> categoryGroups = categoryGroupDAOImplForTest.categoryGroup_v1List_withoutBoolean();
        LOG.debug(">>>categoryGroup_v1List: " + categoryGroups);
        assertEquals(true, categoryGroups.size() > 0);
        for(CategoryGroup_v1 categoryGroup : categoryGroups){
            assertEquals(true, categoryGroup.getCategories().size() == 0);
        }
    }

    @Test
    public void test_47_seleteAllCategoryGroups_noCategories_withCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category_v1> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);

        LOG.debug(">>>category group credit to delete: " + categoryGroupCredit);
        LOG.debug(">>>credit categories to delete: " + creditCategories);

        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        LOG.debug(">>>category group debit to delete: " + categoryGroupDebit);
        LOG.debug(">>>debit categories to delete: " + debitCategories);

        List<CategoryGroup_v1> categoryGroupsNoCategories = categoryGroupDAOImplForTest.categoryGroup_v1List_withoutBoolean();
        LOG.debug(">>>categoryGroup_v1List: " + categoryGroupsNoCategories);
        assertEquals(true, categoryGroupsNoCategories.size() > 0);
        for(CategoryGroup_v1 categoryGroup : categoryGroupsNoCategories){
            assertEquals(true, categoryGroup.getCategories().size() == 0);
        }

        List<CategoryGroup_v1> categoryGroupsWithCategories = categoryGroupDAOImplForTest.categoryGroup_v1ListWithNativeQuery();
        LOG.debug(">>>categoryGroup_v1List: " + categoryGroupsWithCategories);
        assertEquals(true, categoryGroupsWithCategories.size() > 0);
        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        int qtyCategoryGroupsDebitWithoutCategories = 0;
        int qtyCategoryGroupsCreditWithoutCategories = 0;
        int qtyCategoryGroupsOtherWithoutCategories = 0;
        for(CategoryGroup_v1 categoryGroup : categoryGroupsWithCategories){
            if(categoryGroup.getCategories().size() > 0){
                ++qtyCategoryGroupsWithCategories;
            } else {
                ++qtyCategoryGroupsWithoutCategories;
            }

            if(categoryGroup instanceof CategoryGroupDebit_v1){
                ++qtyCategoryGroupsDebitWithoutCategories;
            } else if(categoryGroup instanceof CategoryGroupCredit_v1){
                ++qtyCategoryGroupsCreditWithoutCategories;
            } else {
                ++qtyCategoryGroupsOtherWithoutCategories;
            }
        }
        LOG.debug(">>>qtyCategoryGroupsWithCategories: " + qtyCategoryGroupsWithCategories);
        LOG.debug(">>>qtyCategoryGroupsWithoutCategories: " + qtyCategoryGroupsWithoutCategories);
        LOG.debug(">>>qtyCategoryGroupsDebitWithoutCategories: " + qtyCategoryGroupsDebitWithoutCategories);
        LOG.debug(">>>qtyCategoryGroupsCreditWithoutCategories: " + qtyCategoryGroupsCreditWithoutCategories);
        LOG.debug(">>>qtyCategoryGroupsOtherWithoutCategories: " + qtyCategoryGroupsOtherWithoutCategories);
        assertEquals(true, qtyCategoryGroupsWithCategories > 0);
    }

    @Test
    public void test_45_1_seleteDebitCategoryGroupsWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        LOG.debug(">>>category group to delete: " + categoryGroupDebit);
        LOG.debug(">>>categories to delete: " + debitCategories);

//        List<CategoryGroupDebit_v1> categoryGroupDebits = categoryGroupDAOImplForTest.categoryGroupDebitList_old(true);
        List<CategoryGroupDebit_v1> categoryGroupDebits = categoryGroupDAOImplForTest.categoryGroup_v1ByType(CategoryGroupDebit_v1.class, true);
        LOG.debug(">>>categoryGroupDebits: " + categoryGroupDebits);
        assertEquals(true, categoryGroupDebits.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup_v1 categoryGroup : categoryGroupDebits){
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
    public void test_45_2_seleteDebitCategoryGroupsWithCategories_withLeftJoinFetch() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        LOG.debug(">>>category group to delete: " + categoryGroupDebit);
        LOG.debug(">>>categories to delete: " + debitCategories);

//        List<CategoryGroupDebit_v1> categoryGroupDebits = categoryGroupDAOImplForTest.categoryGroupDebitList_old(true);
        List<CategoryGroupDebit_v1> categoryGroupDebits = categoryGroupDAOImplForTest.categoryGroup_v1ByType_withLeftJoinFetch(CategoryGroupDebit_v1.class, true);
        LOG.debug(">>>categoryGroupDebits: " + categoryGroupDebits);
        assertEquals(true, categoryGroupDebits.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup_v1 categoryGroup : categoryGroupDebits){
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
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category_v1> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            creditCategories.add(category);
        }
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);

        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + creditCategories);

//        List<CategoryGroupCredit> categoryGroupCredits = categoryGroupDAOImplForTest.categoryGroupCreditList_old(true);
        List<CategoryGroupCredit_v1> categoryGroupCredits = categoryGroupDAOImplForTest.categoryGroup_v1ByType(CategoryGroupCredit_v1.class
                ,true);
        LOG.debug(">>>categoryGroup_v1List: " + categoryGroupCredits);
        assertEquals(true, categoryGroupCredits.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup_v1 categoryGroup : categoryGroupCredits){
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
    (expected = LazyInitializationException.class)
    /**
     * not working without eager loading
     */
    public void test_44_2_seleteAllCategoryGroupsWithCategories_withLeftJoinFetch() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        String categoryGroupName = "categoryGroup nameAndType " + randomInt;
        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1(categoryGroupName,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat db same name " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        LOG.debug(">>>category group name and type: " + categoryGroupDebit);
        LOG.debug(">>>categories of category group: " + debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        //byName return list as it might be debit or credit and return categories
        List<CategoryGroup_v1> categoryGroups = categoryGroupDAOImplForTest
                .categoryGroup_v1ListWithLeftJoinFetch(true);

        LOG.debug(">>>categoryGroups: " + categoryGroups);
        assertEquals(true, categoryGroups.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        for(CategoryGroup_v1 categoryGroup : categoryGroups){
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
    /**
     * 1 query
     * see CategoryGroupSelectAll_cGWithCats_nativeQuery.log
     */
    public void test_44_3_1_seleteAllCategoryGroupsWithCategories_withNativeQuery() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        String categoryGroupName = "categoryGroup nameAndType " + randomInt;
        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1(categoryGroupName,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat db same name " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        LOG.debug(">>>category group name and type: " + categoryGroupDebit);
        LOG.debug(">>>categories of category group: " + debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        //byName return list as it might be debit or credit and return categories
        List<CategoryGroup_v1> categoryGroups = categoryGroupDAOImplForTest
                .categoryGroup_v1ListWithNativeQuery();

        LOG.debug(">>>categoryGroups: " + categoryGroups);
        assertEquals(true, categoryGroups.size() > 0);

        int qtyCategoryGroupsWithCategories = 0;
        int qtyCategoryGroupsWithoutCategories = 0;
        int qtyCategoryGroupsDebitWithoutCategories = 0;
        int qtyCategoryGroupsCreditWithoutCategories = 0;
        int qtyCategoryGroupsOtherWithoutCategories = 0;
        for(CategoryGroup_v1 categoryGroup : categoryGroups){
            if(categoryGroup.getCategories().size() > 0){
                ++qtyCategoryGroupsWithCategories;
            } else {
                ++qtyCategoryGroupsWithoutCategories;
            }

            if(categoryGroup instanceof CategoryGroupDebit_v1){
                ++qtyCategoryGroupsDebitWithoutCategories;
            } else if(categoryGroup instanceof CategoryGroupCredit_v1){
                ++qtyCategoryGroupsCreditWithoutCategories;
            } else {
                ++qtyCategoryGroupsOtherWithoutCategories;
            }
        }
        LOG.debug(">>>qtyCategoryGroupsWithCategories: " + qtyCategoryGroupsWithCategories);
        LOG.debug(">>>qtyCategoryGroupsWithoutCategories: " + qtyCategoryGroupsWithoutCategories);
        LOG.debug(">>>qtyCategoryGroupsDebitWithoutCategories: " + qtyCategoryGroupsDebitWithoutCategories);
        LOG.debug(">>>qtyCategoryGroupsCreditWithoutCategories: " + qtyCategoryGroupsCreditWithoutCategories);
        LOG.debug(">>>qtyCategoryGroupsOtherWithoutCategories: " + qtyCategoryGroupsOtherWithoutCategories);
        assertEquals(true, qtyCategoryGroupsWithCategories > 0);
    }

    @Test
    public void test_44_3_2_seleteAllCategoryGroupsWithCategories_withNativeQuery_emptyCGAndCatTables() throws Exception {
        List<CategoryGroup_v1> categoryGroups = categoryGroupDAOImplForTest.categoryGroup_v1List(false);
        for(CategoryGroup_v1 categoryGroup : categoryGroups) {
            categoryGroupDAOImplForTest.removeCategoryGroup_v1(categoryGroup.getId());
        }

        //byName return list as it might be debit or credit and return categories
        List<CategoryGroup_v1> categoryGroupList = categoryGroupDAOImplForTest
                .categoryGroup_v1ListWithNativeQuery();

        LOG.debug(">>>categoryGroups: " + categoryGroupList);
        assertEquals(true, categoryGroupList.size() == 0);
    }

    @Test
    public void test_43_seleteCategoryGroupByName_forValidation() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        String categoryGroupName = "categoryGroup nameAndType " + randomInt;
        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1(categoryGroupName,
                "debit category group to select " + randomInt);

        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1(categoryGroupName,
                "credit category group to select " + randomInt);

        LOG.debug(">>>category group debit name and type: " + categoryGroupDebit);
        LOG.debug(">>>category group credit name and type: " + categoryGroupCredit);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);

        //byName return list as it might be debit or credit and return categories
        List<CategoryGroup_v1> categoryGroupByNameResult = categoryGroupDAOImplForTest
                .getCategoryGroup_v1ByName_forValidation(categoryGroupName);

        LOG.debug(">>>categoryGroupByNameResult: " + categoryGroupByNameResult);

        assertEquals(true, categoryGroupByNameResult.size() == 2);
        assertEquals(true, categoryGroupByNameResult.stream()
                .filter(cg -> cg.getName().equals(categoryGroupName) && cg.getCategoryGroupType().equals(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT))
                .collect(Collectors.toList()).size() == 1);
        assertEquals(true, categoryGroupByNameResult.stream()
                .filter(cg -> cg.getName().equals(categoryGroupName) && cg.getCategoryGroupType().equals(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT))
                .collect(Collectors.toList()).size() == 1);
    }
}
