package com.easyvan.goodstracker;

import com.easyvan.goodstracker.utils.MapUtils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by sm5 on 6/18/2019.
 */

public class GoodsTrackerTest {

    @Test
    public void validateDistanceBetweenCoordinates() {

        double source_lattitude = 12.9716, source_longtitude = 77.5946,
                dest_lattitude = 13.0827, dest_longtitude = 80.2707;

        double expectedDistance = 290.16;
        double actualDistance = MapUtils.distanceBetween(source_lattitude,source_longtitude,dest_lattitude,dest_longtitude, "K");

        assertEquals(expectedDistance, actualDistance, 0);
    }
}
