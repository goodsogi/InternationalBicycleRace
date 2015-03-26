package com.internationalbicyclerace.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.internationalbicyclerace.server.BikerModel;
import com.internationalbicyclerace.IBRApiConstants;
import com.internationalbicyclerace.IBRConstants;
import com.internationalbicyclerace.R;
import com.internationalbicyclerace.main.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 23.
 */
public class LoginActivity extends Activity {


    private String mUserEmail;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


    }

    public void doFacebookLogin(View v) {
        //사용자 이메일 가져오기 퍼미션
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        //permissions.add("manage_pages");
        // start Facebook Login
        Session.openActiveSession(this, true, permissions, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                PlusToaster.doIt(LoginActivity.this, getString(R.string.succeeded_facebook_login));
                                BikerModel model = parseResponse(response);
                                sendUserInfoToServer(model);
                            }
                        }
                    }).executeAsync(); //executeAsync();를 반드시 추가해야 함
                }
            }
        });


    }


    private void sendUserInfoToServer(BikerModel model) {

        RequestParams params = new RequestParams();
        params.put("email", model.getEmail());
        params.put("facebookId", model.getFacebookId());
        params.put("name", model.getName());
        params.put("profileImageUrl", model.getProfileImageUrl());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(IBRApiConstants.SEND_USER_INFO_TO_SERVER, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 서버 결과가 success이면 MainActivity로 이동 !!
                if(response.opt("result").equals("success")) {
                    goToMainActivity();
                    saveLoginInfoToSharedPreference();

                } else {
                    // 서버 결과가 fail인 경우 처리!!

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }


        });


        mUserEmail = model.getEmail();

    }

    private void saveLoginInfoToSharedPreference() {
        SharedPreferences sharedPreference = getSharedPreferences(
                IBRConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(IBRConstants.KEY_IS_SIGNUP, true);
        e.putString(IBRConstants.KEY_USER_EMAIL, mUserEmail);
        e.commit();
    }

    private BikerModel parseResponse(Response response) {
        BikerModel model = new BikerModel();
        GraphObject graphObject = response.getGraphObject();
        if (graphObject != null) {
            JSONObject jsonObject = graphObject.getInnerJSONObject();

            //수정이 필요할 수 있음!!
            model.setFacebookId(jsonObject.optString("id"));
            model.setName(jsonObject.optString("name"));
            model.setEmail(jsonObject.optString("email"));
            model.setProfileImageUrl("http://graph.facebook.com/"+jsonObject.optString("id")+"/picture?type=large");


        }

        return model;


    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }


}