package com.scuvanov.sofit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = MainActivity.class.getCanonicalName();
    TextView tvSignIn, tvSignUp, tvRecover;
    EditText etEmail, etPassword1, etResetEmail;
    String email, password1;
    MaterialDialog mDialog;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    ActivityUtil.goToNavigationActivity(MainActivity.this);
                } else {
                    tvSignUp = (TextView) findViewById(R.id.textViewSignUp);
                    tvSignUp.setOnClickListener(MainActivity.this);
                    tvSignIn = (TextView) findViewById(R.id.textViewSignIn);
                    tvSignIn.setOnClickListener(MainActivity.this);
                    tvRecover = (TextView) findViewById(R.id.textViewRecover);
                    tvRecover.setOnClickListener(MainActivity.this);
                    etEmail = (EditText) findViewById(R.id.etEmail);
                    etPassword1 = (EditText) findViewById(R.id.etPassword1);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewSignUp:
                ActivityUtil.goToRegisterActivity(this);
                break;
            case R.id.textViewSignIn:
                if (checkFields()) {
                    mDialog = new MaterialDialog.Builder(this)
                            .content("Maximizing Gains..")
                            .progress(true, 0)
                            .show();

                    FirebaseAuthUtil.signInWithEmailAndPassword(email, password1,
                            new FirebaseAuthUtil.FireBaseAuthUtilCallback() {
                                @Override
                                public void onAuthStateChanged(FirebaseAuth firebaseAuth) {}

                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    mDialog.dismiss();

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, R.string.auth_failed,
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
        email = etEmail.getText().toString().trim();
        password1 = etPassword1.getText().toString().trim();
        if (!(isAnyEmpty(email, password1))) {
            return true;
        }
        return false;
    }

    public Boolean isAnyEmpty(String email, String password1) {
        if (email.equals("") || email.isEmpty()) {
            setEmptyError(etEmail, "email");
            return true;
        }
        if (password1.equals("") || password1.isEmpty()) {
            setEmptyError(etPassword1, "password1");
            return true;
        }
        return false;
    }

    public void setEmptyError(EditText et, String etName) {
        if (etName.equals("email")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Email");
        } else if (etName.equals("password1")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Password");
        }
    }
}
