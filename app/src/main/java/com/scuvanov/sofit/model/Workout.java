package com.scuvanov.sofit.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Workout implements Serializable {

    public String id;
    public String uid;
    public String title, difficulty, time;
    public ArrayList<WorkoutItem> workout_items;
    public ArrayList<Integer> workout_pictures;
    public Map<String, Boolean> likes = new HashMap<String, Boolean>();
    public Map<String, Boolean> favorites = new HashMap<String, Boolean>();;
    public Date workout_date;
    public Date created_date;
    public Integer like_count = 0;
    public Integer favorite_count = 0;
    public Integer comment_count = 0;

    public Workout() {
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<WorkoutItem> getWorkout_items() {
        return workout_items;
    }

    public void setWorkout_items(ArrayList<WorkoutItem> workout_items) {
        this.workout_items = workout_items;
    }

    public ArrayList<Integer> getWorkout_pictures() {
        return workout_pictures;
    }

    public void setWorkout_pictures(ArrayList<Integer> workout_pictures) {
        this.workout_pictures = workout_pictures;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public Map<String, Boolean> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }

    public Date getWorkout_date() {
        return workout_date;
    }

    public void setWorkout_date(Date workout_date) {
        this.workout_date = workout_date;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }

    public Integer getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("uid", uid);
        result.put("created_date", created_date);
        result.put("title", title);
        result.put("difficulty", difficulty);
        result.put("time", time);
        result.put("workout_date", workout_date);
        result.put("workout_items", workout_items);
        result.put("workout_pictures", workout_pictures);
        result.put("likes", likes);
        result.put("favorites", favorites);
        result.put("like_count", like_count);
        result.put("favorite_count", favorite_count);
        result.put("comment_count", comment_count);
        return result;
    }
}
