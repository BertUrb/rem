package com.openclassrooms.realestatemanager;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {
    @Test
    public void internet_should_be_available() {
        assertTrue(Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().getTargetContext()));
    }

    @Test
    public void convertEuroToDollars() {
        assertEquals(1000,Utils.convertEuroToDollars(812));
    }

    @Test
    public void convertDollarToEuro() {
        assertEquals(812,Utils.convertDollarToEuro(1000));
    }

    @Test
    public void getTodayDate() {
        assertEquals("27/06/2023",Utils.getTodayDate());
    }

}
