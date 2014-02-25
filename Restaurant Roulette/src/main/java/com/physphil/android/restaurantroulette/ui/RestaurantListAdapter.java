package com.physphil.android.restaurantroulette.ui;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.models.Restaurant;

import java.util.List;

/**
 * Created by pshadlyn on 2/25/14.
 */
public class RestaurantListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Restaurant> mRestaurants;

    public RestaurantListAdapter(Context context, List<Restaurant> restaurants){

        mContext = context;
        mRestaurants = restaurants;
    }

    @Override
    public int getCount() {
        return mRestaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return mRestaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_restaurant_list, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView tvGenre = (TextView) convertView.findViewById(R.id.restaurant_genre);

        tvName.setText(mRestaurants.get(position).getName());
        tvGenre.setText(mRestaurants.get(position).getGenre());

        return convertView;
    }
}
