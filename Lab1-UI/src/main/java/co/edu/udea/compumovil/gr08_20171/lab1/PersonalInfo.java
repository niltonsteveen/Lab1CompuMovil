package co.edu.udea.compumovil.gr08_20171.lab1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PersonalInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button btnSiguiente1,btnMostrarFecha;
    EditText etNom,etApe;
    TextView etNacimiento;
    RadioButton rbSexo,rbSexoM,rbSexoF;
    Spinner spEduca;
    boolean entro = false;
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
                entro = true;
            }
        });
        rbSexoM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSexo= (RadioButton)findViewById(R.id.radioButtonMale);
                entro = true;
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
                if(validar()) {
                    Intent intent = new Intent(PersonalInfo.this, ContactInfo.class);
                    intent.putExtra("Info", getString(R.string.name) + ": " + etNom.getText().toString()
                            + "\n" + getString(R.string.lastName) + ": " + etApe.getText().toString()
                            + "\n" + getString(R.string.date) + ": " + etNacimiento.getText().toString()
                            + "\n" + getString(R.string.sex) + ": " + rbSexo.getText().toString()
                            + "\n" + getString(R.string.Educacion) + ": " + spEduca.getSelectedItem().toString());
                    startActivity(intent);
                }
            }
        });
        arraySpinner();
    }

    public boolean validar(){
        boolean validado = true;
        etNom.setError(null);
        etApe.setError(null);

        String nombre = etNom.getText().toString();
        String Apellido = etApe.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            etNom.setError(getString(R.string.error_campo_obligatorio));
            etNom.requestFocus();
            validado = false;
        }

        if(!checkAlpha(nombre)){
            etNom.setError(getString(R.string.error_campo_alfabetico));
            etNom.requestFocus();
            validado = false;
        }

        if(TextUtils.isEmpty(Apellido)){
            etApe.setError(getString(R.string.error_campo_obligatorio));
            etApe.requestFocus();
            validado = false;
        }

        if(!checkAlpha(Apellido)){
            etApe.setError(getString(R.string.error_campo_alfabetico));
            etApe.requestFocus();
            validado = false;
        }

        if(!entro){
            Toast.makeText(getApplicationContext(),"Debe seleccionar un sexo",Toast.LENGTH_LONG).show();
            validado = false;
        }
        return validado;
    }

    public static boolean checkAlpha(String str) {
        boolean respuesta = false;
        if ((str).matches("([a-z]|[A-Z]|\\s)+")) {
            respuesta = true;
        }
        return respuesta;
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
