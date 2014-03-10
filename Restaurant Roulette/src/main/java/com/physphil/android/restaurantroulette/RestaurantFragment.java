package com.physphil.android.restaurantroulette;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.ui.CustomFontArrayAdapter;
import com.physphil.android.restaurantroulette.util.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantFragment extends Fragment {

    public static final String ACTION_DELETE_RESTAURANT = "com.physphil.android.restaurantroulette.ACTION_DELETE_RESTAURANT";
    public static final String EXTRA_RESTAURANT_ID = "com.physphil.android.restaurantroulette.EXTRA_RESTAURANT_ID";
    public static final String EXTRA_UPDATED = "com.physphil.android.restaurantroulette.EXTRA_UPDATED";

    private DatabaseHelper mDatabaseHelper;
    private Restaurant mRestaurant;
    private EditText etName;
    private Spinner spinnerGenre;
    private RatingBar rbUserRating;
    private RatingBar rbPriceLevel;
    private EditText etNotes;
    private Button btnDirections;
    private Typeface mTf;
    /**
     * Keeps track of if restaurant entry was modified by user
     */
    private boolean mUpdated;

    public RestaurantFragment(){}

    /**
     * @param id database id of restaurant to show
     * @return new RestaurantFragment
     */
    public static RestaurantFragment newInstance(String id){

        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();

        args.putString(EXTRA_RESTAURANT_ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);

        mTf = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_DEFAULT);

        etName = (EditText) v.findViewById(R.id.restaurant_name);
        spinnerGenre = (Spinner) v.findViewById(R.id.spinner_restaurant_genre);
        rbUserRating = (RatingBar) v.findViewById(R.id.rating_bar_restaurant);
        rbPriceLevel = (RatingBar) v.findViewById(R.id.price_restaurant);
        etNotes = (EditText) v.findViewById(R.id.restaurant_notes);
        btnDirections = (Button) v.findViewById(R.id.btn_get_directions);

        setFonts(v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        String id;

        if(savedInstanceState != null){
            id = savedInstanceState.getString(EXTRA_RESTAURANT_ID);
            mUpdated = savedInstanceState.getBoolean(EXTRA_UPDATED, false);
        }
        else{
            id = getArguments().getString(EXTRA_RESTAURANT_ID);
            mUpdated = false;
        }

        // If a valid restaurant id from the db exists, populate views with restaurant info
        if(id != null){

            // Get existing object from database
            mRestaurant = mDatabaseHelper.getRestaurantById(id);
        }
        else{

            mRestaurant = new Restaurant();
        }

        initializeViewContent();
    }

    @Override
    public void onPause(){
        super.onPause();

        // Save all entered restaurant info to database
//        mRestaurant.setName(etName.getText().toString());
//        mRestaurant.setNotes(etNotes.getText().toString());

        // Only save to db if entry was updated by user
        if(mUpdated){

            // Save all entered restaurant info to db
            mDatabaseHelper.addRestaurant(mRestaurant);

            // Send broadcast to update listview
            Intent i = new Intent(RestaurantListFragment.ACTION_UPDATE_RESTAURANT_LIST);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save restaurant id for recreation
        outState.putString(EXTRA_RESTAURANT_ID, mRestaurant.getId());
        outState.putBoolean(EXTRA_UPDATED, mUpdated);
    }

    /**
     * Set default font for all views
     * @param v parent view containing all views which require font to be set
     */
    private void setFonts(View v){

        // Set font for text fields
        etName.setTypeface(mTf);
        etNotes.setTypeface(mTf);
        btnDirections.setTypeface(mTf);

        // Set font for static text
        ((TextView) v.findViewById(R.id.restaurant_name_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_genre_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_rating_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_price_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_notes_text)).setTypeface(mTf);
    }

    private void initializeViewContent(){

        etName.setText(mRestaurant.getName());
        etName.setSelection(etName.getText().length());
        etNotes.setText(mRestaurant.getNotes());

        // Add text watchers to update mRestaurant object when edited by user
        etName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mRestaurant.setName(s.toString());
                mUpdated = true;
                Log.v("PS", "name changed");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mRestaurant.setNotes(s.toString());
                mUpdated = true;
                Log.v("PS", "notes changed");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        rbUserRating.setRating(mRestaurant.getUserRating());
        rbUserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Set new rating every time it changes
                mRestaurant.setUserRating((int) rating);
                mUpdated = true;
                Log.v("PS", "rating changed");
            }
        });

        rbPriceLevel.setRating(mRestaurant.getPriceLevel());
        rbPriceLevel.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Set new price level every time it changes
                mRestaurant.setPriceLevel((int) rating);
                mUpdated = true;
                Log.v("PS", "price changed");
            }
        });

        // Set spinner adapter and initialize.
        String[] genres = getResources().getStringArray(R.array.genres);
        spinnerGenre.setAdapter(new CustomFontArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres));
        spinnerGenre.setSelection(getIndex(spinnerGenre, mRestaurant.getGenre()), false);

        // Add listener to set genre when changed
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {

                mRestaurant.setGenre((String) spinner.getItemAtPosition(position));
                mUpdated = true;
                Log.v("PS", "genre changed");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Get directions button
        btnDirections.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               showOnMap();
            }
        });
    }

    /**
     * Gets position of genre in spinner
     * @param spinner
     * @param genre
     * @return index of Genre in Spinner, returns index of Other if not found or if genre is null
     */
    private int getIndex(Spinner spinner, String genre){

        if(genre != null){

            for(int i = 0; i < spinner.getCount(); i++){

                if(spinner.getItemAtPosition(i).equals(genre)){

                    return i;
                }
            }
        }

        // Item not found, set as 'Other', which is always last position in spinner
        return spinner.getCount() - 1;
    }

    /**
     * Search for restaurant on map based on entered name
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
        else{

            Toast.makeText(getActivity(), R.string.toast_no_restaurant_name, Toast.LENGTH_SHORT).show();
        }
    }
}
