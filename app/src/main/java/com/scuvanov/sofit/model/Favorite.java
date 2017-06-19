package com.scuvanov.sofit.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Favorite implements Serializable {

    private String id;
    private String uid;
    private String workout_id;
    private String workout_title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(String workout_id) {
        this.workout_id = workout_id;
    }

    public String getWorkout_title() {
        return workout_title;
    }

    public void setWorkout_title(String workout_title) {
        this.workout_title = workout_title;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("uid", uid);
        result.put("workout_id", workout_id);
        result.put("workout_title", workout_title);
        return result;
    }
}
