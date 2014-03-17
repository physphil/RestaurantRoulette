package com.physphil.android.restaurantroulette.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.models.RestaurantHistory;
import com.physphil.android.restaurantroulette.util.Constants;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by pshadlyn on 2/25/14.
 */
public class RestaurantHistoryListAdapter extends BaseAdapter {

    private Context mContext;
    private List<RestaurantHistory> mHistory;

    public RestaurantHistoryListAdapter(Context context, List<RestaurantHistory> history){

        mContext = context;
        mHistory = history;
    }

    @Override
    public int getCount() {
        return mHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RestaurantHistory history = mHistory.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_history_list, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.history_name);
        TextView tvGenre = (TextView) convertView.findViewById(R.id.history_genre);
        RatingBar rbRating = (RatingBar) convertView.findViewById(R.id.history_rating);
        TextView tvDate = (TextView) convertView.findViewById(R.id.history_date);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_DEFAULT);

        tvName.setText(history.getName());
        tvName.setTypeface(tf);

        tvGenre.setText(history.getGenre());
        tvGenre.setTypeface(tf);

        rbRating.setRating(history.getUserRating());

        DateFormat df = DateFormat.getDateInstance();
        tvDate.setText(df.format(history.getDate()));
        tvDate.setTypeface(tf);

        return convertView;
    }
}
