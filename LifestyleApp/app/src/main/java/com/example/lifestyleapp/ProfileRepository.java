package com.example.lifestyleapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

/**
 * A Repository is a class that abstracts access to multiple data sources. A Repository class
 * handles data operations. It provides a clean API to the rest of the app for app data.
 *
 * A Repository manages query threads and allows you to use multiple backends. In the most common
 * example, the Repository implements the logic for deciding whether to fetch data from a network
 * or use results cached in a local database.
 */
public class ProfileRepository {

    private ProfileDao mProfileDao;
    private LiveData<UserProfile> mUserProfile;


    ProfileRepository(Application application) {
        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
        mProfileDao = db.profileDao();
        mUserProfile = mProfileDao.getFirstProfile(); // Defaults mUserProfile to first in db
    }

    // Gets the user profile data
    LiveData<UserProfile> getUserProfile(){
        return mUserProfile;
    }

    // Gets the number of rows in the table
    public int getNumberOfProfilesInDatabase(){
        return mProfileDao.getNumberOfProfilesInDatabase();
    }

    // Wrapper function for the insert() method
    public void insert(UserProfile profile){
        new insertAsyncTask(mProfileDao).execute(profile);
    }

    // Updates the user profile
    public void update(UserProfile profile){
        new insertAsyncTask(mProfileDao).execute(profile);
    }

    // Necessary code taken from Google code labs
    private static class insertAsyncTask extends AsyncTask<UserProfile, Void, Void> {

        private ProfileDao mAsyncTaskDao;

        insertAsyncTask(ProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserProfile... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
