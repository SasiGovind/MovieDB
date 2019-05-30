package com.sasig.moviedb;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView movies_list;
    private AdapterMovies adapter;
    private MoviesRepo moviesRepo;
    private BottomNavigationView bottomNavigationView;

    private List<Genre> genres_list;
    private String sortBy = MoviesRepo.POPULAR;
    private boolean moviesFetching;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    private void getGenres() {
        moviesRepo.getGenres(new CallbackGenres() {
            @Override
            public void onSuccess(List<Genre> genres) {
                genres_list = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private void getMovies(int page) {
        moviesFetching = true;
        moviesRepo.getMovies(page, sortBy, new CallbackMovies() {
            @Override
            public void onSuccess(List<Movie> movies, int page) {
                if (adapter == null) {
                    adapter = new AdapterMovies(movies, genres_list);
                    movies_list.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.addMovies(movies);
                }
                currentPage = page;
                moviesFetching = false;
                setAppTitle();
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
