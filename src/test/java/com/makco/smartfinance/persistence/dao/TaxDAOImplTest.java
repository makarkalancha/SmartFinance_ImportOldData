package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryDebit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroup_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.Category_v1;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 06 Jul 2016.
 */
public class TaxDAOImplTest {

    private static final Logger LOG = LogManager.getLogger(TaxDAOImplTest.class);
    private static final String taxName = "tax #";
    private static final String taxDesc = "tax_description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private TaxDAO taxDAO = new TaxDAOImpl();

    @Test
    public void test_11_saveOrUpdateTax() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = taxName + randomInt;
        String desc = taxDesc + randomInt;
        Tax tax = new Tax(name, desc, null, null, null, null, null, null);
        taxDAO.saveOrUpdateTax(tax);

        LOG.debug(">>>Tax: " + tax);
        assertEquals(true, tax.getId() != null);
        assertEquals(name, tax.getName());
        assertEquals(desc, tax.getDescription());
        assertEquals(true, tax.getCreatedOn() != null);
        assertEquals(true, tax.getUpdatedOn() != null);
    }

    @Test
    public void test_12_saveOrUpdateTax_withChild() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String nameChild1 = new StringBuilder(taxName).append("Child").append(randomInt).toString();
        String descChild1 = new StringBuilder(taxDesc).append("Child").append(randomInt).toString();;
        Tax taxChild1 = new Tax(nameChild1, descChild1, null, null, null, null, null, null);

        randomInt = randomWithinRange.getRandom();
        String nameChild2 = new StringBuilder(taxName).append("Child").append(randomInt).toString();
        String descChild2 = new StringBuilder(taxDesc).append("Child").append(randomInt).toString();;
        Tax taxChild2 = new Tax(nameChild2, descChild2, null, null, null, null, null, null);

        randomInt = randomWithinRange.getRandom();
        String name = taxName + randomInt;
        String desc = taxDesc + randomInt;
        Tax tax = new Tax(name, desc, null, null, null, null, null, new HashSet<Tax>(){{
            add(taxChild1);
            add(taxChild2);
        }});
        taxDAO.saveOrUpdateTax(tax);

        LOG.debug(">>>Tax: " + tax);
        assertEquals(true, tax.getId() != null);
        assertEquals(name, tax.getName());
        assertEquals(desc, tax.getDescription());
        assertEquals(true, tax.getCreatedOn() != null);
        assertEquals(true, tax.getUpdatedOn() != null);

        assertEquals(2, tax.getChildTaxes().size());
        assertTrue(tax.getChildTaxes().contains(taxChild1));
        assertTrue(tax.getChildTaxes().contains(taxChild2));
    }
}