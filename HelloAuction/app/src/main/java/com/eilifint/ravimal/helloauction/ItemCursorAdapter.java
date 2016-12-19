package com.eilifint.ravimal.helloauction;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.eilifint.ravimal.helloauction.Utils.GetDate;
import com.eilifint.ravimal.helloauction.data.HelloAuctionContract;

import java.util.Date;

/**
 * Created by Ravimal on 11/24/2016.
 */

/**
 * {@link ItemCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */

public class ItemCursorAdapter extends CursorAdapter {

    Context context;
    GetDate date;
    String type;

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c, String type) {
        super(context, c, 0);
        this.type = type;
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        this.context = context;
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView name = (TextView) view.findViewById(R.id.name_item_text_view);
        TextView endTime = (TextView) view.findViewById(R.id.date_item_text_view);
        TextView startPrice = (TextView) view.findViewById(R.id.price_item_text_view);
        // get column indexes
        int nameColumnIndex = cursor.getColumnIndex(HelloAuctionContract.ItemEntry.COLUMN_ITEM_NAME);
        int timeColumnIndex = cursor.getColumnIndex(HelloAuctionContract.ItemEntry.COLUMN_ITEM_END_TIME);
        int priceColumnIndex = cursor.getColumnIndex(HelloAuctionContract.ItemEntry.COLUMN_ITEM_START_PRICE);

        getItemId(cursor.getPosition());

        // Extract properties from cursor
        String nameText = cursor.getString(nameColumnIndex);
        String timeText = cursor.getString(timeColumnIndex);
        String priceText = cursor.getString(priceColumnIndex);
        //instantiate date object
        date = new GetDate();
        Date dateObject = new Date(Long.parseLong(timeText));

        // Populate fields with extracted properties
        name.setText(nameText);
        endTime.setText(date.formatDate(dateObject) + " " + date.formatTime(dateObject));
        startPrice.setText(priceText + "$");
    }
}
