package com.sasig.moviedb.controller;

import com.sasig.moviedb.model.Movie;

import java.util.List;

public interface CallbackMovies {
    void onSuccess(List<Movie> movies, int page, int totalPages);
    void onError();
}
