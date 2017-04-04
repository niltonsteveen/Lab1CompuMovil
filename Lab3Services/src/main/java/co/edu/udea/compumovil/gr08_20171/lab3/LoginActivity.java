package co.edu.udea.compumovil.gr08_20171.lab3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegistro,btnEntrar;
    RadioButton rbRecordar;
    boolean recor = false;
    EditText etUsuario,etContrasena;
   // controladorBD1 controlBD1;
    Bundle bundle;
    String valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Context context = this;

        SharedPreferences sharprefs = getSharedPreferences("ArchivoSP",context.MODE_PRIVATE);

        bundle = getIntent().getExtras();
        if(bundle.getString("sali").toString().equals("cerrar"))
        {
            SharedPreferences sharpref = getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharpref.edit();
            editor.putString("MiDato", "");
            editor.commit();
        }
        SharedPreferences sharpref = getPreferences(context.MODE_PRIVATE);
        valor = sharpref.getString("MiDato","");

        btnRegistro=(Button)findViewById(R.id.btnRegistrarse);
        etUsuario = (EditText)findViewById(R.id.etUserLo);
        etContrasena = (EditText)findViewById(R.id.etPassLo);
        btnEntrar = (Button)findViewById(R.id.btnLogin);
        rbRecordar = (RadioButton)findViewById(R.id.rbRecordar);
      //  controlBD1 = new controladorBD1(getApplicationContext());
        if(valor.equals("")) {
            btnRegistro.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent crearCuenta = new Intent(LoginActivity.this, Register.class);
                    startActivity(crearCuenta);
                }
            });

            rbRecordar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    recor = !recor;
                    rbRecordar.setChecked(recor);
                }
            });


            btnEntrar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    attemptLogin();
                    SQLiteDatabase db = controlBD1.getReadableDatabase();
                    String[] projection = {
                            controladorBD1.DatosTablaUser.COLUMN_EMAIL
                    };
                    String selection = controladorBD1.DatosTablaUser.COLUMN_USUARIO + " = ?"
                            +" AND "
                            +controladorBD1.DatosTablaUser.COLUMN_CONTRASENA + " = ?";
                    String[] arqsel = {
                            etUsuario.getText().toString(),
                            etContrasena.getText().toString()
                    };
                    Cursor c = db.query(
                            controladorBD1.DatosTablaUser.NOMBRE_TABLA,
                            projection,
                            selection,
                            arqsel,
                            null,           // don't group the rows
                            null,           // don't filter by row groups
                            null            // The sort order
                    );

                    System.out.println(c.toString());
                    c.moveToFirst();
                    if(c.getCount()==0){
                        Toast.makeText(getApplicationContext(),"Usuario y/o contraseña erroneas", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(recor)
                        {
                            SharedPreferences sharpref = getPreferences(context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharpref.edit();
                            editor.putString("MiDato", etUsuario.getText().toString());
                            editor.commit();
                        }
                        Intent verPerfil = new Intent(LoginActivity.this,MainActivity.class);

                        System.out.println(c.getString(0));
                        verPerfil.putExtra("user",c.getString(0));
                        startActivity(verPerfil);


                    }*/
                }
            });
        }/*
        else{
            SQLiteDatabase db = controlBD1.getReadableDatabase();
            String[] projection = {
                    controladorBD1.DatosTablaUser.COLUMN_EMAIL
            };
            String selection = controladorBD1.DatosTablaUser.COLUMN_USUARIO + " = ?";
            String[] arqsel = {
                    valor
            };
            Cursor c = db.query(
                    controladorBD1.DatosTablaUser.NOMBRE_TABLA,
                    projection,
                    selection,
                    arqsel,
                    null,           // don't group the rows
                    null,           // don't filter by row groups
                    null            // The sort order
            );
            c.moveToFirst();
            if(c.getString(0).equals(""))
            {
                SharedPreferences.Editor editor = sharpref.edit();
                editor.putString("MiDato", "");
                editor.commit();
            }
            else
            {
                Intent verPerfil = new Intent(LoginActivity.this, MainActivity.class);
                verPerfil.putExtra("user", c.getString(0));
                startActivity(verPerfil);
            }

        }*/
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
}

