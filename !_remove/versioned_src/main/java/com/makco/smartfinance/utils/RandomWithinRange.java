package com.makco.smartfinance.utils;

import java.util.Random;

/**
 * Created by Makar Kalancha on 2016-04-28.
 */
public final class RandomWithinRange {
    private final Random random;
    private final int min;
    private final int max;

    public RandomWithinRange(int min, int max) {
        this.min = min;
        this.max = max;
        this.random = new Random();
    }

    public int getRandom(){
        return random.nextInt((max - min) + 1) + min;
    }
}
