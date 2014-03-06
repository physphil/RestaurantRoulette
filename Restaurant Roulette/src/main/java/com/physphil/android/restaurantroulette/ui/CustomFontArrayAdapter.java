package com.physphil.android.restaurantroulette.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.util.Constants;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Override getView and getDropDownView methods to replace font
 * Created by pshadlyn on 3/4/14.
 */
public class CustomFontArrayAdapter extends ArrayAdapter<String> {

    private Typeface mTf;

    public CustomFontArrayAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);

        mTf = Typeface.createFromAsset(context.getAssets(), Constants.FONT_DEFAULT);
    }

    public CustomFontArrayAdapter(Context context, int resource, String[] items){
        super(context, resource, items);

        mTf = Typeface.createFromAsset(context.getAssets(), Constants.FONT_DEFAULT);
    }

    public CustomFontArrayAdapter(Context context, int resource, int textViewResource, String[] items){
        super(context, resource, textViewResource, items);

        mTf = Typeface.createFromAsset(context.getAssets(), Constants.FONT_DEFAULT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = super.getView(position, convertView, parent);
        ((TextView) v).setTypeface(mTf);
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View v = super.getDropDownView(position, convertView, parent);
        ((TextView) v).setTypeface(mTf);
        return v;
    }
}
