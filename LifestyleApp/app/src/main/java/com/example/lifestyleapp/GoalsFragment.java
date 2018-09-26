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

public class GoalsFragment extends Fragment {

    private TextView mTvGoals, mTvCurrentWeight, mTvTargetWeight, mTvCurrentBMI, mTvTargetCalorie;
    private Button mButtonEdit;
    public GoalsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the detail view
        View view = inflater.inflate(R.layout.view_goals, container, false);

        // Get the text view
        mTvGoals = view.findViewById(R.id.tv_goal);
        mTvCurrentWeight = view.findViewById(R.id.tv_currentWeight);
        mTvTargetWeight = view.findViewById(R.id.tv_targetWeight);
        mTvCurrentBMI = view.findViewById(R.id.tv_currentBMI);
        mTvTargetCalorie = view.findViewById(R.id.tv_targetCalorie);
        mButtonEdit = view.findViewById(R.id.button_editGoal);

        // Get the incoming details
//        mTvGoals.setText(getArguments().getString("GOAL"));
//        mTvCurrentWeight.setText(Integer.toString(getArguments().getInt("CURRENT_WIGHT")));
//        mTvTargetWeight.setText(Integer.toString(getArguments().getInt("TARGET_WEIGHT")));
//        mTvCurrentBMI.setText(Integer.toString(getArguments().getInt("BMI")));
//        mTvTargetCalorie.setText(Integer.toString(getArguments().getInt("TARGET_CALORIES")));

        mTvGoals.setText("Gaining Weight");
        mTvCurrentWeight.setText("50");
        mTvTargetWeight.setText("60");
        mTvCurrentBMI.setText("15.3");
        mTvTargetCalorie.setText("2500");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        // Get all string and int values associated with this fragment
//        String goal = mSpGoals.getSelectedItem().toString();
//        String activity = mSpActivity.getSelectedItem().toString();
        String goal = mTvGoals.getText().toString();
        String currentWeight = mTvCurrentWeight.getText().toString();
        String targetWeight = mTvTargetWeight.getText().toString();
        String currentBMI = mTvCurrentBMI.getText().toString();
        String targetCalorie = mTvTargetCalorie.getText().toString();


        // Store all string and int values
//        outState.putString("GOAL", goal);
//        outState.putString("ACTIVITY", activity);
        outState.putString("goal", goal);
        outState.putString("currentWeight", currentWeight);
        outState.putString("targetWeight", targetWeight);
        outState.putString("currentBMI", currentBMI);
        outState.putString("targetCalorie", targetCalorie);

        //Save the view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored (Bundle savedInstanceState){

        if(savedInstanceState != null){
            // Restore simple saved data
//            mSpGoals.setSelection(savedInstanceState.getInt("GOAL"));
//            mSpActivity.setSelection(savedInstanceState.getInt("ACTIVITY"));

            mTvGoals = getView().findViewById(R.id.tv_goal);
            mTvCurrentWeight = getView().findViewById(R.id.tv_currentWeight);
            mTvTargetWeight = getView().findViewById(R.id.tv_targetWeight);
            mTvCurrentBMI = getView().findViewById(R.id.tv_currentBMI);
            mTvTargetCalorie = getView().findViewById(R.id.tv_targetCalorie);

            mTvGoals.setText(savedInstanceState.getString("goal"));
            mTvCurrentWeight.setText(savedInstanceState.getString("currentWeight"));
            mTvTargetWeight.setText(savedInstanceState.getString("targetWeight"));
            mTvCurrentBMI.setText(savedInstanceState.getString("currentBMI"));
            mTvTargetCalorie.setText(savedInstanceState.getString("targetCalorie"));
        }

        super.onViewStateRestored(savedInstanceState);
    }

}
