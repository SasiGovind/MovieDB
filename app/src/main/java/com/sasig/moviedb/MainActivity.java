package com.sasig.moviedb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView movies_list;
    private AdapterMovies adapter;
    private MoviesRepo moviesRepo;
    private BottomNavigationView bottomNavigationView;

    SharedPreferences myPrefs;

    private List<Genre> genres_list;
    private String sortBy = MoviesRepo.POPULAR;
    private boolean moviesFetching;
    private int currentPage = 1;

    CallbackMoviesClick callbackMoviesClick = new CallbackMoviesClick() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.ID_MOVIE, movie.getId());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());//getBaseContext().getSharedPreferences("", MODE_PRIVATE);//getPreferences(MODE_PRIVATE);

        moviesRepo = MoviesRepo.getInstance();
        movies_list = findViewById(R.id.movies);
        movies_list.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.activity_main_bottom_navigation);

        this.configureBottomNavigationView();

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

    private void errorToast() {
        Toast.makeText(MainActivity.this, "Verify your internet connection !", Toast.LENGTH_SHORT).show();
    }
}