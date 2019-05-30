package com.sasig.moviedb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    public static String ID_MOVIE = "movie_id";

    private static String POSTER_URL = "https://image.tmdb.org/t/p/w780";
    private static String YT_URL = "https://www.youtube.com/watch?v=%s";
    private static String YT_THUMB_URL = "https://img.youtube.com/vi/%s/0.jpg";

    private ImageView md_backdrop;
    private TextView md_title;
    private TextView md_genres;
    private TextView md_overview;
    private TextView md_overviewlabel;
    private TextView md_releasedate;
    private RatingBar md_rating;
    private LinearLayout md_trailers;
    private LinearLayout md_reviews;

    private MoviesRepo moviesRepo;
    private int id_movie;

    SharedPreferences myPrefsMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        myPrefsMovie = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        id_movie = getIntent().getIntExtra(ID_MOVIE, id_movie);

        Log.d("MoviesID", "Movie id = "+id_movie);

        moviesRepo = MoviesRepo.getInstance();

        configureToolbar();
        gatherMovieUI();
        getMovieDetail();
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void gatherMovieUI() {
        md_backdrop = findViewById(R.id.md_backdrop);
        md_title = findViewById(R.id.md_title);
        md_genres = findViewById(R.id.md_genres);
        md_overview = findViewById(R.id.md_overview);
        md_overviewlabel = findViewById(R.id.md_summarylabel);
        md_releasedate = findViewById(R.id.md_releasedate);
        md_rating = findViewById(R.id.md_rating);
        md_trailers = findViewById(R.id.md_trailers);
        md_reviews = findViewById(R.id.md_reviews);
    }

    private Movie checkNgetMovie(){
        if(myPrefsMovie.contains(id_movie+"")){
            Gson gson = new Gson();
            String json = myPrefsMovie.getString(id_movie+"", "");
            Movie movie = gson.fromJson(json, Movie.class);
            return movie;
        }else return null;
    }

    private void saveOfflineMovie(String name, Movie movie){
        Gson gson = new Gson();
        SharedPreferences.Editor myPrefsEdit = myPrefsMovie.edit();
        String json = gson.toJson(movie, Movie.class);
        myPrefsEdit.putString(name, json);
        myPrefsEdit.commit();
    }

    private void getMovieDetailPlus(String connection_type, Movie movie){
        md_title.setText(movie.getTitle());
        md_overviewlabel.setVisibility(View.VISIBLE);
        md_overview.setText(movie.getOverview());
        md_rating.setVisibility(View.VISIBLE);
        md_rating.setRating(movie.getRating() / 2);
        getGenres(movie);
        md_releasedate.setText(movie.getReleaseDate());
        if (!isFinishing()) {
            Glide.with(MovieDetailActivity.this)
                    .load(POSTER_URL + movie.getBackdrop())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(md_backdrop);
        }
        if(connection_type.equals("online")) saveOfflineMovie(id_movie+"", movie);
    }

    private void getMovieDetail() {
        Movie movie_check = checkNgetMovie();
        if(movie_check != null){
            getMovieDetailPlus("offline", movie_check);
            return;
        }
        moviesRepo.getMovie(id_movie, new CallbackMovie() {
            @Override
            public void onSuccess(Movie movie) {
                getMovieDetailPlus("online", movie);
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getGenres(final Movie movie) {
        if (movie.getGenres() != null) {
            List<String> movie_genres = new ArrayList<>();
            for (Genre genre : movie.getGenres()) {
                movie_genres.add(genre.getName());
            }
            md_genres.setText(TextUtils.join(", ", movie_genres));
        }
        /*
        moviesRepo.getGenres(new CallbackGenres() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> movie_genres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        movie_genres.add(genre.getName());
                    }
                    md_genres.setText(TextUtils.join(", ", movie_genres));
                }
            }

            @Override
            public void onError() {
                errorToast();
            }
        });*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void errorToast() {
        Toast.makeText(MovieDetailActivity.this, "Verify your internet connection !", Toast.LENGTH_SHORT).show();
    }
}
