package com.scuvanov.sofit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.scuvanov.sofit.model.Workout;

/**
 * Created by Sean on 3/2/2015.
 */
public class WorkoutView extends LinearLayout {

    private String workoutName, muscleGroup, sets, reps, workoutMainCategory, workoutType, workoutCategory, workoutWeight, workoutDesc, workoutTime, workoutDistance;
    int id;
    Context mContext;

    public WorkoutView(Context context) {
        super(context);
        mContext = context;

    }

    public WorkoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public WorkoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWorkoutMainCategory() {
        return workoutMainCategory;
    }

    public void setWorkoutMainCategory(String workoutMainCategory) {
        this.workoutMainCategory = workoutMainCategory;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getWorkoutCategory() {
        return workoutCategory;
    }

    public void setWorkoutCategory(String workoutCategory) {
        this.workoutCategory = workoutCategory;
    }

    public String getWorkoutWeight() {
        return workoutWeight;
    }

    public void setWorkoutWeight(String workoutWeight) {
        this.workoutWeight = workoutWeight;
    }

    public String getWorkoutDesc() {
        return workoutDesc;
    }

    public void setWorkoutDesc(String workoutDesc) {
        this.workoutDesc = workoutDesc;
    }

    public String getWorkoutTime() {
        return workoutTime;
    }

    public void setWorkoutTime(String workoutTime) {
        this.workoutTime = workoutTime;
    }

    public String getWorkoutDistance() {
        return workoutDistance;
    }

    public void setWorkoutDistance(String workoutDistance) {
        this.workoutDistance = workoutDistance;
    }

    public Workout getAll() {
        //to be designed
        return null;
    }
}
