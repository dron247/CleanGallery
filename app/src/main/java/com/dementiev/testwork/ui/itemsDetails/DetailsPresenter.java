package com.dementiev.testwork.ui.itemsDetails;

import android.database.Cursor;
import android.database.SQLException;

import com.dementiev.testwork.model.entity.Item;
import com.dementiev.testwork.model.storage.ItemsCacheDb;
import com.dementiev.testwork.model.util.CursorList;
import com.dementiev.testwork.ui.base.BaseView;
import com.dementiev.testwork.ui.itemsDetails.base.ItemDetailsPresenter;
import com.dementiev.testwork.ui.itemsDetails.base.ItemDetailsView;

import java.util.AbstractList;

/**
 * Created by dron on 05.03.17.
 */

public final class DetailsPresenter implements ItemDetailsPresenter {
    private Cursor itemsCursor;
    private ItemsCacheDb itemsCacheDb;
    private ItemDetailsView itemDetailsView = null;
    private DetailsPresenter() {
        //
    }

    public static DetailsPresenter create() {
        return new DetailsPresenter();
    }

    @Override
    public <E extends BaseView> void bind(E view) {
        itemDetailsView = (ItemDetailsView) view;
        itemsCacheDb = new ItemsCacheDb(itemDetailsView.getContext());
    }

    @Override
    public void unbind() {
        itemDetailsView = null;
        // probably a good place to close cursor
        if (itemsCursor != null) {
            itemsCursor.close();
        }
        if (itemsCacheDb != null) {
            itemsCacheDb.close();
        }
    }

    @Override
    public boolean isAttached() {
        return itemDetailsView != null;
    }


    @Override
    public AbstractList<Item> getItemsSource() {
        // TODO: 04.03.17 make data, REUSE Cursor, or close it
        try {
            if (itemsCursor != null) {
                itemsCursor.close();
            }
            itemsCursor = itemsCacheDb.getItems();
            return new CursorList<>(itemsCursor, cursor -> {
                int idColIndex = cursor.getColumnIndex("id");
                int titleColIndex = cursor.getColumnIndex("title");
                int imgColIndex = cursor.getColumnIndex("img");
                return new Item(
                        cursor.getInt(idColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getString(imgColIndex)
                );
            });
        } catch (SQLException sex) {
            return null;
        }
    }
}
