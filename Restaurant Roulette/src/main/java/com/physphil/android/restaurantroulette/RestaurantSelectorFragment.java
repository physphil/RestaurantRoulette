package com.physphil.android.restaurantroulette;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.physphil.android.restaurantroulette.ui.CustomFontArrayAdapter;
import com.physphil.android.restaurantroulette.util.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pshadlyn on 2/27/14.
 */
public class RestaurantSelectorFragment extends Fragment {

    public static String ACTION_HISTORY_CLEARED = "com.physphil.android.restaurantroulette.ACTION_HISTORY_CLEARED";
    public static String PREFS_GENRE_FILTER_SELECTOR = "genre_filter_selector";

    private Restaurant mRestaurant;
    private List<RestaurantHistory> mHistory;
    private DatabaseHelper mDatabaseHelper;
    private Spinner spinnerGenre;
    private RelativeLayout rlAnswer;
    private RelativeLayout rlNumberVisits;
    private RelativeLayout rlLastVisit;
    private Button btnSelectRestaurant;
    private Button btnGetDirections;
    private TextView tvHeader;
    private TextView tvAnswer;
    private RatingBar rbRating;
    private RatingBar rbPrice;
    private TextView tvLastVisit;
    private TextView tvNumberOfVisits;
    private int mFilter;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_restaurant_selector, container, false);

        rlAnswer = (RelativeLayout) v.findViewById(R.id.restaurant_answer_layout);
        rlLastVisit = (RelativeLayout) v.findViewById(R.id.answer_summary_last_visit_layout);
        rlNumberVisits = (RelativeLayout) v.findViewById(R.id.answer_summary_number_visits_layout);
        btnSelectRestaurant = (Button) v.findViewById(R.id.btn_select_restaurant);
        btnGetDirections = (Button) v.findViewById(R.id.btn_get_directions);
        spinnerGenre = (Spinner) v.findViewById(R.id.spinner_restaurant_genre);
        tvHeader = (TextView) v.findViewById(R.id.restaurant_selector_header);
        tvAnswer = (TextView) v.findViewById(R.id.restaurant_selector_answer);
        rbRating = (RatingBar) v.findViewById(R.id.answer_summary_rating_bar);
        rbPrice = (RatingBar) v.findViewById(R.id.answer_summary_price_bar);
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

        // Set answer through fragment objects mRestaurant and mHistory. Don't save to history as no new selection was made
        setAnswer(false);
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

    private void initViewContent(){

        btnSelectRestaurant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectRestaurant();
            }
        });

        btnGetDirections.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showOnMap();
            }
        });

        List<String> genres = Restaurant.getGenresForAdapter(getActivity());

        // Override adapter to set font
        spinnerGenre.setAdapter(new CustomFontArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres));
        spinnerGenre.setSelection(prefs.getInt(PREFS_GENRE_FILTER_SELECTOR, Restaurant.GENRE_ALL), false);  // use false for animate to not trigger listener when setting initial selection. Weird, but works.
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {

                mFilter = position;
                prefs.edit()
                        .putInt(PREFS_GENRE_FILTER_SELECTOR, position)
                        .commit();
                clearAnswer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Set fonts for all text views in layout
     * @param v parent view containing all views which need font to be set
     */
    private void setFonts(View v){

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_DEFAULT);
        btnSelectRestaurant.setTypeface(tf);
        btnGetDirections.setTypeface(tf);
        tvHeader.setTypeface(tf);
        tvAnswer.setTypeface(tf);
        tvLastVisit.setTypeface(tf);
        tvNumberOfVisits.setTypeface(tf);

        // change font of static text
        ((TextView) v.findViewById(R.id.selector_filter_text)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.restaurant_genre_text)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.answer_summary_last_visit_text)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.answer_summary_number_visits_text)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.answer_summary_rating_text)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.answer_summary_price_text)).setTypeface(tf);
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
            setAnswer(true);
        }
        else{

            Toast.makeText(getActivity(), R.string.toast_no_restaurants_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Use values of mRestaurant and mHistory to set answer.  If objects are null then answer fields are hidden
     * @param addToHistory whether to add this selection to selection history
     */
    private void setAnswer(boolean addToHistory){

        if(mRestaurant != null){

            rlAnswer.setVisibility(View.VISIBLE);
            btnSelectRestaurant.setText(R.string.restaurant_selector_button_pick_another);
            tvAnswer.setText(mRestaurant.getName());
            rbRating.setRating(mRestaurant.getUserRating());
            rbPrice.setRating(mRestaurant.getPriceLevel());

            // Only show Get Directions button if restaurant has a valid name to search for
            if(mRestaurant.hasName()){
                btnGetDirections.setVisibility(View.VISIBLE);
            }
            else{
                btnGetDirections.setVisibility(View.GONE);
            }

            if(mHistory.size() > 0){

                // set summary fields
                setSummaryFieldsVisibility(true);
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
            if(addToHistory){
                mDatabaseHelper.addRestaurantHistory(mRestaurant.getId());
            }
        }
        else{

            // No answer
            rlAnswer.setVisibility(View.INVISIBLE);
            btnGetDirections.setVisibility(View.GONE);
            btnSelectRestaurant.setText(R.string.restaurant_selector_button);
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
//            rlRating.setVisibility(View.VISIBLE);
        }
        else{
            rlNumberVisits.setVisibility(View.GONE);
            rlLastVisit.setVisibility(View.GONE);
//            rlRating.setVisibility(View.GONE);
        }
    }

    /**
     * Reset restaurant selection
     */
    private void clearAnswer(){

        mRestaurant = null;
        mHistory = null;
        setAnswer(false);
    }

    /**
     * Open maps and search for restaurant by name
     */
    private void showOnMap(){

        if(mRestaurant.hasName()){

            try{
                // Encode restaurant name and generate maps URI
                String encodedName = URLEncoder.encode(mRestaurant.getName(), "UTF-8");
                Uri mapUri = Uri.parse("geo:0,0?q=" + encodedName);

                // Launch maps application and search by restaurant name
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(mapUri);
                startActivity(i);
            }
            catch(UnsupportedEncodingException e){
                // This shouldn't happen, do nothing.
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(ACTION_HISTORY_CLEARED)){

                // Clear answer if restaurant history has been erased
                clearAnswer();
            }
        }
    };
    
}
