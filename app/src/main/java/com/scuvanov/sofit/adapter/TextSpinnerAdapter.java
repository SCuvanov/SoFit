package com.scuvanov.sofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scuvanov.sofit.R;

/**
 * Created by Sean on 2/28/2015.
 */
public class TextSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    String[] array;
    int resourceId;

    public TextSpinnerAdapter(Context context, int resourceId,
                               String[] array) {
        // TODO Auto-generated constructor stub

        super(context, resourceId, array);
        this.context = context;
        this.array = array;
    }



    /* private view holder class */
    private class ViewHolder {
        TextView tvName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String text = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.spinner_text_item, null);
            viewHolder.tvName = (TextView) convertView
                    .findViewById(R.id.textName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvName.setText(text);
         // Return the completed view to render on screen
        return convertView;

    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        String text = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.spinner_text_item, null);
            viewHolder.tvName = (TextView) convertView
                    .findViewById(R.id.textName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvName.setText(text);
        // Return the completed view to render on screen
        return convertView;
    }
}