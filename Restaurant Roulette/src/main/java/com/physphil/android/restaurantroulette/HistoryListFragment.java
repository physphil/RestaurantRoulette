package com.physphil.android.restaurantroulette;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.RestaurantHistory;
import com.physphil.android.restaurantroulette.ui.RestaurantHistoryListAdapter;
import com.physphil.android.restaurantroulette.util.Constants;

import java.util.List;

/**
 * Created by pshadlyn on 3/10/14.
 */
public class HistoryListFragment extends ListFragment {

    public static String ACTION_HISTORY_CLEARED = "com.physphil.android.restaurantroulette.ACTION_HISTORY_CLEARED";

    private List<RestaurantHistory> mHistory;
    private DatabaseHelper mDatabaseHelper;
    private RestaurantHistoryListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        updateHistoryList();

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(mReceiver, new IntentFilter(RestaurantFragment.ACTION_RESTAURANT_UPDATED));
        lbm.registerReceiver(mReceiver, new IntentFilter(ACTION_HISTORY_CLEARED));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_list, container, false);

        // Set font for text when listview is empty
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_DEFAULT);
        ((TextView) v.findViewById(android.R.id.empty)).setTypeface(tf);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setDivider(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        String restaurantId = mHistory.get(position).getRestaurantId();
        viewRestaurantDetail(restaurantId);
    }

    /**
     * Start activity to view restaurant information
     * @param id database id of restaurant to view, or null if a new restaurant
     */
    private void viewRestaurantDetail(String id){

        Intent i = new Intent(getActivity(), RestaurantActivity.class);

        if(id != null){
            i.putExtra(RestaurantFragment.EXTRA_RESTAURANT_ID, id);
        }

        startActivity(i);
    }

    private void updateHistoryList(){
        mHistory = mDatabaseHelper.getAllHistory();
        mAdapter = new RestaurantHistoryListAdapter(getActivity(), mHistory);
        setListAdapter(mAdapter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(ACTION_HISTORY_CLEARED)){

                // Refresh adapter as history data has been cleared
                mHistory.clear();
                mAdapter.notifyDataSetChanged();
            }
            else if(intent.getAction().equals(RestaurantFragment.ACTION_RESTAURANT_UPDATED)){

                // Refresh history list to capture updated info
                updateHistoryList();
            }
        }
    };
}
