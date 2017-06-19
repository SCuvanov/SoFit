package com.scuvanov.sofit.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Sean on 6/15/2017.
 */

public class FirebaseStorageUtil {

    private static FirebaseUser mUser;
    private static StorageReference mStorageRef;

    public static void uploadProfilePhoto(byte[] imageData) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference profilePhotoRef = mStorageRef.child("images/profile_pictures/" + mUser.getUid());

        UploadTask uploadTask = profilePhotoRef.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                FirebaseUserUtil.updateUserPhoto(downloadUrl);
            }
        });
    }

    public static void getProfilePhoto(final FirebaseStorageUtilCallback callback){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference profilePhotoRef = mStorageRef.child("images/profile_pictures/" + mUser.getUid());

        final long ONE_MEGABYTE = 1024 * 1024;
        profilePhotoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                callback.onSuccess(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                callback.onFailure(exception);
            }
        });
    }

    public static void getProfilePhoto(String uid, final FirebaseStorageUtilCallback callback){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference profilePhotoRef = mStorageRef.child("images/profile_pictures/" + uid);

        final long ONE_MEGABYTE = 1024 * 1024;
        profilePhotoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                callback.onSuccess(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                callback.onFailure(exception);
            }
        });
    }

    public interface FirebaseStorageUtilCallback {
        void onSuccess(byte[] bytes);
        void onFailure(Exception exception);
    }
}
