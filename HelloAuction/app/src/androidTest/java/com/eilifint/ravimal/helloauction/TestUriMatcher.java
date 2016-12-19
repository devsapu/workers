package com.eilifint.ravimal.helloauction;

import android.content.UriMatcher;
import android.net.Uri;

import com.eilifint.ravimal.helloauction.data.HelloAuctionContract;
import com.eilifint.ravimal.helloauction.data.HelloAuctionProvider;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Ravimal on 11/26/2016.
 */

public class TestUriMatcher {

    // content://com.eilifint.ravimal.helloauction/item"
    private static final Uri TEST_ITEM_DIR = HelloAuctionContract.ItemEntry.CONTENT_URI;
    // content://com.eilifint.ravimal.helloauction/user"
    private static final Uri TEST_USER_DIR = HelloAuctionContract.UserEntry.CONTENT_URI;
    // content://com.eilifint.ravimal.helloauction/bid"
    private static final Uri TEST_BID_DIR = HelloAuctionContract.BidEntry.CONTENT_URI;

    /*
       This function tests that UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.
     */

    @Test
    public void testUriMatcher() {


        UriMatcher testMatcher = HelloAuctionProvider.buildUriMatcher();


        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_ITEM_DIR), HelloAuctionProvider.ITEMS);

        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_USER_DIR), HelloAuctionProvider.USERS);

        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_BID_DIR), HelloAuctionProvider.BIDS);
    }
}
