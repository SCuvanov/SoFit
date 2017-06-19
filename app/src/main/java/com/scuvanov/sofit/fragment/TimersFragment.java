package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.adapter.TextSpinnerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.scuvanov.sofit.fragment.TimersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.scuvanov.sofit.fragment.TimersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimersFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = TimersFragment.class.getCanonicalName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Chronometer mChronometer;
    private Button btnStartStopwatch, btnStopStopwatch, btnResetStopwatch, btnStartAMRAP, btnStopAMRAP, btnResetAMRAP;
    private TextView tvAMRAP, tvDialogCounter;
    private Spinner spinnerMinutes, spinnerSeconds;
    private String[] array;

    private View view;
    private long timeWhenStopped = 0;
    private Boolean isCountdownRunning = false;
    private Boolean isStopwatchRunning = false;
    private Boolean isDialogShowing = false;
    private Boolean isCountdownDialogRunning = false;
    private Boolean didCountdownDialogFinish = false;
    private CountDownTimer mCountDownTimer, mCountdownDialogTimer;
    private MaterialDialog mDialog;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimersFragment newInstance(String param1, String param2) {
        TimersFragment fragment = new TimersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TimersFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timers, container, false);
        initValues();
        return view;
    }

    public void initValues() {
        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        btnStartStopwatch = (Button) view.findViewById(R.id.btnStartStopwatch);
        btnStopStopwatch = (Button) view.findViewById(R.id.btnStopStopwatch);
        btnResetStopwatch = (Button) view.findViewById(R.id.btnResetStopwatch);
        btnStartAMRAP = (Button) view.findViewById(R.id.btnStartAMRAP);
        btnStopAMRAP = (Button) view.findViewById(R.id.btnStopAMRAP);
        btnResetAMRAP = (Button) view.findViewById(R.id.btnResetAMRAP);

        tvAMRAP = (TextView) view.findViewById(R.id.textViewAMRAPTimer);
        tvAMRAP.setText("Set Time");
        tvAMRAP.setOnClickListener(this);

        btnStartStopwatch.setOnClickListener(this);
        btnStopStopwatch.setOnClickListener(this);
        btnResetStopwatch.setOnClickListener(this);
        btnStartAMRAP.setOnClickListener(this);
        btnStopAMRAP.setOnClickListener(this);
        btnResetAMRAP.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    public void onDestroyView() {
        super.onDestroyView();
        mChronometer.stop();
        if (isCountdownRunning) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartStopwatch:
                if(!isStopwatchRunning) {
                    mDialog = new MaterialDialog.Builder(getActivity())
                            .customView(R.layout.dialog_countdown, false)
                            .showListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    //start countdown timer inside dialog
                                    didCountdownDialogFinish = false;
                                    if (!(isCountdownDialogRunning)) {

                                        isCountdownDialogRunning = true;
                                        mCountdownDialogTimer = new CountDownTimer(6000, 1000) {
                                            public void onTick(long millisUntilFinished) {
                                                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                                                String timerStr = String.format("%02d", (seconds % 60));
                                                tvDialogCounter.setText(timerStr);
                                                tvDialogCounter.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));

                                            }

                                            public void onFinish() {
                                                //add some type of sound effect to indicate go time
                                                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.stopwatch_single_click_to_start_timing);
                                                mediaPlayer.start();

                                                didCountdownDialogFinish = true;
                                                isCountdownDialogRunning = false;
                                                isDialogShowing = false;
                                                mDialog.dismiss();
                                            }
                                        }.start();
                                    }
                                }
                            })
                            .dismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (isCountdownDialogRunning) {
                                        mCountdownDialogTimer.cancel();
                                        isCountdownDialogRunning = false;
                                        isDialogShowing = false;
                                    } else {
                                        if (didCountdownDialogFinish) {
                                            isCountdownDialogRunning = false;
                                            isDialogShowing = false;

                                            isStopwatchRunning = true;
                                            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                                            mChronometer.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));
                                            mChronometer.start();

                                        }
                                    }
                                }
                            })
                            .build();

                    View dialogViewStopwatch= mDialog.getCustomView();
                    tvDialogCounter = (TextView) dialogViewStopwatch.findViewById(R.id.textViewDialogTimer);

                    if (!(isDialogShowing)) {
                        mDialog.show();
                        isDialogShowing = true;
                    }
                }
                break;
            case R.id.btnStopStopwatch:
                mChronometer.setTextColor(getActivity().getResources().getColor(R.color.material_red_300));
                timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
                mChronometer.stop();
                isStopwatchRunning = false;
                break;
            case R.id.btnResetStopwatch:
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));
                mChronometer.stop();
                isStopwatchRunning = false;
                timeWhenStopped = 0;
                break;
            case R.id.btnStartAMRAP:
                if(!isCountdownRunning) {
                    mDialog = new MaterialDialog.Builder(getActivity())
                            .customView(R.layout.dialog_countdown, false)
                            .showListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    //start countdown timer inside dialog
                                    didCountdownDialogFinish = false;
                                    if (!(isCountdownDialogRunning)) {

                                        isCountdownDialogRunning = true;
                                        mCountdownDialogTimer = new CountDownTimer(6000, 1000) {
                                            public void onTick(long millisUntilFinished) {
                                                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                                                String timerStr = String.format("%02d", (seconds % 60));
                                                tvDialogCounter.setText(timerStr);
                                                tvDialogCounter.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));

                                            }

                                            public void onFinish() {
                                                //add some type of sound effect to indicate go time
                                                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.stopwatch_single_click_to_start_timing);
                                                mediaPlayer.start();



                                                didCountdownDialogFinish = true;
                                                isCountdownDialogRunning = false;
                                                isDialogShowing = false;
                                                mDialog.dismiss();
                                            }
                                        }.start();
                                    }
                                }
                            })
                            .dismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (isCountdownDialogRunning) {
                                        mCountdownDialogTimer.cancel();
                                        isCountdownDialogRunning = false;
                                        isDialogShowing = false;
                                    } else {
                                        if (didCountdownDialogFinish) {
                                            isCountdownDialogRunning = false;
                                            isDialogShowing = false;
                                            startCountdown();
                                        }
                                    }
                                }
                            })
                            .build();

                    View dialogViewAMRAP = mDialog.getCustomView();
                    tvDialogCounter = (TextView) dialogViewAMRAP.findViewById(R.id.textViewDialogTimer);

                    if (!(isDialogShowing) && !(tvAMRAP.getText().toString().trim().equals("Set Time")) && !(tvAMRAP.getText().toString().trim().equals("00:00"))
                            && !(tvAMRAP.getText().toString().trim().equals("DONE!"))) {
                        mDialog.show();
                        isDialogShowing = true;
                    }
                }
                break;
            case R.id.btnStopAMRAP:
                if (isCountdownRunning) {
                    tvAMRAP.setTextColor(getActivity().getResources().getColor(R.color.material_red_300));
                    mCountDownTimer.cancel();
                    isCountdownRunning = false;
                }
                break;
            case R.id.btnResetAMRAP:
                if (isCountdownRunning) {
                    mCountDownTimer.cancel();
                }
                tvAMRAP.setText("00:00");
                tvAMRAP.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));
                isCountdownRunning = false;
                break;

            case R.id.textViewAMRAPTimer:
                mDialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_time_chooser, false)
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Log.e(TAG, "Positive Pressed");
                                isDialogShowing = false;
                                String minutes = (String) spinnerMinutes.getSelectedItem();
                                String seconds = (String) spinnerSeconds.getSelectedItem();
                                tvAMRAP.setText(minutes + ":" + seconds);
                                tvAMRAP.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));
                                //need to store the milliseconds and need to set the text of the timer


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
                spinnerMinutes = (Spinner) dialogView.findViewById(R.id.spinnerMinutes);
                spinnerSeconds = (Spinner) dialogView.findViewById(R.id.spinnerSeconds);

                array = getActivity().getResources().getStringArray(R.array.string_array_minutes);
                TextSpinnerAdapter minAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerMinutes.setAdapter(minAdapter);

                array = getActivity().getResources().getStringArray(R.array.string_array_seconds);
                TextSpinnerAdapter secAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                secAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerSeconds.setAdapter(secAdapter);


                if (!(isDialogShowing)) {
                    mDialog.show();
                    isDialogShowing = true;
                }
                break;
        }

    }

    public void startCountdown() {
        isCountdownRunning = true;
        if (tvAMRAP.getText().toString().trim().equals("DONE!") || tvAMRAP.getText().toString().trim().equals("Set Time")) {
            tvAMRAP.setText("00:00");
            tvAMRAP.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));
        }
        String presetTime = tvAMRAP.getText().toString().trim();
        String[] parts = presetTime.split(":");
        long minutes = Long.valueOf(parts[0]);
        long seconds = Long.valueOf(parts[1]);
        long totalMilliseconds = 0;
        totalMilliseconds = TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);

        mCountDownTimer = new CountDownTimer(totalMilliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                String timerStr = String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60));
                tvAMRAP.setText(timerStr);
                tvAMRAP.setTextColor(getActivity().getResources().getColor(R.color.material_grey_500));
            }

            public void onFinish() {
                isCountdownRunning = false;
                tvAMRAP.setText("DONE!");
                tvAMRAP.setTextColor(getActivity().getResources().getColor(R.color.material_green_400));
            }
        }.start();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
