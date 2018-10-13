package com.example.lifestyleapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<UserProfile> userData = new
        MutableLiveData<UserProfile>();

    public LiveData<UserProfile> getData() {
        return userData;
    }

}
