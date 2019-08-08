package com.yusriyusron.tvmovies.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.controller.adapter.ContentAdapter;
import com.yusriyusron.tvmovies.controller.adapter.MoviesAdapter;
import com.yusriyusron.tvmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> listMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ContentAdapter contentAdapter = new ContentAdapter(this,getSupportFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(contentAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.navigation_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchQuery(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navigation_settings){
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchQuery(String query){
        listMovie = new ArrayList<>();
        String language = "";
        if (getSupportActionBar() != null){
            if (getSupportActionBar().getTitle().equals("TV Movies")){
                language = "en-US";
            }else if (getSupportActionBar().getTitle().equals("TV dan Film")){
                language = "id-ID";
            }
        }

        String url = "https://api.themoviedb.org/3/search/movie?api_key=c1c287ec318c5d212f5d101e39ed3220&language="+language+"&query="+query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject results = jsonArray.getJSONObject(i);

                        String id = results.getString("id");
                        String title = results.getString("title");
                        String overview = results.getString("overview");
                        String image = results.getString("poster_path");

//                        Movie movie = new Movie(id,"https://image.tmdb.org/t/p/w185"+image,title,overview);
//                        listMovie.add(movie);
                        Log.e("MOVIE",title);
                    }

//                    progressBar.setVisibility(View.GONE);
//
//                    MoviesAdapter adapter = new MoviesAdapter(getContext(),listMovie);
//                    recyclerView.setAdapter(adapter);
//
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    recyclerView.setLayoutManager(layoutManager);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
