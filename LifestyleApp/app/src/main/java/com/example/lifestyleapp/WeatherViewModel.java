package com.example.lifestyleapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherData> jsonData = new
            MutableLiveData<WeatherData>();

    public LiveData<WeatherData> getData() {
        return jsonData;
    }
}
