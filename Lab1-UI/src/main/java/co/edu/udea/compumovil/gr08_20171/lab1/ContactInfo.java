package co.edu.udea.compumovil.gr08_20171.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

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
                Intent sig2 = new Intent(ContactInfo.this, OtherInfo.class);
                sig2.putExtra("Info2",bundle.getString("Info").toString()
                        +"\n"+getString(R.string.numberContact)+":"+ etTelefono.getText().toString()
                        +"\n"+getString(R.string.email)+":"+ etCorreo_Elect.getText().toString()
                        +"\n"+getString(R.string.country)+":"+ etPais.getText().toString()
                        +"\n"+getString(R.string.cityName)+":"+etCiudad.getText().toString()
                        +"\n"+getString(R.string.direction)+":"+etDireccion.getText().toString());
                startActivity(sig2);
            }
        });

        AutoCompleteTextView paisesSugerencias = (AutoCompleteTextView) findViewById(R.id.campo_pais);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, paises_lati);
        paisesSugerencias.setAdapter(adaptador);

        AutoCompleteTextView ciudadesSugerencias = (AutoCompleteTextView) findViewById(R.id.campo_ciudad);
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,ciudad_prin);
        ciudadesSugerencias.setAdapter(adaptador2);
    }
}
