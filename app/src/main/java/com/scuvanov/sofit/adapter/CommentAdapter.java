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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.model.Comment;
import com.scuvanov.sofit.model.Workout;
import com.scuvanov.sofit.utils.FirebaseStorageUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 3/21/2015.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    String TAG = CommentAdapter.class.getCanonicalName();
    private Context mContext;
    private Workout mWorkout;
    private Query mQuery;
    private List<Comment> mComments = new ArrayList<Comment>();
    private List<String> mCommentIds = new ArrayList<>();

    public CommentAdapter(Context context, Workout workout) {
        mContext = context;
        mWorkout = workout;

        mQuery = FirebaseDatabase.getInstance().getReference()
                .child("comments").child(mWorkout.getId()).limitToLast(50);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                mComments.add(0, comment);
                mCommentIds.add(0, dataSnapshot.getKey());
                notifyItemInserted(mComments.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    // Replace with the new data
                    mComments.set(commentIndex, comment);
                    // Update the RecyclerView
                    notifyItemChanged(commentIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String commentKey = dataSnapshot.getKey();

                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    // Remove data from the list
                    mCommentIds.remove(commentIndex);
                    mComments.remove(commentIndex);
                    // Update the RecyclerView
                    notifyItemRemoved(commentIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Comment movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mQuery.addChildEventListener(childEventListener);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        final Comment comment = mComments.get(position);

        holder.tvComment.setText(comment.getComment());

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        holder.tvCreatedDate.setText(formatter.format(comment.getCreated_date()));

        FirebaseStorageUtil.getProfilePhoto(comment.getUid(), new FirebaseStorageUtil.FirebaseStorageUtilCallback() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.ivImage.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception exception) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;
        public TextView tvComment;
        public TextView tvCreatedDate;

        public CommentViewHolder(View commentView) {
            super(commentView);

            ivImage = (ImageView) commentView.findViewById(R.id.imageViewProfilePic);
            tvComment = (TextView) commentView.findViewById(R.id.textViewComment);
            tvCreatedDate = (TextView) commentView.findViewById(R.id.textViewCommentDate);
        }
    }
}
