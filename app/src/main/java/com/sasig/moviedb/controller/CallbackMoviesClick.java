package com.sasig.moviedb.controller;

import android.widget.ImageView;
import android.widget.TextView;

import com.sasig.moviedb.model.Movie;

public interface CallbackMoviesClick {
    void onClick(TextView v1, TextView v2, TextView v3, ImageView v4, Movie movie);
}
