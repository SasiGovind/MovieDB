package com.sasig.moviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView movies_list;
    private AdapterMovies adapter;
    private MoviesRepo moviesRepo;

    private List<Genre> genres_list;
    private boolean moviesFetching;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRepo = MoviesRepo.getInstance();
        movies_list = findViewById(R.id.movies);
        movies_list.setLayoutManager(new LinearLayoutManager(this));

        scrollListener();
        getGenres();

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
        moviesRepo.getMovies(new CallbackMovies() {
            @Override
            public void onSuccess(List<Movie> movies,int page) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new AdapterMovies(movies, genres_list);
                    movies_list.setAdapter(adapter);
                } else {
                    adapter.addMovies(movies);
                }
                currentPage = page;
                moviesFetching = false;
            }

            @Override
            public void onError() {
                errorToast();
            }
        },page);
    }

    private void errorToast() {
        Toast.makeText(MainActivity.this, "Verify your internet connection !", Toast.LENGTH_SHORT).show();
    }
}
