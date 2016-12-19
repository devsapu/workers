package com.eilifint.ravimal.helloauction;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ravimal on 11/24/2016.
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {

    private Context context;

    //Array contains titles for each tab
    private String tabTitles[];

    //No of tabs
    final static int NO_OF_TABS = 3;

    /**
     * Constructor
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param fm      FragmentManager object for super class
     */
    public HomeFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        //retrieves string array
        tabTitles = context.getResources().getStringArray(R.array.title_array);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return title name
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        //if statement for return fragment objects according to position
        if (position == 0) {
            return new AuctionFragment();
        } else if (position == 1) {
            return new WonItemFragment();
        } else {
            return new AddedItemFragment();
        }
    }

    @Override
    public int getCount() {
        return NO_OF_TABS;
    }
}