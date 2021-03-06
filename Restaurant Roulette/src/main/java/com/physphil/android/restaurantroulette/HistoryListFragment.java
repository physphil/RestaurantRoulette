/**
 * Restaurant Roulette for Android
 * Copyright (C) 2014  Phil Shadlyn
 *
 * Restaurant Roulette is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @copyright 2014 Phil Shadlyn - physphil@gmail.com
 * @license GNU General Public License - https://www.gnu.org/licenses/gpl.html
 */

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
        lbm.registerReceiver(mLifetimeReceiver, new IntentFilter(RestaurantFragment.ACTION_RESTAURANT_UPDATED));
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

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(mVisibleReceiver, new IntentFilter(NavigationDrawerFragment.ACTION_DRAWER_CLOSED));
        lbm.registerReceiver(mVisibleReceiver, new IntentFilter(ACTION_HISTORY_CLEARED));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mVisibleReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mLifetimeReceiver);
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

    private BroadcastReceiver mLifetimeReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(RestaurantFragment.ACTION_RESTAURANT_UPDATED)){

                // Refresh history list to capture updated info
                updateHistoryList();
            }
        }
    };

    private BroadcastReceiver mVisibleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(ACTION_HISTORY_CLEARED)){

                // Refresh adapter as history data has been cleared
                mHistory.clear();
                mAdapter.notifyDataSetChanged();
            }
            else if(intent.getAction().equals(NavigationDrawerFragment.ACTION_DRAWER_CLOSED)){

                // Show help dialog if never been shown before
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                boolean showHelp = prefs.getBoolean(PREFS_SHOW_HELP_HISTORY, true);
                if(showHelp){

                    showHelpDialog();
                }
            }
        }
    };
}
