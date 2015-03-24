package com.internationalbicyclerace;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
            PlusToaster.doIt(this, "GPS가 잡힌 다음 시도하세요");
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
}
