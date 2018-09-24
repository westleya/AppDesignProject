package com.example.lifestyleapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements EditProfileFragment.OnDataPass, RCViewAdapter.DataPasser
{

    private UserProfile mUserProfile;
    private String profileName = "user_profile.txt";
    private String pictureName = "thumbnail.jpg";
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If there's a saved instance state
        if(savedInstanceState != null){
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "FRAG");
        }

        // check to see if it's a tablet or not

        // check if there's a saved file or not. If not, bring up the edit_profile page.
        // If there is, bring up the menu page.
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        File file = new File(getApplicationContext().getFilesDir(), profileName);
        if(file.exists()) {
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

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void saveProfileToFile() {

        // Open a file and write to it
        File file = new File(getApplicationContext().getFilesDir(), profileName);
        if(file.exists()) { file.delete(); }
        try {
            FileOutputStream out = openFileOutput(profileName, Context.MODE_PRIVATE);
            DataOutputStream d_out = new DataOutputStream(out);
            d_out.writeChars(mUserProfile.getName());
            d_out.writeInt(mUserProfile.getAge());
            d_out.writeChars(mUserProfile.getCity());
            d_out.writeChars(mUserProfile.getCountry());
            d_out.writeInt(mUserProfile.getHeight());
            d_out.writeInt(mUserProfile.getWeight());
            d_out.writeBoolean(mUserProfile.getGender());
            d_out.writeDouble(mUserProfile.getPoundsPerWeek());
            d_out.writeDouble(mUserProfile.getActivityLevel());
            d_out.flush();
            d_out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveImageToFile() {
        // Open a file and write to it
        File file = new File(getApplicationContext().getFilesDir(), pictureName);
        if(file.exists()) { file.delete(); }
        try {
            FileOutputStream out = openFileOutput(pictureName, Context.MODE_PRIVATE);
            mUserProfile.getProfilePicture().compress(Bitmap.CompressFormat.JPEG, 90, out);
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
     * @param profilePic - profile picture for user
     */
    @Override
    public void passData(String name, int age, int weight, int height, String activityLevel, boolean sex, String country,
                         String city, Bitmap profilePic) {

        // Create the UserProfile
        UserProfile mUserProfile = new UserProfile(name, age, weight, height, activityLevel, sex, country, city, profilePic);

        // Save user's credentials to file
        saveProfileToFile();
        saveImageToFile();
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
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Goals Fragment");
        }
        else if(position == 1){ // WEATHER
            mFragment = new WeatherFragment();
            ftrans.replace(R.id.fl_frag_masterlist_container_phone, mFragment, "Edit_Profile_Fragment");
        }
        else{ // HIKING
            // WES, HOW DO I DO THIS?
            //findHikes(this, mUserProfile);
        }

        // Create and inflate the
        ftrans.commit();

    }

    // Create an action bar
}
