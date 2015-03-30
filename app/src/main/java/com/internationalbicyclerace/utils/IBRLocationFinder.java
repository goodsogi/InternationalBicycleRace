package com.internationalbicyclerace.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.internationalbicyclerace.IBRConstants;
import com.internationalbicyclerace.R;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusToaster;

/**
 * 현재 위치 가져오기
 * 
 * @author jeff
 * 
 */
public class IBRLocationFinder implements android.location.LocationListener {

    private static final int REQUEST_LOCATION_AGREEMENT = 22;
    private static final String IBR_LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    private Activity mActivity;
	private LocationManager mLocationManager;
	private String mProvider;

    private static IBRLocationFinder instance;
    private boolean mIsGpsCatched;

    private static final long DELAY_TIME = 1000 * 10;

    static private IBRLocationListener mListener;
    private boolean mLocationUpdateRemoved;

    private IBRLocationFinder(Activity activity) {
		mActivity = activity;

        mLocationManager = (LocationManager) mActivity
                .getSystemService(Context.LOCATION_SERVICE);

	}

    public static IBRLocationFinder getInstance(Activity activity) {
        if(instance == null) {
             instance = new IBRLocationFinder(activity);
        }
            return instance;
    }
    
    public void setLocationListener(IBRLocationListener listener) {
        mListener = listener;
    }
	
	
	public boolean doIt() {
		if(canGetLocation()) {
			getCurrentLocation();
			return true;
		} else {
			moveToSetting();
			return false;
		}
	}

	public boolean canGetLocation() {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mActivity);
		if (status != ConnectionResult.SUCCESS)
			return false;

        //return status == ConnectionResult.SUCCESS;
		Criteria criteria = new Criteria();
		mProvider = mLocationManager.getBestProvider(criteria, true);

		if (mProvider == null
				|| mProvider.equals(LocationManager.PASSIVE_PROVIDER))
			return false;

		// 구글 플레이 서비스를 사용할 수 있고 설정에서 위치 정보 액세스가 on된 경우
		return true;

	}

	/**
	 * 위치 정보 액세스 설정으로 이동
	 */

	public void moveToSetting() {

		new AlertDialog.Builder(mActivity)
				.setMessage(mActivity.getString(R.string.wanna_use_location_service))
				.setNeutralButton(mActivity.getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mActivity
                                        .startActivityForResult(
                                                new Intent(
                                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                                IBRLocationFinder.REQUEST_LOCATION_AGREEMENT);
                            }
                        })
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                }).show();

	}

	/**
	 * 현재 위치 가져오기
	 */
	public void getCurrentLocation() {
		// TODO Auto-generated method stub
		// mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 1000, 10, this);
		// 네트워크 제공자가 제공하는 위치. GPS를 사용하면 변경 필요!!
		mIsGpsCatched = false;
        mLocationUpdateRemoved = false;
//		mLocationManager.requestLocationUpdates(
//				LocationManager.GPS_PROVIDER, 3000, 0, this);

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mActivity);

        int refreshInterval = getRefreshInterval(Integer.parseInt(preferences
                .getString(IBRConstants.KEY_PREF_REFRESH_INTERVAL, "0")));

        //테스트용
        mLocationManager.requestLocationUpdates(
                IBR_LOCATION_PROVIDER, refreshInterval, 0, this);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                if (!mIsGpsCatched) {
//
//                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            0, 0, IBRLocationFinder.this);
//                }
//
//            }
//        }, DELAY_TIME);

	}

    private int getRefreshInterval(int value) {
        switch (value) {
            case 0:
                return 1000;
            case 1:
                return 5 * 1000;
            case 2:
                return 10 * 1000;
            case 3:
                return 30 * 1000;
        }
        return 1000;
    }



    @Override
	public void onLocationChanged(Location location) {

        mIsGpsCatched = true;
        if(mListener != null) mListener.onGPSCatched(location);

		
	}


    public boolean isGpsCatched() {
        return mIsGpsCatched;
    }
    public boolean isLocationUpdateRemoved() {
        return mLocationUpdateRemoved;
    }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

    public void removeLocationUpdate() {
        mLocationManager.removeUpdates(this);
        mLocationUpdateRemoved = true;
    }


    public void setRefreshInterval(int refreshInterval) {
        mLocationManager.removeUpdates(this);

        int refreshIntervalInMilliSec = getRefreshInterval(refreshInterval);
        mLocationManager.requestLocationUpdates(
                IBR_LOCATION_PROVIDER, refreshIntervalInMilliSec, 0, this);
    }
}
