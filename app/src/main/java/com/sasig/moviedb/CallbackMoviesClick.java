package com.sasig.moviedb;

import android.widget.ImageView;
import android.widget.TextView;

public interface CallbackMoviesClick {
    void onClick(TextView v1, TextView v2, TextView v3, ImageView v4, Movie movie);
}
