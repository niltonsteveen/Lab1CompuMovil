package co.edu.udea.compumovil.gr08_20171.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button btnRegistro,btnEntrar;
    RadioButton rbRecordar;
    boolean recor = false;
    EditText etUsuario,etContrasena;
    controladorBD1 controlBD1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        btnRegistro=(Button)findViewById(R.id.btnRegistrarse);
        etUsuario = (EditText)findViewById(R.id.etUserLo);
        etContrasena = (EditText)findViewById(R.id.etPassLo);
        btnEntrar = (Button)findViewById(R.id.btnLogin);
        rbRecordar = (RadioButton)findViewById(R.id.rbRecordar);
        controlBD1 = new controladorBD1(getApplicationContext());

        Button mEmailSignInButton = (Button) findViewById(R.id.btnLogin);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crearCuenta = new Intent(LoginActivity.this, Register.class);
                startActivity(crearCuenta);
            }
        });

        rbRecordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recor = !recor;
                rbRecordar.setChecked(recor);
            }
        });


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Intent verPerfil = new Intent(Login.this,perfil.class);
                    verPerfil.putExtra("user",c.getString(0));
                    startActivity(verPerfil);
                }
            }
        });
    }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!validateEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
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

