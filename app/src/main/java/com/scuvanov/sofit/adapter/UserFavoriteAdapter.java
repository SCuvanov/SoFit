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
import com.scuvanov.sofit.model.Favorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 2/28/2015.
 */
public class UserFavoriteAdapter extends RecyclerView.Adapter<UserFavoriteAdapter.UserFavoriteViewHolder> {
    String TAG = WorkoutAdapter.class.getCanonicalName();
    private List<Favorite> mFavorites = new ArrayList<Favorite>();
    private List<String> mFavoriteIds = new ArrayList<>();

    private Context mContext;
    private static FirebaseUser mUser;
    private Query mQuery;

    private UserFavoriteAdapter.UserFavoriteAdapterOnItemClickListener mListener;

    public UserFavoriteAdapter(Context context, UserFavoriteAdapterOnItemClickListener listener) {
        mContext = context;
        mListener = listener;

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mQuery = FirebaseDatabase.getInstance().getReference()
                .child("favorites").child(mUser.getUid()).limitToLast(50);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Favorite favorite = dataSnapshot.getValue(Favorite.class);
                mFavorites.add(0, favorite);
                mFavoriteIds.add(0, dataSnapshot.getKey());
                notifyItemInserted(mFavorites.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Favorite favorite = dataSnapshot.getValue(Favorite.class);
                String favoriteKey = dataSnapshot.getKey();

                int favoriteIndex = mFavoriteIds.indexOf(favoriteKey);
                if (favoriteIndex > -1) {
                    mFavorites.set(favoriteIndex, favorite);

                    notifyItemChanged(favoriteIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String favoriteKey = dataSnapshot.getKey();

                int favoriteIndex = mFavoriteIds.indexOf(favoriteKey);
                if (favoriteIndex > -1) {
                    mFavoriteIds.remove(favoriteIndex);
                    mFavorites.remove(favoriteIndex);

                    notifyItemRemoved(favoriteIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Favorite movedFavorite = dataSnapshot.getValue(Favorite.class);
                String favoriteKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mQuery.addChildEventListener(childEventListener);
    }

    @Override
    public UserFavoriteAdapter.UserFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_recent_favorites, parent, false);
        return new UserFavoriteAdapter.UserFavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserFavoriteAdapter.UserFavoriteViewHolder holder, int position) {
        final Favorite favorite = mFavorites.get(position);
        holder.tvTitle.setText(favorite.getWorkout_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(favorite);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    public static class UserFavoriteViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;

        public UserFavoriteViewHolder(View workoutView) {
            super(workoutView);

            tvTitle = (TextView) workoutView.findViewById(R.id.textViewTitle);
        }
    }

    public interface UserFavoriteAdapterOnItemClickListener {
        void onClick(Favorite favorite);
    }
}