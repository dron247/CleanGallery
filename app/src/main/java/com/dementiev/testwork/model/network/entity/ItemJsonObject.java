package com.dementiev.testwork.model.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dron on 04.03.17.
 */

public class ItemJsonObject {

    @SerializedName("id")
    int id;

    @SerializedName("title")
    String title;

    @SerializedName("img")
    String uri;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }
}
