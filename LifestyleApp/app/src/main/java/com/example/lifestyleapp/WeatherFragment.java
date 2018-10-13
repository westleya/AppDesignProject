package com.example.lifestyleapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.net.URL;

public class WeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private TextView mTvLocation, mTvWeather, mTvWind, mTvHumidity, mTvPressure;
    public static final String URL_STRING = "query";
    WeatherData mWeatherData;

    //Uniquely identify loader
    private static final int SEARCH_LOADER = 11;

    public WeatherFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the detail view
        View view = inflater.inflate(R.layout.view_weather, container, false);

        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER, null, this);

        //Get the text view
        mTvLocation = (TextView) view.findViewById(R.id.tv_location);
        mTvWeather = (TextView) view.findViewById(R.id.tv_weather);
        mTvWind = (TextView) view.findViewById(R.id.tv_wind);
        mTvHumidity = (TextView) view.findViewById(R.id.tv_humidity);
        mTvPressure = (TextView) view.findViewById(R.id.tv_press);

        mTvLocation.setText(getArguments().getString("location").replace('&', ' '));

        /**
         New for Part 2. Gets the same instance of the WeatherViewModel as was created in Main.
         */
        WeatherViewModel mWeatherViewModel = ViewModelProviders.of(getActivity()).get(WeatherViewModel.class);
        /**
         The rest of the WeatherViewModel class has to be implemented for this to actually work.
         Currently it's just a shell class.
         */
        mWeatherViewModel.getData().observe(this, weatherObserver);

        /**
         The old implementation that will be deleted once we get ViewModels working
         */
        LoadWeatherData(getArguments().getString("location"));

        return view;
    }

    /**
     https://developer.android.com/topic/libraries/architecture/viewmodel#sharing used the
     another format to observe, while the examples from class created this separate 'observer'
     method. They seem to be comparable to me, but I couldn't get the web's format to work.
     */
    // Create an observer that watches the LiveData<WeatherData> object
    final Observer<WeatherData> weatherObserver = new Observer<WeatherData>() {
        @Override
        public void onChanged(@Nullable WeatherData weatherData) {
            // Update the UI if the data variable changes
            if(mWeatherData != null) {
                // location, weather (temperature), wind, humidity, pressure;
                // Temperature is sent from openWeatherAPI in Kelvin. We're only reporting it
                // in Fahrenheit. Therefore, it needs to be converted.
                mTvWeather.setText(mWeatherData.getCurrentCondition().getDescription() + ", " +
                        Integer.toString((int) (9 * ((mWeatherData.getTemperature().getTemp() -
                                273) / 5) + 32)) + " \u00b0F");
                mTvWind.setText(Double.toString(mWeatherData.getWind().getSpeed()) + " m/s");
                mTvHumidity.setText(Double.toString(mWeatherData.getCurrentCondition().getHumidity()) + " %");
                mTvPressure.setText(Double.toString(mWeatherData.getCurrentCondition().getPressure()) + " hPa");
            }
        }
    };

    ////////GET WEATHER DATA/////////////

    public void LoadWeatherData(String location) {

        Bundle searchQueryBundle = new Bundle();
        searchQueryBundle.putString(URL_STRING, location);
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> searchLoader = loaderManager.getLoader(SEARCH_LOADER);

        if(searchLoader == null) {
            loaderManager.initLoader(SEARCH_LOADER, searchQueryBundle,this);
        }
        else {
            loaderManager.restartLoader(SEARCH_LOADER, searchQueryBundle,this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String>(getActivity().getBaseContext()) {
            private String mLoaderData;

            @Override
            protected void onStartLoading() {
                if(bundle == null) {
                    return;
                }
                if(mLoaderData != null) {
                    //Cache data for onPause instead of loading all over again
                    //Other config changes are handled automatically
                    deliverResult(mLoaderData);
                }
                else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String location = bundle.getString(URL_STRING);
                URL weatherDataURL = WeatherUtils.buildURLFromLocation(location);
                String jsonWeatherData = null;
                try {
                    jsonWeatherData = WeatherUtils.getDataFromURL(weatherDataURL);
                    return jsonWeatherData;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                mLoaderData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String jsonWeatherData) {
        if(jsonWeatherData != null) {

            try {
                mWeatherData = WeatherUtils.CreateWeatherData(jsonWeatherData);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            if(mWeatherData != null) {

                // location, weather (temperature), wind, humidity, pressure;
                // Temperature is sent from openWeatherAPI in Kelvin. We're only reporting it
                // in Fahrenheit. Therefore, it needs to be converted.
                mTvWeather.setText(mWeatherData.getCurrentCondition().getDescription() + ", " +
                        Integer.toString((int) (9 * ((mWeatherData.getTemperature().getTemp() -
                                273) / 5) + 32)) + " \u00b0F");
                mTvWind.setText(Double.toString(mWeatherData.getWind().getSpeed()) + " m/s");
                mTvHumidity.setText(Double.toString(mWeatherData.getCurrentCondition().getHumidity()) + " %");
                mTvPressure.setText(Double.toString(mWeatherData.getCurrentCondition().getPressure()) + " hPa");

            }

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
