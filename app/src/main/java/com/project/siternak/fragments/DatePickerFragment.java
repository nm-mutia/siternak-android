package com.project.siternak.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.siternak.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private int year;
    private int month;
    private int day;

    public DatePickerFragment(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), R.style.DialogTheme,(DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }
}
