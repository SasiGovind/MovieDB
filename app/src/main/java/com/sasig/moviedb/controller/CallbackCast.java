package com.sasig.moviedb.controller;

import com.sasig.moviedb.model.Cast;

import java.util.List;

public interface CallbackCast {
    void onSuccess(List<Cast> casts);
    void onError();
}
