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

import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.view.MainActivity;

import java.util.Calendar;

public class DailyReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "tvmovies channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        buildNotification(context,"TV Movies","Aku rindu, sini dong");
    }


    private void buildNotification(Context context, String title, String desc){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentToMainActivity = new Intent(context, MainActivity.class);
        intentToMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intentToMainActivity,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_favorite_filled)
                .setContentTitle(title)
                .setContentText(desc)
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
            mNotificationManager.notify(100,mBuilder.build());
        }
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
        Intent intent = new Intent(context, DailyReceiver.class);
        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
