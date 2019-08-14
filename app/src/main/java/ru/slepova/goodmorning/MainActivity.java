package ru.slepova.goodmorning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ru.slepova.goodmorning.res.Pictures;


public class MainActivity extends AppCompatActivity {

    public static final String phraseCategoryId = "phraseCategoryId";
    private static final int TIME_OUT = 4000;
    private static final String AskForReviewMomentTag = "launchDatetime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // Remove system UI
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_main);

        LinearLayout root3 = findViewById(R.id.RootLayout3);
        root3.setVisibility(View.VISIBLE);
        Pictures p = new Pictures();
        Random r = new Random();
        int picCategory = r.nextInt(3) + 1;
        int picIndex = r.nextInt(p.Ids.get(picCategory).size());
        int picId = p.Ids.get(picCategory).get(picIndex);
        root3.setBackgroundResource(picId);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final LinearLayout root3 = findViewById(R.id.RootLayout3);
                root3.setVisibility(View.GONE);

                if (!isFirstLaunch()) {
                    final Context context = MainActivity.this;
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.pref), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String askForReviewTime = sharedPref.getString(AskForReviewMomentTag, "");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dt = new Date();
                    try {
                        dt = df.parse(askForReviewTime);
                    } catch (ParseException e) {
                    }

                    Date now = new Date();
                    if (now.getTime() - dt.getTime() > 60*60*24*30 /* true*/) {

                        Context context2 = MainActivity.this;
                        SharedPreferences sharedPref2 = context2.getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPref2.edit();

                        Calendar c2 = Calendar.getInstance();
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String launchDatetime2 = df2.format(c2.getTime());
                        editor2.putString(AskForReviewMomentTag, launchDatetime2);
                        editor2.apply();

                        final DialogInterface.OnClickListener dialog2ClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                        // To count with Play market backstack, After pressing back button,
                                        // to taken back to our application, we need to add following flags to intent.
                                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                        try {
                                            startActivity(goToMarket);
                                        } catch (ActivityNotFoundException e) {
                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                                        }
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                }
                            }
                        };

                        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                        // To count with Play market backstack, After pressing back button,
                                        // to taken back to our application, we need to add following flags to intent.
                                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                        try {
                                            startActivity(goToMarket);
                                        } catch (ActivityNotFoundException e) {
                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                                        }
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Хотите оставить отзыв?")
                                                .setPositiveButton("Да", dialog2ClickListener)
                                                .setNegativeButton("Нет", dialog2ClickListener)
                                                .show();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Вам нравится это приложение?")
                                .setPositiveButton("Да", dialogClickListener)
                                .setNegativeButton("Нет", dialogClickListener)
                                .show();
                    }
                }

            }
        }, TIME_OUT);

        MobileAds.initialize(this, "ca-app-pub-1874183522465795~9519015849");

        LinearLayout menu_1 = findViewById(R.id.menu_item_1);
        menu_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phraseIntent = new Intent(MainActivity.this, PhraseActivity.class);
                phraseIntent.putExtra(MainActivity.phraseCategoryId, 1);
                startActivity(phraseIntent);
            }
        });

        LinearLayout menu_2 = findViewById(R.id.menu_item_2);
        menu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phraseIntent = new Intent(MainActivity.this, PhraseActivity.class);
                phraseIntent.putExtra(MainActivity.phraseCategoryId, 2);
                startActivity(phraseIntent);
            }
        });

        LinearLayout menu_3 = findViewById(R.id.menu_item_3);
        menu_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phraseIntent = new Intent(MainActivity.this, PhraseActivity.class);
                phraseIntent.putExtra(MainActivity.phraseCategoryId, 3);
                startActivity(phraseIntent);
            }
        });

        TextView menu_41 = findViewById(R.id.menu_item_41);
        menu_41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout root2 = findViewById(R.id.RootLayout2);
                root2.setVisibility(View.VISIBLE);
            }
        });

        TextView menu_42 = findViewById(R.id.menu_item_42);
        menu_42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "https://sites.google.com/view/verygoodmorning";
                    Uri webpage = Uri.parse(url);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request. Please install a web browser.",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        IfNotSetSetEverydayAlarm();

        Boolean firstLaunch = isFirstLaunch();
        if (firstLaunch) {

            Context context = this;
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.pref), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt(getString(R.string.fl), 1);
            editor.apply();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String launchDatetime = df.format(c.getTime());
            editor.putString(AskForReviewMomentTag, launchDatetime);
            editor.apply();

            // Show Layout
            LinearLayout root2 = findViewById(R.id.RootLayout2);
            root2.setVisibility(View.VISIBLE);
        }

        ImageView cc = findViewById(R.id.closeCross);
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout root2 = findViewById(R.id.RootLayout2);
                root2.setVisibility(View.GONE);
            }
        });
    }

    private Boolean isFirstLaunch()
    {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.pref), Context.MODE_PRIVATE);

        Boolean firstLaunch = !sharedPref.contains(getResources().getString(R.string.fl));

        return firstLaunch;
    }

    public void IfNotSetSetEverydayAlarm(){
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(getApplicationContext(), NotificationReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            if (calendar.getTime().compareTo(new Date()) < 0)
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }
}
