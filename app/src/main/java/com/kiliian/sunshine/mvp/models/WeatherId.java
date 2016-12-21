package com.kiliian.sunshine.mvp.models;

import com.google.gson.annotations.SerializedName;


public class WeatherId {

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
