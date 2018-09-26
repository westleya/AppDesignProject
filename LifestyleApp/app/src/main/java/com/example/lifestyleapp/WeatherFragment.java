package com.example.lifestyleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

        LoadWeatherData(getArguments().getString("location"));

        return view;
    }


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
                mTvWeather.setText(mWeatherData.getCurrentCondition().getDescription() + ", " +
                        Integer.toString((int) (9 * ((mWeatherData.getTemperature().getTemp() -
                                273) / 5) + 32)) + " F");
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
