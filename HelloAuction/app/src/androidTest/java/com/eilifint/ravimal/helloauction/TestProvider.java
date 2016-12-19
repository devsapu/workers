package com.eilifint.ravimal.helloauction;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract;
import com.eilifint.ravimal.helloauction.data.HelloAuctionDbHelper;
import com.eilifint.ravimal.helloauction.data.HelloAuctionProvider;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Ravimal on 11/26/2016.
 */

public class TestProvider {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.
       Replace the calls to deleteAllRecordsFromDB with this one after have written
       the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {

        getTargetContext().getContentResolver().delete(
                HelloAuctionContract.ItemEntry.CONTENT_URI,
                null,
                null
        );
        getTargetContext().getContentResolver().delete(
                HelloAuctionContract.BidEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = getTargetContext().getContentResolver().query(
                HelloAuctionContract.ItemEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from item table during delete", 0, cursor.getCount());

        cursor.close();

        cursor = getTargetContext().getContentResolver().query(
                HelloAuctionContract.BidEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from bid table during delete", 0, cursor.getCount());

        cursor.close();
    }

    /*
        Refactor this function to use the deleteAllRecordsFromProvider functionality once
       have implemented delete functionality there.
    */
    @Test
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Before
    public void setUp() throws Exception {
        deleteAllRecords();
    }

    /*
     This test checks to make sure that the content provider is registered correctly.

  */
    @Test
    public void testProviderRegistry() {
        PackageManager pm = getTargetContext().getPackageManager();

        // We define the component name based on the package name from the context and the
        // HelloAuctionProvider class.
        ComponentName componentName = new ComponentName(getTargetContext().getPackageName(),
                HelloAuctionProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: HelloAuctionProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + HelloAuctionContract.CONTENT_AUTHORITY,
                    providerInfo.authority, HelloAuctionContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: HelloAuctionProvider not registered at " + getTargetContext().getPackageName(),
                    false);
        }
    }

    @Test
    public void testGetType() {
        // content://com.eilifint.android.helloauction.app/user/
        String type = getTargetContext().getContentResolver().getType(HelloAuctionContract.UserEntry.CONTENT_URI);

        assertEquals("Error: the UserEntry CONTENT_URI should return UserEntry.CONTENT_LIST_TYPE",
                HelloAuctionContract.UserEntry.CONTENT_LIST_TYPE, type);

        int userId = 1;
        // content://com.eilifint.android.helloauction.app/user/1

        type = getTargetContext().getContentResolver().getType(ContentUris.withAppendedId(HelloAuctionContract.
                UserEntry.CONTENT_URI, userId));

        assertEquals("Error: the UserEntry CONTENT_URI should return UserEntry.CONTENT_ITEM_TYPE",
                HelloAuctionContract.UserEntry.CONTENT_ITEM_TYPE, type);

// content://com.eilifint.android.helloauction.app/item/
        type = getTargetContext().getContentResolver().getType(HelloAuctionContract.ItemEntry.CONTENT_URI);

        assertEquals("Error: the ItemEntry CONTENT_URI should return ItemEntry.CONTENT_LIST_TYPE",
                HelloAuctionContract.ItemEntry.CONTENT_LIST_TYPE, type);

        int itemId = 5;
        // content://com.eilifint.android.helloauction.app/item/1

        type = getTargetContext().getContentResolver().getType(ContentUris.withAppendedId(HelloAuctionContract.
                ItemEntry.CONTENT_URI, itemId));

        assertEquals("Error: the ItemEntry CONTENT_URI should return ItemEntry.CONTENT_ITEM_TYPE",
                HelloAuctionContract.ItemEntry.CONTENT_ITEM_TYPE, type);

        // content://com.eilifint.android.helloauction.app/bid/
        type = getTargetContext().getContentResolver().getType(HelloAuctionContract.BidEntry.CONTENT_URI);

        assertEquals("Error: the BidEntry CONTENT_URI should return BidEntry.CONTENT_LIST_TYPE",
                HelloAuctionContract.BidEntry.CONTENT_LIST_TYPE, type);

        int bidId = 10;
        // content://com.eilifint.android.helloauction.app/bid/1

        type = getTargetContext().getContentResolver().getType(ContentUris.withAppendedId(HelloAuctionContract.
                BidEntry.CONTENT_URI, itemId));

        assertEquals("Error: the BidEntry CONTENT_URI should return BidEntry.CONTENT_ITEM_TYPE",
                HelloAuctionContract.BidEntry.CONTENT_ITEM_TYPE, type);
    }

    /*
     This test uses the database directly to insert and then uses the ContentProvider to
     read out the data.
  */
    @Test
    public void testBasicUserQuery() {
        // insert  test records into the database
        HelloAuctionDbHelper dbHelper = new HelloAuctionDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createUserValues();
        long userRowId = TestUtilities.insertUserValues(getTargetContext());

        db.close();

        // Test the basic content provider query
        Cursor userCursor = getTargetContext().getContentResolver().query(
                HelloAuctionContract.UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicUserQueries, user query", userCursor, testValues);
    }

    @Test
    public void testBasicItemQuery() {
        // insert test records into the database
        HelloAuctionDbHelper dbHelper = new HelloAuctionDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowId = 1;
        ContentValues testValues = TestUtilities.createItemValues(rowId);

        long itemRowId = db.insert(HelloAuctionContract.ItemEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert ItemEntry into the Database", itemRowId != -1);
        db.close();

        // Test the basic content provider query
        Cursor itemCursor = getTargetContext().getContentResolver().query(
                HelloAuctionContract.ItemEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicItemQueries, Item query", itemCursor, testValues);
    }

    @Test
    public void testBasicBidQuery() {
        // insert test records into the database
        HelloAuctionDbHelper dbHelper = new HelloAuctionDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowId = 1;
        int itemId = 1;
        ContentValues testValues = TestUtilities.createBidValues(rowId, itemId);

        long bidRowId = db.insert(HelloAuctionContract.BidEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert BidEntry into the Database", bidRowId != -1);
        db.close();

        // Test the basic content provider query
        Cursor bidCursor = getTargetContext().getContentResolver().query(
                HelloAuctionContract.BidEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicBidQueries, bid query", bidCursor, testValues);
    }

    /*
        This test uses the provider to insert and then update the data.
     */
    public void testUpdateItem() {
        int userId = 1;
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createItemValues(userId);

        Uri itemUri = getTargetContext().getContentResolver().
                insert(HelloAuctionContract.ItemEntry.CONTENT_URI, values);
        long itemRowId = ContentUris.parseId(itemUri);

        // Verify we got a row back.
        assertTrue(itemRowId != -1);
        Log.d(LOG_TAG, "New row id: " + itemRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(HelloAuctionContract.ItemEntry._ID, itemRowId);
        updatedValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_NAME, "item");
        updatedValues.put(HelloAuctionContract.ItemEntry.COLUMN_ITEM_DESCRIPTION, "Its a book");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor itemCursor = getTargetContext().getContentResolver().
                query(HelloAuctionContract.ItemEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        itemCursor.registerContentObserver(tco);

        int count = getTargetContext().getContentResolver().update(
                HelloAuctionContract.ItemEntry.CONTENT_URI, updatedValues, HelloAuctionContract.ItemEntry._ID + "= ?",
                new String[]{Long.toString(itemRowId)});
        assertEquals(count, 1);

        // Test to make sure observer is called.  If not, we throw an assertion.
        //
        // If code is failing here, it means that content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        itemCursor.unregisterContentObserver(tco);
        itemCursor.close();

        // A cursor is primary interface to the query results.
        Cursor cursor = getTargetContext().getContentResolver().query(
                HelloAuctionContract.ItemEntry.CONTENT_URI,
                null,   // projection
                HelloAuctionContract.ItemEntry._ID + " = " + itemRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateItem.  Error validating item entry update.",
                cursor, updatedValues);

        cursor.close();
    }


    // Make sure we can still delete after adding/updating stuff

    @Test
    public void testInsertReadProvider() {
        int rowId = 1;

        ContentValues testValues = TestUtilities.createItemValues(rowId);

        // Register a content observer for  insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        getTargetContext().getContentResolver().registerContentObserver(HelloAuctionContract.ItemEntry.CONTENT_URI, true, tco);
        Uri itemUri = getTargetContext().getContentResolver().insert(HelloAuctionContract.ItemEntry.CONTENT_URI, testValues);

        // Did content observer get called?  :  If this fails, insert item
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        getTargetContext().getContentResolver().unregisterContentObserver(tco);

        long itemRowId = ContentUris.parseId(itemUri);

        // Verify we got a row back.
        assertTrue(itemRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is primary interface to the query results.
        Cursor cursor = getTargetContext().getContentResolver().query(
                HelloAuctionContract.ItemEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating itemEntry.",
                cursor, testValues);
    }

    // Make sure we can still delete after adding/updating stuff

    @Test
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for item delete.
        TestUtilities.TestContentObserver itemObserver = TestUtilities.getTestContentObserver();
        getTargetContext().getContentResolver().registerContentObserver(HelloAuctionContract.ItemEntry.CONTENT_URI, true, itemObserver);

        deleteAllRecordsFromProvider();

        // If either of these fail, most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        itemObserver.waitForNotificationOrFail();

        getTargetContext().getContentResolver().unregisterContentObserver(itemObserver);

    }

}