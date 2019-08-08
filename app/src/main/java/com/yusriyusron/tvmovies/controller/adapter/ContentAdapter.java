package com.yusriyusron.tvmovies.controller.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.controller.fragment.FavoriteFragment;
import com.yusriyusron.tvmovies.controller.fragment.MoviesFragment;
import com.yusriyusron.tvmovies.controller.fragment.TvShowsFragment;

public class ContentAdapter extends FragmentPagerAdapter {

    private Context context;

    public ContentAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0){
            fragment = new MoviesFragment();
        }else if (position == 1){
            fragment = new TvShowsFragment();
        }else {
            fragment = new FavoriteFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.title_movie);
            case 1:
                return context.getResources().getString(R.string.title_tv);
            case 2:
                return context.getResources().getString(R.string.favorite);
        }
        return null;
    }
}
