package com.makco.smartfinance.utils.collection;

import javafx.collections.ObservableSet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 2016-06-16.
 */
public class CollectionUtilsTest {

    @Test
    public void testConvertCollectionToObservableSet() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");
        list.add("third");
        list.add("fourth");
        list.add("second");
        ObservableSet<String> set = CollectionUtils.convertCollectionToObservableSet(list);

        assertTrue(set.size() == 4);
    }

    @Test
    public void testConvertCollectionToObservableSet_passNullCollection() throws Exception {
        ObservableSet<String> set = CollectionUtils.convertCollectionToObservableSet(null);

        assertTrue(set.size() == 0);
    }

    @Test
    public void testConvertCollectionToObservableSet_passEmptyCollection() throws Exception {
        List<String> list = new ArrayList<>();
        ObservableSet<String> set = CollectionUtils.convertCollectionToObservableSet(list);

        assertTrue(set.size() == 0);
    }
}