package com.example.lifestyleapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    // View objects
    private EditText m_etName;
    private Spinner m_spAge ;
    private Spinner m_spCountry;
    private Spinner m_spCity;
    private Spinner m_spHeight;
    private Spinner m_spWeight;
    private Button mSubmitButton;

    // Profile data
    private boolean mSex;
    private String mName;
    private int mAge;
    private String mCountry;
    private String mCity;
    private int mHeight; // In inches
    private int mWeight; // In pounds
    private Bitmap mPicture;


    /**
     * Required empty constructor
     */
    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Set up the button and register it for clicks
        mSubmitButton = (Button) view.findViewById(R.id.button_profile_edit_submit);
        mSubmitButton.setOnClickListener(this);

        // Assign the various views as members
        m_etName = (EditText) view.findViewById(R.id.et_name);
        m_spAge = (Spinner) view.findViewById(R.id.spinner_age);
        m_spCountry = (Spinner) view.findViewById(R.id.spinner_country);
        m_spCity = (Spinner) view.findViewById(R.id.spinner_city);
        m_spHeight = (Spinner) view.findViewById(R.id.spinner_height);
        m_spWeight = (Spinner) view.findViewById(R.id.spinner_weight);

        // FILL THE SPINNERS WITH DATA
        setSpinners();

        return view;
    }

    /**
     * Helper method that sets all of the data into the scrollable spinners for when a
     * user has to select items such as age, height, and weight.
     */
    private void setSpinners() {

        // Set age spinner
        ArrayList<Integer> ageValues = new ArrayList<>();
        for(int i = 12; i < 111; i++){
            ageValues.add(i);
        }
        ArrayAdapter<Integer> ageAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, ageValues);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spAge.setAdapter(ageAdapter);

        // Set Height spinner (
        ArrayList<String> heightValues = new ArrayList<>();
        for(int feet = 4; feet < 9; feet++){ // account for feet
            String height = feet + "\'";
            heightValues.add(height);
            for(int inches = 1; inches < 13; inches++){ // acount for inches
                heightValues.add(height + inches + "\"");
            }
        }
        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, heightValues);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spHeight.setAdapter(heightAdapter);

        // Set Weight spinner (
        ArrayList<Integer> weightValues = new ArrayList<>();
        for(int i = 50; i < 400; i++){ // account for feet
            weightValues.add(i);
        }
        ArrayAdapter<Integer> weightAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, weightValues);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spWeight.setAdapter(weightAdapter);

    }

    /**
     * Handles if the submit profile data button is clicked. Will save user data to a
     * file.
     *
     * @param view - View
     */
    @Override
    public void onClick(View view) {
        // Get the profile data out of the View objects
        mName = m_etName.getText().toString();
        mAge = Integer.parseInt(m_spAge.getSelectedItem().toString());
        mWeight = Integer.parseInt((m_spWeight.getSelectedItem().toString()));

        // Get height data;
        String heightAsString = m_spHeight.getSelectedItem().toString();
        // Replace all non numbers with a space
        heightAsString = heightAsString.replaceAll("[^-?0-9]+", " ");
        // Split all numbers into their own string
        String[] splitHeight = heightAsString.split("\'");
        mWeight = Integer.parseInt(splitHeight[0]) * 12; // feet*inches
        if(splitHeight[1] != null){
            mWeight += Integer.parseInt(splitHeight[1]);
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }



    // THE FOLLOWING NEEDS TO GO INTO THE MAIN ACTIVITY I THINK!!!


    /**
     * Handles when the male/female radio buttons are clicked
     *
     * @param view
     */
        /*
    public void onRadioButtonClicked(View view) {
        // Is one of the buttons now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            // User is male
            case R.id.radio_male:
                if (checked){
                    //mSex = true;
                    break;
                }

                // User is female
            case R.id.radio_female:
                if (checked){
                    //mSex = false;
                    break;
                }
        } // End switch case
    }

    */


}
