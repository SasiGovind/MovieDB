package com.sasig.moviedb.controller;

import com.sasig.moviedb.model.People;

public interface CallbackPeople {
    void onSuccess(People people);
    void onError();
}
