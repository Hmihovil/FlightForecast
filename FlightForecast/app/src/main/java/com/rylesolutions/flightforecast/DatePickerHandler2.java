package com.rylesolutions.flightforecast;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;

import java.util.Calendar;


public class DatePickerHandler2 extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog;
        DateSettings2 dateSettings = new DateSettings2(getActivity());
        dialog = new DatePickerDialog(getActivity(), 0, dateSettings, year, month, day);
        return dialog;

    }
}
