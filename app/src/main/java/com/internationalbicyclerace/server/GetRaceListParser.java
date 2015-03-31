package com.internationalbicyclerace.server;

import com.internationalbicyclerace.server.BikerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 24.
 */
public class GetRaceListParser {
    public ArrayList<BikerModel> doIt(String rawData) {
        ArrayList<BikerModel> model = new ArrayList<>();
 JSONObject response = null;
        try {
            response = new JSONObject(rawData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            JSONArray jsonArray = response.optJSONArray("result");
            for(int i=0; i<jsonArray.length();i++) {
                BikerModel data = new BikerModel();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                data.setName(jsonObject.optString("name"));
                data.setEmail(jsonObject.optString("email"));
                data.setFacebookProfileLink(jsonObject.optString("facebookProfileLink"));
                data.setProfileImageUrl(jsonObject.optString("profileImage"));
                data.setSpeed(jsonObject.optString("speed"));
                data.setFacebookId(jsonObject.optString("facebookId"));
                data.setIsFacebookProfileOpen(Boolean.parseBoolean(jsonObject.optString("isFacebookProfileOpen")));
                model.add(data);
            }

        } catch (Exception e) {
            return null;
        }

        return model;
    }
}