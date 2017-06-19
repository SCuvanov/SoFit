package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.adapter.WorkoutCardsAdapter2;
import com.scuvanov.sofit.model.Favorite;
import com.scuvanov.sofit.model.Workout;
import com.scuvanov.sofit.model.WorkoutItem;
import com.scuvanov.sofit.utils.FirebaseDatabaseUtil;
import com.scuvanov.sofit.utils.FirebaseStorageUtil;
import com.scuvanov.sofit.view.WorkoutView;

import java.util.ArrayList;

public class WorkoutDetailFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
    private String TAG = WorkoutDetailFragment.class.getCanonicalName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static final String BUNDLE_WORKOUT = "WORKOUT";
    private static final String BUNDLE_FAVORITE = "FAVORITE";
    private static final String WORKOUT_ITEM = "WORKOUTITEM";
    private static final String COMMENT_FRAGMENT = "COMMENTFRAGMENT";
    private static final String WORKOUT_ITEM_DETAIL_FRAGMENT = "WORKOUTITEMDETAILFRAGMENT";

    private OnFragmentInteractionListener mListener;
    private ImageView ivLike, ivFavorite, ivChat, ivProfilePic;
    private TextView tvTitle, tvDifficulty, tvTime, tvLike, tvFavorite, tvComment;
    private View view;
    private Boolean isLikeChecked = false;
    private Boolean isFavoriteChecked = false;
    private ListView mListView;
    private WorkoutCardsAdapter2 mWorkoutCardsAdapter;
    private ArrayList<WorkoutView> mArrayList = new ArrayList<WorkoutView>();

    private static Workout mWorkout;
    private static Favorite mFavorite;

    private Button btnBack;

    //Firebase
    private static FirebaseUser mUser;
    private static DatabaseReference mDatabaseReference;
    ValueEventListener workoutListener;

    public static WorkoutDetailFragment newInstance(String param1, String param2) {
        WorkoutDetailFragment fragment = new WorkoutDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkoutDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        init();
        return view;
    }

    public void init() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(getArguments() != null && getArguments().containsKey(BUNDLE_WORKOUT)) {
            mWorkout = (Workout) getArguments().getSerializable(BUNDLE_WORKOUT);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("workouts").child(mWorkout.getId());
        }

        if(getArguments() != null && getArguments().containsKey(BUNDLE_FAVORITE)){
            mFavorite = (Favorite)getArguments().getSerializable(BUNDLE_FAVORITE);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("workouts").child(mFavorite.getWorkout_id());
        }

        mArrayList = new ArrayList<WorkoutView>();

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        ivFavorite = (ImageView) view.findViewById(R.id.imageViewFavorite);
        ivFavorite.setOnClickListener(this);
        ivLike = (ImageView) view.findViewById(R.id.imageViewLike);
        ivLike.setOnClickListener(this);
        ivChat = (ImageView) view.findViewById(R.id.imageViewComments);
        ivChat.setOnClickListener(this);
        ivProfilePic = (ImageView) view.findViewById(R.id.imageViewProfilePic);

        tvTitle = (TextView) view.findViewById(R.id.textViewTitle);
        tvTime = (TextView) view.findViewById(R.id.textViewTime);
        tvDifficulty = (TextView) view.findViewById(R.id.textViewDifficulty);
        tvLike = (TextView) view.findViewById(R.id.textViewLikeCount);
        tvFavorite = (TextView) view.findViewById(R.id.textViewFavoriteCount);
        tvComment = (TextView) view.findViewById(R.id.textViewCommentCount);

        tvTitle.setText(mWorkout.getTitle());
        tvTime.setText(mWorkout.getTime());
        tvDifficulty.setText(mWorkout.getDifficulty());
        tvLike.setText(String.valueOf(mWorkout.getLike_count()));
        tvFavorite.setText(String.valueOf(mWorkout.getFavorite_count()));
        tvComment.setText(String.valueOf(mWorkout.getComment_count()));

        FirebaseStorageUtil.getProfilePhoto(mWorkout.getUid(), new FirebaseStorageUtil.FirebaseStorageUtilCallback() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivProfilePic.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception exception) {
            }
        });


        if(mWorkout.likes.containsKey(mUser.getUid())) {
            isLikeChecked = true;
            ivLike.setImageResource(R.drawable.like_content_selector);
        }
        if(mWorkout.favorites.containsKey(mUser.getUid())) {
            isFavoriteChecked = true;
            ivFavorite.setImageResource(R.drawable.favorite_content_selector);
        }

        for (WorkoutItem workoutItem : mWorkout.getWorkout_items()) {
            WorkoutView mWorkoutView = new WorkoutView(getActivity());
            mWorkoutView.setWorkoutName(workoutItem.getActivity_name());
            mWorkoutView.setWorkoutMainCategory(workoutItem.getClassification());
            mWorkoutView.setWorkoutCategory(workoutItem.getCategory());
            mWorkoutView.setReps(workoutItem.getReps());
            mWorkoutView.setSets(workoutItem.getSets());
            mWorkoutView.setWorkoutDistance(workoutItem.getDistance());
            mWorkoutView.setWorkoutTime(workoutItem.getLength());
            mWorkoutView.setWorkoutType(workoutItem.getType());
            mWorkoutView.setWorkoutDesc(workoutItem.getDescription());
            mWorkoutView.setWorkoutWeight(workoutItem.getWeight());
            mArrayList.add(mWorkoutView);
        }

        mWorkoutCardsAdapter = new WorkoutCardsAdapter2(getActivity(), mArrayList, new WorkoutCardsAdapter2.Callback() {
            @Override
            public void onPressed(int pos) {
            }
        });
        mListView = (ListView) view.findViewById(R.id.listViewWorkoutDetails);
        mListView.setAdapter(mWorkoutCardsAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        workoutListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mWorkout = dataSnapshot.getValue(Workout.class);
                updateAttributeCounts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getActivity().finish();
            }
        };
        mDatabaseReference.addValueEventListener(workoutListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (workoutListener != null) {
            mDatabaseReference.removeEventListener(workoutListener);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateAttributeCounts() {
        if(mWorkout.getLikes().containsKey(mUser.getUid())) {
            isLikeChecked = true;
            ivLike.setImageResource(R.drawable.like_content_selector);
        }

        if(mWorkout.getFavorites().containsKey(mUser.getUid())) {
            isFavoriteChecked = true;
            ivFavorite.setImageResource(R.drawable.favorite_content_selector);
        }

        tvLike.setText(String.valueOf(mWorkout.getLike_count()));
        tvFavorite.setText(String.valueOf(mWorkout.getFavorite_count()));
        tvComment.setText(String.valueOf(mWorkout.getComment_count()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WorkoutView choosenSingleWorkoutView = mWorkoutCardsAdapter.getItem(position);
                WorkoutItem choosenWorkoutItem = new WorkoutItem();

                choosenWorkoutItem.setClassification(choosenSingleWorkoutView.getWorkoutMainCategory());
                choosenWorkoutItem.setType(choosenSingleWorkoutView.getWorkoutType());
                choosenWorkoutItem.setCategory(choosenSingleWorkoutView.getWorkoutCategory());
                choosenWorkoutItem.setDescription(choosenSingleWorkoutView.getWorkoutDesc());
                choosenWorkoutItem.setDistance(choosenSingleWorkoutView.getWorkoutDistance());
                choosenWorkoutItem.setReps(choosenSingleWorkoutView.getReps());
                choosenWorkoutItem.setSets(choosenSingleWorkoutView.getSets());
                choosenWorkoutItem.setActivity_name(choosenSingleWorkoutView.getWorkoutName());
                choosenWorkoutItem.setLength(choosenSingleWorkoutView.getWorkoutTime());
                choosenWorkoutItem.setWeight(choosenSingleWorkoutView.getWorkoutWeight());

                Bundle bundle = new Bundle();
                bundle.putSerializable(WORKOUT_ITEM, choosenWorkoutItem);
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new WorkoutItemDetailFragment();
                mFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(WORKOUT_ITEM_DETAIL_FRAGMENT).commit();

            }
        }, 200);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewFavorite:
                onFavoriteClicked();
                if (isFavoriteChecked) {
                    FirebaseDatabaseUtil.unfavoriteWorkout(mWorkout.getId());
                    isFavoriteChecked = false;
                    ivFavorite.setImageResource(R.drawable.favorite_blank_content_selector);
                } else {
                    Favorite favorite = new Favorite();
                    favorite.setUid(mUser.getUid());
                    favorite.setWorkout_id(mWorkout.getId());
                    favorite.setWorkout_title(mWorkout.getTitle());
                    FirebaseDatabaseUtil.favoriteWorkout(favorite);

                    isFavoriteChecked = true;
                    ivFavorite.setImageResource(R.drawable.favorite_content_selector);
                }
                break;
            case R.id.imageViewLike:
                onLikeClicked();
                if (isLikeChecked) {
                    isLikeChecked = false;
                    ivLike.setImageResource(R.drawable.like_blank_content_selector);
                } else {
                    isLikeChecked = true;
                    ivLike.setImageResource(R.drawable.like_content_selector);
                }
                break;

            case R.id.imageViewComments:
                gotoComments();
                break;

            case R.id.buttonBack:
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }

    private void gotoComments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_WORKOUT, mWorkout);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment mFragment = new CommentFragment();
        mFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(COMMENT_FRAGMENT).commit();
    }

    private void onLikeClicked() {
        mDatabaseReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mWorkout = mutableData.getValue(Workout.class);
                if (mWorkout == null) {
                    return Transaction.success(mutableData);
                }

                if (mWorkout.likes.containsKey(mUser.getUid())) {
                    mWorkout.like_count = mWorkout.like_count - 1;
                    mWorkout.likes.remove(mUser.getUid());
                } else {
                    mWorkout.like_count = mWorkout.like_count + 1;
                    mWorkout.likes.put(mUser.getUid(), true);
                }

                mutableData.setValue(mWorkout);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    private void onFavoriteClicked() {
        mDatabaseReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mWorkout = mutableData.getValue(Workout.class);
                if (mWorkout == null) {
                    return Transaction.success(mutableData);
                }

                if (mWorkout.favorites.containsKey(mUser.getUid())) {
                    mWorkout.favorite_count = mWorkout.favorite_count - 1;
                    mWorkout.favorites.remove(mUser.getUid());
                } else {
                    mWorkout.favorite_count = mWorkout.favorite_count + 1;
                    mWorkout.favorites.put(mUser.getUid(), true);
                }

                mutableData.setValue(mWorkout);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
