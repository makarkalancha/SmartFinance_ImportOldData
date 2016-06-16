package com.makco.smartfinance.utils.collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.Collection;

/**
 * Created by mcalancea on 2016-06-16.
 */
public class CollectionUtils {
    public static <T> ObservableSet<T> convertCollectionToObservableSet(Collection<T> col){
        ObservableSet<T> set = FXCollections.observableSet();
        if(col != null) {
            set.addAll(col);
        }
        return set;
    }
}
