package com.example.lifestyleapp;

import android.graphics.Bitmap;

public class UserProfile {

    private String Name;
    private int Age;
    private String City;
    private String Country;
    private int Height; // in inches
    private int Weight; // in lbs
    private int GoalWeight; // in lbs
    private boolean Gender; // M=true, F=false
    private Bitmap ProfilePicture;
    private double poundsPerWeek; // In lbs per week
    private double ActivityLevel; // Sedentary (1.53), Moderate (1.76), Active (2.25)
    // Constructor
    public UserProfile(String name, int age, int weight, int height, String activityLevel, boolean sex, String country,
                       String city, Bitmap profilePic){
        Name = name;
        Age = age;
        City = city;
        Country = country;
        Height = height;
        Weight = weight;
        Gender = sex;
        ProfilePicture = profilePic;
        setActivityLevel(activityLevel);
    }

    // Getters
    public Bitmap getProfilePicture() { return ProfilePicture; }
    public double getActivityLevel() { return ActivityLevel; }
    public double getPoundsPerWeek() { return poundsPerWeek; }
    public int getAge() { return Age; }
    public int getHeight() { return Height; }
    public int getWeight() { return Weight; }
    public int getGoalWeight(){
        return GoalWeight;
    }
    public String getCity() { return City; }
    public String getCountry() { return Country; }
    public String getName() { return Name; }
    public boolean getGender() { return Gender;}

    // Activity level isn't as simple as the other setters.
    // Since its categories are descriptive and not numerical
    // it can't be directly set from the user's input.
    public void setActivityLevel(String activityLevel) {
        if(activityLevel == "Sedentary") {
            ActivityLevel = 1.53;
        }
        else if (activityLevel == "Moderate") {
            ActivityLevel = 1.76;
        }
        else if (activityLevel == "Active") {
            ActivityLevel = 2.25;
        }
    }
    // This version of the setter can be called if the profile is loaded from file.
    public void setActivityLevel(double activityLevel) {
        ActivityLevel = activityLevel;
    }

}
