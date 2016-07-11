package com.makco.smartfinance.user_interface.validation;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-08.
 */
public interface Rule <T> {
    EnumSet<ErrorEnum> validate(T t) throws Exception;
}
