package com.rylesolutions.flightforecast;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;


public class DateSettings implements DatePickerDialog.OnDateSetListener {
    Context context;

    public DateSettings (Context context) {
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setType("text/plain");
        intent.putExtra("takeoffDate", String.format("%d/%d/%d", (monthOfYear + 1), dayOfMonth,
                year));
        intent.putExtra("tYear", year);
        intent.putExtra("tMonth", (monthOfYear + 1));
        intent.putExtra("tDay", dayOfMonth);
        context.startActivity(intent);

    }
}