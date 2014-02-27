package com.physphil.android.restaurantroulette.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.RestaurantFragment;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.util.Constants;

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

        final Restaurant restaurant = mRestaurants.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_restaurant_list, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView tvGenre = (TextView) convertView.findViewById(R.id.restaurant_genre);
        ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.restaurant_delete_button);
        Typeface defaultFont = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_DEFAULT);

        tvName.setText(restaurant.getName());
        //tvName.setTypeface(defaultFont);
        tvGenre.setText(restaurant.getGenre());
        //tvGenre.setTypeface(defaultFont);

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.dialog_delete_restaurant_title)
                        .setMessage(R.string.dialog_delete_restaurant_message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Send broadcast to hosting fragment to delete restaurant from database
                                Intent i = new Intent(RestaurantFragment.ACTION_DELETE_RESTAURANT);
                                i.putExtra(RestaurantFragment.EXTRA_RESTAURANT_ID, restaurant.getId());
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }
}
