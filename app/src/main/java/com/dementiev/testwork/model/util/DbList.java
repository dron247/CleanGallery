package com.dementiev.testwork.model.util;

import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by dron on 04.03.17.
 */

public class DbList<T> extends AbstractList<T> {
    public interface Factory<T> {
        T get(Cursor cursor);
    }

    private final Cursor cursor;
    private final Factory<T> factory;

    public DbList(Cursor cursor, Factory<T> factory) {
        this.cursor = cursor;
        this.factory = factory;
    }

    @Override
    @Nullable
    public T get(int location) {
        if (cursor != null && cursor.moveToPosition(location)) {
            return factory.get(cursor);
        }
        return null;
    }

    @Override
    public int size() {
        return cursor == null ? 0 : cursor.getCount();
    }

}