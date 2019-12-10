package com.sasig.moviedb.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiTMDB {

    ///// Pour films populaires
    @GET("movie/popular")
    Call<ResMovies> getPopularMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    ///// Pour genres
    @GET("genre/movie/list")
    Call<ResGenres> getGenres(
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    ///// Pour Top_rated
    @GET("movie/top_rated")
    Call<ResMovies> getTopRatedMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    ///// Pour Upcoming
    @GET("movie/upcoming")
    Call<ResMovies> getUpcomingMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    ///// Pour Movie detail
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    ///// Pour Movie Trailer
    @GET("movie/{movie_id}/videos")
    Call<ResTrailer> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    ///// Pour Movie Cast
    @GET("movie/{movie_id}/credits")
    Call<ResCast> getCredits(
            @Path("movie_id") int id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    ///// Pour Movie People
    @GET("person/{person_id}")
    Call<People> getPeople(
            @Path("person_id") int id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    ///// Pour Movie Search
    @GET("search/movie")
    Call<ResMovies> getSearchMovie(
            @Query("query") String query,
            @Query("page") int page,
            @Query("api_key") String api_key,
            @Query("language") String language
    );
}
