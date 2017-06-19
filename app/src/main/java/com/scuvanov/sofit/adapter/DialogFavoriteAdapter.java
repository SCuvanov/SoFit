package com.scuvanov.sofit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.model.Workout;

import java.util.List;

/**
 * Created by Sean on 2/28/2015.
 */
public class DialogFavoriteAdapter extends ArrayAdapter<Workout> {
    String TAG = WorkoutAdapter.class.getCanonicalName();
    private LayoutInflater mInflater;
    private Gson gson;
    List<Workout> mViews;

    public DialogFavoriteAdapter(Context context, List<Workout> views) {
        super(context, 0, views);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViews = views;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        Workout singleWorkout = getItem(position);
        convertView = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dialog_text_item, parent, false);
            holder = new ViewHolder();
            holder.tvWorkoutTitle = (TextView) convertView.findViewById(R.id.textName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvWorkoutTitle.setText(singleWorkout.getTitle());

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvWorkoutTitle;
    }
}