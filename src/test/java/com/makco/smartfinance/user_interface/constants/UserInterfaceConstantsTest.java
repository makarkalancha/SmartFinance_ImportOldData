package com.makco.smartfinance.user_interface.constants;

import com.makco.smartfinance.constants.DataBaseConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 2016-05-25.
 */
public class UserInterfaceConstantsTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test_convertCategoryGroupTypeFromBackendToUI_withDEBIT(){
        String backendText = DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.toString();
        String actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(backendText);
        String expectedResult = UserInterfaceConstants.CATEGORY_GROUP_TYPE_DEBIT;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromBackendToUI_withDEBIT_value(){
        String backendText = DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT;
        String actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(backendText);
        String expectedResult = UserInterfaceConstants.CATEGORY_GROUP_TYPE_DEBIT;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromBackendToUI_withCREDIT(){
        String backendText = DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT.toString();
        String actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(backendText);
        String expectedResult = UserInterfaceConstants.CATEGORY_GROUP_TYPE_CREDIT;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromBackendToUI_withCREDIT_value(){
        String backendText = DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT;
        String actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(backendText);
        String expectedResult = UserInterfaceConstants.CATEGORY_GROUP_TYPE_CREDIT;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromBackendToUI_withNonExistentString(){
        String backendText = "asdf";
        String actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(backendText);
        String expectedResult = null;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromBackendToUI_withNullBackendText(){
        String backendText = null;
        String actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(backendText);
        String expectedResult = null;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromBackendToUI_withEmptyBackendText(){
        String backendText = "";
        String actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromBackendToUI(backendText);
        String expectedResult = null;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromUIToBackend_withNullUITextText(){
        String uiText = null;
        DataBaseConstants.CATEGORY_GROUP_TYPE actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromUIToBackend(uiText);
        DataBaseConstants.CATEGORY_GROUP_TYPE expectedResult = null;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromUIToBackend_withEmptyUITextText(){
        String uiText = "";
        DataBaseConstants.CATEGORY_GROUP_TYPE actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromUIToBackend(uiText);
        DataBaseConstants.CATEGORY_GROUP_TYPE expectedResult = null;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromUIToBackend_withNonExitentString(){
        String uiText = "asdf";
        DataBaseConstants.CATEGORY_GROUP_TYPE actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromUIToBackend(uiText);
        DataBaseConstants.CATEGORY_GROUP_TYPE expectedResult = null;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromUIToBackend_withCATEGORY_GROUP_TYPE_DEBIT(){
        String uiText = UserInterfaceConstants.CATEGORY_GROUP_TYPE_DEBIT;
        DataBaseConstants.CATEGORY_GROUP_TYPE actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromUIToBackend(uiText);
        DataBaseConstants.CATEGORY_GROUP_TYPE expectedResult = DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_convertCategoryGroupTypeFromUIToBackend_withCATEGORY_GROUP_TYPE_CREDIT(){
        String uiText = UserInterfaceConstants.CATEGORY_GROUP_TYPE_CREDIT;
        DataBaseConstants.CATEGORY_GROUP_TYPE actualResult = UserInterfaceConstants.convertCategoryGroupTypeFromUIToBackend(uiText);
        DataBaseConstants.CATEGORY_GROUP_TYPE expectedResult = DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT;

        assertEquals(expectedResult, actualResult);
    }

}