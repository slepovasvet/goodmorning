package ru.slepova.goodmorning.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import ru.slepova.goodmorning.NotificationReceiver;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class RestartAlarmService extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent i) {
        final SharedPreferences sPref = context.getSharedPreferences("AppDB", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong("ALARM_TIMESTAMP",  System.currentTimeMillis());
        ed.apply();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 3);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent alarmIntentRTC = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManagerRTC = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        if (alarmManagerRTC != null) {
            alarmManagerRTC.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);
        }
    }
}
