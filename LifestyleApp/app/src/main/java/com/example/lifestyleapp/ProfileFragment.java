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

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private TextView mTvName, mTvAge, mTvCountry, mTvCity, mTvHeight, mTvWeight, mTvSex, mTvActivity;
    private Button mButtonEdit;
    private ProfileFragment.OnDataPass mDataPasser;
    public ProfileFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the detail view
        View view = inflater.inflate(R.layout.view_profile, container, false);

        //Get the text view
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvAge = (TextView) view.findViewById(R.id.tv_age);
        mTvCountry = (TextView) view.findViewById(R.id.tv_country);
        mTvCity = (TextView) view.findViewById(R.id.tv_city);
        mTvHeight = (TextView) view.findViewById(R.id.tv_height);
        mTvWeight = (TextView) view.findViewById(R.id.tv_weight);
        mTvSex = (TextView) view.findViewById(R.id.tv_sex);
        mTvActivity = (TextView) view.findViewById(R.id.tv_activityLevel);
        mButtonEdit = view.findViewById(R.id.button_editProfile);
        mButtonEdit.setOnClickListener(this);

        mTvName.setText(getArguments().getString("name"));
        mTvAge.setText(getArguments().getString("age"));
        mTvCountry.setText(getArguments().getString("country"));
        mTvCity.setText(getArguments().getString("city"));
        mTvHeight.setText(getArguments().getString("height"));
        mTvWeight.setText(getArguments().getString("weight"));
        mTvSex.setText(getArguments().getString("sex"));
        mTvActivity.setText(getArguments().getString("activityLevel"));

        return view;
    }

    public interface OnDataPass{
        void passDataProfile(String name, int age, int weight, int height, String activityLevel, boolean sex, String country,
                      String city);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mDataPasser = (ProfileFragment.OnDataPass) context;
        }
        catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_editGoal : {
                mDataPasser.passDataProfile(mTvName.getText().toString(), Integer.parseInt(mTvAge.getText().toString()),
                        Integer.parseInt(mTvWeight.getText().toString()), Integer.parseInt(mTvHeight.getText().toString()),
                        mTvActivity.getText().toString(), mTvSex.getText().toString().equals("MALE"), mTvCountry.getText().toString(),
                        mTvCity.getText().toString());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        // Get all string and int values associated with this fragment
//        String goal = mSpGoals.getSelectedItem().toString();
//        String activity = mSpActivity.getSelectedItem().toString();
        String name = mTvName.getText().toString();
        int age = Integer.parseInt(mTvAge.getText().toString());
        int weight = Integer.parseInt(mTvWeight.getText().toString());
        int height = Integer.parseInt(mTvHeight.getText().toString());
        String activityLevel = mTvActivity.getText().toString();
        String sex = mTvSex.getText().toString();
        String country = mTvCountry.getText().toString();
        String city = mTvCity.getText().toString();

        // Store all string and int values
//        outState.putString("GOAL", goal);
//        outState.putString("ACTIVITY", activity);
        outState.putString("NAME", name);
        outState.putInt("AGE", age);
        outState.putInt("WEIGHT", weight);
        outState.putInt("HEIGHT", height);
        outState.putString("ACTIVITYLEVEL", activityLevel);
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
//            mSpGoals.setSelection(savedInstanceState.getInt("GOAL"));
//            mSpActivity.setSelection(savedInstanceState.getInt("ACTIVITY"));

            mTvName.setText(savedInstanceState.getString("NAME"));
            mTvAge.setText(savedInstanceState.getString("AGE"));
            mTvCountry.setText(savedInstanceState.getString("COUNTRY"));
            mTvCity.setText(savedInstanceState.getString("CITY"));
            mTvHeight.setText(savedInstanceState.getString("HEIGHT"));
            mTvWeight.setText(savedInstanceState.getString("WEIGHT"));
            mTvSex.setText(savedInstanceState.getString("SEX"));
            mTvActivity.setText(savedInstanceState.getString("ACTIVITYLEVEL"));
        }

        super.onViewStateRestored(savedInstanceState);
    }
}
