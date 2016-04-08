package com.makco.smartfinance.user_interface.validation;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-08.
 */
public interface RuleSet<T> {
    List<Rule> getRuleSet();
    EnumSet<ErrorEnum> validate(T t);
}
