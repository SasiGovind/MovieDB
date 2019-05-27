package com.sasig.moviedb;

import java.util.List;

public interface CallbackGenres {
    void onSuccess(List<Genre> genres);
    void onError();
}
