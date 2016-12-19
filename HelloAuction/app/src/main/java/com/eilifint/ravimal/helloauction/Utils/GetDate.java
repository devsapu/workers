package com.eilifint.ravimal.helloauction.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Ravimal on 11/24/2016.
 */

public class GetDate {
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    public String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    public String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return random value between 20000 and 40000 milliseconds.
     */
    public int getRandomVlue() {
        Random r = new Random();
        int Low = 20000;
        int High = 40000;
        int Result = r.nextInt(High - Low) + Low;
        return Result;
    }
}