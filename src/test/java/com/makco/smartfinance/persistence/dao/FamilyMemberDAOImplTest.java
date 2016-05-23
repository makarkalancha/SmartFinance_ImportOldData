package com.makco.smartfinance.persistence.dao;

import org.junit.Test;

/**
 * User: Makar Kalancha
 * Date: 20/04/2016
 * Time: 21:40
 */
public class FamilyMemberDAOImplTest {
    // see: saveOrUpdateWithMergeCategoryGroup.log
    // hibernate persistence book p281
    // session.saveOrUpdate no, better use detach-merge approach
    // compare if this approach uses less queries
    @Test
    public void test_saveOrUpdate_vs_detachMerge() throws Exception{

    }
}