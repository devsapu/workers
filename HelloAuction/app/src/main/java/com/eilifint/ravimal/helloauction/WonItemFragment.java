package com.eilifint.ravimal.helloauction;

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
public class WonItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * List view to display list of data
     */
    ListView wonListView;

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    /**
     * Database helper that will provide us access to the database
     */
    ItemCursorAdapter mCursorAdapter;

    /***
     * User id
     */
    private int mUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding view to use the list.xml layout file
        View rootView = inflater.inflate(R.layout.list, container, false);
        // Find the ListView which will be populated with the product data
        wonListView = (ListView) rootView.findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = rootView.findViewById(R.id.empty_view);
        wonListView.setEmptyView(emptyView);

        wonListView.setItemsCanFocus(true);
        mCursorAdapter = new ItemCursorAdapter(getActivity(), null, getString(R.string.won_item));
        //set {@link ItemCursorAdapter}
        wonListView.setAdapter(mCursorAdapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserId = preferences.getInt(getString(R.string.user), 0);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

         /*
         * Initializes the CursorLoader. The URL_LOADER value is eventually passed
         * to onCreateLoader().
         */
        getLoaderManager().initLoader(URL_LOADER, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HelloAuctionContract.ItemEntry._ID,
                HelloAuctionContract.ItemEntry.COLUMN_ITEM_NAME,
                HelloAuctionContract.ItemEntry.COLUMN_ITEM_END_TIME,
                HelloAuctionContract.ItemEntry.COLUMN_ITEM_START_PRICE
        };

        String selection = HelloAuctionContract.ItemEntry.COLUMN_ITEM_WINNER_ID + "=?" +
                " AND " + HelloAuctionContract.ItemEntry.COLUMN_ITEM_IS_END + "=?";
        String[] selectionArgs = {Integer.toString(mUserId), getString(R.string.one)};

        // creating a Cursor for the data being displayed.
        return new android.support.v4.content.CursorLoader(getActivity(),
                HelloAuctionContract.ItemEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

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
