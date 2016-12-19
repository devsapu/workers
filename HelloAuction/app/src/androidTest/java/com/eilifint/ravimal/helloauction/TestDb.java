package com.eilifint.ravimal.helloauction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract;
import com.eilifint.ravimal.helloauction.data.HelloAuctionDbHelper;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Ravimal on 11/25/2016.
 */

public class TestDb {
    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        getTargetContext().deleteDatabase(HelloAuctionDbHelper.DATABASE_NAME);
    }

    /*
       This function gets called before each test is executed to delete the database
    */
    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(HelloAuctionContract.ItemEntry.TABLE_NAME);
        tableNameHashSet.add(HelloAuctionContract.UserEntry.TABLE_NAME);
        tableNameHashSet.add(HelloAuctionContract.BidEntry.TABLE_NAME);

        getTargetContext().deleteDatabase(HelloAuctionDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new HelloAuctionDbHelper(
                getTargetContext()).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that  database doesn't contain  the user,item entry
        // and bid entry tables
        assertTrue("Error:  database was created without user,item,bid entry tables",
                tableNameHashSet.isEmpty());

        // now, check tables contain the correct columns? for the item table
        c = db.rawQuery("PRAGMA table_info(" + HelloAuctionContract.ItemEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> itemColumnHashSet = new HashSet<String>();
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry._ID);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_NAME);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_DESCRIPTION);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_IMAGE);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_START_PRICE);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_END_TIME);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_START_TIME);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_IS_END);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_WINNER_ID);
        itemColumnHashSet.add(HelloAuctionContract.ItemEntry.COLUMN_ITEM_USER_ID);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            itemColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that database doesn't contain all of the required item
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required user entry columns",
                itemColumnHashSet.isEmpty());

        // now, do tables contain the correct columns? for the user table
        c = db.rawQuery("PRAGMA table_info(" + HelloAuctionContract.ItemEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> userColumnHashSet = new HashSet<String>();
        itemColumnHashSet.add(HelloAuctionContract.UserEntry._ID);
        itemColumnHashSet.add(HelloAuctionContract.UserEntry.COLUMN_USER_NAME);
        itemColumnHashSet.add(HelloAuctionContract.UserEntry.COLUMN_USER_PASSWORD);
        itemColumnHashSet.add(HelloAuctionContract.UserEntry.COLUMN_USER_EMAIL);

        int userColumnNameIndex = c.getColumnIndex("name");

        do {
            String columnName = c.getString(userColumnNameIndex);
            userColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that  database doesn't contain all of the required item
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required item entry columns",
                userColumnHashSet.isEmpty());

        // now, do  tables contain the correct columns? for the user table
        c = db.rawQuery("PRAGMA table_info(" + HelloAuctionContract.BidEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> bidColumnHashSet = new HashSet<String>();
        itemColumnHashSet.add(HelloAuctionContract.BidEntry._ID);
        itemColumnHashSet.add(HelloAuctionContract.BidEntry.COLUMN_BID_ITEM_ID);
        itemColumnHashSet.add(HelloAuctionContract.BidEntry.COLUMN_BID_TIME);
        itemColumnHashSet.add(HelloAuctionContract.BidEntry.COLUMN_BID_USER_ID);
        itemColumnHashSet.add(HelloAuctionContract.BidEntry.COLUMN_BID_VALUE);

        int bidColumnNameIndex = c.getColumnIndex("name");

        do {
            String columnName = c.getString(bidColumnNameIndex);
            bidColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that  database doesn't contain all of the required item
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required item entry columns",
                userColumnHashSet.isEmpty());

        db.close();
    }

    /*
          code to test that we can insert and query the
           hello_auction database.
       */
    @Test
    public void testUserTable() {
        insertUser();
    }

    /*
         helper method for the testUserTable .
     */
    public long insertUser() {
        // Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when  try to get a writable database.
        HelloAuctionDbHelper dbHelper = new HelloAuctionDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues to insert
        ContentValues testValues = TestUtilities.createUserValues();

        //  Insert ContentValues into database and get a row ID back
        long userRowId;
        userRowId = db.insert(HelloAuctionContract.UserEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(userRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Query the database and receive a Cursor back
        // A cursor is primary interface to the query results.
        Cursor cursor = db.query(
                HelloAuctionContract.UserEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue("Error: No Records returned from user query", cursor.moveToFirst());

        //  Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: user Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from user query",
                cursor.moveToNext());

        //  Close Cursor and Database
        cursor.close();
        db.close();
        return userRowId;
    }

    /*
       build code to test that we can insert and query the
       database
    */
    @Test
    public void testItemTable() {
        insertItem();
    }

    /*
            helper method for the testUserTable .
        */
    public long insertItem() {

        long userRowId = insertUser();

        // Make sure we have a valid row ID.
        assertFalse("Error: user Not Inserted Correctly", userRowId == -1L);

        // et reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when try to get a writable database.
        HelloAuctionDbHelper dbHelper = new HelloAuctionDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create item values
        ContentValues itemValues = TestUtilities.createItemValues(userRowId);

        //  Insert ContentValues into database and get a row ID back
        long itemRowId = db.insert(HelloAuctionContract.ItemEntry.TABLE_NAME, null, itemValues);
        assertTrue(itemRowId != -1);

        //  Query the database and receive a Cursor back
        // A cursor is  primary interface to the query results.
        Cursor itemCursor = db.query(
                HelloAuctionContract.ItemEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from item query", itemCursor.moveToFirst());

        //  Validate the item Query
        TestUtilities.validateCurrentRecord("testInsertReadDb itemEntry failed to validate",
                itemCursor, itemValues);

        // Move the cursor to demonstrate more than one record is in the database
        assertFalse("Error: More than one record returned from item query",
                itemCursor.moveToNext());

        //  Close cursor and database
        itemCursor.close();
        dbHelper.close();

        return itemRowId;
    }

    /*
        build code to test that we can insert and query the
       database
    */
    @Test
    public void testBidTable() {

        long userRowId = 1;
        long itemRowId = 1;

        // Make sure we have a valid row ID.
        assertFalse("Error: user Not Inserted Correctly", userRowId == -1L);
        assertFalse("Error: Locatitemion Not Inserted Correctly", itemRowId == -1L);
        //  Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when  try to get a writable database.
        HelloAuctionDbHelper dbHelper = new HelloAuctionDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create bid values
        ContentValues bidValues = TestUtilities.createBidValues(userRowId, itemRowId);

        // Insert ContentValues into database and get a row ID back
        long bidRowId = db.insert(HelloAuctionContract.BidEntry.TABLE_NAME, null, bidValues);
        assertTrue(itemRowId != -1);

        //  Query the database and receive a Cursor back
        // A cursor is  primary interface to the query results.
        Cursor bidCursor = db.query(
                HelloAuctionContract.BidEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from bid query", bidCursor.moveToFirst());

        // Validate the bid Query
        TestUtilities.validateCurrentRecord("testInsertReadDb bidEntry failed to validate",
                bidCursor, bidValues);

        // Move the cursor to demonstrate more than one record is in the database
        assertFalse("Error: More than one record returned from bid query",
                bidCursor.moveToNext());

        //  Close cursor and database
        bidCursor.close();
        dbHelper.close();
    }
}