package com.sasig.moviedb;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepo {
    private static final String URL = "https://api.themoviedb.org/3/";
    private static final String LANG = "en-US";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private static MoviesRepo repo;

    private ApiTMDB api;

    private MoviesRepo(ApiTMDB api) {
        this.api = api;
    }

    public static MoviesRepo getInstance() {
        if (repo == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repo = new MoviesRepo(retrofit.create(ApiTMDB.class));
        }

        return repo;
    }

    public void getMovies(int page, String sortBy, final CallbackMovies callback) {
        Log.d("MoviesRepo", "Currently on Page = " + page);
        Callback<ResMovies> call = new Callback<ResMovies>() {
            @Override
            public void onResponse(Call<ResMovies> call, Response<ResMovies> resp) {
                if (resp.isSuccessful()) {
                    ResMovies responseMovie = resp.body();
                    if (responseMovie != null && responseMovie.getMovies() != null) {
                        callback.onSuccess(responseMovie.getMovies(), responseMovie.getPage());
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
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies(BuildConfig.APIKEY, LANG, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies(BuildConfig.APIKEY, LANG, page)
                        .enqueue(call);
                break;
            case POPULAR:
            default:
                api.getPopularMovies(BuildConfig.APIKEY, LANG, page)
                        .enqueue(call);
                break;
        }
    }

    public void getGenres(final CallbackGenres callback) {
        api.getGenres(BuildConfig.APIKEY, LANG)
                .enqueue(new Callback<ResGenres>() {
                    @Override
                    public void onFailure(Call<ResGenres> call, Throwable t) {
                        callback.onError();
                    }

                    @Override
                    public void onResponse(Call<ResGenres> call, Response<ResGenres> resp) {
                        if (resp.isSuccessful()) {
                            ResGenres resGenres = resp.body();
                            if (resGenres != null && resGenres.getGenres() != null) {
                                callback.onSuccess(resGenres.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }
                });
    }
}
