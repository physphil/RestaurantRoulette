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

package com.physphil.android.restaurantroulette.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.util.Constants;

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
