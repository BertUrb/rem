package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsUnitTest {


    @Test
    public void conversion_dollars_to_euro_isCorrect() throws Exception {
        assertEquals(81, Utils.convertDollarToEuro(100));
    }
    @Test
    public void conversion_euro_to_dollars_isCorrect() throws Exception {
        assertEquals(100, Utils.convertEuroToDollars(81));
    }

    @Test
    public void date_format_isCorrect() {
        assertEquals("05/01/2023", Utils.getTodayDate());
    }

}