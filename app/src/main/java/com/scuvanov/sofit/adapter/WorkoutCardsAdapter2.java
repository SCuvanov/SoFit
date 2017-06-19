package com.scuvanov.sofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.view.WorkoutView;

import java.util.List;


public class WorkoutCardsAdapter2 extends ArrayAdapter<WorkoutView> {

    private LayoutInflater mInflater;
    String TAG = WorkoutCardsAdapter2.class.getCanonicalName();
    Callback mCallback;
    List<WorkoutView> mViews;

    public WorkoutCardsAdapter2(Context context, List<WorkoutView> views, Callback callback) {
        super(context, 0, views);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCallback = callback;
        this.mViews = views;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        WorkoutView workoutView = getItem(position);
        convertView = null;


        if (convertView == null) {
            holder = new ViewHolder();
            if (workoutView.getWorkoutMainCategory().equals("Traditional")) {
                if (workoutView.getWorkoutType().equals("Strength")) {
                    if (workoutView.getWorkoutCategory().equals("Weightlifting")) {
                        convertView = mInflater.inflate(R.layout.single_workout_layout_traditional_weights2, parent, false);
                        holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                        holder.tvWorkoutCategory = (TextView) convertView.findViewById(R.id.textViewWorkoutCategoryItem);
                        holder.tvSetsItem = (TextView) convertView.findViewById(R.id.textViewSetsItem);
                        holder.tvRepsItem = (TextView) convertView.findViewById(R.id.textViewRepsItem);
                        holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                        holder.tvWorkoutWeight = (TextView) convertView.findViewById(R.id.textViewWorkoutWeightItem);
                        convertView.setTag(holder);
                    } else if (workoutView.getWorkoutCategory().equals("Isometric") || workoutView.getWorkoutCategory().equals("Circuit") ||
                            workoutView.getWorkoutCategory().equals("Other")) {
                        convertView = mInflater.inflate(R.layout.single_workout_layout_traditional_strength2, parent, false);
                        holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                        holder.tvWorkoutCategory = (TextView) convertView.findViewById(R.id.textViewWorkoutCategoryItem);
                        holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                        holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                        convertView.setTag(holder);
                    }
                } else if (workoutView.getWorkoutType().equals("Endurance")){
                    if(workoutView.getWorkoutCategory().equals("Running")){
                        convertView = mInflater.inflate(R.layout.single_workout_layout_traditional_endurance2, parent, false);
                        holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                        holder.tvWorkoutCategory = (TextView) convertView.findViewById(R.id.textViewWorkoutCategoryItem);
                        holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                        holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);

                    } else if (workoutView.getWorkoutCategory().equals("Other")){
                       convertView = mInflater.inflate(R.layout.single_workout_layout_traditional_endurance2, parent, false);
                        holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                        holder.tvWorkoutCategory = (TextView) convertView.findViewById(R.id.textViewWorkoutCategoryItem);
                        holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                        holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                    }
                } else if (workoutView.getWorkoutType().equals("Other")){
                    convertView = mInflater.inflate(R.layout.single_workout_layout_traditional_other2, parent, false);
                    holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                    holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                    holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                }
            } else if (workoutView.getWorkoutMainCategory().equals("CrossFit")) {
                //TODO: DEFINE CROSSFIT
                if(workoutView.getWorkoutType().equals("For Reps & Time")){
                    convertView = mInflater.inflate(R.layout.single_workout_layout_crossfit_repstime2, parent, false);
                    holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                    holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                    holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                    holder.tvSetsItem = (TextView) convertView.findViewById(R.id.textViewSetsItem);
                    holder.tvRepsItem = (TextView) convertView.findViewById(R.id.textViewRepsItem);
                } else if (workoutView.getWorkoutType().equals("For Reps")){
                    convertView = mInflater.inflate(R.layout.single_workout_layout_crossfit_reps2, parent, false);
                    holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                    holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                    holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                    holder.tvSetsItem = (TextView) convertView.findViewById(R.id.textViewSetsItem);
                    holder.tvRepsItem = (TextView) convertView.findViewById(R.id.textViewRepsItem);

                } else if (workoutView.getWorkoutType().equals("For Time")){
                    convertView = mInflater.inflate(R.layout.single_workout_layout_crossfit_time2, parent, false);
                    holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                    holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                    holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                    holder.tvSetsItem = (TextView) convertView.findViewById(R.id.textViewSetsItem);
                } else if (workoutView.getWorkoutType().equals("For Weight")){
                    convertView = mInflater.inflate(R.layout.single_workout_layout_crossfit_weight2, parent, false);
                    holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                    holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                    holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                    holder.tvSetsItem = (TextView) convertView.findViewById(R.id.textViewSetsItem);
                } else if (workoutView.getWorkoutType().equals("Other")){
                    convertView = mInflater.inflate(R.layout.single_workout_layout_traditional_other2, parent, false);
                    holder.tvWorkoutType = (TextView) convertView.findViewById(R.id.textViewWorkoutTypeItem);
                    holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
                    holder.tvDescription = (TextView) convertView.findViewById(R.id.textViewWorkoutDescriptionItem);
                }

            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (workoutView.getWorkoutMainCategory().equals("Traditional")) {
            if (workoutView.getWorkoutType().equals("Strength")) {
                if (workoutView.getWorkoutCategory().equals("Weightlifting")) {
                    holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                    holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                    holder.tvWorkoutCategory.setText(workoutView.getWorkoutCategory());
                    holder.tvWorkoutWeight.setText(workoutView.getWorkoutWeight());
                    holder.tvSetsItem.setText(workoutView.getSets());
                    holder.tvRepsItem.setText(workoutView.getReps());
                } else if (workoutView.getWorkoutCategory().equals("Isometric") || workoutView.getWorkoutCategory().equals("Circuit") ||
                        workoutView.getWorkoutCategory().equals("Other")) {
                    holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                    holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                    holder.tvWorkoutCategory.setText(workoutView.getWorkoutCategory());
                    holder.tvDescription.setText(workoutView.getWorkoutDesc());
                }
            } else if (workoutView.getWorkoutType().equals("Endurance")){
                if(workoutView.getWorkoutCategory().equals("Running")){
                    holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                    holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                    holder.tvWorkoutCategory.setText(workoutView.getWorkoutCategory());
                    holder.tvDescription.setText(workoutView.getWorkoutDistance());
                } else if (workoutView.getWorkoutCategory().equals("Other")){
                    holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                    holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                    holder.tvWorkoutCategory.setText(workoutView.getWorkoutCategory());
                    holder.tvDescription.setText(workoutView.getWorkoutDesc());
                }
            } else if (workoutView.getWorkoutType().equals("Other")){
                holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                holder.tvDescription.setText(workoutView.getWorkoutDesc());
            }
        } else if (workoutView.getWorkoutMainCategory().equals("CrossFit")) {
            //TODO: DEFINE CROSSFIT
            if(workoutView.getWorkoutType().equals("For Reps & Time")){
                holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                holder.tvSetsItem.setText(workoutView.getWorkoutTime());
                holder.tvRepsItem.setText(workoutView.getReps());
                holder.tvDescription.setText(workoutView.getWorkoutDesc());
            } else if (workoutView.getWorkoutType().equals("For Reps")){
                holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                holder.tvSetsItem.setText(workoutView.getSets());
                holder.tvRepsItem.setText(workoutView.getReps());
                holder.tvDescription.setText(workoutView.getWorkoutDesc());
            } else if (workoutView.getWorkoutType().equals("For Time")){
                holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                holder.tvSetsItem.setText(workoutView.getWorkoutTime());
                holder.tvDescription.setText(workoutView.getWorkoutDesc());
            } else if (workoutView.getWorkoutType().equals("For Weight")){
                holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                holder.tvSetsItem.setText(workoutView.getWorkoutWeight());
                holder.tvDescription.setText(workoutView.getWorkoutDesc());
            } else if (workoutView.getWorkoutType().equals("Other")){
                holder.tvWorkoutName.setText(workoutView.getWorkoutName());
                holder.tvWorkoutType.setText(workoutView.getWorkoutType());
                holder.tvDescription.setText(workoutView.getWorkoutDesc());
            }

        }

        return convertView;
    }

    @Override
    public WorkoutView getItem(final int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    public interface Callback {
        void onPressed(int pos);
    }

    private static class ViewHolder {
        public TextView tvDescription;
        public TextView tvWorkoutType;
        public TextView tvWorkoutCategory;
        public TextView tvWorkoutWeight;
        public TextView tvSetsItem;
        public TextView tvRepsItem;
        public TextView tvWorkoutName;
    }
}
