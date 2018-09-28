package com.example.lifestyleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GoalsFragment extends Fragment {

    private TextView mTvGoals, mTvCurrentWeight, mTvTargetWeight, mTvCurrentBMI, mTvTargetCalorie;
    public GoalsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the detail view
        View view = inflater.inflate(R.layout.view_goals, container, false);

        // Get the Views
        mTvGoals = view.findViewById(R.id.tv_goal);
        mTvCurrentWeight = view.findViewById(R.id.tv_currentWeight);
        mTvTargetWeight = view.findViewById(R.id.tv_targetWeight);
        mTvCurrentBMI = view.findViewById(R.id.tv_currentBMI);
        mTvTargetCalorie = view.findViewById(R.id.tv_targetCalorie);

        // Get the incoming details
        String goal = getArguments().getString("GOAL");
        int currentWeight = getArguments().getInt("CURRENT_WEIGHT");
        int targetWeight = getArguments().getInt("TARGET_WEIGHT");
        int BMI = getArguments().getInt("BMI");
        int dailyCalorieTarget = getArguments().getInt("TARGET_CALORIES");

        if (MainActivity.debug) {
            assert(targetWeight == 60);
        }
        // If user has not yet defined a goal, we need filler data
        if(goal.equals("")){
            mTvGoals.setText("No Goal Set");
            mTvCurrentWeight.setText(Integer.toString(currentWeight));
            mTvTargetWeight.setText("");
            mTvCurrentBMI.setText(Integer.toString(BMI));
            mTvTargetCalorie.setText("");
        }
        else{ // Set the known details
            mTvGoals.setText(goal);
            mTvCurrentWeight.setText(Integer.toString(currentWeight));
            mTvTargetWeight.setText(Integer.toString(targetWeight));
            mTvCurrentBMI.setText(Integer.toString(BMI));
            mTvTargetCalorie.setText(Integer.toString(dailyCalorieTarget));
        }

        return view;
    }

}
