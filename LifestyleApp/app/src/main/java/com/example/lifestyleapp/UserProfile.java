package com.example.lifestyleapp;

import java.io.Serializable;

public class UserProfile implements Serializable{

    // User Stats Data
    private String Name;
    private int Age;
    private String City;
    private String Country;
    private int Height; // in inches
    private int Weight; // in lbs
    private boolean Gender; // M=true, F=false
    private double ActivityLevel; // Sedentary (1.53), Moderate (1.76), Active (2.25)

    // User Goal Related Data
    private String Goal; // Lose, maintain, or gain weight
    private int TargetWeight; // in lbs
    private double PoundsPerWeek; // In lbs per week

    // Constructor
    public UserProfile(String name, int age, int weight, int height, String activityLevel, boolean sex, String country,
                       String city){
        Name = name;
        Age = age;
        City = city;
        Country = country;
        Height = height;
        Weight = weight;
        Gender = sex;

        // Set these values later once the user has defined a health/fitness goal.
        // Will be initially set to negative values to denote that no gaol has been defined
        Goal = "";
        TargetWeight = -1;
        PoundsPerWeek = -1;

        setActivityLevel(activityLevel);
    }

    // Getters
    public double getActivityLevel() { return ActivityLevel; }
    public double getPoundsPerWeek() { return PoundsPerWeek; }
    public int getAge() { return Age; }
    public int getHeight() { return Height; }
    public int getWeight() { return Weight; }
    public int getTargetWeight(){
        return TargetWeight;
    }
    public String getCity() { return City; }
    public String getCountry() { return Country; }
    public String getName() { return Name; }
    public boolean getGender() { return Gender;}
    public String getGoal(){ return Goal; }

    // Setters
    public void setTargetWeight(int weight){ TargetWeight = weight; }
    public void setPoundsPerWeek(double poundsPerWeek) { PoundsPerWeek = poundsPerWeek; }
    public void setGoal(String goal) { Goal = goal; }
    public void setActivityLevel(double activityLevel) {
        ActivityLevel = activityLevel;
    }

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

}
