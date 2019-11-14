package com.yusriyusron.tvmovies.model.session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static final String EXTRA_TVMOVIES = "extra_tvmovies";
    private static final String EXTRA_DAILY_REMINDER = "extra_daily_reminder";
    private static final String EXTRA_RELEASE_REMINDER = "extra_release_reminder";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Session(Context context) {
        sharedPreferences = context.getSharedPreferences(EXTRA_TVMOVIES,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDailyReminder(boolean dailyReminder){
        editor.putBoolean(EXTRA_DAILY_REMINDER,dailyReminder);
        editor.commit();
    }

    public void setReleaseReminder(boolean releaseReminder){
        editor.putBoolean(EXTRA_RELEASE_REMINDER,releaseReminder);
        editor.commit();
    }

    public Boolean getDailyReminder(){
        return sharedPreferences.getBoolean(EXTRA_DAILY_REMINDER,false);
    }

    public Boolean getReleaseReminder(){
        return sharedPreferences.getBoolean(EXTRA_RELEASE_REMINDER,false);
    }
}
