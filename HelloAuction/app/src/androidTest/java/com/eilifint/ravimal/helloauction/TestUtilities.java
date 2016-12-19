package com.eilifint.ravimal.helloauction;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract;
import com.eilifint.ravimal.helloauction.data.HelloAuctionDbHelper;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Ravimal on 11/25/2016.
 */

public class TestUtilities {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            //since there is no valid image value for test.only tested with integer value
            if (columnName.equals(HelloAuctionContract.ItemEntry.COLUMN_ITEM_IMAGE))
                break;
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createBidValues(long userRowId, long itemRowId) {

        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(HelloAuctionContract.BidEntry.COLUMN_BID_USER_ID, userRowId);
        testValues.put(HelloAuctionContract.BidEntry.COLUMN_BID_ITEM_ID, itemRowId);
        testValues.put(HelloAuctionContract.BidEntry.COLUMN_BID_TIME, "3231321312");
        testValues.put(HelloAuctionContract.BidEntry.COLUMN_BID_VALUE, 100);

        return testValues;
    }

    //helper method to create item values
    static ContentValues createItemValues(long userRowId) {
        //Java String object
        String str = "Hello World";

        byte[] bytes = str.getBytes();
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_USER_ID, userRowId);
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_NAME, "book");
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_START_PRICE, 100);
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_IMAGE, bytes);
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_END_TIME, "1009212122");
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_DESCRIPTION, "Description");
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_IS_END, 1);
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_START_TIME, "1009212122");
        testValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_WINNER_ID, userRowId);


        return testValues;
    }

    static ContentValues createUserValues() {

        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(HelloAuctionContract.UserEntry.COLUMN_USER_EMAIL, "ami@gmail.com");
        testValues.put(HelloAuctionContract.UserEntry.COLUMN_USER_NAME, "Amilno ogera");
        testValues.put(HelloAuctionContract.UserEntry.COLUMN_USER_PASSWORD, "amil123wrd");

        return testValues;
    }

    static long insertUserValues(Context context) {
        // insert our test records into the database
        HelloAuctionDbHelper dbHelper = new HelloAuctionDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createUserValues();

        long userRowId;
        userRowId = db.insert(HelloAuctionContract.UserEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole user Values", userRowId != -1);

        return userRowId;
    }

    /*
       The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
