package com.sasig.moviedb.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sasig.moviedb.R;
import com.sasig.moviedb.model.People;

public class PeopleActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        Intent movieDetails = getIntent();
        People people = (People) movieDetails.getSerializableExtra("people");
        String movie_title = movieDetails.getStringExtra("movie_title");
        displayPeople(people, movie_title);

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void displayPeople (People people, String movie_title) {
        PeopleFragment peopleFragment = new PeopleFragment();

        Bundle bundle = new Bundle();
        bundle.putString("name", people.getActorName());
        bundle.putString("biography", people.getBiography());
        bundle.putString("picture", people.getProfileImagePath());
        bundle.putString("birthday", people.getBirthday());
        bundle.putString("birthplace", people.getPlaceOfBirth());
        bundle.putString("popularity", people.getPopularity());
        bundle.putString("movie", movie_title);
        peopleFragment.setArguments(bundle);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, peopleFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
