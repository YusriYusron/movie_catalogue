package com.yusriyusron.tvmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.controller.receiver.DailyReceiver;
import com.yusriyusron.tvmovies.controller.receiver.ReleaseReceiver;
import com.yusriyusron.tvmovies.model.session.Session;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayoutLanguage;
    private Switch aSwitchReleaseReminder;
    private Switch aSwitchDailyReminder;
    private TextView textViewLocale;

    private Session session;

    private ReleaseReceiver mReleaseReceiver;
    private DailyReceiver mDailyReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.settings));
        }

        session = new Session(this);
        mReleaseReceiver = new ReleaseReceiver();
        mDailyReceiver = new DailyReceiver();

        linearLayoutLanguage = findViewById(R.id.linear_layout_language);
        aSwitchReleaseReminder = findViewById(R.id.switch_release_reminder);
        aSwitchDailyReminder = findViewById(R.id.switch_daily_reminder);
        textViewLocale = findViewById(R.id.text_view_locale);

        linearLayoutLanguage.setOnClickListener(this);
        aSwitchDailyReminder.setOnClickListener(this);
        aSwitchReleaseReminder.setOnClickListener(this);

        //getCurrent Locale
        getCurrentLocale();

        if (session.getDailyReminder().equals(true)){
            aSwitchDailyReminder.setChecked(true);
        }

        if (session.getReleaseReminder().equals(true)){
            aSwitchReleaseReminder.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            makeIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        makeIntent();
    }

    @Override
    public void onClick(View view) {
        if (view == linearLayoutLanguage) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        else if (view == aSwitchDailyReminder){
            if (session.getDailyReminder().equals(false)){
                session.setDailyReminder(true);
                aSwitchDailyReminder.setChecked(true);
                mDailyReceiver.setRepeating(this,7);
            }else if (session.getDailyReminder().equals(true)){
                session.setDailyReminder(false);
                aSwitchDailyReminder.setChecked(false);
            }
        }else if (view == aSwitchReleaseReminder){
            if (session.getReleaseReminder().equals(false)){
                session.setReleaseReminder(true);
                aSwitchReleaseReminder.setChecked(true);
//                mReleaseReceiver.setRepeating(this,8);
//                getReleaseReminder();
                mReleaseReceiver.setRepeating(this,8);
            }else if (session.getReleaseReminder().equals(true)){
                session.setReleaseReminder(false);
                aSwitchReleaseReminder.setChecked(false);
            }
        }
    }

    private void getCurrentLocale() {
        Locale locale = getResources().getConfiguration().locale;
        textViewLocale.setText(locale.getDisplayLanguage());
    }

//    private void getReleaseReminder() {
//        final ArrayList<Integer> listMovieId = new ArrayList<>();
//        final ArrayList<String> listMovieTitle = new ArrayList<>();
//
//        Date date = Calendar.getInstance().getTime();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String currentDate = dateFormat.format(date);
//
//        String url = "https://api.themoviedb.org/3/discover/movie?api_key=c1c287ec318c5d212f5d101e39ed3220&primary_release_date.gte="+currentDate+"&primary_release_date.lte="+currentDate;
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("results");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        int id = jsonObject.getInt("id");
//                        String title = jsonObject.getString("title");
//                        listMovieId.add(id);
//                        listMovieTitle.add(title);
//                        setDailyReminder(listMovieId,listMovieTitle);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonObjectRequest);
//    }
//
//    private void setDailyReminder(ArrayList id, ArrayList title) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,8);
//        calendar.set(Calendar.MINUTE,0);
//        calendar.set(Calendar.SECOND,0);
//
//        Intent intent = new Intent(getApplicationContext(), ReleaseReceiver.class);
//        intent.putExtra("id",id);
//        intent.putExtra("title",title);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
//    }
}
