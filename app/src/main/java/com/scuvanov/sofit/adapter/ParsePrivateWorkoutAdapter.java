package com.scuvanov.sofit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.model.Workout;

import java.util.List;

/**
 * Created by Sean on 3/21/2015.
 */
public class ParsePrivateWorkoutAdapter extends ArrayAdapter<Workout> {
    String TAG = ParsePrivateWorkoutAdapter.class.getCanonicalName();
    private LayoutInflater mInflater;
    private Gson gson;
    List<Workout> mViews;

    public ParsePrivateWorkoutAdapter(Context context, List<Workout> views) {
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
            convertView = mInflater.inflate(R.layout.list_item_cards, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.imageViewProfilePic);
            holder.ivPeek1 = (ImageView) convertView.findViewById(R.id.imageViewPeek1);
            holder.ivPeek2 = (ImageView) convertView.findViewById(R.id.imageViewPeek2);
            holder.ivPeek3 = (ImageView) convertView.findViewById(R.id.imageViewPeek3);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.tvDifficulty = (TextView) convertView.findViewById(R.id.textViewDifficulty);
            holder.tvTime = (TextView) convertView.findViewById(R.id.textViewTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(singleWorkout.getTitle());
        holder.tvTime.setText(singleWorkout.getTime());
        holder.tvDifficulty.setText(singleWorkout.getDifficulty());

/*        try {
            //JSONArray mJsonArray = new JSONArray(singleWorkout.get_pictureArrayJSON());
            String pictureVal1 = "w" +mJsonArray.getString(0);
            String pictureVal2 = "w" + mJsonArray.getString(1);
            String pictureVal3 = "w" + mJsonArray.getString(2);

            holder.ivPeek1.setImageResource(getContext().getResources().getIdentifier(pictureVal1, "drawable", getContext().getPackageName()));
            holder.ivPeek2.setImageResource(getContext().getResources().getIdentifier(pictureVal2, "drawable", getContext().getPackageName()));
            holder.ivPeek3.setImageResource(getContext().getResources().getIdentifier(pictureVal3, "drawable", getContext().getPackageName()));


        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        //get user profile pic
        //Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
        //holder.ivImage.setImageBitmap(bitmap);

        //still need to populate the ivPeek with icons

        return convertView;
    }

    private static class ViewHolder {
        public ImageView ivImage, ivPeek1, ivPeek2, ivPeek3;
        public TextView tvTitle, tvDifficulty, tvTime;
    }
}
