package com.sasig.moviedb.controller;

import com.sasig.moviedb.model.Trailer;

import java.util.List;

public interface CallbackTrailers {
    void onSuccess(List<Trailer> trailers);
    void onError();
}
