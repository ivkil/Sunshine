package com.kiliian.sunshine.models;

import com.google.gson.annotations.SerializedName;


class WeatherId {

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
