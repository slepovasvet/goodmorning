package ru.slepova.goodmorning;

import android.content.ActivityNotFoundException;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import ru.slepova.goodmorning.res.Pictures;
import ru.slepova.goodmorning.utils.NotificationManagerUtil;
import ru.slepova.goodmorning.views.FeedbackView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String phraseCategoryId = "phraseCategoryId";
    private static final int TIME_OUT = 4000;
    private Context mContext;
    private int firstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mContext = this;
        new NotificationManagerUtil(this);
        final SharedPreferences sPref = getSharedPreferences("AppDB", MODE_PRIVATE);
        firstLaunch = sPref.getInt("FIRST_LAUNCH", 1);

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

                if(firstLaunch == 1){
                    LinearLayout root2 = findViewById(R.id.RootLayout2);
                    root2.setVisibility(View.VISIBLE);
                    long timeMillis = 2380713120L + System.currentTimeMillis();
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putLong("LAST_TIMESTAMP", timeMillis);
                    ed.putInt("FIRST_LAUNCH", 0);
                    ed.apply();
                }

                new FeedbackView(mContext);
            }
        }, TIME_OUT);

        LinearLayout menu_1 = findViewById(R.id.menu_item_1);
        LinearLayout menu_2 = findViewById(R.id.menu_item_2);
        LinearLayout menu_3 = findViewById(R.id.menu_item_3);
        TextView menu_41 = findViewById(R.id.menu_item_41);
        TextView menu_42 = findViewById(R.id.menu_item_42);
        ImageView closeCross = findViewById(R.id.closeCross);
        menu_1.setOnClickListener(this);
        menu_2.setOnClickListener(this);
        menu_3.setOnClickListener(this);
        menu_41.setOnClickListener(this);
        menu_42.setOnClickListener(this);
        closeCross.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_item_1:
                Intent phraseIntent = new Intent(mContext, PhraseActivity.class);
                phraseIntent.putExtra(MainActivity.phraseCategoryId, 1);
                startActivity(phraseIntent);
                break;

            case R.id.menu_item_2:
                Intent phraseIntentTwo = new Intent(mContext, PhraseActivity.class);
                phraseIntentTwo.putExtra(MainActivity.phraseCategoryId, 2);
                startActivity(phraseIntentTwo);
                break;

            case R.id.menu_item_3:
                Intent phraseIntentThree = new Intent(mContext, PhraseActivity.class);
                phraseIntentThree.putExtra(MainActivity.phraseCategoryId, 3);
                startActivity(phraseIntentThree);
                break;

            case R.id.menu_item_41:
                LinearLayout root2 = findViewById(R.id.RootLayout2);
                root2.setVisibility(View.VISIBLE);
                break;

            case R.id.menu_item_42:
                try {
                    String url = "https://sites.google.com/view/verygoodmorning";
                    Uri webPage = Uri.parse(url);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, webPage);
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, "No application can handle this request. Please install a web browser.",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;

            case R.id.closeCross:
                LinearLayout rootCloseCross = findViewById(R.id.RootLayout2);
                rootCloseCross.setVisibility(View.GONE);
                break;
        }
    }

}
