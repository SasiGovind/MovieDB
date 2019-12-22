package com.sasig.moviedb.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView movies_list;
    private AdapterMovies adapter;
    private MoviesRepo moviesRepo;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefresh;
    private SearchView searchView;
    private MenuItem menuItem;
    private Menu menuOptions;

    SharedPreferences myPrefs;

    private List<Genre> genres_list;
    private List<Genre> genres_keep_list;
    private String sortBy = MoviesRepo.POPULAR;
    private String lastSearchMovie = "";
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
            intent.putExtra("lang", moviesRepo.LANG);
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
        // GetGenres(retrieve genre and movies) = CALLED ON onCreateOptionsMenu(Menu menu)

        //// ALERT : BE CAREFUL AT THIS MAY CAUSE APP CRASH WITHOUT EXPLICATIONS (PLACER A LA FIN EXPRES)
        bottomNavigationView.getMenu().findItem(R.id.popular).setChecked(true);//setSelectedItemId(R.id.top_rated);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Search Movie
        getMenuInflater().inflate(R.menu.movie_main_options, menu);
        menuItem = menu.findItem(R.id.movie_search);
        searchView = (SearchView) menuItem.getActionView();

        //searchView.setQueryHint("Search movie");
        /*menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) { // work
                Toast.makeText(MainActivity.this, "Test !", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() { //doesn't work
            @Override
            public boolean onClose() {
                Toast.makeText(MainActivity.this, "Close !", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/
        searchView.setOnQueryTextListener(this);
        menuOptions = menu;
        getGenres(true); // Called here because it requires menuOptions to be setted
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.english:
            if(MoviesRepo.LANG != "en-US"){
                item.setChecked(true);
                moviesRepo.setLANG("en-US");
                manualRefresh();
            }
                return true;
            case R.id.french:
            if(MoviesRepo.LANG != "fr-FR"){
                item.setChecked(true);
                moviesRepo.setLANG("fr-FR");
                manualRefresh();
            }
                return true;
            case R.id.cache_erase:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cache_erase();
                                Toast.makeText(MainActivity.this, "All offline storage erased !", Toast.LENGTH_SHORT).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure to erase all stored data ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            case R.id.select_all:
                if(menuOptions != null){
                    SubMenu genresMenu = menuOptions.findItem(R.id.action_genres_selection).getSubMenu();
                    for (int i = 0; i < genresMenu.size(); i++){
                        genresMenu.getItem(i).setChecked(true);
                        genres_keep_list = new ArrayList<>(genres_list);
                    }
                    manualRefresh();
                }
                return true;
            case R.id.deselect_all:
                if(menuOptions != null){
                    SubMenu genresMenu = menuOptions.findItem(R.id.action_genres_selection).getSubMenu();
                    for (int i = 0; i < genresMenu.size(); i++){
                        genresMenu.getItem(i).setChecked(false);
                        genres_keep_list = new ArrayList<>();
                    }
                    manualRefresh();
                }
                return true;
        }
        // genres checking
        int itemId = item.getItemId();
        for(Genre g : genres_list){
            if(itemId == g.getId()) {
                item.setChecked(!item.isChecked());
                if(item.isChecked()) genres_keep_list.add(g);
                else genres_keep_list.remove(g);
                manualRefresh();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Toast.makeText(MainActivity.this, "Verify : "+query, Toast.LENGTH_SHORT).show();
        lastSearchMovie = query;
        getSearchMovies(lastSearchMovie, 1);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        lastSearchMovie = query;
        return false;
    }

    private void configureBottomNavigationView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item));
    }

    private Boolean updateMainFragment(MenuItem item){
        Integer integer = item.getItemId();
        if(integer != R.id.previous && integer != R.id.next)currentPage = 1;
        switch (integer) {
            case R.id.previous:
                navigateOnPage(--currentPage);
                return false;
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
            case R.id.next:
                navigateOnPage(++currentPage);
                return false;
            default:
                return false;
        }
    }

    private void configureRefreshView(){
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clear_searchview();
                manualRefresh();
            }
        });
    }

    private void clear_searchview() {
        searchView.onActionViewExpanded();
        searchView.setQuery("", false);
        lastSearchMovie = "";
        menuItem.collapseActionView(); //searchView.onActionViewCollapsed(); //searchView.clearFocus();
    }

    private void clearOfflineCurrentSortbyEntriesIfOnline(){
        // optimise manualRefresh, cache_erase, navigate_pages
    }

    private void manualRefresh(){
        currentPage = 1;
        adapter.clearMovies();
        SharedPreferences.Editor editor = myPrefs.edit();
        Map<String, ?> allEntries = myPrefs.getAll();
        if(isOnline()){
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                if(entry.getKey().startsWith(sortBy)){ //if(entry.getKey().startsWith(sortBy) || entry.getKey().startsWith("genres")){
                    editor.remove(entry.getKey());
                    editor.apply();
                }
            }
        }
        /*if(resetGenres && myPrefs.contains("genres")){
            editor.remove("genres");
            editor.commit();
            getGenres();
        }else */
        getMovies(currentPage);//getGenres();//swipeRefresh.setRefreshing(false);
    }

    private void cache_erase() {
        currentPage = 1;
        adapter.clearMovies();
        SharedPreferences.Editor editor = myPrefs.edit();
        Map<String, ?> allEntries = myPrefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                editor.remove(entry.getKey());
                editor.apply();
        }
        //getGenres();
    }

    private void navigateOnPage(int page){ // Not compatible with offline storage
        currentPage = (page > 0)?page:1;
        adapter.clearMovies();
        SharedPreferences.Editor editor = myPrefs.edit();
        Map<String, ?> allEntries = myPrefs.getAll();
        if(isOnline()){
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                if(entry.getKey().startsWith(sortBy)){//if(entry.getKey().startsWith(sortBy) || entry.getKey().startsWith("genres")){
                    editor.remove(entry.getKey());
                    editor.apply();
                }
            }
        }
        getMovies(currentPage);
    }

    private void scrollListener() {
/*        if(searchView != null && searchView.isIconified()){
            Toast.makeText(MainActivity.this, "Open !", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(MainActivity.this, "Closed !", Toast.LENGTH_SHORT).show();*/
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        movies_list.setLayoutManager(manager);
        movies_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItems = manager.getItemCount();
                int visibleItems = manager.getChildCount();
                int firstScreenItem = manager.findFirstVisibleItemPosition();
                int newPageLimit;
                if(totalItems < 40) newPageLimit = totalItems / 2;
                else newPageLimit = totalItems-20;
                //Toast.makeText(MainActivity.this, "Scroll : "+firstScreenItem+" + "+visibleItems+" >= "+newPageLimit, Toast.LENGTH_SHORT).show();
                if (firstScreenItem + visibleItems >= newPageLimit) {
                    if (!moviesFetching) {
                        if(lastSearchMovie!="") getSearchMovies(lastSearchMovie, currentPage + 1);
                        else getMovies(currentPage + 1);
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

    private void setGenres(List<Genre> genres){
        genres_list = genres;
        genres_keep_list = new ArrayList<>(genres_list);
        genres_setup(menuOptions);
    }

    private void genres_setup(Menu menuOptions){
        if(menuOptions != null){
            SubMenu genresMenu = menuOptions.findItem(R.id.action_genres_selection).getSubMenu();
            if(genres_list == null) {
                while (genres_list == null){}
            }
            for (int i = 0; i < genres_list.size(); i++){
                Genre genre = genres_list.get(i);
                genresMenu.add(Menu.NONE, genre.getId(), Menu.NONE, genre.getName()).setCheckable(true).setChecked(true);
            }
        }
    }

    private void getGenres(boolean movieCall) {
        List<Genre> genres_check = checkNget("genres");
        if(genres_check != null){
            setGenres(genres_check);
            if(movieCall) getMovies(currentPage);
            return;
        }
        moviesRepo.getGenres(new CallbackGenres() {
            @Override
            public void onSuccess(List<Genre> genres) {
                setGenres(genres);
                if(movieCall) getMovies(currentPage);
                saveOffline("genres", genres);
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private List<Movie> remove_unwanted_movies(List<Movie> moviesList){
        List<Movie> movies = new ArrayList<Movie>(moviesList);
        List<Integer> ids = new ArrayList<>();
        for(Genre g : genres_keep_list) ids.add(g.getId());
        for(Movie movie : movies){
            boolean keep_movie = false;
            List<Integer> genresId = movie.getGenreIds();
            for(Integer i : genresId){
                if(ids.contains(i)) {
                    keep_movie = true;
                    continue;
                }
                /*boolean nextId = false;
                for(Genre g : genres_keep_list){
                    if(i == g.getId()){
                        moviesList.remove(movie);
                        next = true;
                        nextId = true;
                        break;
                    }
                }
                if(nextId) break;*/
            }
            if(!keep_movie) moviesList.remove(movie);
            //if(next) continue;
        }
        return moviesList;
    }

    private void getMoviePlus(String connection_type, List<Movie> moviesList, int page){
        moviesList = remove_unwanted_movies(moviesList);

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
            public void onSuccess(List<Movie> movies, int page, int totalPages) {
                if(page > totalPages){
                    Toast.makeText(MainActivity.this, "Total "+totalPages+" pages loaded", Toast.LENGTH_SHORT).show();
                    //moviesFetching = false; //enable this to display total loaded toast each time user reach the limit
                    return;
                }
                getMoviePlus("online", movies, page);
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private void getSearchMovies(String query, int page) {
        moviesFetching = true;

        moviesRepo.getSearchMovies(query, page, new CallbackMovies() {
            @Override
            public void onSuccess(List<Movie> movies, int page, int totalPages) {
                if(page > totalPages){
                    if(totalPages == 0) Toast.makeText(MainActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MainActivity.this, "Total "+totalPages+" pages loaded", Toast.LENGTH_SHORT).show();
                    //moviesFetching = false; //enable this to display total loaded toast each time user reach the limit
                    return;
                }
                if (adapter == null) {
                    adapter = new AdapterMovies(movies, genres_list, callbackMoviesClick);
                    movies_list.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.addMovies(movies);
                }
                Toast.makeText(MainActivity.this, "Pages loaded : "+page, Toast.LENGTH_SHORT).show();
                currentPage = page;
                moviesFetching = false;
                //setAppTitle();
                swipeRefresh.setRefreshing(false);
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void errorToast() {
        Toast.makeText(MainActivity.this, "Verify your internet connection !", Toast.LENGTH_SHORT).show();
    }
}