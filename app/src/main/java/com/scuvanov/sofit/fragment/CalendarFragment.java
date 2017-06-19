package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.model.Workout;
import com.scuvanov.sofit.utils.DateUtil;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarFragment extends Fragment implements View.OnClickListener {
    private String TAG = CalendarFragment.class.getCanonicalName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TUTORIAL_SCHEDULE = "TUTORIALSCHEDULE";
    private static final String CREATE_FRAGMENT = "CREATEFRAGMENT";
    private static final String WORKOUT_DATE = "WORKOUTDATE";
    private static final String IS_PERSONAL = "ISPERSONAL";
    private static final String FEED_FRAGMENT = "FEEDFRAGMENT";

    private String mParam1;
    private String mParam2;

    private View view;
    private Button btnAddDate;
    private TextView tvTutorialSchedule;
    private SharedPreferences prefs;

    private OnFragmentInteractionListener mListener;
    private static FirebaseUser mUser;
    private ChildEventListener workoutDateListener;
    private Map<String, Date> workoutDateSet = new HashMap<String, Date>();
    private static DatabaseReference mDatabaseReference;

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() {}

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
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        workoutDateListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Workout mWorkout = dataSnapshot.getValue(Workout.class);
                workoutDateSet.put(DateUtil.formatDate(mWorkout.getWorkout_date()), mWorkout.getWorkout_date());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Workout mWorkout = dataSnapshot.getValue(Workout.class);
                workoutDateSet.remove(DateUtil.formatDate(mWorkout.getWorkout_date()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mDatabaseReference.addChildEventListener(workoutDateListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (workoutDateListener != null) {
            mDatabaseReference.removeEventListener(workoutDateListener);
        }
    }

    public void init() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user_workouts").child(mUser.getUid());

        btnAddDate = (Button) view.findViewById(R.id.buttonAddDate);
        btnAddDate.setOnClickListener(this);

        tvTutorialSchedule = (TextView) view.findViewById(R.id.textViewTutorialCalendar);

        prefs = getActivity().getSharedPreferences("com.scuvanov.sofit", Context.MODE_PRIVATE);
        if(prefs.getBoolean(TUTORIAL_SCHEDULE, false)){
            tvTutorialSchedule.setVisibility(View.GONE);
        } else {
            tvTutorialSchedule.setVisibility(View.VISIBLE);
        }

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        CalendarPickerView calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();


        calendar.init(todate1, nextYear.getTime())
                .withSelectedDate(today);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //do a query and find out if there are any dates that exist, if there are open a different fragment
                if(!(prefs.getBoolean(TUTORIAL_SCHEDULE, false))) {
                    prefs.edit().putBoolean(TUTORIAL_SCHEDULE, true).apply();
                }

                if(workoutDateSet.containsKey(DateUtil.formatDate(date)))
                    gotoFeedFragment(date);
                else
                    gotoCreateFragment(date);
            }

            @Override
            public void onDateUnselected(Date date) {}
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddDate:
                gotoCreateFragment(null);
               break;
        }
    }

    private void gotoCreateFragment(Date date){
        Bundle bundle = new Bundle();
        if(date == null)
            bundle.putSerializable(WORKOUT_DATE, DateUtil.getCurrentDate());
        else
            bundle.putSerializable(WORKOUT_DATE, date);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment mFragment = new CreateWorkoutFragment();
        mFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(CREATE_FRAGMENT).commit();
    }

    private void gotoFeedFragment(Date date){
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_PERSONAL, true);
        bundle.putSerializable(WORKOUT_DATE, date);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment mFragment = new FeedFragment();
        mFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(FEED_FRAGMENT).commit();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
