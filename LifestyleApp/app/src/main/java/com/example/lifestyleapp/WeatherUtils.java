package com.example.lifestyleapp;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WeatherUtils {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String APPIDQUERY = "&APPID=";
    private static final String app_id = "98be1485c455ef847338d54d176fa78b";

    // Parses JSON data from the string, data, provided
    public static WeatherData CreateWeatherData(String data) throws JSONException {
        WeatherData weatherData = new WeatherData();


        return weatherData;
    }

    public static URL buildURLFromProfile(UserProfile profile) {

        // Sanitize the location data
        String country = profile.getCountry().replace(' ', '&');
        String city = profile.getCity().replace(' ', '&');

        URL myURL = null;
        try{
            myURL = new URL(BASE_URL + city + "," + country + APPIDQUERY + app_id);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return myURL;
    }


    public static String getDataFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream inputStream = urlConnection.getInputStream();

            //Search for the next 'beginning of the input stream
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else{
                return null;
            }
        }finally {

        }
    }
}
