package com.sasig.moviedb.view;

import android.animation.ValueAnimator;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sasig.moviedb.R;
import com.sasig.moviedb.controller.CallbackMovie;
import com.sasig.moviedb.controller.CallbackTrailers;
import com.sasig.moviedb.controller.MoviesRepo;
import com.sasig.moviedb.model.Genre;
import com.sasig.moviedb.model.Movie;
import com.sasig.moviedb.model.Trailer;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    public static String ID_MOVIE = "movie_id";

    private static String BACKDROP_URL = "https://image.tmdb.org/t/p/w780";
    private static String POSTER_URL = "https://image.tmdb.org/t/p/w500";
    private static String YT_URL = "https://www.youtube.com/watch?v=%s";
    private static String YT_THUMB_URL = "https://img.youtube.com/vi/%s/0.jpg";

    private ImageView md_backdrop;
    private ImageView md_poster;
    private TextView md_title;
    private TextView md_genres;
    private TextView md_overview;
    private TextView md_overviewlabel;
    private TextView md_releasedate;
    private RatingBar md_rating;
    private TextView md_trailersLabel;
    private LinearLayout md_trailers;
    private LinearLayout md_reviews;

    private MoviesRepo moviesRepo;
    private int id_movie;

    SharedPreferences myPrefsMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        myPrefsMovie = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        id_movie = getIntent().getIntExtra(ID_MOVIE, id_movie);

        Log.d("MoviesID", "Movie id = "+id_movie);

        moviesRepo = MoviesRepo.getInstance();

        configureToolbar();
        gatherMovieUI();
        getMovieDetail();
        animTitleOn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                StringBuilder text = new StringBuilder();
                text.append("Hi, i'm sharing some movie details.");
                text.append("\n\nMovie : "+ md_title.getText()+".");
                text.append("\n\nRating : "+ md_rating.getRating()+"/5.");
                text.append("\n\nRelease Date : "+ md_releasedate.getText()+".");
                text.append("\n\nOverview : "+md_overview.getText());
                text.append("\n\nHave a wonderful day :)");
                shareIntent.putExtra(Intent.EXTRA_TEXT, text.toString());
                startActivity(shareIntent);
                return true;
            case R.id.web_search:
                String query = "Movie "+md_title.getText()+" "+((String)md_releasedate.getText()).split("-")[0];
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void animTitleOn(){

        final float startSize = 18; // Size in pixels
        final float endSize = 30;
        long animationDuration = 300; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                md_title.setTextSize(animatedValue);
            }
        });

        animator.start();
    }

    private long animTitleOff(){

        final float startSize = 30; // Size in pixels
        final float endSize = 18;
        long animationDuration = 100; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                md_title.setTextSize(animatedValue);
            }
        });
        animator.start();
        return animationDuration;
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
        md_poster = findViewById(R.id.md_poster);
        md_title = findViewById(R.id.md_title);
        md_genres = findViewById(R.id.md_genres);
        md_overview = findViewById(R.id.md_overview);
        md_overviewlabel = findViewById(R.id.md_summarylabel);
        md_releasedate = findViewById(R.id.md_releasedate);
        md_rating = findViewById(R.id.md_rating);
        md_trailersLabel = findViewById(R.id.md_trailerslabel);
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
                    .load(BACKDROP_URL + movie.getBackdrop())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(md_backdrop);
        }
        if (!isFinishing()) {
            Glide.with(MovieDetailActivity.this)
                    .load(POSTER_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(md_poster);
        }
        getTrailers(movie);
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

    private void getTrailers(Movie movie) {
        moviesRepo.getTrailers(movie.getId(), new CallbackTrailers() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                if(!trailers.isEmpty()) md_trailersLabel.setVisibility(View.VISIBLE);
                md_trailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.thumb_trailer, md_trailers, false);
                    ImageView thumb_trailer_view = parent.findViewById(R.id.trailer_poster);
                    TextView trailer_title = parent.findViewById(R.id.trailer_name);
                    thumb_trailer_view.requestLayout();
                    thumb_trailer_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launchTrailer(String.format(YT_URL, trailer.getKey()));
                        }
                    });
                    trailer_title.setText(trailer.getTitle());
                    Glide.with(MovieDetailActivity.this)
                            .load(String.format(YT_THUMB_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumb_trailer_view);
                    md_trailers.addView(parent);
                }
            }

            @Override
            public void onError() {
                md_trailersLabel.setVisibility(View.GONE);
            }
        });
    }

    private void launchTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
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
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        long duration = animTitleOff();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAfterTransition();
            }
        }, duration/2);
        //onBackPressed();
        return true;
    }

    private void errorToast() {
        Toast.makeText(MovieDetailActivity.this, "Verify your internet connection !", Toast.LENGTH_SHORT).show();
    }
}
