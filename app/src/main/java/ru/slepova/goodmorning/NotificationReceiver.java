package ru.slepova.goodmorning;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.slepova.goodmorning.db.DailyResources;
import ru.slepova.goodmorning.res.Phrases;

import static android.content.Context.MODE_PRIVATE;


public class NotificationReceiver extends BroadcastReceiver  {

    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if(skippingOutdatedNotification()) return;
        createNotificationChannel(context);
        createNotification();
    }

    //Hide obsolete notifications
    private boolean skippingOutdatedNotification() {
        SharedPreferences sPref = context.getSharedPreferences("AppDB", MODE_PRIVATE);
        long installTimestamp = sPref.getLong("ALARM_TIMESTAMP", 0) +300000L;
        return installTimestamp > System.currentTimeMillis();
    }

    private String notificationDescription(int id, Context context){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        String today = fmt.format(new Date());
        DailyResources DailyResources = PhraseActivity.GetDailyResources(context, id, today);
        return new Phrases().List.get(id).get(DailyResources.phrase_id);
    }

    private void createNotification() {
        String[] title = {context.getString(R.string.notification_one),
                context.getString(R.string.notification_two),context.getString(R.string.notification_three)};
        for (int call = 1; call< 4; call++){
            Intent notificationIntent = new Intent(context, PhraseActivity.class);
            notificationIntent.putExtra(MainActivity.phraseCategoryId, call);
            notificationIntent.putExtra("notification", 1);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    call, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_lightbulb_outline_black_18dp)
                    .setContentTitle(title[call-1])
                    .setContentText(notificationDescription(call, context))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notificationDescription(call, context)));
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify((int) System.currentTimeMillis(), mBuilder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= 26){
            SharedPreferences sPref = context.getSharedPreferences("AppDB", MODE_PRIVATE);
            int notification_channel = sPref.getInt("NOTIFICATION_CHANNEL", 0);
            if(notification_channel != 0) return;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.notification_channel_name), importance);
            channel.setDescription(context.getString(R.string.notification_channel_description));
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("NOTIFICATION_CHANNEL", 1);
                ed.apply();
            }
        }
    }
}
