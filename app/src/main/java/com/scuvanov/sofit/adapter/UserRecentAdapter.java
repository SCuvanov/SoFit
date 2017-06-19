package com.scuvanov.sofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.model.Workout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 2/28/2015.
 */
public class UserRecentAdapter extends RecyclerView.Adapter<UserRecentAdapter.UserRecentViewHolder> {
    String TAG = WorkoutAdapter.class.getCanonicalName();
    private List<Workout> mWorkouts = new ArrayList<Workout>();
    private List<String> mWorkoutIds = new ArrayList<>();

    private Context mContext;
    private static FirebaseUser mUser;
    private Query mQuery;

    private UserRecentAdapter.UserRecentAdapterOnItemClickListener mListener;

    public UserRecentAdapter(Context context, UserRecentAdapter.UserRecentAdapterOnItemClickListener listener) {
        mContext = context;
        mListener = listener;

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mQuery = FirebaseDatabase.getInstance().getReference()
                .child("user_workouts").child(mUser.getUid()).limitToLast(50);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Workout workout = dataSnapshot.getValue(Workout.class);
                mWorkouts.add(0, workout);
                mWorkoutIds.add(0, dataSnapshot.getKey());
                notifyItemInserted(mWorkouts.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Workout workout = dataSnapshot.getValue(Workout.class);
                String workoutKey = dataSnapshot.getKey();

                int workoutIndex = mWorkoutIds.indexOf(workoutKey);
                if (workoutIndex > -1) {
                    mWorkouts.set(workoutIndex, workout);

                    notifyItemChanged(workoutIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String workoutKey = dataSnapshot.getKey();

                int workoutIndex = mWorkoutIds.indexOf(workoutKey);
                if (workoutIndex > -1) {
                    mWorkoutIds.remove(workoutIndex);
                    mWorkouts.remove(workoutIndex);

                    notifyItemRemoved(workoutIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Workout movedWorkout = dataSnapshot.getValue(Workout.class);
                String workoutKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mQuery.addChildEventListener(childEventListener);
    }

    @Override
    public UserRecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_recent_favorites, parent, false);
        return new UserRecentAdapter.UserRecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserRecentViewHolder holder, int position) {
        final Workout workout = mWorkouts.get(position);
        holder.tvTitle.setText(workout.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(workout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    public static class UserRecentViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;

        public UserRecentViewHolder(View workoutView) {
            super(workoutView);

            tvTitle = (TextView) workoutView.findViewById(R.id.textViewTitle);
        }
    }

    public interface UserRecentAdapterOnItemClickListener {
        void onClick(Workout workout);
    }
}