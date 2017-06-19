package com.scuvanov.sofit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scuvanov.sofit.adapter.DrawerAdapter;
import com.scuvanov.sofit.fragment.CalendarFragment;
import com.scuvanov.sofit.fragment.CommentFragment;
import com.scuvanov.sofit.fragment.CreateWorkoutFragment;
import com.scuvanov.sofit.fragment.WorkoutDetailFragment;
import com.scuvanov.sofit.fragment.FeedFragment;
import com.scuvanov.sofit.fragment.SettingsFragment;
import com.scuvanov.sofit.fragment.WorkoutItemDetailFragment;
import com.scuvanov.sofit.fragment.TimersFragment;
import com.scuvanov.sofit.fragment.UserFragment;
import com.scuvanov.sofit.model.DrawerItem;
import com.scuvanov.sofit.utils.FirebaseStorageUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LeftMenusActivity extends AppCompatActivity implements FeedFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, CreateWorkoutFragment.OnFragmentInteractionListener, TimersFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener, WorkoutDetailFragment.OnFragmentInteractionListener, WorkoutItemDetailFragment.OnFragmentInteractionListener,
        CommentFragment.OnFragmentInteractionListener {

    public static final String LEFT_MENU_OPTION = "com.scuvanov.sofit.app.LeftMenusActivity";
    public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
    public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";
    private String TAG = LeftMenusActivity.class.getCanonicalName();

    private ListView mDrawerList;
    private List<DrawerItem> mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    FirebaseUser mUser;

    private ImageView ivProfilePic;
    private  TextView tvDisplayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_menus);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_view);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        prepareNavigationDrawerItems();
        setAdapter();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            mDrawerLayout.closeDrawer(mDrawerList);
            FragmentManager fragmentManager = getFragmentManager();
            Fragment mFragment = new FeedFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
        }
    }

    private void setAdapter() {
        String option = LEFT_MENU_OPTION_1;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(LEFT_MENU_OPTION)) {
            option = extras.getString(LEFT_MENU_OPTION, LEFT_MENU_OPTION_1);
        }

        boolean isFirstType = true;

        View headerView = null;
        if (option.equals(LEFT_MENU_OPTION_1)) {
            headerView = prepareHeaderView(R.layout.header_navigation_drawer_1);
        } else if (option.equals(LEFT_MENU_OPTION_2)) {
            headerView = prepareHeaderView(R.layout.header_navigation_drawer_2);
            isFirstType = false;
        }

        BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems, isFirstType);

        mDrawerList.addHeaderView(headerView);//Add header before adapter (for pre-KitKat)
        mDrawerList.setAdapter(adapter);
    }

    private View prepareHeaderView(int layoutRes) {
        View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList, false);
        ivProfilePic = (ImageView) headerView.findViewById(R.id.imageViewProfilePic);
        tvDisplayName = (TextView) headerView.findViewById(R.id.email);

        FirebaseStorageUtil.getProfilePhoto(new FirebaseStorageUtil.FirebaseStorageUtilCallback() {
            @Override
            public void onSuccess(final byte[] bytes) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ivProfilePic.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        tvDisplayName.setText(mUser.getDisplayName());

        return headerView;
    }

    private void prepareNavigationDrawerItems() {
        mDrawerItems = new ArrayList<DrawerItem>();
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.feed,
                        R.string.drawer_title_feed,
                        DrawerItem.DRAWER_ITEM_TAG_FEED));
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.user,
                        R.string.drawer_title_user,
                        DrawerItem.DRAWER_ITEM_TAG_USER));
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.stopwatch,
                        R.string.drawer_title_timers,
                        DrawerItem.DRAWER_ITEM_TAG_TIMERS));
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.calendar,
                        R.string.drawer_title_calendar,
                        DrawerItem.DRAWER_ITEM_TAG_CALENDAR));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onDisplayNameChanged(String displayName) {
        if(!StringUtils.isBlank(displayName))
            tvDisplayName.setText(displayName);
    }

    @Override
    public void onProfilePhotoChanged(byte[] bytes) {
        setPhoto(bytes);
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

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position/*, mDrawerItems.get(position - 1).getTag()*/);
        }
    }

    //set up fragment switching here
    private void selectItem(int position/*, int drawerTag*/) {
        // minus 1 because we have header that has 0 position
        FragmentManager fragmentManager = getFragmentManager();
        Fragment mFragment;
        if (position < 1) { //because we have header, we skip clicking on it
            return;
        }
        switch (position) {
            case 1:
                mFragment = new FeedFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
            case 2:
                mFragment = new UserFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
            case 3:
                mFragment = new TimersFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
            case 4:
                mFragment = new CalendarFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerItems.get(position - 1).getTitle());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
