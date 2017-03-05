package com.dementiev.testwork.ui.itemsCollection;

import android.database.Cursor;
import android.database.SQLException;

import com.dementiev.testwork.model.entity.Item;
import com.dementiev.testwork.model.interactor.base.Interactor;
import com.dementiev.testwork.model.storage.ItemsCacheDb;
import com.dementiev.testwork.model.util.CursorList;
import com.dementiev.testwork.ui.base.BaseView;
import com.dementiev.testwork.ui.itemsCollection.base.ItemsCollectionPresenter;
import com.dementiev.testwork.ui.itemsCollection.base.ItemsCollectionView;
import com.dementiev.testwork.ui.itemsDetails.DetailsActivity;

import java.util.AbstractList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by dron on 03.03.17.
 */

final class MainPresenter implements ItemsCollectionPresenter {
    private ItemsCollectionView itemsCollectionView = null;
    private ItemsCacheDb itemsCacheDb;
    private Cursor itemsCursor;
    private Interactor loadItemsInteractor;
    private Disposable loadItemsSubscription = null;


    private MainPresenter(Interactor loadItemsInteractor) {
        this.loadItemsInteractor = loadItemsInteractor;
    }

    public static MainPresenter create(Interactor loadItemsInteractor) {
        return new MainPresenter(loadItemsInteractor);
    }

    @Override
    public void bind(BaseView view) {
        itemsCollectionView = (ItemsCollectionView) view;
        itemsCacheDb = new ItemsCacheDb(view.getContext());
        // probably a good place to create cursor, or do it lazy on first run

    }

    @Override
    public void unbind() {
        itemsCollectionView = null;
        // other deinit
        if (loadItemsSubscription != null && !loadItemsSubscription.isDisposed()) {
            loadItemsSubscription.dispose();
        }
        // probably a good place to close cursor
        if (itemsCursor != null) {
            itemsCursor.close();
        }
        if (itemsCacheDb != null) {
            itemsCacheDb.close();
        }
    }

    @Override
    public AbstractList<Item> getItemsSource() {
        // TODO: 04.03.17 make data, REUSE Cursor, or close it
        try {
            if (itemsCursor != null) {
                itemsCursor.close();
            }
            itemsCursor = itemsCacheDb.getItems();
            AbstractList<Item> list = new CursorList<>(itemsCursor, cursor -> {
                int idColIndex = cursor.getColumnIndex("id");
                int titleColIndex = cursor.getColumnIndex("title");
                int imgColIndex = cursor.getColumnIndex("img");
                return new Item(
                        cursor.getInt(idColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getString(imgColIndex)
                );
            });
            if (list.size() == 0) {
                onRefreshCommand();
            }
            return list;
        } catch (SQLException sex) {
            return null;
        }
    }


    @Override
    public void onItemSelected(Item item, int selectedIndex) {
        DetailsActivity.create(itemsCollectionView.getContext(), selectedIndex);
    }


    @Override
    public void onRefreshCommand() {
        itemsCollectionView.showUpdate();
        loadItemsSubscription = loadItemsInteractor.perform()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (isAttached()) { // should be fine without this check(see unbind method), just precaution
                                itemsCollectionView.hideUpdate();
                                itemsCollectionView.onItemsCollectionUpdated(false);
                            }
                        },
                        error -> {
                            if (isAttached()) {
                                itemsCollectionView.hideUpdate();
                                itemsCollectionView.onItemsCollectionUpdated(true);
                            }
                        }
                );
    }

    @Override
    public boolean isAttached() {
        return itemsCollectionView != null;
    }
}
