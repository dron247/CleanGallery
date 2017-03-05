package com.dementiev.testwork.ui.itemsCollection.base;

import com.dementiev.testwork.model.entity.Item;
import com.dementiev.testwork.ui.base.BasePresenter;

import java.util.AbstractList;

/**
 * Created by dron on 03.03.17.
 */

public interface ItemsCollectionPresenter extends BasePresenter {
    AbstractList<Item> getItemsSource();

    void onItemSelected(Item item, int selectedIndex);

    void onRefreshCommand();
}
