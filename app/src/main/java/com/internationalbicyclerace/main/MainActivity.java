package com.internationalbicyclerace.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.internationalbicyclerace.utils.IBRLocationFinder;
import com.internationalbicyclerace.utils.IBRLocationListener;
import com.internationalbicyclerace.R;
import com.internationalbicyclerace.race.RaceListActivity;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;


public class MainActivity extends Activity implements IBRLocationListener {

    private boolean GPSCatched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void doStart(View v) {
        PlusClickGuard.doIt(v);

        if (isGPSCatched()) {
            goToRacelistActivity();
        } else {
            PlusToaster.doIt(this, getString(R.string.try_after_gps_catched));
        }
    }

    private void goToRacelistActivity() {
        Intent intent = new Intent(MainActivity.this, RaceListActivity.class);
        startActivity(intent);
    }

    public boolean isGPSCatched() {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this, this);

        return locationFinder.isGpsCatched();
    }

    @Override
    public void onGPSCatched(Location location) {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this, this);
        locationFinder.removeLocationUpdate();
    }

    @Override
    public void onDestroy() {
        stopLocationFinder();
        super.onDestroy();
    }

    private void stopLocationFinder() {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this, this);
        locationFinder.removeLocationUpdate();
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
