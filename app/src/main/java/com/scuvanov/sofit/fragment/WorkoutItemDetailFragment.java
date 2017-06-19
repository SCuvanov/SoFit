package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scuvanov.sofit.R;
import com.scuvanov.sofit.model.WorkoutItem;

public class WorkoutItemDetailFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static final String WORKOUT_ITEM = "WORKOUTITEM";

    private OnFragmentInteractionListener mListener;
    private View view;
    private WorkoutItem choosenSingleWorkoutItem;
    private LinearLayout linearLayoutType, linearLayoutCategory, linearLayoutWeightSetsReps, linearLayoutDistance, linearLayoutForRepsTime, linearLayoutForReps,
            linearLayoutForTime, linearLayoutForWeight, linearLayoutDesc, linearLayoutCardDiv3, linearLayoutCardDiv4, linearLayoutCardDiv5, linearLayoutCardDiv6,
            linearLayoutCardDiv7, linearLayoutCardDiv8;

    private TextView tvType, tvCategory, tvWeight, tvSets, tvReps, tvDistance, tvForRepsTimeReps, tvForRepsTimeTime, tvForRepsReps, tvForRepsSets, tvForTime, tvForWeight, tvDesc, tvTitle;

    private Button btnBack;

    public static WorkoutItemDetailFragment newInstance(String param1, String param2) {
        WorkoutItemDetailFragment fragment = new WorkoutItemDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkoutItemDetailFragment() {}

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
        view = inflater.inflate(R.layout.fragment_single_workout_item_detail, container, false);
        init();
        return view;
    }

    public void init() {
        choosenSingleWorkoutItem = (WorkoutItem) getArguments().getSerializable(WORKOUT_ITEM);

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        linearLayoutType = (LinearLayout) view.findViewById(R.id.linearLayoutType);
        linearLayoutCategory = (LinearLayout) view.findViewById(R.id.linearLayoutCategory);
        linearLayoutWeightSetsReps = (LinearLayout) view.findViewById(R.id.linearLayoutWeightSetsReps);
        linearLayoutDistance = (LinearLayout) view.findViewById(R.id.linearLayoutDistance);
        linearLayoutForRepsTime = (LinearLayout) view.findViewById(R.id.linearLayoutForRepsTime);
        linearLayoutForReps = (LinearLayout) view.findViewById(R.id.linearLayoutForReps);
        linearLayoutForTime = (LinearLayout) view.findViewById(R.id.linearLayoutForTime);
        linearLayoutForWeight = (LinearLayout) view.findViewById(R.id.linearLayoutForWeight);
        linearLayoutDesc = (LinearLayout) view.findViewById(R.id.linearLayoutWorkoutDesc);
        linearLayoutCardDiv3 = (LinearLayout) view.findViewById(R.id.cardDivider3);
        linearLayoutCardDiv4 = (LinearLayout) view.findViewById(R.id.cardDivider4);
        linearLayoutCardDiv5 = (LinearLayout) view.findViewById(R.id.cardDivider5);
        linearLayoutCardDiv6 = (LinearLayout) view.findViewById(R.id.cardDivider6);
        linearLayoutCardDiv7 = (LinearLayout) view.findViewById(R.id.cardDivider7);
        linearLayoutCardDiv8 = (LinearLayout) view.findViewById(R.id.cardDivider8);

        tvTitle = (TextView) view.findViewById(R.id.textViewTitle);
        tvType = (TextView) view.findViewById(R.id.textViewWorkoutTypeItem);
        tvCategory = (TextView) view.findViewById(R.id.textViewWorkoutCategoryItem);
        tvWeight = (TextView) view.findViewById(R.id.textViewWorkoutWeightItem);
        tvSets = (TextView) view.findViewById(R.id.textViewWorkoutSetsItem);
        tvReps = (TextView) view.findViewById(R.id.textViewWorkoutRepsItem);
        tvDistance = (TextView) view.findViewById(R.id.textViewWorkoutDistanceItem);
        tvForRepsTimeReps = (TextView) view.findViewById(R.id.textViewWorkoutForRepsTimeRepsItem);
        tvForRepsTimeTime = (TextView) view.findViewById(R.id.textViewWorkoutForRepsTimeTimeItem);
        tvForRepsReps = (TextView) view.findViewById(R.id.textViewWorkoutForRepsRepsItem);
        tvForRepsSets = (TextView) view.findViewById(R.id.textViewWorkoutForRepsSetsItem);
        tvForTime = (TextView) view.findViewById(R.id.textViewWorkoutForTimeItem);
        tvForWeight = (TextView) view.findViewById(R.id.textViewWorkoutForWeightItem);
        tvDesc = (TextView) view.findViewById(R.id.textViewWorkoutDescriptionItem);

        setupUI();
    }

    public void setupUI() {
        linearLayoutCategory.setVisibility(View.GONE);
        linearLayoutWeightSetsReps.setVisibility(View.GONE);
        linearLayoutDistance.setVisibility(View.GONE);
        linearLayoutForRepsTime.setVisibility(View.GONE);
        linearLayoutForReps.setVisibility(View.GONE);
        linearLayoutForTime.setVisibility(View.GONE);
        linearLayoutForWeight.setVisibility(View.GONE);
        linearLayoutDesc.setVisibility(View.GONE);
        linearLayoutCardDiv3.setVisibility(View.GONE);
        linearLayoutCardDiv4.setVisibility(View.GONE);
        linearLayoutCardDiv5.setVisibility(View.GONE);
        linearLayoutCardDiv6.setVisibility(View.GONE);
        linearLayoutCardDiv7.setVisibility(View.GONE);
        linearLayoutCardDiv8.setVisibility(View.GONE);

        tvTitle.setText(choosenSingleWorkoutItem.getActivity_name());
        tvType.setText(choosenSingleWorkoutItem.getType());

        if (choosenSingleWorkoutItem.getType().equals("Strength")) {
            if (choosenSingleWorkoutItem.getCategory().equals("Weightlifting")) {
                linearLayoutCategory.setVisibility(View.VISIBLE);
                linearLayoutWeightSetsReps.setVisibility(View.VISIBLE);
                tvCategory.setText(choosenSingleWorkoutItem.getCategory());
                tvWeight.setText(choosenSingleWorkoutItem.getWeight());
                tvSets.setText(choosenSingleWorkoutItem.getSets());
                tvReps.setText(choosenSingleWorkoutItem.getReps());

            } else if (choosenSingleWorkoutItem.getCategory().equals("Isometric") || choosenSingleWorkoutItem.getCategory().equals("Circuit")
                    || choosenSingleWorkoutItem.getCategory().equals("Other")) {
                linearLayoutCategory.setVisibility(View.VISIBLE);
                linearLayoutDesc.setVisibility(View.VISIBLE);
                tvCategory.setText(choosenSingleWorkoutItem.getCategory());
                tvDesc.setText(choosenSingleWorkoutItem.getDescription());
            }

        } else if (choosenSingleWorkoutItem.getType().equals("Endurance")) {
            if (choosenSingleWorkoutItem.getCategory().equals("Running")) {
                linearLayoutCategory.setVisibility(View.VISIBLE);
                linearLayoutDistance.setVisibility(View.VISIBLE);
                tvCategory.setText(choosenSingleWorkoutItem.getCategory());
                tvDistance.setText(choosenSingleWorkoutItem.getDistance());

            } else if (choosenSingleWorkoutItem.getCategory().equals("Other")) {
                linearLayoutCategory.setVisibility(View.VISIBLE);
                linearLayoutDesc.setVisibility(View.VISIBLE);
                tvCategory.setText(choosenSingleWorkoutItem.getCategory());
                tvDesc.setText(choosenSingleWorkoutItem.getDescription());
            }

        } else if (choosenSingleWorkoutItem.getType().equals("Other")) {
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(choosenSingleWorkoutItem.getDescription());

        } else if (choosenSingleWorkoutItem.getType().equals("For Reps & Time")) {
            linearLayoutForRepsTime.setVisibility(View.VISIBLE);
            linearLayoutCardDiv5.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForRepsTimeReps.setText(choosenSingleWorkoutItem.getReps());
            tvForRepsTimeTime.setText(choosenSingleWorkoutItem.getLength());
            tvDesc.setText(choosenSingleWorkoutItem.getDescription());

        } else if (choosenSingleWorkoutItem.getType().equals("For Reps")) {
            linearLayoutForReps.setVisibility(View.VISIBLE);
            linearLayoutCardDiv6.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForRepsReps.setText(choosenSingleWorkoutItem.getReps());
            tvForRepsSets.setText(choosenSingleWorkoutItem.getSets());
            tvDesc.setText(choosenSingleWorkoutItem.getDescription());

        } else if (choosenSingleWorkoutItem.getType().equals("For Time")) {
            linearLayoutForTime.setVisibility(View.VISIBLE);
            linearLayoutCardDiv7.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForTime.setText(choosenSingleWorkoutItem.getLength());
            tvDesc.setText(choosenSingleWorkoutItem.getDescription());

        } else if (choosenSingleWorkoutItem.getType().equals("For Weight")) {
            linearLayoutForWeight.setVisibility(View.VISIBLE);
            linearLayoutCardDiv8.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForWeight.setText(choosenSingleWorkoutItem.getWeight());
            tvDesc.setText(choosenSingleWorkoutItem.getDescription());
        }
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
            case R.id.buttonBack:
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
