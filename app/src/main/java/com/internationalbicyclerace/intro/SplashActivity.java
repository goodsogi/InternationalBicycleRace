package com.internationalbicyclerace.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.Bundle;

import com.internationalbicyclerace.IBRConstants;
import com.internationalbicyclerace.utils.IBRLocationFinder;
import com.internationalbicyclerace.utils.IBRLocationListener;
import com.internationalbicyclerace.R;
import com.internationalbicyclerace.login.LoginActivity;
import com.internationalbicyclerace.main.MainActivity;


public class SplashActivity extends Activity {

    private static final int DELAY_TIME = 3000;
    private boolean signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    public void onResume() {
        super.onResume();
        runLocationFinder();
    }

    private void runLocationFinder() {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this);
        if(locationFinder.canGetLocation()) {
            locationFinder.getCurrentLocation();
            goToNextActivity();
        } else {
            locationFinder.moveToSetting();
        }
    }

    private void goToNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, isSignUp() ? MainActivity.class : LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }, DELAY_TIME);
    }




    public boolean isSignUp() {
        SharedPreferences sharedPreference = getSharedPreferences(
                IBRConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(IBRConstants.KEY_IS_SIGNUP, false);
    }
}
