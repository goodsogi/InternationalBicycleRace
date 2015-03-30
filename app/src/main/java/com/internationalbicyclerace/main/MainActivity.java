package com.internationalbicyclerace.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.internationalbicyclerace.utils.AdManager;
import com.internationalbicyclerace.utils.IBRLocationFinder;
import com.internationalbicyclerace.utils.IBRLocationListener;
import com.internationalbicyclerace.R;
import com.internationalbicyclerace.race.RaceListActivity;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;


public class MainActivity extends Activity {

    private boolean GPSCatched;
    private boolean mIsLaunchingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showAd();
    }


    public void doStart(View v) {
        PlusClickGuard.doIt(v);

        if (isGPSCatched()) {
            goToRacelistActivity();
        } else {
            PlusToaster.doIt(this, getString(R.string.try_after_gps_catched));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsLaunchingActivity = false;
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this);
        if(locationFinder.isLocationUpdateRemoved()) locationFinder.getCurrentLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!mIsLaunchingActivity) removeLocationUpdate();

    }

    private void removeLocationUpdate() {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this);
        locationFinder.removeLocationUpdate();
    }

    private void goToRacelistActivity() {
        mIsLaunchingActivity = true;
        Intent intent = new Intent(MainActivity.this, RaceListActivity.class);
        startActivity(intent);
    }

    public boolean isGPSCatched() {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this);

        return locationFinder.isGpsCatched();
    }
    private void showAd() {
        AdManager.showAd(this);

    }

    public void launchSettingActivity(View v) {
        mIsLaunchingActivity = true;
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        removeLocationUpdate();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle(getString(R.string.wanna_exit));
        ab.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        ab.show();
    }
}
