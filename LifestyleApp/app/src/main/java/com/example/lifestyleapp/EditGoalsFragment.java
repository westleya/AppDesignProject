package com.example.lifestyleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditGoalsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
//    private Spinner mSpGoals;
//    private Spinner mSpActivity;
    private Spinner mSpTargetWeight;
    private Spinner mSpTargetYear;
    private Spinner mSpTargetMonth;
    private Spinner mTargetDay;
    private Button mButtonCalculate;
    private EditGoalsFragment.OnDataPass mDataPasser;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_calculate:{
//                if (!mSpTargetWeight.isSelected() ||
//                        !mSpTargetYear.isSelected() || !mSpTargetMonth.isSelected() || !mTargetDay.isSelected()) {
//                    Toast.makeText(this.getContext(), "Please select all items.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String goal = mSpGoals.getSelectedItem().toString();
//                String activity = mSpActivity.getSelectedItem().toString();
                int weight = Integer.parseInt(mSpTargetWeight.getSelectedItem().toString());
                int year = Integer.parseInt(mSpTargetYear.getSelectedItem().toString());
                int month = Integer.parseInt(mSpTargetMonth.getSelectedItem().toString());
                int day = Integer.parseInt(mTargetDay.getSelectedItem().toString());
                boolean leap = false;
                if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
                    leap = true;
                }
                int[] dates = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                dates[1] += leap ? 1 : 0;
                if (day > dates[month]) {
                    Toast.makeText(this.getContext(), "Please select correct date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String targetStr = day + "/" + month + "/" + year;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date targetDate = sdf.parse(targetStr);
                    Date c = Calendar.getInstance().getTime();
                    sdf.format(c);
                    long diff = targetDate.getTime() - c.getTime();
                    int currentCal = getArguments().getInt("CAL");
                    int currentWeight = getArguments().getInt("WEIGHT");
                    if (Math.abs((double) (currentWeight - weight) / diff * 7) > 2) {
                        Toast.makeText(this.getContext(), "Warning, you are planning to lose / gain more than 2 lbs per week, please select a different target", Toast.LENGTH_SHORT).show();
                    }
                    if (currentWeight > weight) {
                        double cal = currentCal - ((double) (currentWeight - weight) * 3500) / diff;

                        if (getArguments().getBoolean("MALE") && cal < 1200) {
                            Toast.makeText(this.getContext(), "Warning, your plan will result in unsafe low calorie intake, please select a different target", Toast.LENGTH_SHORT).show();
                        }
                        if (!getArguments().getBoolean("MALE") && cal < 1000) {
                            Toast.makeText(this.getContext(), "Warning, your plan will result in unsafe low calorie intake, please select a different target", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mDataPasser.passData(weight, year, month, day);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the detail view
        View view = inflater.inflate(R.layout.edit_goals, container, false);

        //Get the text view
//        mSpGoals = view.findViewById(R.id.spinner_goals);
//        mSpActivity = view.findViewById(R.id.spinner_activityLevel);
        mSpTargetWeight =  view.findViewById(R.id.spinner_targetWeight);
        mSpTargetYear =  view.findViewById(R.id.spinner_targetYear);
        mSpTargetMonth =  view.findViewById(R.id.spinner_targetMonth);
        mTargetDay =  view.findViewById(R.id.spinner_targetDay);

        mButtonCalculate = view.findViewById(R.id.button_calculate);
        mButtonCalculate.setOnClickListener(this);

//        String[] goals = new String[] {"Gaining weight", "Losing weight"};
//        ArrayAdapter<String> goalsAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, goals);
//        goalsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpGoals.setAdapter(goalsAdapter);

//        String[] activity = new String[] {"Active", "Sedentary", "Moderate"};
//        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, activity);
//        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpActivity.setAdapter(activityAdapter);

        String[] weight = new String[351];
        for (int i = 0; i < 351; ++i) {
            weight[i] = Integer.toString(i + 50);
        }
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, weight);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTargetWeight.setAdapter(weightAdapter);

        String[] year = new String[10];
        for (int i = 0; i < 10; ++i) {
            year[i] = Integer.toString(i + 2018);
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, year);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTargetYear.setAdapter(yearAdapter);

        String[] month = new String[12];
        for (int i = 0; i < 12; ++i) {
            month[i] = Integer.toString(i + 1);
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, month);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTargetMonth.setAdapter(monthAdapter);

        String[] day = new String[31];
        for (int i = 0; i < 31; ++i) {
            day[i] = Integer.toString(i + 1);
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, day);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTargetDay.setAdapter(dayAdapter);
//        mSpGoals.setSelection(getIndex(mSpGoals, getArguments().getString("goals")));
//        mSpActivity.setSelection(getIndex(mSpActivity, getArguments().getString("goals")));
//        mSpTargetWeight.setSelection(getIndex(mSpTargetWeight, getArguments().getString("goals")));
//        mSpTargetYear.setSelection(getIndex(mSpTargetYear, getArguments().getString("goals")));
//        mSpTargetMonth.setSelection(getIndex(mSpTargetMonth, getArguments().getString("goals")));
//        mTargetDay.setSelection(getIndex(mTargetDay, getArguments().getString("goals")));

        return view;
    }

    public interface OnDataPass{
        void passData(int weight, int year, int month, int day);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mDataPasser = (EditGoalsFragment.OnDataPass) context;
        }
        catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        // Get all string and int values associated with this fragment
//        String goal = mSpGoals.getSelectedItem().toString();
//        String activity = mSpActivity.getSelectedItem().toString();
        int weight = Integer.parseInt(mSpTargetWeight.getSelectedItem().toString());
        int year = Integer.parseInt(mSpTargetYear.getSelectedItem().toString());
        int month = Integer.parseInt(mSpTargetMonth.getSelectedItem().toString());
        int day = Integer.parseInt(mTargetDay.getSelectedItem().toString());

        // Store all string and int values
//        outState.putString("GOAL", goal);
//        outState.putString("ACTIVITY", activity);
        outState.putInt("WEIGHT", weight);
        outState.putInt("YEAR", year);
        outState.putInt("MONTH", month);
        outState.putInt("DAY", day);

        //Save the view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored (Bundle savedInstanceState){

        if(savedInstanceState != null){
            // Restore simple saved data
//            mSpGoals.setSelection(savedInstanceState.getInt("GOAL"));
//            mSpActivity.setSelection(savedInstanceState.getInt("ACTIVITY"));
            mSpTargetWeight.setSelection(savedInstanceState.getInt("WEIGHT"));
            mSpTargetYear.setSelection(savedInstanceState.getInt("YEAR"));
            mSpTargetMonth.setSelection((savedInstanceState.getInt("MONTH")));
            mTargetDay.setSelection((savedInstanceState.getInt("DAY")));
        }

        super.onViewStateRestored(savedInstanceState);
    }
}
