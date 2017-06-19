package com.scuvanov.sofit.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.scuvanov.sofit.R;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by Sean on 6/1/2017.
 */

public class FirebaseUserUtil {

    private static FirebaseUser mUser;
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";

    private static EditText etEmail, etPassword;

    public static void updateUserPassword(final Context context, final String password) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            return;
                        }

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthRecentLoginRequiredException e1) {
                            showReauthenticateDialog(context, new ReauthenticateDialogCallback() {
                                @Override
                                public void onPositive(HashMap<String, String> userCred) {
                                    if (userCred.isEmpty() || StringUtils.isBlank(userCred.get(EMAIL)) || StringUtils.isBlank(userCred.get(PASSWORD)))
                                        return;

                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(userCred.get(EMAIL), userCred.get(PASSWORD));

                                    reauthenticateUser(credential, new FirebaseUserUtilCallback() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                updateUserPassword(context, password);
                                            } else {
                                                return;
                                            }
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            return;
                        }
                    }
                });
    }

    private static void showReauthenticateDialog(Context context, final ReauthenticateDialogCallback callback) {
        final HashMap<String, String> userCred = new HashMap<String, String>();

        boolean wrapInScrollView = false;
        MaterialDialog mReauthenticateDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_reauthenticate, wrapInScrollView)
                .title("Reauthenticate User")
                .positiveText("ACCEPT")
                .negativeText("CANCEL")
                .negativeColorRes(R.color.material_grey_900)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        etEmail = (EditText) dialog.findViewById(R.id.etEmail);
                        etPassword = (EditText) dialog.findViewById(R.id.etPassword);

                        String email = etEmail.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();

                        if (StringUtils.isBlank(email)) etEmail.setError("Email Required");
                        if (StringUtils.isBlank(password)) etPassword.setError("Password Required");
                        if (!StringUtils.isBlank(email) && !StringUtils.isBlank(password)) {
                            userCred.put(EMAIL, email);
                            userCred.put(PASSWORD, password);
                            dialog.dismiss();

                            callback.onPositive(userCred);
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private static void reauthenticateUser(AuthCredential credential, final FirebaseUserUtilCallback callback) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onComplete(task);
                    }
                });
    }

    public static void updateUserDisplayName(String displayName, final FirebaseUserUtilCallback callback) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onComplete(task);
                    }
                });
    }

    public static void updateUserPhoto(Uri photoUri) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUri)
                .build();

        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    public interface FirebaseUserUtilCallback {
        void onComplete(Task<Void> task);
    }

    public interface ReauthenticateDialogCallback {
        void onPositive(HashMap<String, String> userCred);
    }
}
