package com.eilifint.ravimal.helloauction.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ravimal on 11/24/2016.
 */

public class HelloAuctionContract {
    /**
     * Content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.eilifint.ravimal.helloauction";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths (appended to base content URI for possible URI's)
     */
    public static final String PATH_USER = "user";
    public static final String PATH_ITEM = "item";
    public static final String PATH_BID = "bid";

    /**
     * Inner class that defines constant values for the user database table.
     * Each entry in the table represents a user.
     */

    public static final class UserEntry implements BaseColumns {

        /**
         * The content URI to access the user data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);
        /**
         * Name of database table for products
         */
        public final static String TABLE_NAME = "user";

        /**
         * Unique ID number for the user (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the user.
         * Type: TEXT
         */
        public final static String COLUMN_USER_NAME = "name";

        /**
         * Email address of the user.
         * Type: TEXT
         */
        public final static String COLUMN_USER_EMAIL = "email";

        /**
         * Password of the user.
         * Type: TEXT
         */
        public final static String COLUMN_USER_PASSWORD = "password";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of users.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a user.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

    }

    /**
     * Inner class that defines constant values for the item database table.
     * Each entry in the table represents a single item.
     */

    public static final class ItemEntry implements BaseColumns {

        /**
         * The content URI to access the item data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEM);
        /**
         * Name of database table for item
         */
        public final static String TABLE_NAME = "item";

        /**
         * Unique ID number for the item (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_NAME = "name";

        /**
         * Starting price of the item.
         * Type: REAL
         */
        public final static String COLUMN_ITEM_USER_ID = "user_id";

        /**
         * Starting price of the item.
         * Type: REAL
         */
        public final static String COLUMN_ITEM_START_PRICE = "start_price";

        /**
         * Description of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_DESCRIPTION = "description";
        /**
         * Image of the item.
         * Type: BLOB
         */
        public final static String COLUMN_ITEM_IMAGE = "image";

        /**
         * Start time of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_START_TIME = "start_time";
        /**
         * End time of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_END_TIME = "end_time";

        /**
         * Is auction ended for the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_IS_END = "is_end";

        /**
         * Winner id of the  item.
         * Type: INTEGER
         */
        public final static String COLUMN_ITEM_WINNER_ID = "winner_id";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of item.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

    }

    /**
     * Inner class that defines constant values for the bid database table.
     * Each entry in the table represents a bid.
     */

    public static final class BidEntry implements BaseColumns {

        /**
         * The content URI to access the bid data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BID);
        /**
         * Name of database table for bid
         */
        public final static String TABLE_NAME = "bid";

        /**
         * Unique ID number for the bid (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * User id of the bid.
         * Type: INTEGER
         */
        public final static String COLUMN_BID_USER_ID = "user_id";

        /**
         * Item id of the bid.
         * Type: INTEGER
         */
        public final static String COLUMN_BID_ITEM_ID = "item_id";

        /**
         * Value of the bid.
         * Type: REAL
         */
        public final static String COLUMN_BID_VALUE = "value";

        /**
         * Value of the bid.
         * Type: TEXT
         */
        public final static String COLUMN_BID_TIME = "time";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of bid.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BID;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a bid.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BID;

    }
}