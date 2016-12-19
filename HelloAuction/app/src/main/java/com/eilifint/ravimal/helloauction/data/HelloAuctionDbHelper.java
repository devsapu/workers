package com.eilifint.ravimal.helloauction.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.UserEntry;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.ItemEntry;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.BidEntry;

/**
 * Created by Ravimal on 11/24/2016.
 */

public class HelloAuctionDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    public static final String DATABASE_NAME = "hello_auction.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link HelloAuctionDbHelper}.
     *
     * @param context of the app
     */
    public HelloAuctionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the user table
        String SQL_CREATE_USER_TABLE = "CREATE TABLE "
                + UserEntry.TABLE_NAME + " ("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL,"
                + UserEntry.COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

        // Create a String that contains the SQL statement to create the item table
        String SQL_CREATE_ITEM_TABLE = "CREATE TABLE "
                + ItemEntry.TABLE_NAME + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_DESCRIPTION + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_USER_ID + " INTEGER NOT NULL, "
                + ItemEntry.COLUMN_ITEM_START_PRICE + " REAL NOT NULL DEFAULT 0, "
                + ItemEntry.COLUMN_ITEM_IMAGE + " BLOB NOT NULL,"
                + ItemEntry.COLUMN_ITEM_IS_END + " INTEGER NOT NULL,"
                + ItemEntry.COLUMN_ITEM_START_TIME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_END_TIME + " TEXT NOT NULL,"
                + ItemEntry.COLUMN_ITEM_WINNER_ID + " INTEGER NOT NULL,"
                // Set up the user_id column as a foreign key to user table.
                + " FOREIGN KEY (" + ItemEntry.COLUMN_ITEM_USER_ID + ") REFERENCES "
                + UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), "

                // Set up the winner_id column as a foreign key to user table.
                + " FOREIGN KEY (" + ItemEntry.COLUMN_ITEM_WINNER_ID + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + ") );";

        // Create a String that contains the SQL statement to create the bid table
        String SQL_CREATE_BID_TABLE = "CREATE TABLE "
                + BidEntry.TABLE_NAME + " ("
                + BidEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BidEntry.COLUMN_BID_ITEM_ID + " INTEGER NOT NULL,"
                + BidEntry.COLUMN_BID_USER_ID + " INTEGER NOT NULL,"
                + BidEntry.COLUMN_BID_VALUE + " REAL NOT NULL DEFAULT 0,"
                + BidEntry.COLUMN_BID_TIME + " TEXT NOT NULL,"
                // Set up the item_id column as a foreign key to item table.
                + " FOREIGN KEY (" + BidEntry.COLUMN_BID_ITEM_ID + ") REFERENCES "
                + ItemEntry.TABLE_NAME + " (" + ItemEntry._ID + "), "
                // Set up the user_id column as a foreign key to user table.
                + " FOREIGN KEY (" + BidEntry.COLUMN_BID_USER_ID + ") REFERENCES "
                + UserEntry.TABLE_NAME + " (" + UserEntry._ID + ") );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_ITEM_TABLE);
        db.execSQL(SQL_CREATE_BID_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}