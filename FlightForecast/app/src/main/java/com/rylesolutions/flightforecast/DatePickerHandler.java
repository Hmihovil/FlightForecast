package com.rylesolutions.flightforecast;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;

import java.util.Calendar;


public class DatePickerHandler extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog;
        DateSettings dateSettings = new DateSettings(getActivity());
        dialog = new DatePickerDialog(getActivity(), 0, dateSettings, year, month, day);
        return dialog;

    }
}
