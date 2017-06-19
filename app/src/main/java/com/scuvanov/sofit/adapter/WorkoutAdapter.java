package com.scuvanov.sofit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.model.Workout;
import com.scuvanov.sofit.utils.DateUtil;
import com.scuvanov.sofit.utils.FirebaseStorageUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sean on 3/21/2015.
 */
public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    final String TAG = WorkoutAdapter.class.getCanonicalName();

    private Context mContext;
    private Boolean mIsPersonal = false;
    private Date mDate;
    private Query mQuery;
    private List<Workout> mWorkouts = new ArrayList<Workout>();
    private List<String> mWorkoutIds = new ArrayList<>();
    private WorkoutAdapterOnItemClickListener mListener;

    private static FirebaseUser mUser;

    public WorkoutAdapter(Context context, WorkoutAdapterOnItemClickListener listener) {
        this(context, null, null, listener);
    }

    public WorkoutAdapter(Context context, Boolean isPersonal, Date date, WorkoutAdapterOnItemClickListener listener) {
        mContext = context;
        mListener = listener;
        mIsPersonal = isPersonal;
        mDate = date;

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mIsPersonal)
            mQuery = FirebaseDatabase.getInstance().getReference()
                    .child("user_workouts").child(mUser.getUid()).limitToLast(50);
        if(!mIsPersonal)
            mQuery = FirebaseDatabase.getInstance().getReference()
                    .child("workouts").limitToLast(50);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Workout workout = dataSnapshot.getValue(Workout.class);

                if(mIsPersonal) {
                    String formattedWorkoutDate = DateUtil.formatDate(workout.getWorkout_date());
                    String formattedGivenDate = DateUtil.formatDate(mDate);
                    if(formattedWorkoutDate.equals(formattedGivenDate)){
                        mWorkouts.add(0, workout);
                        mWorkoutIds.add(0, dataSnapshot.getKey());
                        notifyItemInserted(mWorkouts.size() - 1);
                    }
                } else {
                    mWorkouts.add(0, workout);
                    mWorkoutIds.add(0, dataSnapshot.getKey());
                    notifyItemInserted(mWorkouts.size() - 1);
                }
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

                int commentIndex = mWorkoutIds.indexOf(workoutKey);
                if (commentIndex > -1) {
                    mWorkoutIds.remove(commentIndex);
                    mWorkouts.remove(commentIndex);

                    notifyItemRemoved(commentIndex);
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
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_cards, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WorkoutViewHolder holder, int position) {
        final Workout workout = mWorkouts.get(position);
        holder.tvTitle.setText(workout.getTitle());
        holder.tvTime.setText(workout.getTime());
        holder.tvDifficulty.setText(workout.getDifficulty());

        String pictureVal1 = "w" + workout.getWorkout_pictures().get(0);
        String pictureVal2 = "w" + workout.getWorkout_pictures().get(1);
        String pictureVal3 = "w" + workout.getWorkout_pictures().get(2);

        holder.ivPeek1.setImageResource(mContext.getResources().getIdentifier(pictureVal1, "drawable", mContext.getPackageName()));
        holder.ivPeek2.setImageResource(mContext.getResources().getIdentifier(pictureVal2, "drawable", mContext.getPackageName()));
        holder.ivPeek3.setImageResource(mContext.getResources().getIdentifier(pictureVal3, "drawable", mContext.getPackageName()));

        FirebaseStorageUtil.getProfilePhoto(workout.getUid(), new FirebaseStorageUtil.FirebaseStorageUtilCallback() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.ivImage.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception exception) {
            }
        });

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


    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage, ivPeek1, ivPeek2, ivPeek3;
        public TextView tvTitle, tvDifficulty, tvTime;

        public WorkoutViewHolder(View workoutView) {
            super(workoutView);

            ivImage = (ImageView) workoutView.findViewById(R.id.imageViewProfilePic);
            ivPeek1 = (ImageView) workoutView.findViewById(R.id.imageViewPeek1);
            ivPeek2 = (ImageView) workoutView.findViewById(R.id.imageViewPeek2);
            ivPeek3 = (ImageView) workoutView.findViewById(R.id.imageViewPeek3);
            tvTitle = (TextView) workoutView.findViewById(R.id.textViewTitle);
            tvDifficulty = (TextView) workoutView.findViewById(R.id.textViewDifficulty);
            tvTime = (TextView) workoutView.findViewById(R.id.textViewTime);
        }
    }

    public interface WorkoutAdapterOnItemClickListener {
        void onClick(Workout workout);
    }
}
