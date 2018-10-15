package com.example.lifestyleapp;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements RCViewAdapter.DataPasser, EditGoalsFragment.OnDataPass {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private int mProfilesInDatabase = 0;
    private UserProfile mUserProfile;
    private String fileName = "user_profile.txt";
    private Fragment mFragment; // Sufficient for the phone
    private Fragment mTabletFragment; // Necessary for the two-pane nature of the tablet
    private Toolbar mToolBar;
    private AppCompatImageView mToolBarPic;
    private LocationManager mLocMgr;
    private double longitude;
    private double latitude;
    private String mSearchFor = "hikes";
    private WeatherViewModel mWeatherViewModel;
    private ProfileViewModel mProfileViewModel;
    public static boolean debug = false;


    //Uniquely identify loader
    private static final int SEARCH_LOADER = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside of the activity_layout
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle("");
        // Find the picture view inside the activity_layout
        mToolBarPic = findViewById(R.id.iv_toolbar_profile_pic);
        // Add the toolbar in as the actionbar
        setSupportActionBar(mToolBar);

<<<<<<< HEAD
=======
        /**
         Create View Models. New for part 2.
         */
>>>>>>> 9599b8a7dbab770c70ca29193d08ce9595ce95ee
        // ProfileViewModel needed to make sure mUserProfile us up to date as well as profile pic
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // Any time the profile is updated, the profileObserver will run.
        mProfileViewModel.getProfile().observe(this, profileObserver);

        /*
         * THIS HANDLES WHETHER OR NOT THERE IS A ROW IN THE TABLE
         */
        VoidAsyncTask rowsInDatabaseTask = mProfileViewModel.getNumberOfProfilesInDatabase();
        rowsInDatabaseTask.execute();
        try {
            mProfilesInDatabase = (int) rowsInDatabaseTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start a transaction for filling the screen with
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();

        // Check if there's a saved instance state. If there is, then restore the fragment
        // that was active. Check if there's a saved file. If there is a file, bring up
        // the menu page. If there's no file, bring up the edit_profile page.
        // The implementation is different depending on tablet/phone.

        if(savedInstanceState != null){
            if(mProfilesInDatabase > 0) {
                if(isTablet()) {
                    // Set the Menu
                    mFragment = new MasterFragment();
                    ftrans.replace(R.id.fl_frag_masterlist_container_tablet, mFragment, "Menu_Fragment");
                    // Set the detail pane
                    mTabletFragment = getSupportFragmentManager().getFragment(savedInstanceState, "FRAG");
                    ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                            mTabletFragment.getTag());
                }
                else {
                    mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "FRAG");
                    ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, mFragment.getTag());
                }
            }
            else { // They shouldn't have made it past the edit profile portion yet if this is true.
                if (isTablet()) {
                    // Set the detail pane
                    // I'm electing not to set the menu pane in this case since we don't want the
                    // user to be able to skip the profile creation process.
                    mTabletFragment = getSupportFragmentManager().getFragment(savedInstanceState, "FRAG");
                    ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                            mTabletFragment.getTag());
                } else {
                    mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "FRAG");
                    ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, mFragment.getTag());
                }
            }
        }
        else {
            if(mProfilesInDatabase > 0) { // The user is restarting the app and has a profile.
                if(isTablet()) {
                    // Set the menu. The right pane can be left blank unless we decide otherwise aesthetically.
                    mFragment = new MasterFragment();
                    ftrans.replace(R.id.fl_frag_masterlist_container_tablet, mFragment, "Menu_Fragment");
                }
                else {
                    mFragment = new MasterFragment();
                    ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Menu_Fragment");
                }

            }
            else { // The user is opening the app for the first time.
                if (isTablet()) {
                    // Set the detail pane
                    // I'm electing not to set the menu pane in this case since we don't want the
                    // user to be able to skip the profile creation process.
                    mTabletFragment = new EditProfileFragment();
                    ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                            "Edit_Profile_Fragment");
                } else {
                    mFragment = new EditProfileFragment();
                    ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
                }
            }

        }
        ftrans.commit();
    }

    /**
     https://developer.android.com/topic/libraries/architecture/viewmodel#sharing used the
     another format to observe, while the examples from class created this separate 'observer'
     method. They seem to be comparable to me, but I couldn't get the web's format to work.
     */
    // Create an observer that watches the LiveData<UserProfile> object.
    final Observer<UserProfile> profileObserver = new Observer<UserProfile>() {
        @Override
        public void onChanged(@Nullable UserProfile profile) {
            // Update the UI if the data variable changes
            if(profile != null) {
                // Set the user profile to the new data
                mUserProfile = profile;
                // Set the Toolbar's picture to the new picture data
                mToolBarPic.setImageBitmap(GeneralUtils.convertImage(mUserProfile.getImage()));

                //Log.d("NUMBER OF ROWS FROM MAIN ACTIVITY", Integer.toString(mProfileViewModel.getNumberOfProfilesInDatabase()));
            }

        }

    };


    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void saveProfileToFile() {

        // Open a file and write to it
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        if(file.exists()) { file.delete(); }
        try {
            FileOutputStream out = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream o_out = new ObjectOutputStream(out);
            o_out.writeObject(mUserProfile);
            o_out.flush();
            o_out.close();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves data required for lifecycle awareness lifecycle awareness
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        // Since the left pane is just the menu in tablet
        // it can be manually restored instead of saved.

        // Save the current fragment's instance
        // mTabletFragment is the fragment to be saved for tablet.
        // I'm realizing that I may have used an inefficient naming convention.
        if(isTablet()) {
            getSupportFragmentManager().putFragment(outState, "FRAG", mTabletFragment);
        }
        else {
            getSupportFragmentManager().putFragment(outState, "FRAG", mFragment);
        }
        //Save the view hierarchy
        super.onSaveInstanceState(outState);
    }


    /**
     * Handles the incoming data from the EditGoalsFragment.
     *
     * @param goalWeight
     */
    @Override
    public void passDataEditGoal(int goalWeight, String goal, double poundsPerWeek ) {

        // Set the new data to the user profile
        mUserProfile.setTargetWeight(goalWeight);
        mUserProfile.setPoundsPerWeek(poundsPerWeek);
        mUserProfile.setGoal(goal);

        Bundle detailBundle = new Bundle();
        detailBundle.putString("GOAL", mUserProfile.getGoal());
        detailBundle.putInt("CURRENT_WEIGHT", mUserProfile.getWeight());
        detailBundle.putInt("TARGET_WEIGHT", mUserProfile.getTargetWeight());
        detailBundle.putInt("BMI", FitnessUtils.calculateBMI(mUserProfile));
        detailBundle.putInt("TARGET_CALORIES", FitnessUtils.calculateExpectedCaloricIntake(mUserProfile));

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        if(isTablet()) {
            mTabletFragment = new GoalsFragment();
            mTabletFragment.setArguments(detailBundle);
            ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                    "Goals_Fragment");
        }
        else {
            mFragment = new GoalsFragment();
            mFragment.setArguments(detailBundle);
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Goals_Fragment");
        }

        ftrans.addToBackStack(null);
        ftrans.commit();
    }

    /**
     * Handles the incoming position from the Recycler View Adapter.
     * Basically handles which menu item was clicked.
     *
     * @param position
     */
    @Override
    public void passData(int position) {

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        Bundle detailBundle = new Bundle();

        // FIGURE OUT POSITION OF CLICK (WHICH MENU ITEM WAS SELECTED)
        if(position == 0){ // GOALS

            detailBundle.putString("GOAL", mUserProfile.getGoal());
            detailBundle.putInt("CURRENT_WEIGHT", mUserProfile.getWeight());
            detailBundle.putInt("TARGET_WEIGHT", mUserProfile.getTargetWeight());
            detailBundle.putInt("BMI", FitnessUtils.calculateBMI(mUserProfile));
            detailBundle.putInt("TARGET_CALORIES", FitnessUtils.calculateExpectedCaloricIntake(mUserProfile));

            if(isTablet()) {
                mTabletFragment = new GoalsFragment();
                mTabletFragment.setArguments(detailBundle);
                ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                        "Goals_Fragment");
            }
            else {
                mFragment = new GoalsFragment();
                mFragment.setArguments(detailBundle);
                ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Goals_Fragment");
            }
        }
        else if(position == 1){ // WEATHER
            // Sanitize the location for querying the openWeather API
            String location = mUserProfile.getCity().replace(' ', '&');
            location += "&" + mUserProfile.getCountry().replace(' ', '&');
           // Add the data to the bundle to be sent to the fragment
            detailBundle.putString("location", location);

            if(isTablet()) {
                mTabletFragment = new WeatherFragment();
                mTabletFragment.setArguments(detailBundle);
                ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                        "Weather_Fragment");
            }
            else {
                mFragment = new WeatherFragment();
                mFragment.setArguments(detailBundle);
                ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Weather_Fragment");
            }
        }
        else{ // HIKING
            FindHikes();
        }

        // Create and inflate the fragment
        ftrans.addToBackStack(null);
        ftrans.commit();
    }

