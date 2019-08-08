package com.yusriyusron.tvmovies.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.model.TvShow;
import com.yusriyusron.tvmovies.view.DetailActivity;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {

    private Context context;
    private ArrayList<TvShow> listTvShow;

    private TvShow tvShows;

    public TvShowAdapter(Context context, ArrayList<TvShow> listTvShow) {
        this.context = context;
        this.listTvShow = listTvShow;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_movies_tv_item,viewGroup,false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder tvShowViewHolder, final int i) {
        TvShow tvShow = listTvShow.get(i);

        Glide.with(context)
                .load(tvShow.getImageTvShow())
                .override(150,150)
                .into(tvShowViewHolder.imageViewTvShow);

        tvShowViewHolder.textViewTitleTvShow.setText(tvShow.getTitleTvShow());
        tvShowViewHolder.textViewOverviewTvShow.setText(tvShow.getOverviewTvShow());

        tvShowViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvShows = listTvShow.get(i);
                makeIntent(DetailActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTvShow.size();
    }

    public class TvShowViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewTvShow;
        final TextView textViewTitleTvShow;
        final TextView textViewOverviewTvShow;

        public TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewTvShow = itemView.findViewById(R.id.image_movies);
            textViewTitleTvShow = itemView.findViewById(R.id.title_movie);
            textViewOverviewTvShow = itemView.findViewById(R.id.overview_movie);
        }
    }

    private void makeIntent(Class destination){
        Intent intent = new Intent(context,destination);
        intent.putExtra(DetailActivity.TV_SHOWS,tvShows);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
