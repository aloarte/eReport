package com.uc3m.p4r4d0x.emergapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.R;

import java.util.Calendar;

/**
 * Created by Alvaro Loarte Rodríguez on 13/08/16.
 *
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    View mView=null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        EditText etDate= (EditText)  getActivity().findViewById(R.id.etSIdate);
        etDate.setText(""+day+"/"+month+"/"+year);

    }
}
