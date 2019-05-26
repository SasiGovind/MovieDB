package com.sasig.moviedb;

import java.util.List;

public interface CallbackMovies {
    void onSuccess(List<Movie> movies);
    void onError();
}
