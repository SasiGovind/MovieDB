package com.sasig.moviedb;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdapterMovies extends RecyclerView.Adapter<AdapterMovies.ViewHolderMovie > {

    private List<Movie> movies_list;
    private List<Genre> genres_list;

    public AdapterMovies(List<Movie> movies, List<Genre> genres) {
        this.movies_list = movies;
        this.genres_list = genres;
    }

    @Override
    public ViewHolderMovie onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolderMovie(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderMovie holder, int position) {
        holder.bind(movies_list.get(position));
    }

    @Override
    public int getItemCount() {
        return movies_list.size();
    }

    class ViewHolderMovie extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;

        public ViewHolderMovie(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
        }

        public void bind(Movie movie) {
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
        }

        private String getGenres(List<Integer> idsGenre) {
            List<String> genres_films = new ArrayList<>();
            for (Integer id_genre : idsGenre) {
                for (Genre genre : genres_list) {
                    if (genre.getId() == id_genre) {
                        genres_films.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", genres_films);
        }
    }
}