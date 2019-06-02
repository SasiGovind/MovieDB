package com.sasig.moviedb.controller;

import com.sasig.moviedb.model.Movie;

public interface CallbackMovie {
    void onSuccess(Movie movie);
    void onError();
}
