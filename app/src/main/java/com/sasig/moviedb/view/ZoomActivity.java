package com.sasig.moviedb.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sasig.moviedb.R;

public class ZoomActivity extends AppCompatActivity {

    private static String POSTER_URL = "https://image.tmdb.org/t/p/w780";
    private ImageView expanded_poster;
    private String poster_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        poster_path = getIntent().getStringExtra("poster_path");
        expanded_poster = findViewById(R.id.expanded_poster);

        if (!isFinishing()) {
            Glide.with(ZoomActivity.this)
                    .load(POSTER_URL + poster_path)
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(expanded_poster);
        }

    }
}
