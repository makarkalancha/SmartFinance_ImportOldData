package com.makco.smartfinance.persistence.utils;

import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Makar Kalancha on 2016-04-28.
 */
public final class RandomWithinRangeTest {
    private final static Logger LOG = LogManager.getLogger(RandomWithinRangeTest.class);
    @Test
    public void test_100_withinRangeOf_100_1000() {
        final int limit = 100;
        final int min = 100;
        final int max = 1_000;
        Set<Integer> randoms = new HashSet<>();

        RandomWithinRange randomWithinRange = new RandomWithinRange(min, max);
        int repeats = 0;
        do {
            int random = randomWithinRange.getRandom();
            assert (random < max);
            assert (random >= min);

            if (!randoms.contains(random)) {
                randoms.add(random);
            } else {
                ++repeats;
                LOG.debug("random of " + random + " is the set");
            }

        } while (randoms.size() < 100);
        LOG.debug("Total repeats of random: " + repeats);
        assertEquals(limit, randoms.size());
    }
}
