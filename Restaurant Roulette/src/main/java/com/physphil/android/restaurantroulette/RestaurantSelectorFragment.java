package com.physphil.android.restaurantroulette;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.physphil.android.restaurantroulette.util.LocationHelper;
import com.physphil.android.restaurantroulette.util.Util;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pshadlyn on 2/27/14.
 */
public class RestaurantSelectorFragment extends Fragment {

    public static String PREFS_GENRE_FILTER_SELECTOR = "genre_filter_selector";
    public static String PREFS_SHOW_HELP_RESTAURANT_SELECTOR = "show_help_selector";

    private Restaurant mRestaurant;
    private List<RestaurantHistory> mHistory;
    private DatabaseHelper mDatabaseHelper;
    private LocationHelper mLocationHelper;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());

        lbm.registerReceiver(mReceiver, new IntentFilter(HistoryListFragment.ACTION_HISTORY_CLEARED));
        lbm.registerReceiver(mReceiver, new IntentFilter(LocationHelper.ACTION_LOCATION_RETRIEVED));
        lbm.registerReceiver(mReceiver, new IntentFilter(RestaurantFragment.ACTION_RESTAURANT_UPDATED));
    }

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
        setHasOptionsMenu(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        mLocationHelper = new LocationHelper(getActivity());
        mFilter = prefs.getInt(PREFS_GENRE_FILTER_SELECTOR, Restaurant.GENRE_ALL);

        initViewContent();

        // Set answer through fragment objects mRestaurant and mHistory. Don't save to history as no new selection was made
        setAnswer(false);
    }

    @Override
    public void onResume(){
        super.onResume();

        // Show help menu if never been shown
        boolean showHelp = prefs.getBoolean(PREFS_SHOW_HELP_RESTAURANT_SELECTOR, true);
        if(showHelp){

            showHelpDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

//                Util.showOnMap(getActivity(), mRestaurant.getName());
                mLocationHelper.connectAndGetLocation();
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rlAnswer.setOnClickListener(new View.OnClickListener() {

            // Open restaurant detail view when touched
            @Override
            public void onClick(View v) {

                Intent i = RestaurantActivity.getLaunchingIntent(getActivity(), mRestaurant.getRestaurantId());
                startActivity(i);
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
            mHistory = mDatabaseHelper.getHistoryByRestaurant(mRestaurant.getRestaurantId());
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
                mDatabaseHelper.addRestaurantHistory(mRestaurant.getRestaurantId());
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

    private void showHelpDialog(){

        Util.showHelpDialog(getActivity(), R.string.title_restaurant_selector, R.string.dialog_restaurant_selector_help, PREFS_SHOW_HELP_RESTAURANT_SELECTOR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.menu_help:
                showHelpDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(HistoryListFragment.ACTION_HISTORY_CLEARED)){

                // Clear answer if restaurant history has been erased
                clearAnswer();
            }
            else if(intent.getAction().equals(LocationHelper.ACTION_LOCATION_RETRIEVED)){

                Location location = intent.getParcelableExtra(LocationHelper.EXTRA_LOCATION);
                Util.showOnMap(getActivity(), mRestaurant.getName(), location);
                mLocationHelper.disconnect();
            }
            else if(intent.getAction().equals(RestaurantFragment.ACTION_RESTAURANT_UPDATED)){

                // Update restaurant info from db, update fields in answer card
                String id = mRestaurant.getRestaurantId();
                mRestaurant = mDatabaseHelper.getRestaurantById(id);
                setAnswer(false);
            }
        }
    };
    
}
