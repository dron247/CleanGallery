package com.dementiev.testwork.ui.itemsDetails.base;

import com.dementiev.testwork.model.entity.Item;
import com.dementiev.testwork.ui.base.BasePresenter;

import java.util.AbstractList;

/**
 * Created by dron on 05.03.17.
 */

public interface ItemDetailsPresenter extends BasePresenter {
    AbstractList<Item> getItemsSource();
}
