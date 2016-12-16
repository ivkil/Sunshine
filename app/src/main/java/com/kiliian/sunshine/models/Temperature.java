package com.kiliian.sunshine.models;

import com.google.gson.annotations.SerializedName;


class Temperature {

    @SerializedName("min")
    private double min;

    @SerializedName("max")
    private double max;

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
