package com.rylesolutions.flightforecast;

import android.app.Activity;


import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

public class SearchResults extends Activity {

    private String tCode, tCondition, tState, tCity, lCode, lState, lCity, lCondition;
    private int tMonth, tDay, tHour, lMonth, lDay, lHour, tFCTCode, tTemperature, lFCTCode,
            lTemperature;
    private int dCount = -1;
    public static final String EXTRA_LOCATIONS = "searchLocations";
    public static final String EXTRA_TIMES = "searchTimes";
    public final String WUNDERGROUND_URL = "http://api.wunderground.com/api/%s/hourly/q/%s.json";
    public final String WUNDERGROUND_KEY = "8283ea97da239fb7";
    private TextView tLabelView, tTemperatureView, tConditionView, lLabelView,
            lTemperatureView, lConditionView;
    private ImageView dGaugeView, tWeatherIconView, lWeatherIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        String[] searchLocations = (String[]) getIntent().getExtras().get(EXTRA_LOCATIONS);
        int[] searchTimes = (int[]) getIntent().getExtras().get(EXTRA_TIMES);

        dGaugeView = (ImageView)findViewById(R.id.dGauge);
        tLabelView = (TextView)findViewById(R.id.tLabel);
        tWeatherIconView = (ImageView)findViewById(R.id.tWeatherIcon);
        tTemperatureView = (TextView)findViewById(R.id.tTemperature);
        tConditionView = (TextView)findViewById(R.id.tCondition);
        lLabelView = (TextView)findViewById(R.id.lLabel);
        lWeatherIconView = (ImageView)findViewById(R.id.lWeatherIcon);
        lTemperatureView = (TextView)findViewById(R.id.lTemperature);
        lConditionView = (TextView)findViewById(R.id.lCondition);

        tCode = searchLocations[0];
        tState = searchLocations[1];
        tCity = searchLocations[2];
        lCode = searchLocations[3];
        lState = searchLocations[4];
        lCity = searchLocations[5];
        tMonth = searchTimes[0];
        tDay = searchTimes[1];
        tHour = searchTimes[2];
        lMonth = searchTimes[3];
        lDay = searchTimes[4];
        lHour = searchTimes[5];

