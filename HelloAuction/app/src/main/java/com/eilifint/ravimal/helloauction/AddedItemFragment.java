package com.eilifint.ravimal.helloauction;

import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract;

/**
 * Created by Ravimal on 11/24/2016.
 */
public class AddedItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * List view to display list of data
     */
    ListView itemListView;

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    /**
     * Database helper that will provide us access to the database
     */
    ItemCursorAdapter mCursorAdapter;

    /**
     * User id
     */
    private int mUserId;

    /**
     * Default value for shared preferences
     */
    private final static int DEFAULT_VALUE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding view to use the list.xml layout file
        View rootView = inflater.inflate(R.layout.list, container, false);
        // Find the ListView which will be populated with the item data
        itemListView = (ListView) rootView.findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = rootView.findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        itemListView.setItemsCanFocus(true);
        mCursorAdapter = new ItemCursorAdapter(getActivity(), null, getString(R.string.my_item));
        //set {@link ItemCursorAdapter}
        itemListView.setAdapter(mCursorAdapter);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mUserId = preferences.getInt(this.getString(R.string.user), DEFAULT_VALUE);

         /*
         * Initializes the CursorLoader. The URL_LOADER value is eventually passed
         * to onCreateLoader().
         */
        getLoaderManager().initLoader(URL_LOADER, null, this);
        return rootView;
    }

    /**
     * returns {@link CursorLoader} to display data
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies which columns from the database
        String[] projection = {
                HelloAuctionContract.ItemEntry._ID,
                HelloAuctionContract.ItemEntry.COLUMN_ITEM_NAME,
                HelloAuctionContract.ItemEntry.COLUMN_ITEM_END_TIME,
                HelloAuctionContract.ItemEntry.COLUMN_ITEM_START_PRICE
        };
        // Define a selection for where clause
        String selection = HelloAuctionContract.ItemEntry.COLUMN_ITEM_USER_ID + "=?";
        String[] selectionArgs = {Integer.toString(mUserId)};

        // creating a Cursor for the data being displayed.
        return new android.support.v4.content.CursorLoader(getActivity(),
                HelloAuctionContract.ItemEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    /**
     * dataBase data is loaded
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //swap cursor
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}
