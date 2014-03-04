package com.physphil.android.restaurantroulette;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.models.RestaurantHistory;
import com.physphil.android.restaurantroulette.ui.CustomFontSpinnerAdapter;
import com.physphil.android.restaurantroulette.util.Constants;

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

    private Restaurant mRestaurant;
    private List<RestaurantHistory> mHistory;
    private DatabaseHelper mDatabaseHelper;
    private Spinner spinnerGenre;
    private RelativeLayout rlAnswer;
    private RelativeLayout rlRating;
    private RelativeLayout rlNumberVisits;
    private RelativeLayout rlLastVisit;
    private Button btnSelectRestaurant;
    private TextView tvHeader;
    private TextView tvAnswer;
    private RatingBar rbRating;
    private TextView tvLastVisit;
    private TextView tvNumberOfVisits;
    private int mFilter;
    private SharedPreferences prefs;
    private Typeface mTf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_restaurant_selector, container, false);

        rlAnswer = (RelativeLayout) v.findViewById(R.id.restaurant_answer_layout);
        rlLastVisit = (RelativeLayout) v.findViewById(R.id.answer_summary_last_visit_layout);
        rlNumberVisits = (RelativeLayout) v.findViewById(R.id.answer_summary_number_visits_layout);
        rlRating = (RelativeLayout) v.findViewById(R.id.answer_summary_rating_layout);
        btnSelectRestaurant = (Button) v.findViewById(R.id.btn_select_restaurant);
        spinnerGenre = (Spinner) v.findViewById(R.id.spinner_restaurant_genre);
        tvHeader = (TextView) v.findViewById(R.id.restaurant_selector_header);
        tvAnswer = (TextView) v.findViewById(R.id.restaurant_selector_answer);
        rbRating = (RatingBar) v.findViewById(R.id.answer_summary_rating_bar);
        tvLastVisit = (TextView) v.findViewById(R.id.answer_summary_last_visit);
        tvNumberOfVisits = (TextView) v.findViewById(R.id.answer_summary_number_visits);

        setFonts(v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        mFilter = prefs.getInt(PREFS_GENRE_FILTER_SELECTOR, Restaurant.GENRE_ALL);

        initViewContent();
        setAnswer();

//        // Restore answer and summary from before config change
//        if(savedInstanceState != null){
//
//            String answer = savedInstanceState.getString(EXTRA_ANSWER);
//
//            // Only restore if answer is present
//            if(answer != null){
//
//                tvAnswer.setText(answer);
//                btnSelectRestaurant.setText(R.string.restaurant_selector_button_pick_another);
//                tvHeader.setText(R.string.restaurant_answer_header);
//
//                String summary = savedInstanceState.getString(EXTRA_SUMMARY);
//
//                if(summary != null){
//
//                }
//            }
//            else{
//                setAnswer(null);
//            }
//        }
//        else{
//            setAnswer(null);
//        }
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

        // Only save answer if an answer is present
        if(tvAnswer.getText().toString().length() > 0){

            outState.putString(EXTRA_ANSWER, tvAnswer.getText().toString());

        }
    }

    private void initViewContent(){

        btnSelectRestaurant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectRestaurant();
            }
        });

        List<String> genres = Restaurant.getGenresForAdapter(getActivity());

        // Override adapter to set font
