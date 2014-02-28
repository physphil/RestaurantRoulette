package com.physphil.android.restaurantroulette;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.models.RestaurantHistory;
import com.physphil.android.restaurantroulette.util.Util;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pshadlyn on 2/27/14.
 */
public class RestaurantSelectorFragment extends Fragment {

    public static String ACTION_HISTORY_CLEARED = "com.physphil.android.restaurantroulette.ACTION_HISTORY_CLEARED";
    private static String EXTRA_ANSWER = "com.physphil.android.restuarantroulette.EXTRA_ANSWER";
    private static String EXTRA_SUMMARY = "com.physphil.android.restuarantroulette.EXTRA_SUMMARY";
    public static String PREFS_GENRE_FILTER_SELECTOR = "genre_filter_selector";

    private DatabaseHelper mDatabaseHelper;
    private Spinner spinnerGenre;
    private Button btnSelectRestaurant;
    private TextView tvHeader;
    private TextView tvAnswer;
    private TextView tvSummary;
    private int mFilter;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_restaurant_selector, container, false);

        btnSelectRestaurant = (Button) v.findViewById(R.id.btn_select_restaurant);
        spinnerGenre = (Spinner) v.findViewById(R.id.spinner_restaurant_genre);
        tvHeader = (TextView) v.findViewById(R.id.restaurant_selector_header);
        tvAnswer = (TextView) v.findViewById(R.id.restaurant_selector_answer);
        tvSummary = (TextView) v.findViewById(R.id.restaurant_selector_summary);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        mFilter = prefs.getInt(PREFS_GENRE_FILTER_SELECTOR, Restaurant.GENRE_ALL);

        initViewContent();

        // Restore answer and summary from before config change
        if(savedInstanceState != null){

            String answer = savedInstanceState.getString(EXTRA_ANSWER);

            if(answer != null){

                tvAnswer.setText(answer);
                btnSelectRestaurant.setText(R.string.restaurant_selector_button_pick_another);
                tvHeader.setText(R.string.restaurant_answer_header);

                String summary = savedInstanceState.getString(EXTRA_SUMMARY);

                if(summary != null){
                    tvSummary.setText(summary);
                }
            }
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(EXTRA_ANSWER, tvAnswer.getText().toString());
        outState.putString(EXTRA_SUMMARY, tvSummary.getText().toString());
    }

    private void initViewContent(){

        btnSelectRestaurant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectRestaurant();
            }
        });

        List<String> genres = Util.getGenresForAdapter(getActivity());
        spinnerGenre.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres));
        spinnerGenre.setSelection(prefs.getInt(PREFS_GENRE_FILTER_SELECTOR, Restaurant.GENRE_ALL), false);  // use false for animate to not trigger listener when setting initial selection. Weird, but works.
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {

                mFilter = position;
                prefs.edit()
                        .putInt(PREFS_GENRE_FILTER_SELECTOR, position)
                        .commit();
                setAnswer(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void selectRestaurant(){

        // Get list of restaurants to choose from based on filter
        List<Restaurant> restaurants;

        if(mFilter == Restaurant.GENRE_ALL){

            restaurants = mDatabaseHelper.getAllRestaurants();
        }
        else{

            String genre = Util.getGenresForAdapter(getActivity()).get(mFilter);
            restaurants = mDatabaseHelper.getRestaurantsByGenre(genre);
        }

        // Pick one at random and record history
        if(restaurants.size() > 0){
            int randomIndex = (int) (Math.floor(Math.random() * restaurants.size()));
            Restaurant restaurant = restaurants.get(randomIndex);
            setAnswer(restaurant);
        }
        else{

            Toast.makeText(getActivity(), R.string.toast_no_restaurants_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets all views with result of selection. Passing in null clears all views.
     * @param restaurant Restaurant that was selected
     */
    private void setAnswer(Restaurant restaurant){

        if(restaurant != null){

            btnSelectRestaurant.setText(R.string.restaurant_selector_button_pick_another);
            tvHeader.setText(R.string.restaurant_answer_header);
            tvAnswer.setText(restaurant.getName());

            List<RestaurantHistory> history = mDatabaseHelper.getHistoryByRestaurant(restaurant.getId());

            if(history.size() > 0){

                tvSummary.setText(getSummaryText(history));
            }
            else{
                // No history info, so no summary to show
                tvSummary.setText("");
            }

            mDatabaseHelper.addRestaurantHistory(restaurant.getId());
        }
        else{

            tvAnswer.setText("");
            tvSummary.setText("");
            tvHeader.setText("");
            btnSelectRestaurant.setText(R.string.restaurant_selector_button);
        }
    }

    /**
     * Produces text to include in the selection summary
     * @param history RestaurantHistory entry
     * @return summary text
     */
    private String getSummaryText(List<RestaurantHistory> history){

        int numVisit = history.size();
        Date recentVisit = history.get(0).getDate();
        DateFormat df = DateFormat.getDateInstance();

        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.restaurant_number_visits))
                .append(numVisit + "\n")
                .append(getString(R.string.restaurant_last_time_visited))
                .append(df.format(recentVisit));

        return sb.toString();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            setAnswer(null);
        }
    };
    
}
