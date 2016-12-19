package com.eilifint.ravimal.helloauction.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.eilifint.ravimal.helloauction.R;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.UserEntry;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.ItemEntry;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.BidEntry;

/**
 * Created by Ravimal on 11/24/2016.
 */

public class HelloAuctionProvider extends ContentProvider {

    /**
     * URI matcher code for the content URI for the tables
     */
    public static final int USERS = 100;
    public static final int ITEMS = 200;
    public static final int BIDS = 300;

    /**
     * URI matcher code for the content URI for a single instance of the table
     */
    public static final int USER_ID = 101;
    public static final int ITEM_ID = 201;
    public static final int BID_ID = 301;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = HelloAuctionProvider.class.getSimpleName();

    /*database helper object*/
    HelloAuctionDbHelper mDbHelper;

    // Static initializer. This is run the first time anything is called from this class.
    public static UriMatcher buildUriMatcher() {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(HelloAuctionContract.CONTENT_AUTHORITY, HelloAuctionContract.PATH_USER, HelloAuctionProvider.USERS);
        sUriMatcher.addURI(HelloAuctionContract.CONTENT_AUTHORITY, HelloAuctionContract.PATH_USER + "/#", HelloAuctionProvider.USER_ID);
        sUriMatcher.addURI(HelloAuctionContract.CONTENT_AUTHORITY, HelloAuctionContract.PATH_ITEM, HelloAuctionProvider.ITEMS);
        sUriMatcher.addURI(HelloAuctionContract.CONTENT_AUTHORITY, HelloAuctionContract.PATH_ITEM + "/#", HelloAuctionProvider.ITEM_ID);
        sUriMatcher.addURI(HelloAuctionContract.CONTENT_AUTHORITY, HelloAuctionContract.PATH_BID, HelloAuctionProvider.BIDS);
        sUriMatcher.addURI(HelloAuctionContract.CONTENT_AUTHORITY, HelloAuctionContract.PATH_BID + "/#", HelloAuctionProvider.BID_ID);

        return sUriMatcher;
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        sUriMatcher = buildUriMatcher();
        mDbHelper = new HelloAuctionDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                // For the USERS code, query the user table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the user table.
                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case USER_ID:
                // For the USER_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.eilifint.ravimal.helloauction/user/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = UserEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the user table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case ITEMS:
                // For the ITEMS code, query the item table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the item table.
                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                // For the ITEM_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.eilifint.ravimal.helloauction/item/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the item table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case BIDS:
                // For the BIDS code, query the bid table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the bid table.
                cursor = database.query(BidEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BID_ID:
                // For the BID_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.eilifint.ravimal.helloauction/bid/3"",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = BidEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the bid table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(BidEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            //default value
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.cannot_query) + uri);
        }

        //set notification uri on the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * match uri to identify whether it is a list type or a item type
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return UserEntry.CONTENT_LIST_TYPE;
            case USER_ID:
                return UserEntry.CONTENT_ITEM_TYPE;
            case ITEMS:
                return ItemEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return ItemEntry.CONTENT_ITEM_TYPE;
            case BIDS:
                return BidEntry.CONTENT_LIST_TYPE;
            case BID_ID:
                return BidEntry.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalStateException(getContext().getString(R.string.unknow_uri) +
                        uri + getContext().getString(R.string.match) + match);
        }
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return insertUsers(uri, contentValues);
            case ITEMS:
                return insertItems(uri, contentValues);
            case BIDS:
                return insertBids(uri, contentValues);

            default:
                throw new IllegalArgumentException(getContext().getString(R.string.not_support) + uri);
        }
    }

    /**
     * Insert a user into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertUsers(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(UserEntry.COLUMN_USER_NAME);
        if (name == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_name));
        }

        /// Check that the email is not null
        String email = values.getAsString(UserEntry.COLUMN_USER_EMAIL);
        if (email == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_email));
        }

        // Check that the password is not null
        String password = values.getAsString(UserEntry.COLUMN_USER_PASSWORD);
        if (password == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_password));
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new user with the given values
        long id = database.insert(UserEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, getContext().getString(R.string.fail_to_insert_row) + uri);
            return null;
        }

        //notify al listeners that data has changed for user contentURI
        //uri://com.eilifint.ravimal.helloauction/user
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a items into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertItems(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
        if (name == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_name));
        }

        // Check that the price is not null
        Double price = values.getAsDouble(ItemEntry.COLUMN_ITEM_START_PRICE);
        if (price == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_price));
        }

        // Check that the description is not null
        String description = values.getAsString(ItemEntry.COLUMN_ITEM_DESCRIPTION);
        if (description == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_description));
        }

        // Check that the image is not null
        byte[] image = values.getAsByteArray(ItemEntry.COLUMN_ITEM_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_image));
        }

        // Check that the isEnd is not null
        Integer isEnd = values.getAsInteger(ItemEntry.COLUMN_ITEM_IS_END);
        if (isEnd == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_is_end));
        }

        // Check that the startTime is not null
        String startTime = values.getAsString(ItemEntry.COLUMN_ITEM_START_TIME);
        if (startTime == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_start_time));
        }

        // Check that the endTime is not null
        String endTime = values.getAsString(ItemEntry.COLUMN_ITEM_END_TIME);
        if (endTime == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_end_time));
        }

        // Check that the winnerID is not null
        Integer winnerID = values.getAsInteger(ItemEntry.COLUMN_ITEM_WINNER_ID);
        if (winnerID == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_winner));
        }

        // Check that the userId is not null
        Integer userId = values.getAsInteger(ItemEntry.COLUMN_ITEM_USER_ID);
        if (userId == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_user));
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new item with the given values
        long id = database.insert(ItemEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, getContext().getString(R.string.fail_to_insert_row) + uri);
            return null;
        }

        //notify al listeners that data has changed for item contentURI
        //uri://com.eilifint.ravimal.helloauction/item
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a bid into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertBids(Uri uri, ContentValues values) {

        // Check that the price is not null
        Double price = values.getAsDouble(BidEntry.COLUMN_BID_VALUE);
        if (price == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_name));
        }

        // Check that the itemID is not null
        Integer itemID = values.getAsInteger(BidEntry.COLUMN_BID_ITEM_ID);
        if (itemID == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_item));
        }

        // Check that the userId is not null
        Integer userId = values.getAsInteger(BidEntry.COLUMN_BID_USER_ID);
        if (userId == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_user));
        }

        // Check that the time is not null
        String time = values.getAsString(BidEntry.COLUMN_BID_TIME);
        if (time == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_time));
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new bid with the given values
        long id = database.insert(BidEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, getContext().getString(R.string.fail_to_insert_row) + uri);
            return null;
        }

        //notify al listeners that data has changed for bid contentURI
        //uri://com.eilifint.ravimal.helloauction/bid
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * database deletion at the given selection and selection arguments
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:

                // Delete all rows that match the selection and selection args
                // For  case ITEMS:
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsDeleted;
            case ITEM_ID:

                // Delete a single row given by the ID in the URI
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // For case ITEM_ID:
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case BIDS:

                // Delete all rows that match the selection and selection args
                // For  case BIDS:
                rowsDeleted = database.delete(BidEntry.TABLE_NAME, selection, selectionArgs);

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsDeleted;
            case BID_ID:

                // Delete a single row given by the ID in the URI
                selection = BidEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // For case BID_ID:
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(BidEntry.TABLE_NAME, selection, selectionArgs);

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            default:
                throw new IllegalArgumentException(getContext().getString(R.string.deletion_not_support) + uri);
        }
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        //matching uri
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                // For the ITEMT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.update_not_support) + uri);
        }
    }

    /**
     * Update item in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more item).
     * Return the number of rows that were successfully updated.
     */
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Check that the name is not null
        String name = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
        if (name == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_name));
        }

        // Check that the description is not null
        String description = values.getAsString(ItemEntry.COLUMN_ITEM_DESCRIPTION);
        if (description == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.require_description));
        }

        // If there are no values to update, then no need to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //return no of rows updated
        return rowsUpdated;
    }
}