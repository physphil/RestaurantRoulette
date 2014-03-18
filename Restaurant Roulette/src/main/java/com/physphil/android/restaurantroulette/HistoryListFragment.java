package com.physphil.android.restaurantroulette;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.RestaurantHistory;
import com.physphil.android.restaurantroulette.ui.RestaurantHistoryListAdapter;
import com.physphil.android.restaurantroulette.util.Constants;
import com.physphil.android.restaurantroulette.util.Util;

import java.util.List;

/**
 * Created by pshadlyn on 3/10/14.
 */
public class HistoryListFragment extends ListFragment {

    public static final String ACTION_HISTORY_CLEARED = "com.physphil.android.restaurantroulette.ACTION_HISTORY_CLEARED";
    private static final String PREFS_SHOW_HELP_HISTORY = "show_help_history";

    private List<RestaurantHistory> mHistory;
    private DatabaseHelper mDatabaseHelper;
    private RestaurantHistoryListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
    public void onResume() {
        super.onResume();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean showHelp = prefs.getBoolean(PREFS_SHOW_HELP_HISTORY, true);
        if(showHelp){

            showHelpDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        String restaurantId = mHistory.get(position).getRestaurantId();
        startActivity(RestaurantActivity.getLaunchingIntent(getActivity(), restaurantId));
    }

    private void updateHistoryList(){
        mHistory = mDatabaseHelper.getAllHistory();
        mAdapter = new RestaurantHistoryListAdapter(getActivity(), mHistory);
        setListAdapter(mAdapter);
    }

    private void showHelpDialog(){

        Util.showHelpDialog(getActivity(), R.string.title_restaurant_history, R.string.dialog_restaurant_history_help, PREFS_SHOW_HELP_HISTORY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menu_help:
                showHelpDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
