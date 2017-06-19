package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.utils.FirebaseUserUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    private String TAG = SettingsFragment.class.getCanonicalName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button btnBack;
    private TextView tvChangePassword, tvUpdateDisplayName;
    private View view;
    private EditText etPassword1, etPassword2, etDisplayName;

    FirebaseUser mUser;

    private SharedPreferences prefs;

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {}

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
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        init();
        return view;
    }

    private void init() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        prefs = getActivity().getSharedPreferences("com.scuvanov.sofit", Context.MODE_PRIVATE);

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        tvChangePassword = (TextView) view.findViewById(R.id.textViewChangePassword);
        tvChangePassword.setOnClickListener(this);

        tvUpdateDisplayName = (TextView) view.findViewById(R.id.textViewUpdateDisplayName);
        tvUpdateDisplayName.setOnClickListener(this);
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

            case R.id.textViewChangePassword:
                new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_change_password, false)
                        .autoDismiss(false)
                        .title("Change Password")
                        .titleColorRes(R.color.primary_blue)
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                etPassword1 = (EditText) dialog.findViewById(R.id.etPassword1);
                                etPassword2 = (EditText) dialog.findViewById(R.id.etPassword2);
                                String pass1 = etPassword1.getText().toString().trim();
                                String pass2 = etPassword2.getText().toString().trim();

                                if (!StringUtils.isBlank(pass1) && !StringUtils.isBlank(pass2) && pass1.equals(pass2)) {
                                    FirebaseUserUtil.updateUserPassword(getActivity(), pass1);
                                    dialog.dismiss();
                                } else {
                                    etPassword1.getText().clear();
                                    etPassword2.getText().clear();
                                    etPassword1.setError("Password Mismatch");
                                    etPassword2.setError("Password Mismatch");
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {dialog.dismiss();}
                        }).show();
                break;

            case R.id.textViewUpdateDisplayName:
                new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_update_display_name, false)
                        .autoDismiss(false)
                        .title("Update Username")
                        .titleColorRes(R.color.primary_blue)
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                etDisplayName = (EditText) dialog.findViewById(R.id.etDisplayName);
                                final String displayName = etDisplayName.getText().toString().trim();

                                if (!StringUtils.isBlank(displayName)) {
                                    FirebaseUserUtil.updateUserDisplayName(displayName, new FirebaseUserUtil.FirebaseUserUtilCallback() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            mListener.onDisplayNameChanged(displayName);
                                        }
                                    });
                                    dialog.dismiss();
                                } else {
                                    etDisplayName.setError("Empty Username");
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {dialog.dismiss();}
                        }).show();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onDisplayNameChanged(String displayName);
    }

}
