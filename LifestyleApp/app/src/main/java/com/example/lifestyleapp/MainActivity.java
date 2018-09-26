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
, LoaderManager.LoaderCallbacks<String>, EditGoalsFragment.OnDataPass {

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
    public static final String URL_STRING = "query";
    WeatherData mWeatherData;

    //Uniquely identify loader
    private static final int SEARCH_LOADER = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(SEARCH_LOADER, null, this);

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
        ftrans.addToBackStack("back");
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        ftrans.commit();
        ftrans.addToBackStack(null);

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
            mFragment = new GoalsFragment();
            detailBundle.putString("GOAL", "No current Goal");
            detailBundle.putInt("CURRENT_WEIGHT", 10);
            detailBundle.putInt("TARGET_WEIGHT", 10);
            detailBundle.putInt("BMI", 10);
            detailBundle.putInt("TARGET_CALORIES", 100);

            mFragment.setArguments(detailBundle);
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Goals Fragment");

            // Create and inflate the fragment
            ftrans.commit();
            ftrans.addToBackStack(null);
        }
        else if(position == 1){ // WEATHER
            mFragment = new WeatherFragment();

            // Sanitize the location for querying the openWeather API
            String location = mUserProfile.getCity().replace(' ', '&');
            location += "&" + mUserProfile.getCountry().replace(' ', '&');

            // Get the weather data
            getWeather(location);

        }
        else{ // HIKING
            FindHikes();
        }

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

////////GET WEATHER DATA/////////////


    private void getWeather(String location) {

        Bundle searchQueryBundle = new Bundle();
        searchQueryBundle.putString(URL_STRING, location);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> searchLoader = loaderManager.getLoader(SEARCH_LOADER);

        if(searchLoader == null) {
            loaderManager.initLoader(SEARCH_LOADER, searchQueryBundle,this);
        }
        else {
            loaderManager.restartLoader(SEARCH_LOADER, searchQueryBundle,this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            private String mLoaderData;

            @Override
            protected void onStartLoading() {
                if(bundle == null) {
                    return;
                }
                if(mLoaderData != null) {
                    //Cache data for onPause instead of loading all over again
                    //Other config changes are handled automatically
                    deliverResult(mLoaderData);
                }
                else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String location = bundle.getString(URL_STRING);
                URL weatherDataURL = WeatherUtils.buildURLFromLocation(location);
                String jsonWeatherData = null;
                try {
                    jsonWeatherData = WeatherUtils.getDataFromURL(weatherDataURL);
                    return jsonWeatherData;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                mLoaderData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String jsonWeatherData) {
        if(jsonWeatherData != null) {
            try {
                mWeatherData = WeatherUtils.CreateWeatherData(jsonWeatherData);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            if(mWeatherData != null) {
                FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                Bundle detailBundle = new Bundle();

                // Sanitize the location for querying the openWeather API
                String location = mUserProfile.getCity().replace(' ', '&');
                location += "&" + mUserProfile.getCountry().replace(' ', '&');

                // location, weather, wind, humidity, pressure;
                // Add the data to the bundle to be sent to the fragment
                detailBundle.putString("location", location);
                detailBundle.putString("weather", mWeatherData.getCurrentCondition().getDescription());
                detailBundle.putString("wind", Double.toString(mWeatherData.getWind().getSpeed()));
                detailBundle.putString("temperature", Double.toString((9 * ((
                        mWeatherData.getTemperature().getTemp() - 273) / 5) + 32)));
                detailBundle.putString("humidity", Double.toString(mWeatherData.getCurrentCondition().getHumidity()));
                detailBundle.putString("pressure", Double.toString(mWeatherData.getCurrentCondition().getPressure()));

                mFragment.setArguments(detailBundle);
                ftrans.addToBackStack("back");
                ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
                ftrans.commit();
                ftrans.addToBackStack(null);
            }

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    /**
     * Handles when the logo/home button is clicked. Returns to the home menu
     */
    public void returnToHome(View view) {
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        mFragment = new MasterFragment();
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        ftrans.commit();
        ftrans.addToBackStack(null);
    }

    /**
     * Handles a button click to go to the Edit Profile page
     */
    public void goToEditProfile(View view){
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        mFragment = new EditProfileFragment();
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        ftrans.commit();
        ftrans.addToBackStack(null);
    }

    /**
     * Handles a button click to go to the Edit Goal page
     */
    public void goToEditGoal(View view){
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        mFragment = new EditGoalsFragment();
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        ftrans.commit();
        ftrans.addToBackStack(null);
    }

    /**
     * Handles when the user profile icon is clicked. Goes to the user profile screen
     */
    public void goToUserProfile(View view) {

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        Bundle detailBundle = new Bundle();

        detailBundle.putString("NAME", mUserProfile.getName());
        detailBundle.putString("AGE", Integer.toString(mUserProfile.getAge()));
        detailBundle.putString("COUNTRY", mUserProfile.getCountry());
        detailBundle.putString("CITY", mUserProfile.getCity());
        detailBundle.putInt("HEIGHT", mUserProfile.getHeight());
        detailBundle.putString("WEIGHT", Integer.toString(mUserProfile.getWeight()));
        detailBundle.putBoolean("SEX", mUserProfile.getGender());
        detailBundle.putDouble("ACTIVITY_LEVEL", mUserProfile.getActivityLevel());

        mFragment = new ProfileFragment();
        mFragment.setArguments(detailBundle);
        ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        ftrans.commit();
        ftrans.addToBackStack(null);
    }

}