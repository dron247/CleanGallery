package com.dementiev.testwork.ui.itemsCollection.base;

import com.dementiev.testwork.ui.base.BaseView;

/**
 * Created by dron on 03.03.17.
 */

public interface ItemsCollectionView extends BaseView {
    void showUpdate();

    void hideUpdate();

    void onItemsCollectionUpdated(boolean isError);
}
