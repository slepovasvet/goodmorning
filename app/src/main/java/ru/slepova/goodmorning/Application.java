package ru.slepova.goodmorning;

import com.google.android.gms.ads.MobileAds;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this,  "ca-app-pub-1874183522465795~9519015849");
    }
}
