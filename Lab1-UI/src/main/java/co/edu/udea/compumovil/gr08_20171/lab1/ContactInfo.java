package co.edu.udea.compumovil.gr08_20171.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactInfo extends AppCompatActivity {

    Button btnSiguiente;
    EditText etTelefono,etCorreo_Elect,etPais,etCiudad,etDireccion;

    public static String[] paises_lati = {
            "Argentina", "Bolivia", "Brasil", "Chile", "Colombia", "Costa Rica", "Cuba", "Ecuador",
            "El Salvador", "Guayana Francesa", "Granada", "Guatemala", "Guayana", "Haití",
            "Honduras", "Jamaica", "México", "Nicaragua", "Paraguay", "Panamá", "Perú",
            "Puerto Rico", "República Dominicana", "Surinam", "Uruguay", "Venezuela"
    };

    public static String[] ciudad_prin = {
            "Bogotá","Medellín"," Cali "," Barranquilla "," Cartagena de Indias "," Cúcula ",
            " Soledad "," Ibagué "," Bucaramanga "," Soacha "," Santa Marta "," Villavicencio ",
            " Bello "," Pereira "," Valledupar "," Manizales "," Buenaventura "," Pasto ",
            " Montería "," Neiva "
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        btnSiguiente = (Button)findViewById(R.id.btnSiguiente2);
        etTelefono = (EditText)findViewById(R.id.etTelefono);
        etCorreo_Elect = (EditText)findViewById(R.id.etCorreo_Elect);
        etPais = (EditText)findViewById(R.id.campo_pais);
        etCiudad = (EditText)findViewById(R.id.campo_ciudad);
        etDireccion = (EditText)findViewById(R.id.etDireccion);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if(validar()){
                    Intent sig2 = new Intent(ContactInfo.this, OtherInfo.class);
                    sig2.putExtra("Info2", bundle.getString("Info").toString()
                            + "\n" + getString(R.string.numberContact) + ":" + etTelefono.getText().toString()
                            + "\n" + getString(R.string.email) + ":" + etCorreo_Elect.getText().toString()
                            + "\n" + getString(R.string.country) + ":" + etPais.getText().toString()
                            + "\n" + getString(R.string.cityName) + ":" + etCiudad.getText().toString()
                            + "\n" + getString(R.string.direction) + ":" + etDireccion.getText().toString());
                    startActivity(sig2);
                }
            }
        });

        AutoCompleteTextView paisesSugerencias = (AutoCompleteTextView) findViewById(R.id.campo_pais);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, paises_lati);
        paisesSugerencias.setAdapter(adaptador);

        AutoCompleteTextView ciudadesSugerencias = (AutoCompleteTextView) findViewById(R.id.campo_ciudad);
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,ciudad_prin);
        ciudadesSugerencias.setAdapter(adaptador2);
    }

    public boolean validar() {
        boolean validado = true;
        etTelefono.setError(null);
        etCorreo_Elect.setError(null);
        etPais.setError(null);
        etCiudad.setError(null);
        etDireccion.setError(null);

        String telefono = etTelefono.getText().toString();
        String correo = etCorreo_Elect.getText().toString();
        String pais = etPais.getText().toString();
        String ciudad = etCiudad.getText().toString();
        String direccion = etDireccion.getText().toString();

        if(TextUtils.isEmpty(telefono)){
            etTelefono.setError(getString(R.string.error_campo_obligatorio));
            etTelefono.requestFocus();
            validado = false;
        }

        if(!isNumeric(telefono)){
            etTelefono.setError(getString(R.string.error_campo_numerico));
            etTelefono.requestFocus();
            validado = false;
        }

        if(TextUtils.isEmpty(correo)){
            etCorreo_Elect.setError(getString(R.string.error_campo_obligatorio));
            etCorreo_Elect.requestFocus();
            validado = false;
        }

        if(!validateEmail(correo)){
            etCorreo_Elect.setError(getString(R.string.error_sinta));
            etCorreo_Elect.requestFocus();
            validado = false;
        }

        if(TextUtils.isEmpty(pais)){
            etPais.setError(getString(R.string.error_campo_obligatorio));
            etPais.requestFocus();
            validado = false;
        }

        if(!checkAlpha(pais)){
            etPais.setError(getString(R.string.error_campo_alfabetico));
            etPais.requestFocus();
            validado = false;
        }

        if(TextUtils.isEmpty(ciudad)){
            etCiudad.setError(getString(R.string.error_campo_obligatorio));
            etCiudad.requestFocus();
            validado = false;
        }

        if(!checkAlpha(ciudad)){
            etCiudad.setError(getString(R.string.error_campo_alfabetico));
            etCiudad.requestFocus();
            validado = false;
        }

        if(TextUtils.isEmpty(direccion)){
            etDireccion.setError(getString(R.string.error_campo_obligatorio));
            etDireccion.requestFocus();
            validado = false;
        }

        return validado;
    }

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public static boolean checkAlpha(String str) {
        boolean respuesta = false;
        if ((str).matches("([a-z]|[A-Z]|\\s)+")) {
            respuesta = true;
        }
        return respuesta;
    }

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
