package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.scuvanov.sofit.R;
import com.scuvanov.sofit.adapter.WorkoutAdapter;
import com.scuvanov.sofit.model.Workout;

import java.util.Date;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public class FeedFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private String TAG = FeedFragment.class.getCanonicalName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static final String PACKAGE = "com.scuvanov.sofit";
    private static final String BUNDLE_WORKOUT = "WORKOUT";
    private static final String DETAIL_FRAGMENT = "DETAILFRAGMENT";
    private static final String CREATE_FRAGMENT = "CREATEFRAGMENT";
    private static final String TUTORIAL_ADD = "TUTORIALADD";
    private static final String IS_PERSONAL = "ISPERSONAL";
    private static final String WORKOUT_DATE = "WORKOUTDATE";

    private OnFragmentInteractionListener mListener;
    private View view;

    private WorkoutAdapter mWorkoutAdapter;
    private TextView tvTutorialAdd;
    private SharedPreferences prefs;

    private RecyclerView mWorkoutsRecycler;
    private Boolean isPersonal = false;
    private Date workoutDate = null;

    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedFragment() {
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
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        initValues();
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public void initValues() {
        if(getArguments() != null && getArguments().containsKey(IS_PERSONAL))
            isPersonal = getArguments().getBoolean(IS_PERSONAL);
        if(getArguments() != null && getArguments().containsKey(WORKOUT_DATE))
            workoutDate = (Date) getArguments().getSerializable(WORKOUT_DATE);

        Button btnAddContent = (Button) view.findViewById(R.id.buttonAdd);
        btnAddContent.setOnClickListener(this);

        tvTutorialAdd = (TextView) view.findViewById(R.id.textViewTutorialAdd);

        prefs = getActivity().getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
        if (prefs.getBoolean(TUTORIAL_ADD, false)) {
            tvTutorialAdd.setVisibility(View.GONE);
        } else {
            tvTutorialAdd.setVisibility(View.VISIBLE);
        }

        mWorkoutsRecycler = (RecyclerView) view.findViewById(R.id.rvFeed);

        mWorkoutAdapter = new WorkoutAdapter(getActivity(), isPersonal, workoutDate, new WorkoutAdapter.WorkoutAdapterOnItemClickListener() {
            @Override
            public void onClick(final Workout workout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(BUNDLE_WORKOUT, workout);
                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment mFragment = new WorkoutDetailFragment();
                        mFragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(DETAIL_FRAGMENT).commit();
                    }
                }, 200);
            }
        });

        mWorkoutsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWorkoutsRecycler.setAdapter(new ScaleInAnimationAdapter(mWorkoutAdapter));
        mWorkoutsRecycler.setItemAnimator(new FadeInUpAnimator());
        mWorkoutsRecycler.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWorkoutsRecycler.setFadingEdgeLength(0);
        mWorkoutsRecycler.setFitsSystemWindows(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                if (!(prefs.getBoolean(TUTORIAL_ADD, false))) {
                    prefs.edit().putBoolean(TUTORIAL_ADD, true).apply();
                }
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new CreateWorkoutFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(CREATE_FRAGMENT).commit();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
