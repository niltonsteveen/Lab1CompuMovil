package co.edu.udea.compumovil.gr08_20171.lab1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class PersonalInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button btnSiguiente1,btnMostrarFecha;
    EditText etNom,etApe;
    TextView etNacimiento;
    RadioButton rbSexo,rbSexoM,rbSexoF;
    Spinner spEduca;
    private int dia, mes, año;
    private static DatePickerDialog.OnDateSetListener selectorFecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        btnSiguiente1 = (Button)findViewById(R.id.btnSiguiente1);
        etNom = (EditText)findViewById(R.id.etNombre);
        etApe = (EditText)findViewById(R.id.etApellidos);
        etNacimiento = (TextView)findViewById(R.id.tvFechaNaci);
        rbSexoM = (RadioButton)findViewById(R.id.radioButtonMale);
        rbSexoF = (RadioButton)findViewById(R.id.radioButtonFemale);

        rbSexoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSexo= (RadioButton)findViewById(R.id.radioButtonFemale);
            }
        });
        rbSexoM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSexo= (RadioButton)findViewById(R.id.radioButtonMale);
            }
        });
        btnMostrarFecha =(Button)findViewById(R.id.btnFechaNacimiento);
        spEduca = (Spinner)findViewById(R.id.spEducacion);

        final Calendar c = Calendar.getInstance();
        año = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        mostrarFecha();
        btnSiguiente1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PersonalInfo.this,ContactInfo.class);

                intent.putExtra("Info",getString(R.string.name)+": "+etNom.getText().toString()
                        +"\n" + getString(R.string.lastName)+": "+etApe.getText().toString()
                        +"\n" + getString(R.string.date)+": "+etNacimiento.getText().toString()
                        +"\n" + getString(R.string.sex)+": "+rbSexo.getText().toString()
                        +"\n" + getString(R.string.Educacion)+": "+spEduca.getSelectedItem().toString());
                startActivity(intent);
            }
        });
        arraySpinner();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void actuFecha( int year, int month, int dayOfMonth) {
        año = year;
        mes = month;
        dia = dayOfMonth;
        mostrarFecha();
    }

    public void mostrarFecha(){
        etNacimiento.setText(dia +"/"+(mes +1)+"/"+ año);
    }
    public void arraySpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.spEducacion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.grados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        año = year;
        mes = month;
        dia = dayOfMonth;
        mostrarFecha();
    }
}