        getWeather();
    }

    public void getWeather() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                String webQuery = String.format(WUNDERGROUND_URL, WUNDERGROUND_KEY, tCode);

                try {
                    URL url = new URL(webQuery);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null)
                        result.append(line);

                    return result.toString();

                } catch (Exception e) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try {
                    JSONObject data = new JSONObject(s);
                    JSONArray queryResults = data.optJSONArray("hourly_forecast");

                    int i = 0;

                    while(i < 36 && (queryResults.optJSONObject(i).optJSONObject("FCTTIME").
                            optInt("mday") != tDay || queryResults.optJSONObject(i).
                            optJSONObject("FCTTIME").optInt("hour") != tHour))
                        i++;
                    if(i == 36) {
                        tLabelView.setText("Takeoff:\n" + tCity + ", " + tState);
                        int resourceID = getResources().getIdentifier("drawable/icon_" + 0,
                                null, getPackageName());
                        @SuppressWarnings("deprecation")
                        Drawable weatherIconDrawable = getResources().getDrawable(resourceID);
                        tWeatherIconView.setImageDrawable(weatherIconDrawable);
                        tConditionView.setText("Error.\nTime may be up to\n36 hours in the " +
                                "future.");

                    } else {
                        tFCTCode = queryResults.optJSONObject(i).optInt("fctcode");
                        tTemperature = queryResults.optJSONObject(i).optJSONObject("temp").
                                optInt("english");
                        tCondition = queryResults.optJSONObject(i).optString("condition");
                        int resourceID;
                        switch(tMonth) {
                            case 12:
                                if(tHour >= 7 && tHour <= 17)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            tFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (tFCTCode + 40), null, getPackageName());
                                break;
                            case 1:
                            case 11:
                                if(tHour >= 6 && tHour <= 18)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            tFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (tFCTCode + 40), null, getPackageName());
                                break;
                            case 2:
                            case 3:
                            case 4:
                            case 8:
                            case 9:
                            case 10:
                                if(tHour >= 6 && tHour <= 19)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            tFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (tFCTCode + 40), null, getPackageName());
                                break;
                            case 5:
                            case 7:
                                if(tHour >= 6 && tHour <= 20)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            tFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (tFCTCode + 40), null, getPackageName());
                                break;
                            case 6:
                                if(tHour >= 5 && tHour <= 21)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            tFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (tFCTCode + 40), null, getPackageName());
                                break;
                            default:
                                resourceID = getResources().getIdentifier("drawable/icon_" + 0,
                                        null, getPackageName());
                                break;

                        }

                        @SuppressWarnings("deprecation")
                        Drawable weatherIconDrawable = getResources().getDrawable(resourceID);

                        tLabelView.setText("Takeoff:\n" + tCity + ", " + tState);

                        tWeatherIconView.setImageDrawable(weatherIconDrawable);

                        tTemperatureView.setText(tTemperature + "°F");

                        tConditionView.setText(tCondition);

                        if(tFCTCode == 9 ||
                                tFCTCode == 14 ||
                                tFCTCode == 21 ||
                                tFCTCode == 22) {
                            dCount +=3;
                        }else if(tFCTCode == 15 ||
                                tFCTCode == 23 ||
                                tFCTCode == 24) {
                            dCount += 7;
                        }else
                            dCount++;
                    }


                } catch (JSONException e) {
                    tLabelView.setText("Takeoff:\n" + tCity + ", " + tState);
                    int resourceID = getResources().getIdentifier("drawable/icon_" + 0,
                            null, getPackageName());
                    @SuppressWarnings("deprecation")
                    Drawable weatherIconDrawable = getResources().getDrawable(resourceID);
                    tWeatherIconView.setImageDrawable(weatherIconDrawable);
                    tConditionView.setText("Connection Error.");
                }
            }
        }.execute();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                String webQuery = String.format(WUNDERGROUND_URL, WUNDERGROUND_KEY, lCode);

                try {
                    URL url = new URL(webQuery);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null)
                        result.append(line);

                    return result.toString();

                } catch (Exception e) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try {
                    JSONObject data = new JSONObject(s);
                    JSONArray queryResults = data.optJSONArray("hourly_forecast");

                    int i = 0;

                    while(i < 36 && (queryResults.optJSONObject(i).optJSONObject("FCTTIME").
                            optInt("mday") != lDay || queryResults.optJSONObject(i).
                            optJSONObject("FCTTIME").optInt("hour") != lHour))
                        i++;
                    if(i == 36) {
                        lLabelView.setText("Landing:\n" + lCity + ", " + lState);
                        int resourceID = getResources().getIdentifier("drawable/icon_" + 0,
                                null, getPackageName());
                        @SuppressWarnings("deprecation")
                        Drawable weatherIconDrawable = getResources().getDrawable(resourceID);
                        lWeatherIconView.setImageDrawable(weatherIconDrawable);
                        lConditionView.setText("Error.\nTime may be up to\n36 hours in the " +
                                "future.");
                    }else {
                        lFCTCode = queryResults.optJSONObject(i).optInt("fctcode");
                        lTemperature = queryResults.optJSONObject(i).optJSONObject("temp").
                                optInt("english");
                        lCondition = queryResults.optJSONObject(i).optString("condition");
                        int resourceID;
                        switch(lMonth) {
                            case 12:
                                if(lHour >= 7 && lHour <= 17)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            lFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (lFCTCode + 40), null, getPackageName());
                                break;
                            case 1:
                            case 11:
                                if(lHour >= 6 && lHour <= 18)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            lFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (lFCTCode + 40), null, getPackageName());
                                break;
                            case 2:
                            case 3:
                            case 4:
                            case 8:
                            case 9:
                            case 10:
                                if(lHour >= 6 && lHour <= 19)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            lFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (lFCTCode + 40), null, getPackageName());
                                break;
                            case 5:
                            case 7:
                                if(lHour >= 6 && lHour <= 20)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            lFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (lFCTCode + 40), null, getPackageName());
                                break;
                            case 6:
                                if(lHour >= 5 && lHour <= 21)
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            lFCTCode, null, getPackageName());
                                else
                                    resourceID = getResources().getIdentifier("drawable/icon_" +
                                            (lFCTCode + 40), null, getPackageName());
                                break;
                            default:
                                resourceID = getResources().getIdentifier("drawable/icon_" + 0,
                                        null, getPackageName());
                                break;

                        }

                        @SuppressWarnings("deprecation")
                        Drawable weatherIconDrawable = getResources().getDrawable(resourceID);

                        lLabelView.setText("Landing:\n" + lCity + ", " + lState);
                        lWeatherIconView.setImageDrawable(weatherIconDrawable);

                        lTemperatureView.setText(lTemperature + "°F");

                        lConditionView.setText(lCondition);


                        if(lFCTCode == 9 ||
                                lFCTCode == 14 ||
                                lFCTCode == 21 ||
                                lFCTCode == 22) {
                            dCount +=3;
                        }else if(lFCTCode == 15 ||
                                lFCTCode == 23 ||
                                lFCTCode == 24) {
                            dCount += 7;
                        }else
                            dCount++;
                    }
                } catch (JSONException e) {
                    lLabelView.setText("Landing:\n" + lCity + ", " + lState);
                    int resourceID = getResources().getIdentifier("drawable/icon_" + 0,
                            null, getPackageName());
                    @SuppressWarnings("deprecation")
                    Drawable weatherIconDrawable = getResources().getDrawable(resourceID);
                    lWeatherIconView.setImageDrawable(weatherIconDrawable);
                    lConditionView.setText("Connection Error.");
                }
            }
        }.execute();

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                int resourceID;
                if (dCount < 0)
                    resourceID = getResources().getIdentifier("drawable/gauge_" + 0, null,
                            getPackageName());
                else if (dCount < 2)
                    resourceID = getResources().getIdentifier("drawable/gauge_" + 1, null,
                            getPackageName());
                else if (dCount < 6)
                    resourceID = getResources().getIdentifier("drawable/gauge_" + 2, null,
                            getPackageName());
                else
                    resourceID = getResources().getIdentifier("drawable/gauge_" + 3, null,
                            getPackageName());

                @SuppressWarnings("deprecation")
                Drawable weatherIconDrawable = getResources().getDrawable(resourceID);
                dGaugeView.setImageDrawable(weatherIconDrawable);

                handler.postDelayed(this, 1000);
            }
        });
    }
}
