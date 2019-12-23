package com.sasig.moviedb.controller;

import android.util.Log;

import com.sasig.moviedb.BuildConfig;
import com.sasig.moviedb.model.ApiTMDB;
import com.sasig.moviedb.model.Movie;
import com.sasig.moviedb.model.People;
import com.sasig.moviedb.model.ResCast;
import com.sasig.moviedb.model.ResGenres;
import com.sasig.moviedb.model.ResMovies;
import com.sasig.moviedb.model.ResTrailer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepo {
    private static final String URL = "https://api.themoviedb.org/3/";
    public static String LANG = "en-US";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    public static final String SEARCH = "search";

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

    public void getMovie(int id_movie, final CallbackMovie callback) {
        api.getMovie(id_movie, BuildConfig.APIKEY, LANG)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> resp) {
                        if (resp.isSuccessful()) {
                            Movie movie = resp.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getPeople(int id_people, final CallbackPeople callback){
        api.getPeople(id_people, BuildConfig.APIKEY, LANG)
                .enqueue(new Callback<People>() {
                    @Override
                    public void onResponse(Call<People> call, Response<People> resp) {
                        if (resp.isSuccessful()) {
                            People people = resp.body();
                            if (people != null) {
                                callback.onSuccess(people);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<People> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovies(int page, String sortBy, final CallbackMovies callback) {
        Log.d("MoviesRepo", "Currently on Page = " + page);
        Callback<ResMovies> call = new Callback<ResMovies>() {
            @Override
            public void onResponse(Call<ResMovies> call, Response<ResMovies> resp) {
                if (resp.isSuccessful()) {
                    ResMovies responseMovie = resp.body();
                    if (responseMovie != null && responseMovie.getMovies() != null) {
                        callback.onSuccess(responseMovie.getMovies(), responseMovie.getPage(), responseMovie.getTotalPages());
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

    public void getSearchMovies(String query, int page, final CallbackMovies callback){
        api.getSearchMovie(query, page, BuildConfig.APIKEY, LANG)
                .enqueue(new Callback<ResMovies>() {
                    @Override
                    public void onResponse(Call<ResMovies> call, Response<ResMovies> resp) {
                        if (resp.isSuccessful()) {
                            ResMovies responseMovie = resp.body();
                            if (responseMovie != null && responseMovie.getMovies() != null) {
                                callback.onSuccess(responseMovie.getMovies(), responseMovie.getPage(), responseMovie.getTotalPages());
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

    public void getCasts(int id_movie, final CallbackCast callback) {
        api.getCredits(id_movie, BuildConfig.APIKEY, LANG)
                .enqueue(new Callback<ResCast>() {
                    @Override
                    public void onResponse(Call<ResCast> call, Response<ResCast> resp) {
                        if (resp.isSuccessful()) {
                            ResCast resCast = resp.body();
                            if (resCast != null && resCast.getCast() != null) {
                                callback.onSuccess(resCast.getCast());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResCast> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTrailers(int id_movie, final CallbackTrailers callback) {
        api.getTrailers(id_movie, BuildConfig.APIKEY, LANG)
                .enqueue(new Callback<ResTrailer>() {
                    @Override
                    public void onResponse(Call<ResTrailer> call, Response<ResTrailer> resp) {
                        if (resp.isSuccessful()) {
                            ResTrailer resTrailer = resp.body();
                            if (resTrailer != null && resTrailer.getTrailers() != null) {
                                callback.onSuccess(resTrailer.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResTrailer> call, Throwable t) {
                        callback.onError();
                    }
                });
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

    public void setLANG(String lang){
        this.LANG = lang;
    }

    public void getSimilarMovies(int id_movie,  int page, final CallbackMovies callback) {
        api.getSimilarMovies(id_movie, page, BuildConfig.APIKEY, LANG)
                .enqueue(new Callback<ResMovies>() {
                    @Override
                    public void onResponse(Call<ResMovies> call, Response<ResMovies> resp) {
                        if (resp.isSuccessful()) {
                            ResMovies responseMovie = resp.body();
                            if (responseMovie != null && responseMovie.getMovies() != null) {
                                callback.onSuccess(responseMovie.getMovies(), responseMovie.getPage(), responseMovie.getTotalPages());
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
