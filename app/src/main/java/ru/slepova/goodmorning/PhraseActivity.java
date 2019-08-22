package ru.slepova.goodmorning;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ru.slepova.goodmorning.db.AppDatabase;
import ru.slepova.goodmorning.db.DailyResources;
import ru.slepova.goodmorning.db.DailyResourcesDao;
import ru.slepova.goodmorning.res.Phrases;
import ru.slepova.goodmorning.res.Pictures;


public class PhraseActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    SparseArray<DailyResources> DailyResources = new SparseArray<>();

    private AdView mAdView;

    private int categoryId = -1;
    private AppDatabase db;

    private static SparseArray<ArrayList<String>> Phrases = new Phrases().List;
    private static SparseArray<ArrayList<Integer>> Pictures = new Pictures().Ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // Remove system UI

        setContentView(R.layout.activity_phrase);

        Intent data = getIntent();
        categoryId = data.getIntExtra(MainActivity.phraseCategoryId, -1);
        int notification = data.getIntExtra("notification", 0);
        if(notification != 0){
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancelAll();
            }
        }

        final SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        String today = fmt.format(new Date());

        DailyResources.append(1, GetDailyResources(this, 1, today));
        DailyResources.append(2, GetDailyResources(this, 2, today));
        DailyResources.append(3, GetDailyResources(this, 3, today));

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                int categoryId = i+1;
                final LinearLayout phrase_big_layout = findViewById(R.id.phrase_big_layout);
                phrase_big_layout.setBackgroundResource(new Pictures().Ids.get(categoryId).get(DailyResources.get(categoryId).image_id));
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        if (categoryId != -1) {
            mPager.setCurrentItem(categoryId-1);
            final LinearLayout phrase_big_layout = findViewById(R.id.phrase_big_layout);
            phrase_big_layout.setBackgroundResource(new Pictures().Ids.get(categoryId).get(DailyResources.get(categoryId).image_id));
        }

        loadAdMob();
    }

    public static DailyResources GetDailyResources(Context context, int categoryId, String day) {
        DailyResources result = null;
        final SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");

        List<DailyResources> dr = null;
        List<DailyResources> dailyCategoryResources = new ArrayList<>();
        AppDatabase db =
                Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "dailyDb")
                        .allowMainThreadQueries()
                        .build();
        DailyResourcesDao dao = db.dailyResourcesDao();

        dr = db.dailyResourcesDao().getDailyResources(day);
        for (int i = 0; i < dr.size(); i++) {
            DailyResources dri = dr.get(i);
            if (dri.category_id == categoryId) {
                dailyCategoryResources.add(dri);
            }
        }


        if (dailyCategoryResources.size() > 0) {
            result = dailyCategoryResources.get(0);
        } else {
            List<DailyResources> all = db.dailyResourcesDao().getAll();

            Collections.sort(all, new Comparator<DailyResources>() {
                @Override
                public int compare(DailyResources r1, DailyResources r2) {
                    try {
                        Date d1 = fmt.parse(r1.date);
                        Date d2 = fmt.parse(r2.date);

                        return d1.compareTo(d2);
                    } catch (ParseException e) {
                        return 0;
                    }
                }
            });

            int allCount = all.size();
            int PhrasesCount = Phrases.get(categoryId).size();
            int PicturesCount = Pictures.get(categoryId).size();

            int startPhrase = 0;
            while (startPhrase + PhrasesCount <= allCount) {
                startPhrase += PhrasesCount;
            }
            List<DailyResources> lastCyclePhrases = (startPhrase < allCount) ? all.subList(startPhrase, allCount) : new ArrayList<DailyResources>();
            List<Integer> phrases = new ArrayList<>();
            for (int i = 0; i < PhrasesCount; ++i) {
                phrases.add(i);
            }
            for (DailyResources r : lastCyclePhrases) {
                phrases.remove(new Integer(r.phrase_id));
            }

            int startPicture = 0;
            while (startPicture + PicturesCount <= allCount) {
                startPicture += PicturesCount;
            }
            List<DailyResources> lastCyclePictures = (startPicture < allCount) ? all.subList(startPicture, allCount) : new ArrayList<DailyResources>();
            List<Integer> pictures = new ArrayList<>();
            for (int i = 0; i < PicturesCount; ++i) {
                pictures.add(i);
            }
            for (DailyResources r : lastCyclePictures) {
                pictures.remove(new Integer(r.image_id));
            }

            Random r = new Random();

            Integer todayPhraseIndex = phrases.get(r.nextInt(phrases.size()));
            Integer todayPictureIndex = pictures.get(r.nextInt(pictures.size()));

            result = new DailyResources();
            result.date = day;
            result.id = allCount + 1;
            result.category_id = categoryId;
            result.phrase_id = todayPhraseIndex;
            result.image_id = todayPictureIndex;

            dao.insertAll(result);
        }

        return result;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Integer category_id = position+1;

            return PhraseFragment.newInstance(
                    category_id,
                    Pictures.get(category_id).get(DailyResources.get(category_id).image_id),
                    Phrases.get(category_id).get(DailyResources.get(category_id).phrase_id));
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void loadAdMob() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
