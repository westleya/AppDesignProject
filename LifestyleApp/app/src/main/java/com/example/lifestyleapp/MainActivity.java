package com.example.lifestyleapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements EditProfileFragment.OnDataPass, RCViewAdapter.DataPasser
, GoalsFragment.OnDataPass, ProfileFragment.OnDataPass, EditGoalsFragment.OnDataPass {

    private UserProfile mUserProfile;
    private String fileName = "user_profile.txt";
    private String pictureName = "thumbnail.jpg";
    private Fragment mFragment;
    private Toolbar mToolBar;
    private Bitmap mProfilePicture; // Bitmaps aren't serializable so the picture has to be kept separate from the profile info
    private LocationManager mLocMgr;
    private double longitude;
    private double latitude;
    private String mSearchFor = "hikes";

    //Uniquely identify loader
    private static final int SEARCH_LOADER = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside of the activity_layout
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle("");
        // Add the toolbar in as the actionbar
        setSupportActionBar(mToolBar);

        // If there's a saved instance state
        if(savedInstanceState != null){
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "FRAG");
        }

        // check to see if it's a tablet or not
        // TODO: TABLET STUFF

        // check if there's a saved file or not. If not, bring up the edit_profile page.
        // If there is, bring up the fragment_detail page.
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        if(file.exists()) {
            readProfileFromFile();
            readPictureFromFile();
            mFragment = new MasterFragment();
        }
        else {  // Bring up the edit profile page
            if(mFragment == null){ // If there's no lifecycle being restored, make new fragment
                mFragment = new EditProfileFragment();
            }
        }

        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        ftrans.commit();
    }

    private void readProfileFromFile() {
        try {
            FileInputStream in = openFileInput(fileName);
            ObjectInputStream o_in = new ObjectInputStream(in);
            mUserProfile = (UserProfile) o_in.readObject();
            o_in.close();
            in.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private void readPictureFromFile() {
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            mProfilePicture = BitmapFactory.decodeFile(pictureName, options);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

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

    private void savePictureToFile() {
        // Open a file and write to it
        File file = new File(getApplicationContext().getFilesDir(), pictureName);
        if(file.exists()){file.delete();}
        try{
            FileOutputStream out = openFileOutput(pictureName, Context.MODE_PRIVATE);
            mProfilePicture.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
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
        //Save the current fragment's instance
        getSupportFragmentManager().putFragment(outState, "FRAG", mFragment);

        //Save the view hierarchy
        super.onSaveInstanceState(outState);
    }


    /**
     * Handles the incoming data from the EditProfileFragment
     *
     * @param name - user's name
     * @param age - user's age
     * @param weight - user's weight
     * @param height - height in inches
     * @param sex - female = false, male = true
     * @param country - country where user lives
     * @param city - city where user lives
     */
    @Override
    public void passData(String name, int age, int weight, int height, String activityLevel, boolean sex, String country,
                         String city, Bitmap picture) {

        mProfilePicture = picture;
        // Create the UserProfile
        mUserProfile = new UserProfile(name, age, weight, height, activityLevel, sex, country, city);

        // Save user's credentials to file
        saveProfileToFile();
        savePictureToFile();

        // Now that the profile's been made, the menu fragment needs to be brought up.
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        mFragment = new MasterFragment();
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Menu_Fragment");
        ftrans.addToBackStack(null);
        ftrans.commit();

    }

    @Override
    public void passDataGoal(String goal, String currentWeight, String targetWeight, String currentBMI, String targetCalorie) {

        Bundle bundle = new Bundle();
        bundle.putString("GOAL", goal);
        bundle.putString("CURRENT_GOAL", currentWeight);
        bundle.putString("TARGET_WEIGHT", targetWeight);
        bundle.putString("CURRENT_BMI", currentBMI);
        bundle.putString("TARGET_CALORIE", targetCalorie);
        // Now that the profile's been made, the menu fragment needs to be brought up.
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        mFragment = new EditGoalsFragment();
        mFragment.setArguments(bundle);
        ftrans.addToBackStack("back");
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Goals_Fragment");
        ftrans.commit();
    }

    @Override
    public void passDataProfile(String name, int age, int weight, int height, String activityLevel, boolean sex, String country,
             String city) {
        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        bundle.putInt("AGE", age);
        bundle.putInt("WEIGHT", weight);
        bundle.putInt("HEIGHT", height);
        bundle.putString("ACTIVITY_LEVEL", activityLevel);
        bundle.putBoolean("SEX", sex);
        bundle.putString("COUNTRY", country);
        bundle.putString("CITY", city);
        // Now that the profile's been made, the menu fragment needs to be brought up.
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        mFragment = new EditProfileFragment();
        mFragment.setArguments(bundle);
        ftrans.addToBackStack("back");
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Profile_Fragment");
        ftrans.commit();
    }

    @Override
    public void passData(int weight, int year, int month, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt("WEIGHT", weight);
        bundle.putInt("YEAR", year);
        bundle.putInt("MONTH", month);
        bundle.putInt("DAY", day);

        // Now that the profile's been made, the menu fragment needs to be brought up.
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        mFragment = new EditProfileFragment();
        mFragment.setArguments(bundle);
        ftrans.addToBackStack("back");
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Goal_Fragment");
        ftrans.commit();
    }

    /**
     * Handles the incoming position from the Recycler View Adapter
     *
     * @param position
     */
    @Override
    public void passData(int position) {

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        Bundle detailBundle = new Bundle();

        // FIGURE OUT POSITION OF CLICK (WHICH MENU ITEM WAS SELECTED)
        if(position == 0){ // GOALS
            mFragment = new GoalsFragment();
            detailBundle.putString("GOAL", "No current Goal");
            detailBundle.putInt("CURRENT_WEIGHT", 10);
            detailBundle.putInt("TARGET_WEIGHT", 10);
            detailBundle.putInt("BMI", 10);
            detailBundle.putInt("TARGET_CALORIES", 100);

            mFragment.setArguments(detailBundle);
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Goals_Fragment");
        }
        else if(position == 1){ // WEATHER
            mFragment = new WeatherFragment();

            // Sanitize the location for querying the openWeather API
            String location = mUserProfile.getCity().replace(' ', '&');
            location += "&" + mUserProfile.getCountry().replace(' ', '&');

           // Add the data to the bundle to be sent to the fragment
            detailBundle.putString("location", location);

            mFragment.setArguments(detailBundle);
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Weather_Fragment");
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
        Uri queryUri;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            // Use the user's country and city to find hikes since no location is specified
            queryUri = Uri.parse("geo:0,0?q=" + Uri.encode(mUserProfile.getCity() + ", " + mUserProfile.getCountry() +
                    " " + mSearchFor));
        } else {
            // Update the longitude and latitude variables with the device's coordinates
            mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

            // Construct the search for hikes from the device's coordinates
            queryUri = Uri.parse("geo:" + longitude + "," + latitude + "?q=" + mSearchFor);
        }
        // Implicit Intent to Maps App
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, queryUri);

        // If an activity exists for this intent start it
        if (mapsIntent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(mapsIntent);
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

    // Create an action bar
}
