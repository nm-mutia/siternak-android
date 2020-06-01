package com.project.siternak.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.project.siternak.R;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;


public class DateRangePickerFragment extends DialogFragment {
    private DateFormat mDateFormatter, mTimeFormatter;
    private SublimePicker mSublimePicker;
    private Callback mCallback;

    public DateRangePickerFragment(){
        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        mTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        mTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSublimePicker = (SublimePicker) getActivity().getLayoutInflater().inflate(R.layout.sublime_picker, container);

        Bundle arguments = getArguments();
        SublimeOptions options = null;

        if(arguments != null){
            options = arguments.getParcelable("SUBLIME_OPTIONS");
        }

        mSublimePicker.initializePicker(options, mListener);
        return mSublimePicker;
    }

    private SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimePicker, SelectedDate selectedDate, int i, int i1, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String s) {
            if(mCallback != null){
                mCallback.onDateTimeRecurrenceSet(selectedDate, i, i1, recurrenceOption, s);
            }
            dismiss();
        }

        @Override
        public void onCancelled() {
            if(mCallback != null){
                mCallback.onCancelled();
            }
            dismiss();
        }
    };

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public interface Callback{
        void onCancelled();
        void onDateTimeRecurrenceSet(SelectedDate selectedDate, int i, int i1, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String s);
    }

}
