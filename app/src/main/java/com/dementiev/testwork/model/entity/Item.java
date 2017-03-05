package com.dementiev.testwork.model.entity;

/**
 * Created by dron on 04.03.17.
 */

public class Item {
    int id;
    String title;
    String imageUri;

    public Item(int id, String title, String imageUri) {
        this.id = id;
        this.title = title;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUri() {
        return imageUri;
    }
}
