package com.scuvanov.sofit.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Sean on 6/2/2017.
 */

public class FirebaseAuthUtil {

    private static FirebaseAuth mAuth;

    public static void signInWithEmailAndPassword(String email, String password, final FirebaseAuthUtil.FireBaseAuthUtilCallback callback){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        callback.onComplete(task);
                    }
                });
    }

    public static void sendPasswordResetEmail(String email){
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {}
                });
    }

    public static void createUserWithEmailAndPassword(String email, String password, final String displayName, final FireBaseAuthUtilCallback callback){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUserUtil.updateUserDisplayName(displayName, new FirebaseUserUtil.FirebaseUserUtilCallback() {
                                @Override
                                public void onComplete(Task<Void> mTask) {
                                    if(mTask.isSuccessful()) {
                                        callback.onComplete(task);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public interface FireBaseAuthUtilCallback {
        void onAuthStateChanged(FirebaseAuth firebaseAuth);
        void onComplete(Task<AuthResult> task);
    }
}
