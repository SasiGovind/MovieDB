package com.sasig.moviedb.controller;

import com.sasig.moviedb.model.Genre;

import java.util.List;

public interface CallbackGenres {
    void onSuccess(List<Genre> genres);
    void onError();
}
