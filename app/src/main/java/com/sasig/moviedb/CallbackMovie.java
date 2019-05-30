package com.sasig.moviedb;

public interface CallbackMovie {
    void onSuccess(Movie movie);
    void onError();
}
