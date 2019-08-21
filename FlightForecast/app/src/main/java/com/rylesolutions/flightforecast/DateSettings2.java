package com.rylesolutions.flightforecast;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.widget.DatePicker;

public class DateSettings2 implements DatePickerDialog.OnDateSetListener {
    Context context;

    public DateSettings2 (Context context) {
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setType("text/plain");
        intent.putExtra("landingDate", String.format("%d/%d/%d", (monthOfYear + 1), dayOfMonth,
                year));
        intent.putExtra("lYear", year);
        intent.putExtra("lMonth", (monthOfYear + 1));
        intent.putExtra("lDay", dayOfMonth);
        context.startActivity(intent);

    }
}