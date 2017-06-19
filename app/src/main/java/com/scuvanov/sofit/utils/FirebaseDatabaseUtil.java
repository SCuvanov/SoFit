package com.scuvanov.sofit.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scuvanov.sofit.model.Comment;
import com.scuvanov.sofit.model.Favorite;
import com.scuvanov.sofit.model.Workout;

/**
 * Created by Sean on 6/6/2017.
 */

public class FirebaseDatabaseUtil {

    private static FirebaseUser mUser;
    private static DatabaseReference mDatabase;

    public static void createWorkout(Workout workout) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String key = mDatabase.child("workouts").push().getKey();
        workout.setId(key);

        mDatabase.child("workouts").child(key).setValue(workout);
        mDatabase.child("user_workouts").child(mUser.getUid()).child(key).setValue(workout);
    }

    public static void favoriteWorkout(Favorite favorite) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String key = mDatabase.child("favorites").push().getKey();
        favorite.setId(key);
        mDatabase.child("favorites").child(mUser.getUid()).child(favorite.getWorkout_id()).setValue(favorite);
    }

    public static void unfavoriteWorkout(String workout_id) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("favorites").child(mUser.getUid()).child(workout_id).removeValue();
    }

    public static void createComment(Comment comment, String workout_id) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String key = mDatabase.child("comments").child(workout_id).push().getKey();
        comment.setId(key);
        mDatabase.child("comments").child(workout_id).child(key).setValue(comment);
    }
}
