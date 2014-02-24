package com.physphil.android.restaurantroulette;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.physphil.android.restaurantroulette.models.Restaurant;

import java.util.UUID;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantFragment extends Fragment {

    public static final int NEW_RESTAURAUNT = -1;
    public static final String EXTRA_RESTAURANT_ID = "com.physphil.android.restaurantroulette.EXTRA_RESTAURANT_ID";

    private Restaurant mRestaurant;
    private EditText mName;
    private Spinner mGenre;
    private RatingBar mRating;
    private EditText mNotes;

    public RestaurantFragment(){}

    /**
     * @param id database id of restaurant to show
     * @return new RestaurantFragment
     */
    public static RestaurantFragment newInstance(int id){

        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();

        args.putInt(EXTRA_RESTAURANT_ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);

        mName = (EditText) v.findViewById(R.id.restaurant_name);
        mGenre = (Spinner) v.findViewById(R.id.spinner_restaurant_genre);
        mRating = (RatingBar) v.findViewById(R.id.rating_bar_restaurant);
        mNotes = (EditText) v.findViewById(R.id.restaurant_notes);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        int id = getArguments().getInt(EXTRA_RESTAURANT_ID, NEW_RESTAURAUNT);

        // A valid restaurant id from the db was passed in, populate views with restaurant info
        if(id != NEW_RESTAURAUNT){


        }
    }

    @Override
    public void onPause(){
        super.onPause();

        // Save all entered restaurant info to database
        UUID id = UUID.randomUUID();

    }
}
