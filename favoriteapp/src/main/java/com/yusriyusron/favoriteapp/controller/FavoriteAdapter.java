package com.yusriyusron.favoriteapp.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yusriyusron.favoriteapp.R;
import com.yusriyusron.favoriteapp.model.Movie;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private ArrayList<Movie> listMovie;

    private Movie movies;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public FavoriteAdapter(Context context, ArrayList<Movie> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {
        this.listMovie = listMovie;
    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_movies_tv_item,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, final int position) {
        final Movie movie = listMovie.get(position);

        Glide.with(context)
                .load(movie.getImageMovie())
                .override(150,150)
                .into(holder.imageViewMovie);
        holder.textViewTitleMovie.setText(movie.getTitleMovie());
        holder.textViewOverviewMovie.setText(movie.getOverviewMovie());
        
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewMovie;
        final TextView textViewTitleMovie;
        final TextView textViewOverviewMovie;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMovie = itemView.findViewById(R.id.image_movies);
            textViewTitleMovie = itemView.findViewById(R.id.title_movie);
            textViewOverviewMovie = itemView.findViewById(R.id.overview_movie);
        }
    }
}
