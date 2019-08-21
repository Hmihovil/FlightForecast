package com.rylesolutions.flightforecast;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimeSettings2 implements TimePickerDialog.OnTimeSetListener {
    Context context;
    public TimeSettings2(Context context) {
        this.context = context;
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String uAmPm;
        int uDispHour;

        if(hourOfDay > 11)
            uAmPm = "PM";
        else
            uAmPm = "AM";

        if(hourOfDay % 12 == 0)
            uDispHour = 12;
        else
            uDispHour = hourOfDay % 12;

        Intent intent = new Intent(context, MainActivity.class);
        intent.setType("text/plain");
        intent.putExtra("landingTime", String.format("%d:%02d%s", uDispHour, minute, uAmPm));
        intent.putExtra("lHour", hourOfDay);
        intent.putExtra("lMinute", minute);
        Calendar c = Calendar.getInstance();
        intent.putExtra("lMillis", c.getTimeInMillis());
        context.startActivity(intent);
    }
}
