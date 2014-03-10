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
        mHistory = mDatabaseHelper.getAllHistory();
        mAdapter = new RestaurantHistoryListAdapter(getActivity(), mHistory);
        setListAdapter(mAdapter);
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
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,
                new IntentFilter(ACTION_HISTORY_CLEARED));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(ACTION_HISTORY_CLEARED)){

                // Refresh adapter as history data has been cleared
                mHistory.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
