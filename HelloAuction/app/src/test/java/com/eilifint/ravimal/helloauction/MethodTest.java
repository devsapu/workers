package com.eilifint.ravimal.helloauction;

import com.eilifint.ravimal.helloauction.Utils.GetDate;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ravimal on 11/26/2016.
 */

public class MethodTest {

    // tests correct date returned
    @Test
    public void shouldReturnDate() throws Exception {
        long testTime = 1480158526526L;
        GetDate date = new GetDate();
        Date dateObject = new Date(testTime);

        String dateString = "Nov 26, 2016";

        assertEquals(dateString, date.formatDate(dateObject));
    }

    // tests correct time returned
    @Test
    public void shouldReturnTime() throws Exception {
        long testTime = 1480158526526L;
        GetDate date = new GetDate();
        Date dateObject = new Date(testTime);

        String dateString = "4:38 PM";

        assertEquals(dateString, date.formatTime(dateObject));
    }

    // tests random value is between 40000 and 20000
    @Test
    public void shouldReturnValue() throws Exception {
        GetDate date = new GetDate();
        int valePass = 0;

        if (date.getRandomVlue() > 20000 && date.getRandomVlue() < 40000)
            valePass = 1;

        assertEquals(1, valePass);
    }

}
