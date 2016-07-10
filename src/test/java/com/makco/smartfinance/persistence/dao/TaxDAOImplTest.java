package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.dao.dao_implementations.TaxDAOImpl_v1;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.utils.Logs;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

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

    private TaxDAOImpl_v1 taxDAO = new TaxDAOImpl_v1();

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
    public void test_12_saveOrUpdateTax_withChildParent() throws Exception {
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

        assert (taxChild1.getId() != null);
        assert (taxChild2.getId() != null);

    }

    @Test
    public void test_41_select_withChildParent() throws Exception {
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

//        List<Tax> taxList = taxDAO.taxListWithAssociations(); // throwLazyInitializationException
//        List<Tax> taxList = taxDAO.taxListWithChildrenAndParents_leftJoinFetch(); // cartesian product
        List<Tax> taxList = taxDAO.taxListWithChildrenAndParents_HibernateInitialize();
        int taxCount = 0;
        int tax1Count = 0;
        int tax2Count = 0;
        for(Tax taxFromList : taxList){
            if(taxFromList.getId().equals(tax.getId())){
                taxCount++;
                assertEquals(true, tax.getId() != null);
                assertEquals(name, tax.getName());
                assertEquals(desc, tax.getDescription());
                assertEquals(true, tax.getCreatedOn() != null);
                assertEquals(true, tax.getUpdatedOn() != null);

                assertEquals(2, tax.getChildTaxes().size());
                assertTrue(tax.getChildTaxes().contains(taxChild1));
                assertTrue(tax.getChildTaxes().contains(taxChild2));
            }

            if(taxFromList.getId().equals(taxChild1.getId())){
                tax1Count++;
                assertEquals(0, taxFromList.getChildTaxes().size());
                assertEquals(1, taxFromList.getParentTaxes().size());
                assertTrue(taxFromList.getParentTaxes().contains(tax));
                assert (taxFromList.getId() != null);
            }

            if(taxFromList.getId().equals(taxChild2.getId())){
                tax2Count++;
                assertEquals(0, taxFromList.getChildTaxes().size());
                assertEquals(1, taxFromList.getParentTaxes().size());
                assertTrue(taxFromList.getParentTaxes().contains(tax));
                assert (taxFromList.getId() != null);
            }
            System.out.println(">>>taxCount: " + taxCount);
            System.out.println(">>>tax1Count: " + tax1Count);
            System.out.println(">>>tax2Count: " + tax2Count);
        }
    }

    @Test
    public void test_42_select_benchMark() throws Exception {
//        int cycle = 100_000;
        int cycle = 1_000;
        //in root project folder
        try(PrintWriter printWriter = new PrintWriter("test_42_select_benchMark.log", "UTF-8");) {
            for (int i = 0; i < cycle; i++) {
                long start1 = System.nanoTime();
                List<Tax> taxList1 = taxDAO.taxListWithAssociations(); // throwLazyInitializationException
                long end1 = System.nanoTime();

                long start2 = System.nanoTime();
                List<Tax> taxList2 = taxDAO.taxListWithChildrenAndParents_leftJoinFetch(); // cartesian product
                long end2 = System.nanoTime();

                long start3 = System.nanoTime();
                List<Tax> taxList3 = taxDAO.taxListWithChildrenAndParents_HibernateInitialize(); // select + 1
                long end3 = System.nanoTime();

                printWriter.append(">>>>" + i + "\ttaxListWithAssociations (throwLazyInitializationException)\tsize\t" + taxList1.size() + "\ttime\t" + Logs.benchmarkCalcultaion(start1, end1) + "\tnanoTime\t" + (end1 - start1));
                printWriter.append("\r\n");
                printWriter.append(">>>>" + i + "\ttaxListWithChildrenAndParents_leftJoinFetch (cartesian product)\tsize\t" + taxList2.size() + "\ttime\t" + Logs.benchmarkCalcultaion(start2, end2) + "\tnanoTime\t" + (end2 - start2));
                printWriter.append("\r\n");
                printWriter.append(">>>>" + i + "\ttaxListWithChildrenAndParents_HibernateInitialize (select + 1)\tsize\t" + taxList3.size() + "\ttime\t" + Logs.benchmarkCalcultaion(start3, end3) + "\tnanoTime\t" + (end3 - start3));
                printWriter.append("\r\n");

                Thread.sleep(100);
            }
        }
    }

    //todo update rate and parent should update its denormalized formula
}