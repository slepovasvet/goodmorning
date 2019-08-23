package ru.slepova.goodmorning.views;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import ru.slepova.goodmorning.R;

import static android.content.Context.MODE_PRIVATE;

public class FeedbackView {

    private Context context;
    private SharedPreferences sPref;
    private int feedbackCheck;
    private long lastTimestamp;

    public FeedbackView(Context context){
        this.context = context;
        initializationParameters();
        showFeedbackView();
    }

    private void initializationParameters() {
        this.sPref = context.getSharedPreferences("AppDB", MODE_PRIVATE);
        feedbackCheck = sPref.getInt("FEEDBACK_CHECK", 0);
        lastTimestamp = sPref.getLong("LAST_TIMESTAMP", System.currentTimeMillis()+10000L);
    }

    private void showFeedbackView() {
        if(feedbackCheck == 1) return;
        if(lastTimestamp > System.currentTimeMillis()) return;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(R.string.like_app)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putInt("FEEDBACK_CHECK", 1);
                        ed.apply();
                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        } else {
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        }
                        try {
                            context.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                    }
                })
                .setNegativeButton(R.string.not, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        controlFeedbackView();
                    }
                });
        alertDialog.show();

    }

    private void controlFeedbackView() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(R.string.ad_review)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putInt("FEEDBACK_CHECK", 1);
                        ed.apply();
                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        } else {
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        }
                        try {
                            context.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                    }
                })
                .setNegativeButton(R.string.not, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        long timeMillis = 2380713120L + System.currentTimeMillis();
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putLong("LAST_TIMESTAMP", timeMillis);
                        ed.apply();
                    }
                });
        alertDialog.show();
    }
}
