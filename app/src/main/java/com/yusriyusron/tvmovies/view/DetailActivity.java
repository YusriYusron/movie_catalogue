package com.yusriyusron.tvmovies.view;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.model.Movie;
import com.yusriyusron.tvmovies.model.TvShow;
import com.yusriyusron.tvmovies.model.database.DbHelper;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG_ID = "id";
    public static final String TAG_POSTER = "poster";
    public static final String TAG_TITLE = "title";
    public static final String TAG_OVERVIEW = "overview";

    public static String MOVIES = "MOVIES";
    public static String TV_SHOWS = "TV_SHOWS";

    private Boolean isFavorite = true;
    private Menu menu;

    private Movie movies;
    private TvShow tvShows;

    private DbHelper helper;

    private Uri uriWithId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView imageView = findViewById(R.id.image_movies);
        TextView textViewTitle = findViewById(R.id.title_movies);
        TextView textViewOverview = findViewById(R.id.overview_movies);
        TextView overview = findViewById(R.id.overview);

        overview.setText(R.string.overview);
        helper = new DbHelper(this);

        movies = getIntent().getParcelableExtra(MOVIES);
        tvShows = getIntent().getParcelableExtra(TV_SHOWS);

        if (movies != null){
            Glide.with(this).load(movies.getImageMovie()).override(150,150).into(imageView);
            textViewTitle.setText(movies.getTitleMovie());
            textViewOverview.setText(movies.getOverviewMovie());
        }else {
            Glide.with(this).load(tvShows.getImageTvShow()).override(150,150).into(imageView);
            textViewTitle.setText(tvShows.getTitleTvShow());
            textViewOverview.setText(tvShows.getOverviewTvShow());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            makeIntent();
        }else if (item.getItemId() == R.id.menu_favorite){
            if (isFavorite){
                Toast.makeText(this, "Remove from favorite", Toast.LENGTH_SHORT).show();
                setIconFavorite();
                removeFromDatabase();
                isFavorite = false;
            }else {
                Toast.makeText(this, "Save to favorite", Toast.LENGTH_SHORT).show();
                setIconFavorite();
                saveToDatabase();
                isFavorite = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeIntent(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        makeIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite,menu);
        this.menu = menu;
        if (isFavorite.equals(false)){
            setIconFavorite();
        }else {
            isFavorite();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setIconFavorite(){
        if (isFavorite){
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_favorite));
            isFavorite = false;
        }else {
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_favorite_filled));
            isFavorite = true;
        }
    }

    private void isFavorite(){
        if (movies != null){
            selectFromDatabase(movies.getId());
        }else {
            selectFromDatabase(tvShows.getId());
        }
    }

    private void selectFromDatabase(String id){
        if (helper.selectById(id)){
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_favorite_filled));
            isFavorite = true;
        }else {
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_favorite));
            isFavorite = false;
        }
    }

    public void saveToDatabase(){
        ContentValues values = new ContentValues();
        if (movies == null){
//            helper.insert(tvShows.getId(),tvShows.getImageTvShow(),tvShows.getTitleTvShow(),tvShows.getOverviewTvShow());
            values.put(DbHelper.COLUMN_ID,tvShows.getId());
            values.put(DbHelper.COLUMN_POSTER,tvShows.getImageTvShow());
            values.put(DbHelper.COLUMN_TITLE,tvShows.getTitleTvShow());
            values.put(DbHelper.COLUMN_OVERVIEW,tvShows.getOverviewTvShow());
            getContentResolver().insert(DbHelper.CONTENT_URI,values);
        }else if (tvShows == null){
//            helper.insert(movies.getId(),movies.getImageMovie(),movies.getTitleMovie(),movies.getOverviewMovie());
            values.put(DbHelper.COLUMN_ID,movies.getId());
            values.put(DbHelper.COLUMN_POSTER,movies.getImageMovie());
            values.put(DbHelper.COLUMN_TITLE,movies.getTitleMovie());
            values.put(DbHelper.COLUMN_OVERVIEW,movies.getOverviewMovie());
            getContentResolver().insert(DbHelper.CONTENT_URI,values);
        }
    }

    private void removeFromDatabase(){
        if (movies == null){
//            helper.delete(tvShows.getId());
            getContentResolver().delete(DbHelper.CONTENT_URI,DbHelper.COLUMN_ID+"="+tvShows.getId(),null);
        }else if (tvShows == null){
//            helper.delete(movies.getId());
            getContentResolver().delete(DbHelper.CONTENT_URI,DbHelper.COLUMN_ID+"="+movies.getId(),null);
        }
    }
}
