package ru.slepova.goodmorning;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.slepova.goodmorning.db.DailyResources;
import ru.slepova.goodmorning.res.Phrases;

class NotificationHelper {

    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";

    NotificationHelper(Context context) {
        mContext = context;
    }

    void createNotification() {
        Intent intent = new Intent(mContext, PhraseActivity.class);
        intent.putExtra(MainActivity.phraseCategoryId, 1);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        final SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        String today = fmt.format(new Date());
        DailyResources DailyResources = PhraseActivity.GetDailyResources(mContext, 1, today);
        String phrase = new Phrases().List.get(1).get(DailyResources.phrase_id);

        NotificationCompat.BigTextStyle bts = new NotificationCompat.BigTextStyle();
        bts.bigText(phrase);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_lightbulb_outline_black_18dp);
        mBuilder.setContentTitle(mContext.getResources().getString(R.string.notification_title))
                .setContentText(phrase)
                .setAutoCancel(false)
                .setContentIntent(resultPendingIntent)
                .setStyle(bts);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }
}
