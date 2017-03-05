package com.dementiev.testwork.model.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dementiev.testwork.model.entity.Item;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by dron on 05.03.17.
 */

public class ItemsCacheDb extends SQLiteOpenHelper {
    public ItemsCacheDb(Context context) {
        super(context, "ItemsCache", null, 1);
    }


    public Cursor getItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("items", new String[]{"id", "title", "img"}, null, null, null, null, null);
    }

    public void writeItems(List<Item> bundle) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            for (Item item : bundle) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", item.getId());
                contentValues.put("title", item.getTitle());
                contentValues.put("img", item.getImageUri());
                db.insert("items", null, contentValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public void clearCache() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from items");
        db.close();
    }

    public void close() {
        //this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table items ("
                + "idx integer primary key autoincrement, "
                + "id integer, "
                + "title text, "
                + "img text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
