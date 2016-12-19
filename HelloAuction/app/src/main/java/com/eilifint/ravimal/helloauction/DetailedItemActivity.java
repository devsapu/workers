package com.eilifint.ravimal.helloauction;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eilifint.ravimal.helloauction.Utils.GetDate;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.ItemEntry;

import java.util.Date;

public class DetailedItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Content URI for the existing item
     */
    private Uri mCurrentItemUri;
    // Identifies a particular Loader being used in this component
    private static final int EXISTING_ITEM_LOADER = 3;

    /**
     * TextView  to show Current bid value
     */
    private TextView mCurrentBid;

    /**
     * TextView  to show the remaining time
     */
    private TextView mTimeLeft;

    /**
     * TextView  to show item name
     */
    private TextView mItemName;

    /**
     * TextView  to show the item description
     */
    private TextView mDescrition;

    /**
     * TextView  to show item id
     */
    private TextView mItemId;

    /**
     * TextView  to show the started time
     */
    private TextView mStartedTime;

    /**
     * TextView  to show the started time
     */
    private TextView mYourBid;
    /**
     * TextView  to show lst bid text
     */
    private TextView mLastBid;

    /**
     * EditText field to enter the bid
     */
    private EditText mBidValue;

    /**
     * button to submit bid
     */
    private Button mSetBidBtn;

    /**
     * ImageView  to load item image
     */
    private ImageView mImageView;

    /**
     * User id
     */
    private int mUserId;

    /**
     * Starting bid
     */
    private double mStartingBid;

    /**
     * Shows current value of bid
     */
    String currentBidString;

    /**
     * Aution end time for auto bid
     */
    private long endTimeForRobot;
    /**
     * Default value for shared preferences
     */
    private final static int DEFAULT_VALUE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);

        //set title to action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(this.getString(R.string.title_item_detail));

        //adding up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find all relevant views that we will need to read
        mCurrentBid = (TextView) findViewById(R.id.bid_text_view);
        mTimeLeft = (TextView) findViewById(R.id.time_left_text);
        mItemName = (TextView) findViewById(R.id.item_name_text);
        mDescrition = (TextView) findViewById(R.id.description_text_view);
        mItemId = (TextView) findViewById(R.id.item_id);
        mStartedTime = (TextView) findViewById(R.id.start_date_text);
        mBidValue = (EditText) findViewById(R.id.increase_bid_edit);
        mSetBidBtn = (Button) findViewById(R.id.set_price_btn);
        mImageView = (ImageView) findViewById(R.id.image_detail_view);
        mYourBid = (TextView) findViewById(R.id.you_win_view);
        mLastBid = (TextView) findViewById(R.id.last_bid_text_view);

        mYourBid.setVisibility(View.INVISIBLE);
        //getting intent
        Intent intent = getIntent();
        //getting item uri
        mCurrentItemUri = intent.getData();

        mSetBidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBidString = mCurrentBid.getText().toString().trim();
                String bidValueString = mBidValue.getText().toString().trim();

                if (!TextUtils.isEmpty(bidValueString)) {
                    double bidValue = Double.parseDouble(mBidValue.getText().toString().trim());
                    if (currentBidString.equals(DetailedItemActivity.this.getString(R.string.no_bids))) {
                        if (bidValue > mStartingBid) {
                            insertBid();
                            //getting max value of bid to update mCurrentBid {@link TextView}
                            updateCurrentBidValue();
                            updateItemWinner();
                        } else
                            Toast.makeText(DetailedItemActivity.this, getString(R.string.start_large_amount), Toast.LENGTH_SHORT).show();
                    } else if (bidValue > Double.parseDouble(currentBidString)) {
                        insertBid();
                        //getting max value of bid to update mCurrentBid {@link TextView}
                        updateCurrentBidValue();
                        updateItemWinner();
                    } else {
                        Toast.makeText(DetailedItemActivity.this, getString(R.string.enter_large_amount), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserId = preferences.getInt(this.getString(R.string.user), DEFAULT_VALUE);
        //initializing the loader
        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies which columns from the database
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_DESCRIPTION,
                ItemEntry.COLUMN_ITEM_START_PRICE,
                ItemEntry.COLUMN_ITEM_START_TIME,
                ItemEntry.COLUMN_ITEM_IMAGE,
                ItemEntry.COLUMN_ITEM_IS_END,
                ItemEntry.COLUMN_ITEM_END_TIME

        };

        // creating a Cursor for the data being displayed.
        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        try {

            if (cursor.moveToFirst()) {
                // Find the columns of item attributes that we're interested in
                int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
                int descriptionColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_DESCRIPTION);
                int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
                int startPriceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_START_PRICE);
                int startTimeColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_START_TIME);
                int imageColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_IMAGE);
                int endTimeColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_END_TIME);
                int isEndColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_IS_END);

                // Extract out the value from the Cursor for the given column index
                String name = cursor.getString(nameColumnIndex);
                String description = cursor.getString(descriptionColumnIndex);
                String id = cursor.getString(idColumnIndex);
                String startTime = cursor.getString(startTimeColumnIndex);
                String endTime = cursor.getString(endTimeColumnIndex);

                mStartingBid = Double.parseDouble(cursor.getString(startPriceColumnIndex));
                int isEnd = cursor.getInt(isEndColumnIndex);
                byte[] image = cursor.getBlob(imageColumnIndex);

                long currentTime = System.currentTimeMillis();
                long timeLeft = Long.parseLong(endTime) - currentTime;
                endTimeForRobot = Long.parseLong(endTime);
                //check available time difference
                if (timeLeft > 0) {
                    //setting timer
                    new CountDownTimer(timeLeft, 1000) {

                        public void onTick(long millisUntilFinished) {
                            final long MILLI_SEC_FOR_MIN = 60000;
                            long secs = millisUntilFinished % MILLI_SEC_FOR_MIN;
                            //set available time
                            mTimeLeft.setText("" + millisUntilFinished / MILLI_SEC_FOR_MIN + " "
                                    + getString(R.string.min) + " " + secs / 1000 + " " + getString(R.string.sec));
                        }

                        public void onFinish() {
                            mTimeLeft.setText(R.string.ended);
                            mLastBid.setText(R.string.winning_bid);
                            mSetBidBtn.setEnabled(false);
                            mBidValue.setEnabled(false);
                            updateIsEnded();
                        }
                    }.start();

                } else {
                    mTimeLeft.setText(R.string.ended);
                    mLastBid.setText(R.string.winning_bid);
                    mSetBidBtn.setEnabled(false);
                    mBidValue.setEnabled(false);
                    if (isEnd == 0) {
                        updateIsEnded();
                    }

                }

                Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);

                //set image
                mImageView.setImageBitmap(bmp);
                //set values
                mItemName.setText(name);
                mDescrition.setText(description);
                mItemId.setText(id);

                GetDate date = new GetDate();
                Date dateObject = new Date(Long.parseLong(startTime));
                mStartedTime.setText(date.formatDate(dateObject) + " " + date.formatTime(dateObject));

                //getting max value of bid to update mCurrentBid {@link TextView}
                updateCurrentBidValue();

            }
        } catch (IllegalStateException e) {
            Toast.makeText(DetailedItemActivity.this, getString(R.string.large_data), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method to show current bid value .
     */
    public void updateCurrentBidValue() {
        //getting max value of bid
        String[] projection = {
                HelloAuctionContract.BidEntry._ID,
                HelloAuctionContract.BidEntry.COLUMN_BID_USER_ID,
                HelloAuctionContract.BidEntry.COLUMN_BID_VALUE

        };

        String selection = HelloAuctionContract.BidEntry.COLUMN_BID_ITEM_ID + "=?";
        String[] selectionArgs = {mItemId.getText().toString().trim()};
        Cursor bidCursor = getContentResolver().query(HelloAuctionContract.BidEntry.CONTENT_URI,
                projection, selection, selectionArgs, HelloAuctionContract.BidEntry.COLUMN_BID_VALUE + " DESC");
        if (bidCursor.moveToFirst()) {
            double bidValue = bidCursor.getDouble(bidCursor.getColumnIndex(HelloAuctionContract.BidEntry.COLUMN_BID_VALUE));
            int id = bidCursor.getInt(bidCursor.getColumnIndex(HelloAuctionContract.BidEntry.COLUMN_BID_USER_ID));
            if (id == mUserId) {
                mYourBid.setVisibility(View.VISIBLE);
            } else {
                mYourBid.setVisibility(View.INVISIBLE);
            }

            mCurrentBid.setText(Double.toString(bidValue));

            long time = new Long(new GetDate().getRandomVlue());
            new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    automaticInsert();
                }
            }.start();

        } else {

            mCurrentBid.setText(R.string.no_bids);
        }

    }

    /**
     * Helper method to insert  bid data into the database.
     */
    private void insertBid() {

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String itemID = mItemId.getText().toString().trim();
        String bidValue = mBidValue.getText().toString().trim();
        long currentTime = System.currentTimeMillis();
        String date = Long.toString(currentTime);

        // Create a ContentValues object where column names are the keys,
        // and Toto's product attributes are the values.
        if (!TextUtils.isEmpty(itemID) &&
                !TextUtils.isEmpty(bidValue) &&
                !TextUtils.isEmpty(date)) {

            //content value to store key value pair
            ContentValues values = new ContentValues();
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_USER_ID, mUserId);
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_ITEM_ID, itemID);
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_VALUE, bidValue);
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_TIME, date);

            // Insert a new row for Toto in the database, returning the ID of that new row.
            Uri newUri = getContentResolver().insert(HelloAuctionContract.BidEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_bid_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_bid_successful),
                        Toast.LENGTH_SHORT).show();

            }

        }

    }

    /**
     * Helper method to auto insert
     */
    private void automaticInsert() {
        long currentTime = System.currentTimeMillis();
        int NO_OF_SECS = 20;
        double currentBid = Double.parseDouble(mCurrentBid.getText().toString());
        double bidValue = currentBid + 1;

        long diff = (endTimeForRobot - currentTime) / 1000;
        if (diff > NO_OF_SECS) {
            ContentValues values = new ContentValues();
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_USER_ID, mUserId);
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_ITEM_ID, mItemId.getText().toString().trim());
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_VALUE, bidValue);
            values.put(HelloAuctionContract.BidEntry.COLUMN_BID_TIME, currentTime);

            // Insert a new row for Toto in the database, returning the ID of that new row.
            Uri newUri = getContentResolver().insert(HelloAuctionContract.BidEntry.CONTENT_URI, values);
            if (newUri != null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.auto_bid_inserted) + " "
                                + getString(R.string.on_item) + " " + mItemName.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
        mCurrentBid.setText(Double.toString(bidValue));
        mYourBid.setVisibility(View.INVISIBLE);
    }

    /**
     * Helper method to update item
     */
    private void updateItemWinner() {

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mItemName.getText().toString().trim();
        String descriptionString = mDescrition.getText().toString().trim();

        //if all the values are null then exit
        if (mCurrentItemUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(descriptionString)) {
            finish();
            return;
        }
        //all the textFields must be not empty to update a item
        if (!TextUtils.isEmpty(nameString) && !TextUtils.isEmpty(descriptionString)) {

            // Create a ContentValues object where column names are the keys,
            // and product attributes from the editor are the values.
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLUMN_ITEM_WINNER_ID, mUserId);
            values.put(ItemEntry.COLUMN_ITEM_NAME, nameString);
            values.put(ItemEntry.COLUMN_ITEM_DESCRIPTION, descriptionString);

            if (mCurrentItemUri != null) {
                //get updated row count
                int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(DetailedItemActivity.this, getString(R.string.update_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateIsEnded() {
        final int IS_ENDED = 1;
        String nameString = mItemName.getText().toString().trim();
        String descriptionString = mDescrition.getText().toString().trim();
        // Create a ContentValues object where column names are the keys,
        // and product attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_IS_END, IS_ENDED);
        values.put(ItemEntry.COLUMN_ITEM_NAME, nameString);
        values.put(ItemEntry.COLUMN_ITEM_DESCRIPTION, descriptionString);

        if (mCurrentItemUri != null) {
            //get updated row count
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(DetailedItemActivity.this, getString(R.string.update_fail),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}