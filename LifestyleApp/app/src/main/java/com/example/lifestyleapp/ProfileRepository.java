package com.example.lifestyleapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

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
    private Application mApplication;


    ProfileRepository(Application application) {
        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
        mApplication = application;
        mProfileDao = db.profileDao();
        mUserProfile = mProfileDao.getFirstProfile(); // Defaults mUserProfile to first in db
    }

    // Gets the user profile data
    LiveData<UserProfile> getUserProfile(){
        return mUserProfile;
    }

    // Wrapper function for the insert() method
    public void insert(UserProfile profile){
        new insertAsyncTask(mProfileDao).execute(profile);
        uploadProfileDatabase();
    }

    // Updates the user profile
    public void update(UserProfile profile){
        new insertAsyncTask(mProfileDao).execute(profile);
        uploadProfileDatabase();
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

    // Gets the number of rows in the table
    public VoidAsyncTask getNumberOfProfilesInDatabase(){
        rowsInDatabaseTask task = new rowsInDatabaseTask(mProfileDao);
        return task;
    }

    // For getting the number of rows/entries in the database
    private static class rowsInDatabaseTask extends VoidAsyncTask<Integer> {

        private ProfileDao mAsyncTaskDao;
        private int result;

        rowsInDatabaseTask(ProfileDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            result =  mAsyncTaskDao.getNumberOfProfilesInDatabase();
            return result;
        }

        protected int onPostExecute(int result) {
            return result;
        }

        public int getResult(){
            return result;
        }

    }

    /**
     * Helper method that handles uploading all three files associated with the
     * database that stores the user profile.
     */
    private void uploadProfileDatabase(){
        File file = new File("/data/data/com.example.lifestyleapp/databases/word_database");
        uploadWithTransferUtility(file);
        file = new File("/data/data/com.example.lifestyleapp/databases/word_database-shm");
        uploadWithTransferUtility(file);
        file = new File("/data/data/com.example.lifestyleapp/databases/word_database-wal");
        uploadWithTransferUtility(file);
    }


    /**
     * Utility for uploading the database files to the cloud via AWS.
     */
    public void uploadWithTransferUtility(File file) {

        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAJZ5FWSZ76X64CDKA",
                "VLP5DvDx1M945Z8BuEOdgbS6wGgNwRc7UPmTnnN0") ;
        AmazonS3Client client = new AmazonS3Client(credentials) ;

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(mApplication.getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(client)
                        .build();

        // Upload the files
        TransferObserver uploadObserver = transferUtility.upload(file.getName(), file);

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }

}
