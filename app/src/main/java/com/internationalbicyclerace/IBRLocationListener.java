package com.internationalbicyclerace;

import android.location.Location;

/**
 * Created by johnny on 15. 3. 24.
 */
public interface IBRLocationListener {

    public void onGPSCatched(Location location);
}
