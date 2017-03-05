package com.dementiev.testwork.ui.itemsDetails;

/**
 * Created by dron on 05.03.17.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.dementiev.testwork.model.entity.Item;

import java.util.AbstractList;


public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    AbstractList<Item> items;


    public SectionsPagerAdapter(FragmentManager fm, AbstractList<Item> dataSource) {
        super(fm);
        Log.d("PAGER", "Adapter recreated");
        items = dataSource;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DetailsPage (defined as a static inner class below).
        DetailsPage detailsPage = DetailsPage.newInstance(getDataAtPosition(position), position, position >= getCount() - 1);
        return detailsPage;
    }

    private Item getDataAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getDataAtPosition(position).getTitle();
    }
}