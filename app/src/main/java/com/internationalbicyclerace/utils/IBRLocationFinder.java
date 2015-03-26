package com.internationalbicyclerace.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * 현재 위치 가져오기
 * 
 * @author jeff
 * 
 */
public class IBRLocationFinder implements android.location.LocationListener {

    private static final int REQUEST_LOCATION_AGREEMENT = 22;
    private Activity mActivity;
	private LocationManager mLocationManager;
	private String mProvider;

    private static IBRLocationFinder instance;
    private boolean mIsGpsCatched;

    private static final long DELAY_TIME = 1000 * 10;

    static private IBRLocationListener mListener;

    private IBRLocationFinder(Activity activity, IBRLocationListener listener) {
		mActivity = activity;
        mListener = listener;

        mLocationManager = (LocationManager) mActivity
                .getSystemService(Context.LOCATION_SERVICE);

	}

    public static IBRLocationFinder getInstance(Activity activity,IBRLocationListener listener) {
        if(instance == null) {
            return instance = new IBRLocationFinder(activity,listener);
        }
        else{
            mListener = listener;
            return instance;
        }
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
				.setTitle("알림")
				.setMessage("위치 서비스를 사용하시겠습니까?")
				.setNeutralButton("확인",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mActivity
										.startActivityForResult(
												new Intent(
														android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
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
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 3000, 0, this);

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

	@Override
	public void onLocationChanged(Location location) {

        mIsGpsCatched = true;
        mListener.onGPSCatched(location);

		
	}


    public boolean isGpsCatched() {
        return mIsGpsCatched;
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
    }


}
