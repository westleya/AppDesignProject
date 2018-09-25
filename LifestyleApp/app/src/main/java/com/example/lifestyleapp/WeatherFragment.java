package com.example.lifestyleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherFragment extends Fragment{
    private TextView mTvLocation, mTvWeather, mTvWind, mTvHumidity, mTvPressure;

    public WeatherFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the detail view
        View view = inflater.inflate(R.layout.view_weather, container, false);

        //Get the text view
        mTvLocation = (TextView) view.findViewById(R.id.tv_location);
        mTvWeather = (TextView) view.findViewById(R.id.tv_weather);
        mTvWind = (TextView) view.findViewById(R.id.tv_wind);
        mTvHumidity = (TextView) view.findViewById(R.id.tv_humidity);
        mTvPressure = (TextView) view.findViewById(R.id.tv_press);

        mTvLocation.setText(getArguments().getString("location"));
        mTvWeather.setText(getArguments().getString("weather") + ", " +
                getArguments().getString("temperature") + " F");
        mTvWind.setText(getArguments().getString("wind") + "m/s");
        mTvHumidity.setText(getArguments().getString("humidity") + "%");
        mTvPressure.setText(getArguments().getString("pressure") + "hPa");

        return view;
    }


}
