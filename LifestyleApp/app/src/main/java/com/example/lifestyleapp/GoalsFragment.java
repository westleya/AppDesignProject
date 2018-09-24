package com.example.lifestyleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GoalsFragment extends Fragment{

    private TextView mTvGoals, mTvCurrentWeight, mTvTargetWeight, mTvCurrentBMI, mTvTargetCalorie;

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

        // Get the incoming details
        mTvGoals.setText(getArguments().getString("GOAL"));
        mTvCurrentWeight.setText(Integer.toString(getArguments().getInt("CURRENT_WIGHT")));
        mTvTargetWeight.setText(Integer.toString(getArguments().getInt("TARGET_WEIGHT")));
        mTvCurrentBMI.setText(Integer.toString(getArguments().getInt("BMI")));
        mTvTargetCalorie.setText(Integer.toString(getArguments().getInt("TARGET_CALORIES")));

        return view;
    }
}
