package com.scuvanov.sofit.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scuvanov.sofit.MainActivity;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.adapter.UserFavoriteAdapter;
import com.scuvanov.sofit.adapter.UserRecentAdapter;
import com.scuvanov.sofit.model.Favorite;
import com.scuvanov.sofit.model.Workout;
import com.scuvanov.sofit.service.NotificationService;
import com.scuvanov.sofit.utils.FirebaseStorageUtil;
import com.scuvanov.sofit.utils.FirebaseUserUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class UserFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String BUNDLE_WORKOUT = "WORKOUT";
    private static final String BUNDLE_FAVORITE = "FAVORITE";
    private static final String DETAIL_FRAGMENT = "DETAILFRAGMENT";
    private static final String SETTINGS_FRAGMENT = "SETTINGSFRAGMENT";

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1000;
    private Boolean isPermissionGranted = false;

    private String mParam1;
    private String mParam2;
    private String TAG = UserFragment.class.getCanonicalName();

    private TextView tvUsername, tvRecent, tvFavorites;

    private OnFragmentInteractionListener mListener;
    private View view;

    private RecyclerView rvRecent, rvFavorites;
    private UserRecentAdapter mRecentAdapter;
    private UserFavoriteAdapter mFavoriteAdapter;

    private ImageView ivProfilePic;

    final int PHOTO_WIDTH = 300;
    final int PHOTO_HEIGHT = 300;
    private final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private Handler mHandler;

    //Firebase
    private static FirebaseUser mUser;

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {
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
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_user, container, false);
        init();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void init() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        tvUsername = (TextView) view.findViewById(R.id.textViewTitle);
        tvUsername.setText(mUser.getDisplayName());

        ivProfilePic = (ImageView) view.findViewById(R.id.imageViewProfile);
        ivProfilePic.setOnClickListener(this);

        FirebaseStorageUtil.getProfilePhoto(new FirebaseStorageUtil.FirebaseStorageUtilCallback() {
            @Override
            public void onSuccess(byte[] bytes) {
                setPhoto(bytes);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        tvRecent = (TextView) view.findViewById(R.id.textViewRecent);
        tvRecent.setOnClickListener(this);
        tvFavorites = (TextView) view.findViewById(R.id.textViewFavorites);
        tvFavorites.setOnClickListener(this);

        tvFavorites.setTextColor(getActivity().getResources().getColor(R.color.main_color_grey_500));
        tvRecent.setTextColor(getActivity().getResources().getColor(R.color.primary_blue));

        rvRecent = (RecyclerView) view.findViewById(R.id.rvRecent);
        rvFavorites = (RecyclerView) view.findViewById(R.id.rvFavorites);
        rvFavorites.setVisibility(View.GONE);

        mRecentAdapter = new UserRecentAdapter(getActivity(), new UserRecentAdapter.UserRecentAdapterOnItemClickListener() {
            @Override
            public void onClick(Workout workout) {
                goToDetailFragment(workout, null);
            }
        });

        mFavoriteAdapter = new UserFavoriteAdapter(getActivity(), new UserFavoriteAdapter.UserFavoriteAdapterOnItemClickListener() {
            @Override
            public void onClick(Favorite favorite) {
                goToDetailFragment(null, favorite);
            }
        });

        rvRecent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecent.setAdapter(mRecentAdapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavorites.setAdapter(mFavoriteAdapter);
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
            case R.id.textViewRecent:
                rvRecent.setVisibility(View.VISIBLE);
                rvFavorites.setVisibility(View.GONE);
                tvFavorites.setTextColor(getActivity().getResources().getColor(R.color.main_color_grey_500));
                tvRecent.setTextColor(getActivity().getResources().getColor(R.color.primary_blue));
                break;
            case R.id.textViewFavorites:
                rvFavorites.setVisibility(View.VISIBLE);
                rvRecent.setVisibility(View.GONE);
                tvRecent.setTextColor(getActivity().getResources().getColor(R.color.main_color_grey_500));
                tvFavorites.setTextColor(getActivity().getResources().getColor(R.color.primary_blue));
                break;

            case R.id.imageViewProfile:
                choosePicture();
                break;
        }
    }

    public void choosePicture() {
        requestExternalStorageAccess();
        if(!isPermissionGranted) return;

        goToGallery();
    }

    public void setPhoto(final byte[] encodedByteArray) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (encodedByteArray != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);
                        ivProfilePic.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                ivProfilePic.setImageBitmap(bitmap);

                byte[] imageData = stream.toByteArray();
                FirebaseStorageUtil.uploadProfilePhoto(imageData);
                mListener.onProfilePhotoChanged(imageData);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true;
                    goToGallery();
                } else {
                    isPermissionGranted = false;
                }
                return;
            }
        }
    }

    public void requestExternalStorageAccess(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            isPermissionGranted = true;
        }
    }

    public void goToGallery(){
        Intent cropIntent = new Intent();
        cropIntent.setType("image/*");
        cropIntent.setAction(Intent.ACTION_PICK);
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("outputX", PHOTO_WIDTH);
        cropIntent.putExtra("outputY", PHOTO_HEIGHT);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void goToDetailFragment(final Workout workout, final Favorite favorite) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                if (workout != null)
                    bundle.putSerializable(BUNDLE_WORKOUT, workout);
                if (favorite != null)
                    bundle.putSerializable(BUNDLE_FAVORITE, favorite);
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new WorkoutDetailFragment();
                mFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(DETAIL_FRAGMENT).commit();

            }
        }, 200);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                getActivity().stopService(new Intent(getActivity(), NotificationService.class));
                logout();
                return true;

            case R.id.action_settings:
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack(SETTINGS_FRAGMENT).commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onProfilePhotoChanged(byte[] bytes);
    }

}
