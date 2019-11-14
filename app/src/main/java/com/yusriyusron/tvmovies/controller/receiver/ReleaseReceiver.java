package com.yusriyusron.tvmovies.controller.receiver;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReleaseReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "channel_02";
    public static CharSequence CHANNEL_NAME = "tvmovies channel";

    private ArrayList<Integer> listRelease = new ArrayList<>();
    private ArrayList<String> listTitleRelease = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
//        showAlarmNotification(context,intent);
        getReleaseReminder(context);
//        listRelease = intent.getIntegerArrayListExtra("id");
//        ArrayList<String> listReleaseTitle = intent.getStringArrayListExtra("title");
//        for (int i = 0; i < listReleaseTitle.size() ; i++) {
//            buildNotification(context, listReleaseTitle.get(i),listRelease.get(i));
//        }
    }

    private void buildNotification(Context context, String title, int notifyId){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentToMainActivity = new Intent(context, MainActivity.class);
        intentToMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intentToMainActivity,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_favorite_filled)
                .setContentTitle(title)
                .setContentText(title+" Release!")
                .setAutoCancel(true);

        //Android Device Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        if (mNotificationManager != null){
            mNotificationManager.notify(notifyId,mBuilder.build());
        }
    }

    public void getReleaseReminder(final Context context) {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(date);

        String url = "https://api.themoviedb.org/3/discover/movie?api_key=c1c287ec318c5d212f5d101e39ed3220&primary_release_date.gte="+currentDate+"&primary_release_date.lte="+currentDate;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        listRelease.add(id);
                        listTitleRelease.add(title);
                    }
                    if (listRelease.size() > 0){
                        for (int i = 0; i < listRelease.size(); i++) {
                            String title = listTitleRelease.get(i);
                            buildNotification(context,title,listRelease.get(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public void setRepeating(Context context,int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, ReleaseReceiver.class);
        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