//        spinnerGenre.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres));
        spinnerGenre.setAdapter(new CustomFontSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres));
        spinnerGenre.setSelection(prefs.getInt(PREFS_GENRE_FILTER_SELECTOR, Restaurant.GENRE_ALL), false);  // use false for animate to not trigger listener when setting initial selection. Weird, but works.
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {

                mFilter = position;
                prefs.edit()
                        .putInt(PREFS_GENRE_FILTER_SELECTOR, position)
                        .commit();
                setAnswer(null); // TODO - fix this, need a way to clear everything. call clear method, makes objects null?
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Set fonts for all text views in layout
     * @param v parent view containing all views which need font to be set
     */
    private void setFonts(View v){

        mTf = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_DEFAULT);
        btnSelectRestaurant.setTypeface(mTf);

        tvHeader.setTypeface(mTf);
        tvAnswer.setTypeface(mTf);
        tvLastVisit.setTypeface(mTf);
        tvNumberOfVisits.setTypeface(mTf);

        // change font of static text
        ((TextView) v.findViewById(R.id.selector_filter_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_genre_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.answer_summary_last_visit_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.answer_summary_number_visits_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.answer_summary_rating_text)).setTypeface(mTf);
    }

    private void selectRestaurant(){

        // Get list of restaurants to choose from based on filter
        List<Restaurant> restaurants;

        if(mFilter == Restaurant.GENRE_ALL){

            restaurants = mDatabaseHelper.getAllRestaurants();
        }
        else{

            String genre = Restaurant.getGenresForAdapter(getActivity()).get(mFilter);
            restaurants = mDatabaseHelper.getRestaurantsByGenre(genre);
        }

        // Pick one at random and record history
        if(restaurants.size() > 0){
            int randomIndex = (int) (Math.floor(Math.random() * restaurants.size()));
            mRestaurant = restaurants.get(randomIndex);
            mHistory = mDatabaseHelper.getHistoryByRestaurant(mRestaurant.getId());
            setAnswer();
        }
        else{

            Toast.makeText(getActivity(), R.string.toast_no_restaurants_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Use values of mRestaurant and mHistory to set answer.  If objects are null then answer fields are hidden
     */
    private void setAnswer(){

        if(mRestaurant != null){

            rlAnswer.setVisibility(View.VISIBLE);
            btnSelectRestaurant.setText(R.string.restaurant_selector_button_pick_another);
            tvAnswer.setText(mRestaurant.getName());

            if(mHistory.size() > 0){

                // set summary fields
                setSummaryFieldsVisibility(true);
                rbRating.setRating(mRestaurant.getUserRating());
                tvNumberOfVisits.setText(Integer.toString(mHistory.size()));

                // Objects returned from db sorted by date. Entry 0 is the most recent
                Date recentVisit = mHistory.get(0).getDate();
                DateFormat df = DateFormat.getDateInstance();
                tvLastVisit.setText(df.format(recentVisit));
            }
            else{

                // Hide summary fields
                setSummaryFieldsVisibility(false);
            }

            // Add selection to history
            mDatabaseHelper.addRestaurantHistory(mRestaurant.getId());  //TODO - only do this when button is clicked, not when restoring fragment on config change. have addtohistory flag
        }
        else{

            // No answer
            rlAnswer.setVisibility(View.GONE);
        }
    }

    /**
     * Set visibility of summary fields.  If there is no history then they need to be hidden
     * @param isVisible If the fields should be visible or not
     */
    private void setSummaryFieldsVisibility(boolean isVisible){

        if(isVisible){
            rlNumberVisits.setVisibility(View.VISIBLE);
            rlLastVisit.setVisibility(View.VISIBLE);
            rlRating.setVisibility(View.VISIBLE);
        }
        else{
            rlNumberVisits.setVisibility(View.GONE);
            rlLastVisit.setVisibility(View.GONE);
            rlRating.setVisibility(View.GONE);
        }
    }

    /**
     * Sets all views with result of selection. Passing in null clears all views.
     * @param restaurant Restaurant that was selected
     */
    private void setAnswer(Restaurant restaurant){

        if(restaurant != null){

            rlAnswer.setVisibility(View.VISIBLE);
            btnSelectRestaurant.setText(R.string.restaurant_selector_button_pick_another);
            tvHeader.setText(R.string.restaurant_answer_header);
            tvAnswer.setText(restaurant.getName());

            mHistory = mDatabaseHelper.getHistoryByRestaurant(restaurant.getId());

            if(mHistory.size() > 0){

                //tvSummary.setText(getSummaryText(history));
            }
            else{
                // No history info, so no summary to show
                //tvSummary.setText("");
            }

            mDatabaseHelper.addRestaurantHistory(restaurant.getId());
        }
        else{

            tvAnswer.setText("");

            tvHeader.setText("");
            btnSelectRestaurant.setText(R.string.restaurant_selector_button);
            rlAnswer.setVisibility(View.GONE);
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
