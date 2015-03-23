package com.internationalbicyclerace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by johnny on 15. 3. 23.
 */
public class LoginActivity extends Activity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


    }

    public void doFacebookLogin(View v) {
        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

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
                                PlusToaster.doIt(LoginActivity.this, "페이스북 로그인 성공!");
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
        params.put("name", model.getName());
        params.put("profileImageUrl", model.getProfileImageUrl());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(IBRApiConstants.SEND_USER_INFO_TO_SERVER, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 서버 결과가 success이면 MainActivity로 이동 !!
                if(response.opt("result").equals("success"))
                goToMainActivity(); else {
                    // 서버 결과가 fail인 경우 처리!!
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }


        });

    }

    private BikerModel parseResponse(Response response) {
        BikerModel model = new BikerModel();
        GraphObject graphObject = response.getGraphObject();
        if (graphObject != null) {
            JSONObject jsonObject = graphObject.getInnerJSONObject();

            //수정이 필요할 수 있음!!
            //model.setEmail(jsonObject.optString("id"));
            model.setName(jsonObject.optString("name"));
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