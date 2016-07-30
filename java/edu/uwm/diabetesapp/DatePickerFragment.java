package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener  {

    private int picker;

    private OnDatePickedListener mCallback;

    public interface OnDatePickedListener {
        public void onDatePicked(int picker, int year, int month, int dayOfMonth);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mCallback = (OnDatePickedListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDatePickedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallback = (OnDatePickedListener)getActivity();

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            picker = bundle.getInt("DATE", 1); //if there are multiple pickers, track it so we can return to teh right button upon DateSet
        }
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.mCallback.onDatePicked(picker,year,month,dayOfMonth); //months are indexed at 0
    }

}
