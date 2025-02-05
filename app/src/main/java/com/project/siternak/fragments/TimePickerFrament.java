package com.project.siternak.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.siternak.R;

import java.util.Calendar;

public class TimePickerFrament extends DialogFragment {

    private int hour;
    private int minute;

    public TimePickerFrament(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(
                getActivity(),
                R.style.DialogTheme,
                (TimePickerDialog.OnTimeSetListener) getActivity(),
                hour, minute,
                DateFormat.is24HourFormat(getActivity())
        );
    }
}
