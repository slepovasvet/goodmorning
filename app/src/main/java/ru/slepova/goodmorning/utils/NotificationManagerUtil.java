package ru.slepova.goodmorning.utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import ru.slepova.goodmorning.NotificationReceiver;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NotificationManagerUtil {

    private Context context;
    private SharedPreferences sPref;

    public NotificationManagerUtil(Context context){
        this.context = context;
        notificationHide(context);
        scheduleRepeatingRTCNotification();
    }

    //Hides notifications when opening an application
    private void notificationHide(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    //Notification Schedule
    private void scheduleRepeatingRTCNotification(){
        sPref = context.getSharedPreferences("AppDB", MODE_PRIVATE);
        int alarm = sPref.getInt("NOTIFICATION_ALARM", 0);
        if(alarm != 0) return;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent alarmIntentRTC = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManagerRTC = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        if (alarmManagerRTC != null) {
            alarmManagerRTC.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);
        }
        regAlarmNotification();
    }

    private void regAlarmNotification() {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong("ALARM_TIMESTAMP",  System.currentTimeMillis());
        ed.putInt("NOTIFICATION_ALARM", 1);
        ed.apply();
    }
}
