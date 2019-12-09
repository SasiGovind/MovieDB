package com.sasig.moviedb.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sasig.moviedb.R;

public class PeopleFragment extends Fragment {

    View rootView;
    TextView fg_actorName;
    TextView fg_biography;
    ImageView fg_poster;
    TextView fg_birthday;
    TextView fg_birthplace;
    TextView fg_popularity;
    private static String POSTER_URL = "https://image.tmdb.org/t/p/w500";

    String actorName;
    String biography;
    String birthday;
    String birthplace;
    String popularity;
    String poster;
    String movie;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_people, container, false);
        initUi();
        //setHasOptionsMenu(true);
        return rootView;
    }

    private void initUi() {
        fg_actorName = (TextView) rootView.findViewById(R.id.fg_actorName);
        fg_biography = (TextView) rootView.findViewById(R.id.fg_bio);
        fg_poster = (ImageView) rootView.findViewById(R.id.fg_actorPoster);
        fg_birthday = (TextView) rootView.findViewById(R.id.fg_birthday);
        fg_birthplace = (TextView) rootView.findViewById(R.id.fg_birthplace);
        fg_popularity = (TextView) rootView.findViewById(R.id.fg_popularity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        actorName = bundle.getString("name");
        biography = bundle.getString("biography");
        poster = bundle.getString("picture");
        birthday = bundle.getString("birthday");
        birthplace = bundle.getString("birthplace");
        popularity = bundle.getString("popularity");
        movie = bundle.getString("movie");
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getActivity().getMenuInflater().inflate(R.menu.movie_detail_options, menu);
        inflater.inflate(R.menu.movie_detail_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        fg_actorName.setText(actorName);
        fg_biography.setText(biography);
        Glide.with(PeopleFragment.this)
                .load(POSTER_URL + poster)
                .apply(RequestOptions.placeholderOf(R.drawable.baseline_person_pin_24).centerCrop())
                .into(fg_poster);
        fg_birthday.setText(birthday);
        fg_birthplace.setText(birthplace);
        fg_popularity.setText(popularity);
        //Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        TextView toolbar_title = (TextView) rootView.findViewById(R.id.fg_toolbar_title);
        toolbar_title.setText(actorName);
        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = actorName+" in the movie "+movie;
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
            }
        });
    }

}
