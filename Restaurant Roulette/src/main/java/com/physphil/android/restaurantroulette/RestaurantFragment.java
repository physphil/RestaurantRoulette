package com.physphil.android.restaurantroulette;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.ui.CustomFontSpinnerAdapter;
import com.physphil.android.restaurantroulette.util.Constants;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantFragment extends Fragment {

    public static final String ACTION_DELETE_RESTAURANT = "com.physphil.android.restaurantroulette.ACTION_DELETE_RESTAURANT";
    public static final String EXTRA_RESTAURANT_ID = "com.physphil.android.restaurantroulette.EXTRA_RESTAURANT_ID";

    private DatabaseHelper mDatabaseHelper;
    private Restaurant mRestaurant;
    private EditText etName;
    private Spinner spinnerGenre;
    private RatingBar ratingBarUserRating;
    private EditText etNotes;
    private Typeface mTf;

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
        ratingBarUserRating = (RatingBar) v.findViewById(R.id.rating_bar_restaurant);
        etNotes = (EditText) v.findViewById(R.id.restaurant_notes);

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
        }
        else{
            id = getArguments().getString(EXTRA_RESTAURANT_ID);
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
        mRestaurant.setName(etName.getText().toString());
        mRestaurant.setNotes(etNotes.getText().toString());

        mDatabaseHelper.addRestaurant(mRestaurant);

        // Send broadcast to update listview
        Intent i = new Intent(RestaurantListFragment.ACTION_UPDATE_RESTAURANT_LIST);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save restaurant id for recreation
        outState.putString(EXTRA_RESTAURANT_ID, mRestaurant.getId());
    }

    /**
     * Set default font for all views
     * @param v parent view containing all views which require font to be set
     */
    private void setFonts(View v){

        // Set font for text fields
        etName.setTypeface(mTf);
        etNotes.setTypeface(mTf);

        // Set font for static text
        ((TextView) v.findViewById(R.id.restaurant_name_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_genre_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_rating_text)).setTypeface(mTf);
        ((TextView) v.findViewById(R.id.restaurant_notes_text)).setTypeface(mTf);
    }

    private void initializeViewContent(){

        etName.setText(mRestaurant.getName());
        etName.setSelection(etName.getText().length());
        etNotes.setText(mRestaurant.getNotes());

        ratingBarUserRating.setRating(mRestaurant.getUserRating());
        ratingBarUserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Set new rating every time it changes
                mRestaurant.setUserRating((int) rating);
            }
        });

        // Set spinner adapter and initialize.
        String[] genres = getResources().getStringArray(R.array.genres);
        spinnerGenre.setAdapter(new CustomFontSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres));
        spinnerGenre.setSelection(getIndex(spinnerGenre, mRestaurant.getGenre()));

        // Add listener to set genre when changed
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {

                mRestaurant.setGenre((String) spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
}
