package com.sasig.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiTMDB {

    ///// Pour films populaires
    @GET("movie/popular")
    Call<ResMovies> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    ///// Pour genres
    @GET("genre/movie/list")
    Call<ResGenres> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}
