package com.internationalbicyclerace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.pluslibrary.common.CommonAdapter;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 24.
 */
public class GetRaceListAdapter extends CommonAdapter<BikerModel> {

    private final String mMyEmail;
    private int mMyRowPosition;

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
        userSpeed.setText(mDatas.get(position).getSpeed());





        ImageView myRow = PlusViewHolder.get(convertView, R.id.myRow);
        myRow.setSelected(mDatas.get(position).getEmail().equals(mMyEmail));
        if(mDatas.get(position).getEmail().equals(mMyEmail)) {
            mMyRowPosition = position;

        }

        return convertView;
    }

    public int getMyRowPosition() {

        return mMyRowPosition;
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
