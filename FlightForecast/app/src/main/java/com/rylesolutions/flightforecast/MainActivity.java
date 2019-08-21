package com.rylesolutions.flightforecast;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {

    private static String takeoffDate, takeoffTime, landingDate, landingTime, tCode, tState, tCity,
            lCode, lState, lCity;
    private static Spinner tStateSpinner, tCitySpinner, lStateSpinner, lCitySpinner;
    private static int tYear, tMonth, tDay, tHour, tMinute, lYear, lMonth, lDay, lHour, lMinute,
            tStatePosition, tCityPosition, lStatePosition, lCityPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();



        if(intent.getStringExtra("takeoffDate") != null) {
            takeoffDate = intent.getStringExtra("takeoffDate");
            tYear = intent.getIntExtra("tYear", 0);
            tMonth = intent.getIntExtra("tMonth", 0);
            tDay = intent.getIntExtra("tDay", 0);
            TextView messageView = (TextView) findViewById(R.id.takeoff_date_button);
            messageView.setText(takeoffDate);
        }

        if(takeoffDate != null) {
            TextView messageView = (TextView) findViewById(R.id.takeoff_date_button);
            messageView.setText(takeoffDate);
        }

        if(intent.getStringExtra("landingDate") != null) {
            landingDate = intent.getStringExtra("landingDate");
            lYear = intent.getIntExtra("lYear", 0);
            lMonth = intent.getIntExtra("lMonth", 0);
            lDay = intent.getIntExtra("lDay", 0);
            TextView messageView = (TextView) findViewById(R.id.landing_date_button);
            messageView.setText(landingDate);
        }

        if(landingDate != null) {
            TextView messageView = (TextView) findViewById(R.id.landing_date_button);
            messageView.setText(landingDate);
        }

        if(intent.getStringExtra("takeoffTime") != null) {
            takeoffTime = intent.getStringExtra("takeoffTime");
            tHour = intent.getIntExtra("tHour", 0);
            tMinute = intent.getIntExtra("tMinute", 0);
            TextView messageView = (TextView) findViewById(R.id.takeoff_time_button);
            messageView.setText(takeoffTime);
        }

        if(takeoffTime != null) {
            TextView messageView = (TextView) findViewById(R.id.takeoff_time_button);
            messageView.setText(takeoffTime);
        }

        if(intent.getStringExtra("landingTime") != null) {
            landingTime = intent.getStringExtra("landingTime");
            lHour = intent.getIntExtra("lHour", 0);
            lMinute = intent.getIntExtra("lMinute", 0);
            TextView messageView = (TextView) findViewById(R.id.landing_time_button);
            messageView.setText(landingTime);
        }

        if(landingTime != null) {
            TextView messageView = (TextView) findViewById(R.id.landing_time_button);
            messageView.setText(landingTime);
        }

        // Spinner element
        tStateSpinner = (Spinner) findViewById(R.id.t_state_spinner);
        tCitySpinner = (Spinner) findViewById(R.id.t_city_spinner);
        lStateSpinner = (Spinner) findViewById(R.id.l_state_spinner);
        lCitySpinner = (Spinner) findViewById(R.id.l_city_spinner);

        // Spinner click listener
        tStateSpinner.setOnItemSelectedListener(this);
        tCitySpinner.setOnItemSelectedListener(this);
        lStateSpinner.setOnItemSelectedListener(this);
        lCitySpinner.setOnItemSelectedListener(this);

        try{
            SQLiteOpenHelper airportsDBHelper = new DatabaseHandler(this);
            SQLiteDatabase db = airportsDBHelper.getReadableDatabase();
            loadSpinnerData(db);

        }catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_LONG);
            toast.show();

        }
    }



    public void SetTakeoffDate(View v) {

        DatePickerHandler datePickerHandler = new DatePickerHandler();
        datePickerHandler.show(getSupportFragmentManager(), "date_picker");

    }

    public void SetLandingDate(View v) {

        DatePickerHandler2 datePickerHandler = new DatePickerHandler2();
        datePickerHandler.show(getSupportFragmentManager(), "date_picker");

    }

    public void SetTakeoffTime(View v) {
        TimePickerHandler timePickerHandler = new TimePickerHandler();
        timePickerHandler.show(getSupportFragmentManager(), "time_picker");
    }

    public void SetLandingTime(View v) {
        TimePickerHandler2 timePickerHandler = new TimePickerHandler2();
        timePickerHandler.show(getSupportFragmentManager(), "time_picker");
    }

    private void loadSpinnerData(SQLiteDatabase db) {

        Cursor cursor = db.query("STATES", new String[]{"STATE"}, null,
                null, null, null, null);

        List<String> states = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                states.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, states);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        tStateSpinner.setAdapter(dataAdapter);
        lStateSpinner.setAdapter(dataAdapter);

        tStateSpinner.setSelection(tStatePosition);
        lStateSpinner.setSelection(lStatePosition);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("takeoffDate", takeoffDate); // Do I need this?
        savedInstanceState.putString("landingDate", landingDate); //    or this?
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SQLiteOpenHelper airportsDBHelper = new DatabaseHandler(this);
        SQLiteDatabase db = airportsDBHelper.getReadableDatabase();

        switch (parent.getId()) {
            case R.id.t_state_spinner:

                tState = parent.getItemAtPosition(position).toString();

                Cursor tSCursor = db.query("CITIES", new String[]{"STATE", "CITY", "CODE"},
                        "STATE = ?", new String[] {tState}, null, null, null);
                List<String> tCities = new ArrayList<>();
                if (tSCursor.moveToFirst()) {
                    do {
                        tCities.add(tSCursor.getString(1));
                    } while (tSCursor.moveToNext());
                }
                tSCursor.close();

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, tCities);

                // Drop down layout style - list view with radio button
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                tCitySpinner.setAdapter(dataAdapter);

                if(position == tStatePosition)
                    tCitySpinner.setSelection(tCityPosition);
                else
                    tCitySpinner.setSelection(0);
                tStatePosition = position;
                tSCursor.close();
                break;

            case R.id.t_city_spinner:
                tCityPosition = position;

                tCity = parent.getItemAtPosition(position).toString();

                Cursor tCCursor = db.query("CITIES", new String[]{"CODE"}, "CITY = ?",
                        new String[]{tCity}, null, null, null);
                tCCursor.moveToFirst();
                tCode = tCCursor.getString(0);
                tCCursor.close();
                break;

            case R.id.l_state_spinner:
                lState = parent.getItemAtPosition(position).toString();

                Cursor lSCursor = db.query("CITIES", new String[]{"STATE", "CITY"}, "STATE = ?",
                        new String[] {lState}, null, null, null);
                List<String> lCities = new ArrayList<>();
                if (lSCursor.moveToFirst()) {
                    do {
                        lCities.add(lSCursor.getString(1));
                    } while (lSCursor.moveToNext());
                }
                lSCursor.close();

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, lCities);

                // Drop down layout style - list view with radio button
                dataAdapter2
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                lCitySpinner.setAdapter(dataAdapter2);

                if(position == lStatePosition)
                    lCitySpinner.setSelection(lCityPosition);
                else
                    lCitySpinner.setSelection(0);

                lStatePosition = position;
                lSCursor.close();
                break;

            case R.id.l_city_spinner:
                lCityPosition = position;

                lCity = parent.getItemAtPosition(position).toString();

                Cursor lCCursor = db.query("CITIES", new String[]{"CODE"}, "CITY = ?",
                        new String[]{lCity}, null, null, null);
                lCCursor.moveToFirst();
                lCode = lCCursor.getString(0);
                lCCursor.close();
                break;
        }

        db.close();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void doSearch(View view) {
        int tHour, lHour;

        if(tMinute > 29 && MainActivity.tHour == 23) {
            tHour = 0;
            switch(tMonth) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                    if(tDay == 31) {
                        tDay = 1;
                        tMonth++;
                    } else
                        tDay++;
                    break;
                case 12:
                    if(tDay == 31) {
                        tDay = 1;
                        tMonth = 1;
                        tYear++;
                    } else
                        tDay++;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if(tDay == 30) {
                        tDay = 1;
                        tMonth++;
                    } else
                        tDay++;
                    break;
                case 2:
                    if(tDay == 29) {
                        tDay = 1;
                        tMonth++;
                    } else if(tDay == 28 && (tYear % 4) != 0) {
                        tDay = 1;
                        tMonth++;
                    }
                    else
                        tDay++;
                    break;
            }
        } else if(tMinute > 29)
            tHour = MainActivity.tHour + 1;
        else
            tHour = MainActivity.tHour;

        if(lMinute > 29 && MainActivity.lHour == 23) {
            lHour = 0;
            switch(lMonth) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                    if(lDay == 31) {
                        lDay = 1;
                        lMonth++;
                    } else
                        lDay++;
                    break;
                case 12:
                    if(lDay == 31) {
                        lDay = 1;
                        lMonth = 1;
                        lYear++;
                    } else
                        lDay++;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if(lDay == 30) {
                        lDay = 1;
                        lMonth++;
                    } else
                        lDay++;
                    break;
                case 2:
                    if(lDay == 29) {
                        lDay = 1;
                        lMonth++;
                    } else if(lDay == 28 && (lYear % 4) != 0) {
                        lDay = 1;
                        lMonth++;
                    }
                    else
                        lDay++;
                    break;
            }
        } else if(lMinute > 29)
            lHour = MainActivity.lHour + 1;
        else
            lHour = MainActivity.lHour;

        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH) + 1;
        int cDay = c.get(Calendar.DAY_OF_MONTH);
        int cHour = c.get(Calendar.HOUR_OF_DAY);

        long cHours = (long)((cYear * 8802.5) + ((cMonth - 1) * 730.5) + (cDay * 24) + cHour);
        long tHours = (long)((tYear * 8802.5) + ((tMonth - 1) * 730.5) + (tDay * 24) + tHour);
        long lHours = (long)((lYear * 8802.5) + ((lMonth - 1) * 730.5) + (lDay * 24) + lHour);

        if((cHours - 60) < tHours && (cHours - 60) < lHours && (cHours + 60) > tHours &&
                (cHours + 60) > lHours) {
            Intent intent = new Intent(MainActivity.this, SearchResults.class);
            intent.putExtra(SearchResults.EXTRA_LOCATIONS, new String[]{tCode, tState, tCity,
                    lCode, lState, lCity});
            intent.putExtra(SearchResults.EXTRA_TIMES, new int[]{tMonth, tDay, tHour, lMonth, lDay,
                    lHour});

            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Invalid date(s) or time(s).\n" +
                    "Dates and times may be up to 36 hours in the future.", Toast.LENGTH_LONG);
            toast.show();
        }
    }



}
