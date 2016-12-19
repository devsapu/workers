package com.eilifint.ravimal.helloauction;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract.ItemEntry;

/**
 * Created by Ravimal on 11/24/2016.
 */
public class AuctionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * List view to display list of data
     */
    ListView AuctionListView;
    /**
     * Default value for shared preferences
     */
    private final static int DEFAULT_VALUE = 0;

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    /**
     * Database helper that will provide us access to the database
     */
    ItemCursorAdapter mCursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding view to use the list.xml layout file
        View rootView = inflater.inflate(R.layout.list, container, false);

        // Find the ListView which will be populated with the item data
        AuctionListView = (ListView) rootView.findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = rootView.findViewById(R.id.empty_view);
        AuctionListView.setEmptyView(emptyView);
        AuctionListView.setItemsCanFocus(true);
        mCursorAdapter = new ItemCursorAdapter(getActivity(), null, getString(R.string.auction));
        //set {@link ItemCursorAdapter}
        AuctionListView.setAdapter(mCursorAdapter);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        //listView onclick listener
        AuctionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), DetailedItemActivity.class);

                //from the content uri  that represent the specific item that was clicked on
                // Utility methods useful for working with Uri objects that use the "content" (content://) scheme.

                Uri currentPrtUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                //set the uri on the data field on the intent
                intent.setData(currentPrtUri);
                //launch the {@link EditorActivity} to display the data for the current item
                startActivity(intent);

            }

        });
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
        // you will actually use after this query.
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_END_TIME,
                ItemEntry.COLUMN_ITEM_START_PRICE
        };

        String selection = ItemEntry.COLUMN_ITEM_USER_ID + "!=?";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int userId = preferences.getInt(this.getString(R.string.user), DEFAULT_VALUE);

        String[] selectionArgs = {Integer.toString(userId)};

        // creating a Cursor for the data being displayed.
        return new android.support.v4.content.CursorLoader(getActivity(),
                ItemEntry.CONTENT_URI,
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