package com.rylesolutions.flightforecast;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimeSettings implements TimePickerDialog.OnTimeSetListener {
    Context context;
    public TimeSettings(Context context) {
        this.context = context;
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        //Calendar calendar = Calendar.getInstance();
        //int cHour = calendar.get(Calendar.HOUR_OF_DAY);
        //int cMinute = calendar.get(Calendar.MINUTE);
        String uAmPm;
        int uDispHour;

        /*if(cHour > 11)
            cAmPm = "PM";
        else
            cAmPm = "AM";

        if(cHour % 12 == 0)
            cDispHour = 12;
        else
            cDispHour = cHour % 12;*/

        if(hourOfDay > 11)
            uAmPm = "PM";
        else
            uAmPm = "AM";

        if(hourOfDay % 12 == 0)
            uDispHour = 12;
        else
            uDispHour = hourOfDay % 12;

        /*if((cHour * 60 + cMinute) > (hourOfDay * 60 + minute))
            Toast.makeText(context,
                    String.format(context.getString(R.string.time_error_1) + " (%d:%02d%s)",
                            cDispHour, cMinute, cAmPm), Toast.LENGTH_LONG).show();*/

        Intent intent = new Intent(context, MainActivity.class);
        intent.setType("text/plain");
        intent.putExtra("takeoffTime", String.format("%d:%02d%s", uDispHour, minute, uAmPm));
        intent.putExtra("tHour", hourOfDay);
        intent.putExtra("tMinute", minute);
        Calendar c = Calendar.getInstance();
        intent.putExtra("tMillis", c.getTimeInMillis());
        context.startActivity(intent);



    }
}