////////FIND LOCAL HIKES////////

    // Use GPS to get the nearest hikes if possible.
    // If not possible, use the city and country provided.
    public void FindHikes() {
        mLocMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Check for GPS permissions
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // Ask for permission if we don't have it already.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else { // We have permission. Find nearby hikes.
            Uri queryUri;
            // Update the longitude and latitude variables with the device's coordinates
            mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

            // Construct the search for hikes from the device's coordinates
            queryUri = Uri.parse("geo:" + longitude + "," + latitude + "?q=" + mSearchFor);

            // Implicit Intent to Maps App
            Intent mapsIntent = new Intent(Intent.ACTION_VIEW, queryUri);

            // If an activity exists for this intent start it
            if (mapsIntent.resolveActivity(this.getPackageManager()) != null) {
                this.startActivity(mapsIntent);
            }
        }
    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FindHikes();
                } else {

                    // permission denied, boo!
                    // Use the user's country and city to find hikes since no location is specified
                    Uri queryUri = Uri.parse("geo:0,0?q=" + Uri.encode(mUserProfile.getCity() + ", " + mUserProfile.getCountry() +
                            " " + mSearchFor));
                    // Implicit Intent to Maps App
                    Intent mapsIntent = new Intent(Intent.ACTION_VIEW, queryUri);

                    // If an activity exists for this intent start it
                    if (mapsIntent.resolveActivity(this.getPackageManager()) != null) {
                        this.startActivity(mapsIntent);
                    }
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    /**
     * Handles when the logo/home button is clicked. Returns to the home menu
     */
    public void returnToHome(View view) {
        // The home and profile buttons should only work after the user has finished filling
        // out their profile and behave differently depending on tablet/phone.
        if(mProfilesInDatabase > 0) {
            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            if(isTablet()) {
                // I'm not sure what to do here since menu is always visible on tablet.
                return;
            }
            else {
                mFragment = new MasterFragment();
                ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Menu_Fragment");
            }
            ftrans.addToBackStack(null);
            ftrans.commit();
        }
    }

    /**
     * Handles a button click to go to the Edit Profile page
     */
    public void goToEditProfile(View view){

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        if(isTablet()) {
            mTabletFragment = new EditProfileFragment();
            ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                    "Edit_Profile_Fragment");
        }
        else {
            mFragment = new EditProfileFragment();
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        }
        ftrans.addToBackStack(null);
        ftrans.commit();
    }

    /**
     * Handles a button click to go to the Edit Goal page
     */
    public void goToEditGoal(View view){

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();

        if(isTablet()) {
            mTabletFragment = new EditGoalsFragment();
            ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                    "Edit_Goals_Fragment");
        }
        else {
            mFragment = new EditGoalsFragment();
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Goals_Fragment");
        }
        ftrans.addToBackStack(null);
        ftrans.commit();
    }

    /**
     * Handles when the user profile icon is clicked. Goes to the user profile screen
     */
    public void goToUserProfile(View view) {

        // The home and profile buttons should only work after the user has finished filling
        // out their profile and behave differently depending on tablet/phone.
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        if(mProfilesInDatabase > 0) {
            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();

            if(isTablet()) {
                mTabletFragment = new ProfileFragment();
                ftrans.replace(R.id.fl_frag_itemdetail_container_tablet, mTabletFragment,
                        "Profile_Fragment");
            }
            else {
                mFragment = new ProfileFragment();
                ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment,
                        "Profile_Fragment");
            }
            ftrans.addToBackStack(null);
            ftrans.commit();
        }
    }

}

