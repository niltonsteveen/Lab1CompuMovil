package co.edu.udea.compumovil.gr08_20171.lab2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by nilto on 18/02/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int year;
    int month;
    int day;
    DatePickerDialog dpd;
    final Calendar c = Calendar.getInstance();

    TextView etNacimiento;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        dpd=new DatePickerDialog(getActivity(), this, year, month, day);
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((DatePickerDialog.OnDateSetListener)getActivity()).onDateSet(view,year,month,day);
    }


}