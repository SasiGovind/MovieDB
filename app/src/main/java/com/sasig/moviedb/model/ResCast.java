package com.sasig.moviedb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResCast {
    @SerializedName("cast")
    private List<Cast> cast;

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }
}
