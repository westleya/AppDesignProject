package com.example.lifestyleapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    private TextView mTvName, mTvAge, mTvCountry, mTvCity, mTvHeight, mTvWeight, mTvSex, mTvActivity;
    public ProfileFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the detail view
        View view = inflater.inflate(R.layout.view_profile, container, false);

        //Get the text view
        mTvName = view.findViewById(R.id.tv_name);
        mTvAge = view.findViewById(R.id.tv_age);
        mTvCountry = view.findViewById(R.id.tv_country);
        mTvCity = view.findViewById(R.id.tv_city);
        mTvHeight = view.findViewById(R.id.tv_height);
        mTvWeight = view.findViewById(R.id.tv_weight);
        mTvSex = view.findViewById(R.id.tv_sex);
        mTvActivity = view.findViewById(R.id.tv_activityLevel);

        mTvName.setText(getArguments().getString("NAME"));
        mTvAge.setText(getArguments().getString("AGE"));
        mTvCountry.setText(getArguments().getString("COUNTRY"));
        mTvCity.setText(getArguments().getString("CITY"));
        mTvHeight.setText(GeneralUtils.inchesToHeight(getArguments().getInt("HEIGHT")));
        mTvWeight.setText(getArguments().getString("WEIGHT"));
        mTvSex.setText(GeneralUtils.sexToString(getArguments().getBoolean("SEX")));
        mTvActivity.setText(GeneralUtils.doubleToActivityLevel(getArguments().getDouble("ACTIVITY_LEVEL")));

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        // Get all string and int values associated with this fragment
        String name = mTvName.getText().toString();
        String age = mTvAge.getText().toString();
        String weight = mTvWeight.getText().toString();
        String height = mTvHeight.getText().toString();
        String activityLevel = mTvActivity.getText().toString();
        String sex = mTvSex.getText().toString();
        String country = mTvCountry.getText().toString();
        String city = mTvCity.getText().toString();

        // Store all string and int values
        outState.putString("NAME", name);
        outState.putString("AGE", age);
        outState.putString("WEIGHT", weight);
        outState.putString("HEIGHT", height);
        outState.putString("ACTIVITY_LEVEL", activityLevel);
        outState.putString("SEX", sex);
        outState.putString("COUNTRY", country);
        outState.putString("CITY", city);

        //Save the view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored (Bundle savedInstanceState){

        if(savedInstanceState != null){
            // Restore simple saved data
            mTvName.setText(savedInstanceState.getString("NAME"));
            mTvAge.setText(savedInstanceState.getString("AGE"));
            mTvCountry.setText(savedInstanceState.getString("COUNTRY"));
            mTvCity.setText(savedInstanceState.getString("CITY"));
            mTvHeight.setText(savedInstanceState.getString("HEIGHT"));
            mTvWeight.setText(savedInstanceState.getString("WEIGHT"));
            mTvSex.setText(savedInstanceState.getString("SEX"));
            mTvActivity.setText(savedInstanceState.getString("ACTIVITY_LEVEL"));
        }

        super.onViewStateRestored(savedInstanceState);
    }
}
