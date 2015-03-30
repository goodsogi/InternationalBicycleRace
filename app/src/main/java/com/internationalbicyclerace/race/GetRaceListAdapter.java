package com.internationalbicyclerace.race;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.internationalbicyclerace.server.BikerModel;
import com.internationalbicyclerace.R;
import com.pluslibrary.common.CommonAdapter;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 24.
 */
public class GetRaceListAdapter extends CommonAdapter<BikerModel> {

    private final String mMyEmail;

    public GetRaceListAdapter(Context context, ArrayList<BikerModel> datas, String myEmail) {
        super(context, R.layout.race_list_item, datas);
        mMyEmail = myEmail;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.race_list_item,
                    parent, false);
        }




        ImageView profileImage = PlusViewHolder.get(convertView, R.id.profileImage);
        mImageLoader.displayImage(mDatas.get(position).getProfileImageUrl(), profileImage, mOptionRound);


        ImageView medal = PlusViewHolder.get(convertView, R.id.medal);
        medal.setImageResource(getMedalResource(position));
        medal.setVisibility(position > 2? View.GONE:View.VISIBLE);

        TextView userName = PlusViewHolder.get(convertView, R.id.userName);
        userName.setText(mDatas.get(position).getName());

        TextView userRank = PlusViewHolder.get(convertView, R.id.userRank);
        userRank.setText(String.valueOf(position+1));


        TextView userSpeed = PlusViewHolder.get(convertView, R.id.userSpeed);
        userSpeed.setText(getFormattedSpeed(mDatas.get(position).getSpeed()));


        LinearLayout myContainer = PlusViewHolder.get(convertView, R.id.myContainer);

        //리스트뷰의 row의 부모 컨테이너에는 selector가 작동하지 않아 코드를 사용
        if(mDatas.get(position).getEmail().equals(mMyEmail)) {
            myContainer.setBackgroundResource(R.drawable.rectangle);

        } else {
            myContainer.setBackgroundColor(Color.parseColor("#00000000"));
        }


//        ImageView myRow = PlusViewHolder.get(convertView, R.id.myRow);
//        myRow.setSelected(mDatas.get(position).getEmail().equals(mMyEmail));


        return convertView;
    }

    private String getFormattedSpeed(String speed) {
        int speedInt = Integer.parseInt(speed);

        float result = speedInt/100f;
        return String.valueOf(result);
    }


    private int getMedalResource(int position) {
        switch(position) {
            case 0: return R.drawable.gold_medal;
            case 1: return R.drawable.silver_medal;
            case 2: return R.drawable.bronze_medal;

        }
        return R.drawable.gold_medal;
    }



}
