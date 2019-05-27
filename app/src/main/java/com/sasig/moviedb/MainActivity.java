package com.sasig.moviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView movies_list;
    private AdapterMovies adapter;
    private MoviesRepo moviesRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRepo = MoviesRepo.getInstance();
        movies_list = findViewById(R.id.movies);
        movies_list.setLayoutManager(new LinearLayoutManager(this));

        getGenres();

    }

    private void getGenres() {
        moviesRepo.getGenres(new CallbackGenres() {
            @Override
            public void onSuccess(List<Genre> genres) {
                getMovies(genres);
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private void getMovies(final List<Genre> genres) {
        moviesRepo.getMovies(new CallbackMovies() {
            @Override
            public void onSuccess(List<Movie> movies) {
                adapter = new AdapterMovies(movies, genres);
                movies_list.setAdapter(adapter);
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private void errorToast() {
        Toast.makeText(MainActivity.this, "Verify your internet connection !", Toast.LENGTH_SHORT).show();
    }
}
