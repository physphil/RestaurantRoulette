package com.physphil.android.restaurantroulette;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.util.Util;

import java.util.List;

/**
 * Created by pshadlyn on 2/27/14.
 */
public class RestaurantSelectorFragment extends Fragment {

    private DatabaseHelper mDatabaseHelper;
    private Spinner spinnerGenre;
    private Button btnSelectRestaurant;
    private TextView tvAnswer;
    private TextView tvSummary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_restaurant_selector, container, false);

        btnSelectRestaurant = (Button) v.findViewById(R.id.btn_select_restaurant);
        spinnerGenre = (Spinner) v.findViewById(R.id.spinner_restaurant_genre);
        tvAnswer = (TextView) v.findViewById(R.id.restaurant_selector_answer);
        tvSummary = (TextView) v.findViewById(R.id.restaurant_selector_summary);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());

        initViewContent();
    }

    private void initViewContent(){

        btnSelectRestaurant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });

        List<String> genres = Util.getGenresForAdapter(getActivity());
        spinnerGenre.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres));
    }
}
