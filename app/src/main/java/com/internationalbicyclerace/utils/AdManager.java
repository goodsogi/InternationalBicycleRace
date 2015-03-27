package com.internationalbicyclerace.utils;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.internationalbicyclerace.R;

/**
 * Created by johnny on 15. 1. 8.
 */
public class AdManager {
    public static void showAd(Activity activity) {
        AdView ad = (AdView) activity.findViewById(R.id.adView);
        //AdRequest가 2가지 패키지가 있음. com.google.android.gms.ads.AdRequest로 임포트
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);
    }

}
