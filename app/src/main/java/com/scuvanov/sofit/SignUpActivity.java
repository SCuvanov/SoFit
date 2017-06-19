package com.scuvanov.sofit;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scuvanov.sofit.utils.ActivityUtil;
import com.scuvanov.sofit.utils.FirebaseAuthUtil;

import org.apache.commons.lang3.StringUtils;


public class SignUpActivity extends AppCompatActivity implements OnClickListener {
    String TAG = SignUpActivity.class.getCanonicalName();
    TextView tvBack, tvSignUp, tvRecover;
    EditText etDisplayName, etEmail, etPassword1, etPassword2, etResetEmail;
    String displayName, email, password1, password2;
    MaterialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void init() {

        tvSignUp = (TextView) findViewById(R.id.textViewSignUp);
        tvSignUp.setOnClickListener(this);
        tvBack = (TextView) findViewById(R.id.textViewBack);
        tvBack.setOnClickListener(this);
        tvRecover = (TextView) findViewById(R.id.textViewRecover);
        tvRecover.setOnClickListener(this);

        etDisplayName = (EditText) findViewById(R.id.etDisplayName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword1 = (EditText) findViewById(R.id.etPassword1);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewBack:
                ActivityUtil.goToMainActivity(this);
                break;
            case R.id.textViewSignUp:
                if (checkFields()) {
                    mDialog = new MaterialDialog.Builder(this)
                            .content("Maximizing Gains..")
                            .progress(true, 0)
                            .show();

                    FirebaseAuthUtil.createUserWithEmailAndPassword(email, password1, displayName, new FirebaseAuthUtil.FireBaseAuthUtilCallback() {
                        @Override
                        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {}

                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mDialog.dismiss();
                                ActivityUtil.goToNavigationActivity(SignUpActivity.this);
                            } else {
                                Toast.makeText(SignUpActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.textViewRecover:
                boolean wrapInScrollView = false;
                new MaterialDialog.Builder(this)
                        .customView(R.layout.dialog_recover_password, wrapInScrollView)
                        .title("Reset Password")
                        .titleColorRes(R.color.primary_blue)
                        .positiveText("HURRY!")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .autoDismiss(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                etResetEmail = (EditText) dialog.findViewById(R.id.etEmail);
                                String email = etResetEmail.getText().toString().trim();
                                if (StringUtils.isBlank(email)) {
                                    etResetEmail.setError("Enter Password");
                                } else {
                                    FirebaseAuthUtil.sendPasswordResetEmail(email);
                                    dialog.dismiss();
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();

                break;
        }

    }

    public Boolean checkFields() {
        displayName = etDisplayName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password1 = etPassword1.getText().toString().trim();
        password2 = etPassword2.getText().toString().trim();
        if (!(isAnyEmpty(displayName, email, password1, password2))) {
            if (isPasswordsMatching(password1, password2)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public Boolean isAnyEmpty(String displayName, String email, String password1, String password2) {
        if (displayName.equals("") || displayName.isEmpty()) {
            setEmptyError(etDisplayName, "displayName");
            return true;
        }
        if (email.equals("") || email.isEmpty()) {
            setEmptyError(etEmail, "email");
            return true;
        }
        if (password1.equals("") || password1.isEmpty()) {
            setEmptyError(etPassword1, "password1");
            return true;
        }
        if (password2.equals("") || password2.isEmpty()) {
            setEmptyError(etPassword2, "password2");
            return true;
        }
        return false;
    }

    public Boolean isPasswordsMatching(String password1, String password2) {
        if (password1.equals(password2)) {
            return true;
        } else {
            setPasswordError(etPassword1);
            setPasswordError(etPassword2);
            return false;
        }
    }

    public void setEmptyError(EditText et, String etName) {
        if (etName.equals("displayName")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Username");
        } else if (etName.equals("email")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Email");
        } else if (etName.equals("username")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Username");
        } else if (etName.equals("password1")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Password");
        }
        if (etName.equals("password2")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Password");
        }
    }

    public void setPasswordError(EditText et) {
        et.getText().clear();
        et.setHintTextColor(getResources().getColor(R.color.material_red_300));
        et.setHint("Unmatched Passwords");
    }

}
