package com.sasig.moviedb;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepo {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepo repo;

    private ApiTMDB api;

    private MoviesRepo(ApiTMDB api) {
        this.api = api;
    }

    public static MoviesRepo getInstance() {
        if (repo == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repo = new MoviesRepo(retrofit.create(ApiTMDB.class));
        }

        return repo;
    }

    public void getMovies(final CallbackMovies callback) {
        api.getPopularMovies("8ee3f9a8aaca836ce25e10a71de8644b", LANGUAGE, 1)
                .enqueue(new Callback<ResMovies>() {
                    @Override
                    public void onResponse(@NonNull Call<ResMovies> call, @NonNull Response<ResMovies> response) {
                        if (response.isSuccessful()) {
                            ResMovies moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResMovies> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
