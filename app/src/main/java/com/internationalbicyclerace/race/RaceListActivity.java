package com.internationalbicyclerace.race;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.internationalbicyclerace.server.BikerModel;
import com.internationalbicyclerace.server.GetRaceListParser;
import com.internationalbicyclerace.IBRApiConstants;
import com.internationalbicyclerace.IBRConstants;
import com.internationalbicyclerace.utils.IBRLocationFinder;
import com.internationalbicyclerace.utils.IBRLocationListener;
import com.internationalbicyclerace.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pluslibrary.utils.PlusLogger;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 24.
 */
public class RaceListActivity extends Activity implements IBRLocationListener {


    private int userEmail;
    private String mUserEmail;
    private boolean mIsFirstUploadSpeed;
    //private ProgressDialog mProgressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_race_list);
        mIsFirstUploadSpeed = true;
        setUserEmail();
        startRace();

    }

    private void setUserEmail() {
        mUserEmail = getUserEmail();
    }

    private void startRace() {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this, this);
        locationFinder.getCurrentLocation();
    }

//    private void showProgressDialog(String message) {
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setMessage(message);
//        mProgressDialog.show();
//    }


    @Override
    public void onGPSCatched(Location location) {
        //showProgressDialog("순위 갱신 중...");
        sendSpeedToServer(location);
    }

    private void sendSpeedToServer(Location location) {
        if(mIsFirstUploadSpeed) {
            insertSpeedToServer(location);
            mIsFirstUploadSpeed = false;
        }
        else {
            updateSpeedInServer(location);
        }





    }

    private void updateSpeedInServer(Location location) {
        int speed = (int) location.getSpeed() * 60 * 60
                / 1000;

        RequestParams params = new RequestParams();
        params.put("userEmail", mUserEmail);
        params.put("userSpeed", speed);

        AsyncHttpClient client = new AsyncHttpClient();


        client.post(IBRApiConstants.UPDATE_SPEED_IN_SERVER, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 서버 결과가 success이면 경주 순위 데이터 가져옴
                if (response.opt("result").equals("success"))
                    getRaceListFromServer();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }


        });
    }

    private void insertSpeedToServer(Location location) {
        int speed = (int) location.getSpeed() * 60 * 60
                / 1000;

        RequestParams params = new RequestParams();
        params.put("userEmail", mUserEmail);
        params.put("userSpeed", speed);

        AsyncHttpClient client = new AsyncHttpClient();


        client.post(IBRApiConstants.SEND_SPEED_TO_SERVER, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 서버 결과가 success이면 경주 순위 데이터 가져옴
                if (response.opt("result").equals("success"))
                    getRaceListFromServer();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }


        });
    }

    private void getRaceListFromServer() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(IBRApiConstants.GET_RACE_LIST, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                makeList(responseString);
            }
        });
    }

    private void makeList(String response) {
        final ListView list = (ListView) findViewById(R.id.raceList);
        final ArrayList<BikerModel> datas = new GetRaceListParser().doIt(response);
        final GetRaceListAdapter adapter = new GetRaceListAdapter(this,
                datas,mUserEmail);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showFacebookPage((datas.get(position).getFacebookId()));
            }
        });

        list.setSelection(getMyRowPosition(datas));




//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                list.setSelection(getMyRowPosition(datas));
//                PlusLogger.doIt("my row position: " + getMyRowPosition(datas));
//            }
//        }, 2000);

        //mProgressDialog.dismiss();

    }

    private int getMyRowPosition(ArrayList<BikerModel> datas) {
        for(int i=0; i<datas.size(); i++) {
            if(datas.get(i).getEmail().equals(mUserEmail)) return i;

        }
        return 0;
    }

    private void showFacebookPage(String facebookId) {
//        Intent intent =getOpenFacebookIntent(facebookId);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);


        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(IBRConstants.KEY_URL, "https://www.facebook.com/" +facebookId);
        startActivity(intent);
    }

    private Intent getOpenFacebookIntent(String facebookId) {

        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" +facebookId));

        //fb://page/" + facebookId가 프로필 페이지가 열리지 않음, fb://profile/" + facebookId는 이용할 수 없다고 나옴
//        try{
//            // open in Facebook app
//            getPackageManager().getPackageInfo("com.facebook.katana", 0);
//            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + facebookId));
//        } catch (Exception e) {
//            // open in browser
//            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" +facebookId));
//        }
    }

    public String getUserEmail() {
        SharedPreferences sharedPreference = getSharedPreferences(
                IBRConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(IBRConstants.KEY_USER_EMAIL, "");
    }

    @Override
    public void onDestroy() {
        stopLocationFinder();
        deleteRecordInServer();
        super.onDestroy();
    }

    private void stopLocationFinder() {
        IBRLocationFinder locationFinder =IBRLocationFinder.getInstance(this, this);
        locationFinder.removeLocationUpdate();
    }

    private void deleteRecordInServer() {

        RequestParams params = new RequestParams();
        params.put("userEmail", mUserEmail);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(IBRApiConstants.DELETE_RACE_RECORD, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //if (response.opt("result").equals("success"))
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }


        });

    }
}