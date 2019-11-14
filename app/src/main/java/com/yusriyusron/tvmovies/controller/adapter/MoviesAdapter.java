package com.yusriyusron.tvmovies.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.model.Movie;
import com.yusriyusron.tvmovies.view.DetailActivity;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesTvViewHolder> {

    private Context context;
    private ArrayList<Movie> listMovie;

    private Movie movies;

    public MoviesAdapter(Context context, ArrayList<Movie> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public MoviesTvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_movies_tv_item,viewGroup,false);
        return new MoviesTvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesTvViewHolder moviesTvViewHolder, final int i) {
        final Movie movie = listMovie.get(i);

        Glide.with(context)
                .load(movie.getImageMovie())
                .override(150,150)
                .into(moviesTvViewHolder.imageViewMovie);
        moviesTvViewHolder.textViewTitleMovie.setText(movie.getTitleMovie());
        moviesTvViewHolder.textViewOverviewMovie.setText(movie.getOverviewMovie());

        moviesTvViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movies = listMovie.get(i);
                makeIntent();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    class MoviesTvViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewMovie;
        final TextView textViewTitleMovie;
        final TextView textViewOverviewMovie;

        MoviesTvViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMovie = itemView.findViewById(R.id.image_movies);
            textViewTitleMovie = itemView.findViewById(R.id.title_movie);
            textViewOverviewMovie = itemView.findViewById(R.id.overview_movie);
        }
    }

    private void makeIntent(){
        Intent intent = new Intent(context,DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIES,movies);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
