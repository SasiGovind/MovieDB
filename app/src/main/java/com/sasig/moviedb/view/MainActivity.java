package com.sasig.moviedb.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sasig.moviedb.R;
import com.sasig.moviedb.controller.AdapterMovies;
import com.sasig.moviedb.controller.CallbackGenres;
import com.sasig.moviedb.controller.CallbackMovies;
import com.sasig.moviedb.controller.CallbackMoviesClick;
import com.sasig.moviedb.controller.MoviesRepo;
import com.sasig.moviedb.model.Genre;
import com.sasig.moviedb.model.Movie;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView movies_list;
    private AdapterMovies adapter;
    private MoviesRepo moviesRepo;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefresh;

    SharedPreferences myPrefs;

    private List<Genre> genres_list;
    private String sortBy = MoviesRepo.POPULAR;
    private boolean moviesFetching;
    private int currentPage = 1;
    private Activity act;


    CallbackMoviesClick callbackMoviesClick = new CallbackMoviesClick() {
        @Override
        public void onClick(TextView v1, TextView v2, TextView v3, ImageView v4, Movie movie) {

            /*Pair<View, String>[] pair1 = new Pair[2];
            pair1[0] = new Pair<View, String>(v, "movie_title_shared");*/
            Pair<View, String> p1 = Pair.create((View)v1, "movie_title_shared");
            Pair<View, String> p2 = Pair.create((View)v2, "genres_shared");
            Pair<View, String> p3 = Pair.create((View)v3, "year_shared");
            Pair<View, String> p4 = Pair.create((View)v4, "poster_shared");
            //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pair1);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, p1, p2, p3, p4);
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.ID_MOVIE, movie.getId());
            startActivity(intent, options.toBundle());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());//getBaseContext().getSharedPreferences("", MODE_PRIVATE);//getPreferences(MODE_PRIVATE);

        moviesRepo = MoviesRepo.getInstance();
        movies_list = findViewById(R.id.movies);
        movies_list.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.activity_main_bottom_navigation);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        this.configureBottomNavigationView();
        configureRefreshView();
        scrollListener();
        getGenres();
    }

    private void configureBottomNavigationView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    private Boolean updateMainFragment(Integer integer){
        currentPage = 1;
        switch (integer) {
            case R.id.popular:
                sortBy = MoviesRepo.POPULAR;
                getMovies(currentPage);
                return true;
            case R.id.top_rated:
                sortBy = MoviesRepo.TOP_RATED;
                getMovies(currentPage);
                return true;
            case R.id.upcoming:
                sortBy = MoviesRepo.UPCOMING;
                getMovies(currentPage);
                return true;
            default:
                return false;
        }
    }

    private void configureRefreshView(){
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                adapter.clearMovies();
                SharedPreferences.Editor editor = myPrefs.edit();

                Map<String, ?> allEntries = myPrefs.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                    if(entry.getKey().startsWith(sortBy) || entry.getKey().startsWith("genres")){
                        editor.remove(entry.getKey());
                        editor.apply();
                    }
                }
                getGenres();
                //swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void scrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        movies_list.setLayoutManager(manager);
        movies_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItems = manager.getItemCount();
                int visibleItems = manager.getChildCount();
                int firstScreenItem = manager.findFirstVisibleItemPosition();

                if (firstScreenItem + visibleItems >= totalItems / 2) {
                    if (!moviesFetching) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private List checkNget(String key){
        if(myPrefs.contains(key)){
            Gson gson = new Gson();
            String json = myPrefs.getString(key, "");
            Type listType = null;
            if(key.equals("genres"))listType = new TypeToken<List<Genre>>(){}.getType();
            else listType = new TypeToken<List<Movie>>(){}.getType();

            List list = gson.fromJson(json, listType);

            return list;
        }else return null;
    }

    private void saveOffline(String name, List list){
        Gson gson = new Gson();
        SharedPreferences.Editor myPrefsEdit = myPrefs.edit();
        Type listType = null;
        if(name.equals("genres"))listType = new TypeToken<List<Genre>>(){}.getType();
        else listType = new TypeToken<List<Movie>>(){}.getType();

        String json = gson.toJson(list, listType);
        myPrefsEdit.putString(name, json);
        myPrefsEdit.commit();
    }

    private void getGenres() {
        List<Genre> genres_check = checkNget("genres");
        if(genres_check != null){
            genres_list = genres_check;
            getMovies(currentPage);
            return;
        }
        moviesRepo.getGenres(new CallbackGenres() {
            @Override
            public void onSuccess(List<Genre> genres) {
                genres_list = genres;
                getMovies(currentPage);
                saveOffline("genres", genres);
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private void getMoviePlus(String connection_type, List moviesList, int page){
        if (adapter == null) {
            adapter = new AdapterMovies(moviesList, genres_list, callbackMoviesClick);
            movies_list.setAdapter(adapter);
        } else {
            if (page == 1) {
                adapter.clearMovies();
            }
            adapter.addMovies(moviesList);
        }
        if(connection_type.equals("online")) saveOffline(sortBy+"_"+page, moviesList);
        Toast.makeText(MainActivity.this, "Pages loaded : "+page, Toast.LENGTH_SHORT).show();
        currentPage = page;
        moviesFetching = false;
        setAppTitle();
        swipeRefresh.setRefreshing(false);
    }

    private void getMovies(int page) {
        moviesFetching = true;

        List<Movie> movies_check = checkNget(sortBy+"_"+page);
        if(movies_check != null){
            getMoviePlus("offline", movies_check, page);
            return;
        }

        moviesRepo.getMovies(page, sortBy, new CallbackMovies() {
            @Override
            public void onSuccess(List<Movie> movies, int page) {
                getMoviePlus("online", movies, page);
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private void setAppTitle() {
        switch (sortBy) {
            case MoviesRepo.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepo.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case MoviesRepo.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
        }
    }

    @Override
    public void onBackPressed() {

        //ViewGroup viewGroup = findViewById(android.R.id.content);
        //View dialogView = LayoutInflater.from(this).inflate(R.layout.exit_dialog, viewGroup, false);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        final Button buttonOk = (Button) dialogView.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "See you soon :D", Toast.LENGTH_SHORT).show();
                MainActivity.super.onBackPressed();
            }
        });
        final Button buttonNo = (Button) dialogView.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                Toast.makeText(MainActivity.this, "You the BOSS", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("I saw you quitting this app ?").setCancelable(false)
                .setPositiveButton("Yeah I'll come later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "See you soon :D", Toast.LENGTH_SHORT).show();
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Nope I will stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(MainActivity.this, "You the BOSS", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        */

        //super.onBackPressed();
    }

    private void errorToast() {
        Toast.makeText(MainActivity.this, "Verify your internet connection !", Toast.LENGTH_SHORT).show();
    }
}