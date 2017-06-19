package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.adapter.TextSpinnerAdapter;
import com.scuvanov.sofit.adapter.WorkoutCardsAdapter;
import com.scuvanov.sofit.model.Workout;
import com.scuvanov.sofit.model.WorkoutItem;
import com.scuvanov.sofit.utils.DateUtil;
import com.scuvanov.sofit.utils.FirebaseDatabaseUtil;
import com.scuvanov.sofit.view.WorkoutView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class CreateWorkoutFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String WORKOUT_DATE = "WORKOUTDATE";

    private String mParam1;
    private String mParam2;
    private String TAG = CreateWorkoutFragment.class.getCanonicalName();

    private OnFragmentInteractionListener mListener;

    private Button btnAddWorkout, btnAcceptWorkout, btnCancelWorkout;
    private Spinner spinnerDifficulty, spinnerTime, spinnerSets, spinnerReps, spinnerExerciseType, spinnerExerciseCategoryStrength, spinnerExerciseCategoryEndurance;
    private EditText etWorkoutName, etTitle;
    private View view;
    private String[] array;
    private ArrayList<WorkoutView> mArrayList = new ArrayList<WorkoutView>();
    private ListView mListView;
    private WorkoutCardsAdapter mWorkoutCardsAdapter;

    private Animation animFadeOut, animFadeIn;
    private Handler mHandler;
    private Boolean isTimerRunning = false;
    private Boolean isDialogShowing = false;
    private CountDownTimer mCountDownTimer;
    private MaterialDialog mDialog;
    private WorkoutView workoutView;
    private LinearLayout linearLayoutDistance, linearLayoutExerciseCategoryStrength, linearLayoutExerciseCategoryEndurance,
            linearLayoutExerciseType, linearLayoutWeightlifting, linearLayoutOther, linearLayoutTraditional, linearLayoutCrossfit;
    private Spinner spinnerDistance, spinnerCrossfitType, spinnerForRepsReps, spinnerForRepsTime, spinnerForReps, spinnerForRepsRounds, spinnerForTime;
    private RadioButton rbtnTraditional, rbtnCrossFit;

    private Date mWorkoutDate;


    //Crossfit
    private LinearLayout linearLayoutForReps, linearLayoutForRepsTime, linearLayoutForTime, linearLayoutForWeight, linearLayoutCrossfitOther;

    private EditText etWorkoutWeight, etCrossfitWeight, etTraditionalDesc, etCrossfitDesc;

    //Firebase
    private static FirebaseUser mUser;

    public static CreateWorkoutFragment newInstance(String param1, String param2) {
        CreateWorkoutFragment fragment = new CreateWorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateWorkoutFragment() {
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
        view = inflater.inflate(R.layout.fragment_create, container, false);
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
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        if(getArguments() != null && getArguments().containsKey(WORKOUT_DATE))
            mWorkoutDate = (Date) getArguments().getSerializable(WORKOUT_DATE);

        mHandler = new Handler();

        btnAddWorkout = (Button) view.findViewById(R.id.btnAddWorkout);
        btnAddWorkout.setOnClickListener(this);
        btnAcceptWorkout = (Button) view.findViewById(R.id.buttonAccept);
        btnAcceptWorkout.setOnClickListener(this);
        btnCancelWorkout = (Button) view.findViewById(R.id.buttonCancel);
        btnCancelWorkout.setOnClickListener(this);

        etTitle = (EditText) view.findViewById(R.id.textViewTitle);

        mWorkoutCardsAdapter = new WorkoutCardsAdapter(getActivity(), mArrayList, new WorkoutCardsAdapter.Callback() {
            @Override
            public void onPressed(int pos) {
                mWorkoutCardsAdapter.remove(mWorkoutCardsAdapter.getItem(pos));
                mWorkoutCardsAdapter.notifyDataSetChanged();
            }
        });

        mListView = (ListView) view.findViewById(R.id.listViewWorkouts);
        mListView.setAdapter(mWorkoutCardsAdapter);
        mListView.setOnScrollListener(this);


        spinnerDifficulty = (Spinner) view.findViewById(R.id.spinnerDifficulty);
        array = getActivity().getResources().getStringArray(R.array.string_array_difficulty);
        TextSpinnerAdapter difficultyAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
        // Specify the layout to use when the list of choices appears
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDifficulty.setAdapter(difficultyAdapter);

        spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
        array = getActivity().getResources().getStringArray(R.array.string_array_time);
        TextSpinnerAdapter timeAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
        // Specify the layout to use when the list of choices appears
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTime.setAdapter(timeAdapter);

        animFadeOut = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        animFadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnAcceptWorkout.startAnimation(animFadeOut);
                btnAcceptWorkout.setVisibility(View.GONE);
                btnCancelWorkout.startAnimation(animFadeOut);
                btnCancelWorkout.setVisibility(View.GONE);
            }
        }, 750);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddWorkout:
                //defining dialog
                boolean wrapInScrollView = false;
                mDialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_workout_layout, wrapInScrollView)
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                isDialogShowing = false;
                                if (rbtnTraditional.isChecked()) {
                                    //define the workout group based on traditional
                                    String mainCategory = "Traditional";
                                    String activityNameStr = (etWorkoutName.getText().toString().trim());
                                    String workoutType = (String) spinnerExerciseType.getSelectedItem();
                                    if (workoutType.equals("Strength")) {
                                        //check other strength spinner values
                                        String workoutCategory = (String) spinnerExerciseCategoryStrength.getSelectedItem();
                                        if (workoutCategory.equals("Weightlifting")) {
                                            String weight = etWorkoutWeight.getText().toString().trim();
                                            String setsStr = (String) spinnerSets.getSelectedItem();
                                            String repsStr = (String) spinnerReps.getSelectedItem();

                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutWeight(weight);
                                            workoutView.setSets(setsStr);
                                            workoutView.setReps(repsStr);
                                        } else if (workoutCategory.equals("Isometric")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);

                                        } else if (workoutCategory.equals("Circuit")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);

                                        } else if (workoutCategory.equals("Other")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);

                                        }

                                    } else if (workoutType.equals("Endurance")) {
                                        //check other endurance spinner values
                                        String workoutCategory = (String) spinnerExerciseCategoryEndurance.getSelectedItem();
                                        if (workoutCategory.equals("Running")) {
                                            String distance = (String) spinnerDistance.getSelectedItem();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutDistance(distance);
                                        } else if (workoutCategory.equals("Other")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);
                                        }

                                    } else if (workoutType.equals("Other")) {
                                        String desc = etTraditionalDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutDesc(desc);
                                    }

                                    //define crossfit items
                                } else if (rbtnCrossFit.isChecked()) {
                                    String mainCategory = "CrossFit";
                                    String activityNameStr = etWorkoutName.getText().toString().trim();
                                    String workoutType = (String) spinnerCrossfitType.getSelectedItem();
                                    if (workoutType.equals("For Reps & Time")) {
                                        String repsStr = (String) spinnerForRepsReps.getSelectedItem();
                                        String timeStr = (String) spinnerForRepsTime.getSelectedItem();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setReps(repsStr);
                                        workoutView.setWorkoutTime(timeStr);
                                        workoutView.setWorkoutDesc(desc);


                                    } else if (workoutType.equals("For Reps")) {
                                        String repsStr = (String) spinnerForReps.getSelectedItem();
                                        String setsStr = (String) spinnerForRepsRounds.getSelectedItem();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setReps(repsStr);
                                        workoutView.setSets(setsStr);
                                        workoutView.setWorkoutDesc(desc);

                                    } else if (workoutType.equals("For Time")) {
                                        String time = (String) spinnerForTime.getSelectedItem();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutTime(time);
                                        workoutView.setWorkoutDesc(desc);

                                    } else if (workoutType.equals("For Weight")) {
                                        String weight = etCrossfitWeight.getText().toString().trim();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutWeight(weight);
                                        workoutView.setWorkoutDesc(desc);

                                    } else if (workoutType.equals("Other")) {
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutDesc(desc);
                                    }
                                }

                                mWorkoutCardsAdapter.add(workoutView);
                                mWorkoutCardsAdapter.notifyDataSetChanged();


                                if (!(isTimerRunning)) {
                                    isTimerRunning = true;
                                    mCountDownTimer = new CountDownTimer(1000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            //do nothing
                                        }

                                        public void onFinish() {
                                            isTimerRunning = false;
                                            if ((btnAcceptWorkout.getVisibility() == View.GONE) && (btnCancelWorkout.getVisibility() == View.GONE)) {
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        btnAcceptWorkout.startAnimation(animFadeIn);
                                                        btnAcceptWorkout.setVisibility(View.VISIBLE);

                                                        btnCancelWorkout.startAnimation(animFadeIn);
                                                        btnCancelWorkout.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }
                                        }
                                    }.start();
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                isDialogShowing = false;
                                Log.e(TAG, "Negative Pressed");
                            }
                        })
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                isDialogShowing = false;
                            }
                        })
                        .build();

                View dialogView = mDialog.getCustomView();
                etWorkoutName = (EditText) dialogView.findViewById(R.id.etWorkoutName);
                etWorkoutWeight = (EditText) dialogView.findViewById(R.id.editTextWeight);
                etCrossfitWeight = (EditText) dialogView.findViewById(R.id.editTextForWeight);
                etTraditionalDesc = (EditText) dialogView.findViewById(R.id.editTextDesc);
                etCrossfitDesc = (EditText) dialogView.findViewById(R.id.editTextCrossfitDesc);


                rbtnTraditional = (RadioButton) dialogView.findViewById(R.id.radioButtonTraditional);
                rbtnCrossFit = (RadioButton) dialogView.findViewById(R.id.radioButtonCrossFit);

                linearLayoutDistance = (LinearLayout) dialogView.findViewById(R.id.linearLayoutDistance);
                linearLayoutWeightlifting = (LinearLayout) dialogView.findViewById(R.id.linearLayoutWeightlifting);
                linearLayoutExerciseType = (LinearLayout) dialogView.findViewById(R.id.linearLayoutExerciseType);
                linearLayoutExerciseCategoryStrength = (LinearLayout) dialogView.findViewById(R.id.linearLayoutExerciseCategoryStrength);
                linearLayoutExerciseCategoryEndurance = (LinearLayout) dialogView.findViewById(R.id.linearLayoutExerciseCategoryEndurance);
                linearLayoutOther = (LinearLayout) dialogView.findViewById(R.id.linearLayoutOther);
                linearLayoutTraditional = (LinearLayout) dialogView.findViewById(R.id.linearLayoutTraditional);


                //crossfit layouts
                linearLayoutCrossfit = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCrossfit);
                linearLayoutForRepsTime = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForRepsTime);
                linearLayoutForReps = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForReps);
                linearLayoutForTime = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForTime);
                linearLayoutForWeight = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForWeight);
                linearLayoutCrossfitOther = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCrossfitOther);


                //set starting visibilities
                linearLayoutOther.setVisibility(View.GONE);
                linearLayoutDistance.setVisibility(View.GONE);
                linearLayoutCrossfit.setVisibility(View.GONE);
                linearLayoutForReps.setVisibility(View.GONE);
                linearLayoutForTime.setVisibility(View.GONE);
                linearLayoutForWeight.setVisibility(View.GONE);

                rbtnTraditional.setChecked(true);

                rbtnTraditional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            linearLayoutTraditional.setVisibility(View.VISIBLE);
                            linearLayoutCrossfit.setVisibility(View.GONE);
                        }
                    }
                });

                rbtnCrossFit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            linearLayoutCrossfit.setVisibility(View.VISIBLE);
                            linearLayoutTraditional.setVisibility(View.GONE);
                        }

                    }
                });


                spinnerExerciseType = (Spinner) dialogView.findViewById(R.id.spinnerExerciseType);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_type);
                TextSpinnerAdapter extAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                extAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerExerciseType.setAdapter(extAdapter);
                spinnerExerciseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("Strength")) {
                            linearLayoutExerciseCategoryStrength.setVisibility(View.VISIBLE);
                            spinnerExerciseCategoryStrength.setSelection(0);
                            linearLayoutWeightlifting.setVisibility(View.VISIBLE);
                            linearLayoutExerciseCategoryEndurance.setVisibility(View.GONE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Endurance")) {
                            linearLayoutExerciseCategoryEndurance.setVisibility(View.VISIBLE);
                            spinnerExerciseCategoryEndurance.setSelection(0);
                            linearLayoutDistance.setVisibility(View.VISIBLE);
                            linearLayoutExerciseCategoryStrength.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                            linearLayoutExerciseCategoryStrength.setVisibility(View.GONE);
                            linearLayoutExerciseCategoryEndurance.setVisibility(View.GONE);
                            linearLayoutDistance.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                spinnerExerciseCategoryStrength = (Spinner) dialogView.findViewById(R.id.spinnerExerciseCategoryStrength);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_category_strength);
                TextSpinnerAdapter excsAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                excsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerExerciseCategoryStrength.setAdapter(excsAdapter);
                spinnerExerciseCategoryStrength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("Weightlifting")) {
                            linearLayoutWeightlifting.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Isometric")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Circuit")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinnerExerciseCategoryEndurance = (Spinner) dialogView.findViewById(R.id.spinnerExerciseCategoryEndurance);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_category_endurance);
                TextSpinnerAdapter exceAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                exceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerExerciseCategoryEndurance.setAdapter(exceAdapter);
                spinnerExerciseCategoryEndurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("Running")) {
                            linearLayoutDistance.setVisibility(View.VISIBLE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                spinnerCrossfitType = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitType);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_crossfit_type);
                TextSpinnerAdapter ctAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                ctAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerCrossfitType.setAdapter(ctAdapter);
                spinnerCrossfitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("For Reps & Time")) {
                            linearLayoutForRepsTime.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("For Reps")) {
                            linearLayoutForReps.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);

                        } else if (spinnerSelectionStr.equals("For Time")) {
                            linearLayoutForTime.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);

                        } else if (spinnerSelectionStr.equals("For Weight")) {
                            linearLayoutForWeight.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);

                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                //crossfit
                spinnerForRepsReps = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitReps);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter frrAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForRepsReps.setAdapter(frrAdapter);


                spinnerForRepsTime = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitTime);
                array = getActivity().getResources().getStringArray(R.array.string_array_cross_fit_time);
                TextSpinnerAdapter frtAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForRepsTime.setAdapter(frtAdapter);


                spinnerForRepsRounds = (Spinner) dialogView.findViewById(R.id.spinnerForRounds);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter frroAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForRepsRounds.setAdapter(frroAdapter);


                spinnerForReps = (Spinner) dialogView.findViewById(R.id.spinnerForReps);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter frAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForReps.setAdapter(frAdapter);

                spinnerForTime = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitForTime);
                array = getActivity().getResources().getStringArray(R.array.string_array_cross_fit_time);
                TextSpinnerAdapter ftAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                ftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForTime.setAdapter(ftAdapter);


                //traditional
                spinnerDistance = (Spinner) dialogView.findViewById(R.id.spinnerDistance);
                array = getActivity().getResources().getStringArray(R.array.string_array_distance);
                TextSpinnerAdapter dAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerDistance.setAdapter(dAdapter);

                spinnerSets = (Spinner) dialogView.findViewById(R.id.spinnerSets);
                array = getActivity().getResources().getStringArray(R.array.string_array_sets);
                TextSpinnerAdapter setsAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                setsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerSets.setAdapter(setsAdapter);

                spinnerReps = (Spinner) dialogView.findViewById(R.id.spinnerReps);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter repsAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                repsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerReps.setAdapter(repsAdapter);
                if (!(isDialogShowing)) {
                    mDialog.show();
                    isDialogShowing = true;
                }

                break;
            case R.id.buttonAccept:
                //grab data from fields, create the ParseObject
                String title = etTitle.getText().toString().trim();
                String difficulty = spinnerDifficulty.getSelectedItem().toString();
                String workoutTime = spinnerTime.getSelectedItem().toString();

                ArrayList<WorkoutItem> mSingleWorkoutItemArray = new ArrayList<WorkoutItem>();

                if (!(title.equals(""))) {
                    if (mArrayList.size() > 0) {
                        for (WorkoutView wv : mArrayList) {
                            WorkoutItem mSingleWorkoutItem = new WorkoutItem();
                            String workoutMainCategoryStr = wv.getWorkoutMainCategory();
                            String workoutActivityNameStr = wv.getWorkoutName();

                            if (wv.getWorkoutMainCategory().equals("Traditional")) {
                                String workoutTypeStr = wv.getWorkoutType();
                                if (wv.getWorkoutType().equals("Strength")) {
                                    if (wv.getWorkoutCategory().equals("Weightlifting")) {
                                        String workoutCategoryStr = wv.getWorkoutCategory();
                                        String weight = wv.getWorkoutWeight();
                                        String sets = wv.getSets();
                                        String reps = wv.getReps();

                                        mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                        mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                        mSingleWorkoutItem.setType(workoutTypeStr);
                                        mSingleWorkoutItem.setCategory(workoutCategoryStr);
                                        mSingleWorkoutItem.setWeight(weight);
                                        mSingleWorkoutItem.setSets(sets);
                                        mSingleWorkoutItem.setReps(reps);

                                        mSingleWorkoutItem.setLength("");
                                        mSingleWorkoutItem.setDescription("");
                                        mSingleWorkoutItem.setDistance("");

                                    } else if ((wv.getWorkoutCategory().equals("Isometric")) || (wv.getWorkoutCategory().equals("Circuit"))
                                            || (wv.getWorkoutCategory().equals("Other"))) {
                                        String workoutCategoryStr = wv.getWorkoutCategory();
                                        String desc = wv.getWorkoutDesc();

                                        mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                        mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                        mSingleWorkoutItem.setType(workoutTypeStr);
                                        mSingleWorkoutItem.setCategory(workoutCategoryStr);
                                        mSingleWorkoutItem.setDescription(desc);

                                        mSingleWorkoutItem.setLength("");
                                        mSingleWorkoutItem.setReps("");
                                        mSingleWorkoutItem.setSets("");
                                        mSingleWorkoutItem.setWeight("");
                                        mSingleWorkoutItem.setDistance("");

                                    }

                                } else if (wv.getWorkoutType().equals("Endurance")) {
                                    if (wv.getWorkoutCategory().equals("Running")) {
                                        String workoutCategoryStr = wv.getWorkoutCategory();
                                        String distance = wv.getWorkoutDistance();

                                        mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                        mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                        mSingleWorkoutItem.setType(workoutTypeStr);
                                        mSingleWorkoutItem.setCategory(workoutCategoryStr);
                                        mSingleWorkoutItem.setDistance(distance);

                                        mSingleWorkoutItem.setLength("");
                                        mSingleWorkoutItem.setReps("");
                                        mSingleWorkoutItem.setSets("");
                                        mSingleWorkoutItem.setWeight("");
                                        mSingleWorkoutItem.setDescription("");


                                    } else if (wv.getWorkoutCategory().equals("Other")) {
                                        String workoutCategoryStr = wv.getWorkoutCategory();
                                        String desc = wv.getWorkoutDesc();

                                        mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                        mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                        mSingleWorkoutItem.setType(workoutTypeStr);
                                        mSingleWorkoutItem.setCategory(workoutCategoryStr);
                                        mSingleWorkoutItem.setDescription(desc);

                                        mSingleWorkoutItem.setLength("");
                                        mSingleWorkoutItem.setReps("");
                                        mSingleWorkoutItem.setSets("");
                                        mSingleWorkoutItem.setWeight("");
                                        mSingleWorkoutItem.setDistance("");
                                    }

                                } else if (wv.getWorkoutType().equals("Other")) {
                                    String desc = wv.getWorkoutDesc();
                                    mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                    mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                    mSingleWorkoutItem.setType(workoutTypeStr);
                                    mSingleWorkoutItem.setDescription(desc);

                                    mSingleWorkoutItem.setCategory("");
                                    mSingleWorkoutItem.setLength("");
                                    mSingleWorkoutItem.setReps("");
                                    mSingleWorkoutItem.setSets("");
                                    mSingleWorkoutItem.setWeight("");
                                    mSingleWorkoutItem.setDistance("");
                                }

                            } else if (wv.getWorkoutMainCategory().equals("CrossFit")) {
                                String workoutTypeStr = wv.getWorkoutType();
                                if (wv.getWorkoutType().equals("For Reps & Time")) {
                                    String reps = wv.getReps();
                                    String time = wv.getWorkoutTime();
                                    String desc = wv.getWorkoutDesc();

                                    mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                    mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                    mSingleWorkoutItem.setType(workoutTypeStr);
                                    mSingleWorkoutItem.setReps(reps);
                                    mSingleWorkoutItem.setLength(time);
                                    mSingleWorkoutItem.setDescription(desc);

                                    mSingleWorkoutItem.setCategory("");
                                    mSingleWorkoutItem.setSets("");
                                    mSingleWorkoutItem.setWeight("");
                                    mSingleWorkoutItem.setDistance("");


                                } else if (wv.getWorkoutType().equals("For Reps")) {
                                    String reps = wv.getReps();
                                    String sets = wv.getSets();
                                    String desc = wv.getWorkoutDesc();

                                    mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                    mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                    mSingleWorkoutItem.setType(workoutTypeStr);
                                    mSingleWorkoutItem.setReps(reps);
                                    mSingleWorkoutItem.setSets(sets);
                                    mSingleWorkoutItem.setDescription(desc);

                                    mSingleWorkoutItem.setCategory("");
                                    mSingleWorkoutItem.setLength("");
                                    mSingleWorkoutItem.setWeight("");
                                    mSingleWorkoutItem.setDistance("");

                                } else if (wv.getWorkoutType().equals("For Time")) {
                                    String time = wv.getWorkoutTime();
                                    String desc = wv.getWorkoutDesc();

                                    mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                    mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                    mSingleWorkoutItem.setType(workoutTypeStr);
                                    mSingleWorkoutItem.setLength(time);
                                    mSingleWorkoutItem.setDescription(desc);

                                    mSingleWorkoutItem.setCategory("");
                                    mSingleWorkoutItem.setReps("");
                                    mSingleWorkoutItem.setSets("");
                                    mSingleWorkoutItem.setWeight("");
                                    mSingleWorkoutItem.setDistance("");

                                } else if (wv.getWorkoutType().equals("For Weight")) {
                                    String weight = wv.getWorkoutWeight();
                                    String desc = wv.getWorkoutDesc();

                                    mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                    mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                    mSingleWorkoutItem.setType(workoutTypeStr);
                                    mSingleWorkoutItem.setWeight(weight);
                                    mSingleWorkoutItem.setDescription(desc);

                                    mSingleWorkoutItem.setCategory("");
                                    mSingleWorkoutItem.setLength("");
                                    mSingleWorkoutItem.setReps("");
                                    mSingleWorkoutItem.setSets("");
                                    mSingleWorkoutItem.setDistance("");

                                } else if (wv.getWorkoutType().equals("Other")) {
                                    String desc = wv.getWorkoutDesc();

                                    mSingleWorkoutItem.setClassification(workoutMainCategoryStr);
                                    mSingleWorkoutItem.setActivity_name(workoutActivityNameStr);
                                    mSingleWorkoutItem.setType(workoutTypeStr);
                                    mSingleWorkoutItem.setDescription(desc);

                                    mSingleWorkoutItem.setCategory("");
                                    mSingleWorkoutItem.setLength("");
                                    mSingleWorkoutItem.setReps("");
                                    mSingleWorkoutItem.setSets("");
                                    mSingleWorkoutItem.setWeight("");
                                    mSingleWorkoutItem.setDistance("");
                                }
                            }
                            mSingleWorkoutItemArray.add(mSingleWorkoutItem);
                        }

                        ArrayList<Integer> workout_pictures = new ArrayList<Integer>();
                        for (int i = 0; i < 3; i++) {
                            int max = 39;
                            int min = 1;
                            Random rand = new Random();
                            int randomNum = rand.nextInt((max - min) + 1) + min;
                            workout_pictures.add(randomNum);
                        }

                        Workout mWorkout = new Workout();
                        mWorkout.setUid(mUser.getUid());
                        mWorkout.setTitle(title);
                        mWorkout.setDifficulty(difficulty);
                        mWorkout.setTime(workoutTime);

                        if(mWorkoutDate == null)
                            mWorkout.setWorkout_date(DateUtil.getCurrentDate());
                        else
                            mWorkout.setWorkout_date(mWorkoutDate);
                        mWorkout.setCreated_date(DateUtil.getCurrentDate());
                        mWorkout.setWorkout_pictures(workout_pictures);
                        mWorkout.setWorkout_items(mSingleWorkoutItemArray);

                        mDialog = new MaterialDialog.Builder(getActivity())
                                .content("Lifting Weights..")
                                .progress(true, 0)
                                .show();

                        FirebaseDatabaseUtil.createWorkout(mWorkout);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                getActivity().getFragmentManager().popBackStack();
                            }
                        }, 1000);

                    } else {
                        Toast.makeText(getActivity(), "Add Workout Item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etTitle.getText().clear();
                    etTitle.setHintTextColor(getResources().getColor(R.color.material_red_300));
                    etTitle.setHint("Enter Title");
                }
                break;

            case R.id.buttonCancel:
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                isTimerRunning = false;
            }
            if (btnAcceptWorkout.getVisibility() == View.VISIBLE) {
                btnAcceptWorkout.startAnimation(animFadeOut);
                btnAcceptWorkout.setVisibility(View.GONE);
            }
            if (btnCancelWorkout.getVisibility() == View.VISIBLE) {
                btnCancelWorkout.startAnimation(animFadeOut);
                btnCancelWorkout.setVisibility(View.GONE);
            }
        }
        if (scrollState == SCROLL_STATE_IDLE) {
            if (!(isTimerRunning)) {
                isTimerRunning = true;
                mCountDownTimer = new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        isTimerRunning = false;
                        if ((btnAcceptWorkout.getVisibility() == View.GONE) && (btnCancelWorkout.getVisibility() == View.GONE)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    btnAcceptWorkout.startAnimation(animFadeIn);
                                    btnAcceptWorkout.setVisibility(View.VISIBLE);

                                    btnCancelWorkout.startAnimation(animFadeIn);
                                    btnCancelWorkout.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }.start();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
