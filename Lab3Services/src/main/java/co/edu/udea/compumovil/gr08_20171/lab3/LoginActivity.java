package co.edu.udea.compumovil.gr08_20171.lab3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegistro,btnEntrar;
    RadioButton rbRecordar;
    boolean recor = false;
    EditText etUsuario,etContrasena;
    TextView tvEstado;
    Bundle bundle;
    boolean valor;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActualizarBD actualizarBD = new ActualizarBD(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        valor = pref.getBoolean("sesion_activa",false);
        Log.i("Estado ",valor+"");

        btnRegistro=(Button)findViewById(R.id.btnRegistrarse);
        etUsuario = (EditText)findViewById(R.id.etUserLo);
        etContrasena = (EditText)findViewById(R.id.etPassLo);
        btnEntrar = (Button)findViewById(R.id.btnLogin);
        tvEstado = (TextView)findViewById(R.id.tvEstaActu);

        if(!valor){
            tvEstado.setText("Ingresar datos...");
            btnRegistro.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent crearCuenta = new Intent(LoginActivity.this, Register.class);
                    startActivity(crearCuenta);
                }
            });

            btnEntrar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvEstado.setText("Iniciando sesión...");
                    correo = "";
                    ObtenerUser tarea = new ObtenerUser();
                    tarea.execute(etUsuario.getText().toString());
                }
            });
        }
        else {
            etUsuario.setEnabled(false);
            etContrasena.setEnabled(false);
            btnEntrar.setEnabled(false);
            btnRegistro.setEnabled(false);
            tvEstado.setText("Iniciando sesión...");
            SharedPreferences sharpref = getSharedPreferences("Preferent",this.MODE_PRIVATE);
            correo = sharpref.getString("correoUser","no");
            Log.i("Correo",correo);
            ObtenerUser tarea = new ObtenerUser();
            tarea.execute(correo);
        }
    }

    private void attemptLogin() {
        etUsuario.setError(null);
        etContrasena.setError(null);


        String email = etUsuario.getText().toString();
        String password = etContrasena.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            etContrasena.setError(getString(R.string.error_invalid_password));
            focusView = etContrasena;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etUsuario.setError(getString(R.string.error_field_required));
            focusView = etUsuario;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }


    //tarea asincrona para llamar
    public class ObtenerUser extends AsyncTask<String,Void,Void> {
        //campos a recibir
        String oUsuario, oClave, oName, oCorreo, oPhone, oCountry, oDepartment, oCity, oDirection, oAge, oPhoto;


        @Override
        protected Void doInBackground(String... params){
            Log.i("ConsultaUser","doInBackground");
            HttpClient httpClient = new DefaultHttpClient();

            String sCorreo = params[0];
            Log.i("correo actual",sCorreo);
            HttpGet get = new HttpGet("https://apirest-eventos.herokuapp.com/user_set/" + sCorreo);
            get.setHeader("Content-type","application/json");

            try{

                HttpResponse resp = httpClient.execute(get);
                String respString = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respString);
                oUsuario = respJSON.getString("username");
                oClave = respJSON.getString("password");
                oName = respJSON.getString("name");
                oCorreo = respJSON.getString("email");
                oPhone = respJSON.getString("phone");
                oCountry = respJSON.getString("country");
                oDepartment = respJSON.getString("department");
                oCity = respJSON.getString("city");
                oDirection = respJSON.getString("direction");
                oAge = respJSON.getString("age");
                oPhoto = respJSON.getString("photo");
            }
            catch (Exception ex)
            {
                Log.e("ServicioRest","Error!",ex);
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            tvEstado.setText("Sesión iniciada...");
            Log.i("ServicioRest","onPostExecute");
            Log.e("Correo-----------", correo);
            if(correo.equals("")) {
                 if (oClave.equals(etContrasena.getText().toString())) {
                    String[] datos = {
                            oUsuario, oClave, oName, oCorreo, oPhone, oCountry, oDepartment, oCity, oDirection, oAge, oPhoto
                    };
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("datos", datos);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario y/o contraseña erroneas", Toast.LENGTH_LONG).show();
                }
            }
            else{
                String[] datos = {
                        oUsuario, oClave, oName, oCorreo, oPhone, oCountry, oDepartment, oCity, oDirection, oAge, oPhoto
                };
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("datos", datos);
                startActivity(intent);
            }
        }

        @Override
        protected void onPreExecute(){
            Log.i("ServicioRest","onPreExecute");
            //antes de la ejecucion

        }

    }
}

