package co.edu.udea.compumovil.gr08_20171.lab4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegistro,btnEntrar;
    EditText etUsuario,etContrasena;
    TextView tvEstado;
    Bundle bundle;
    boolean valor;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        valor = pref.getBoolean("sesion_activa",false);
        Log.i("Estado ",valor+"");

        btnRegistro=(Button)findViewById(R.id.btnRegistrarse);
        etUsuario = (EditText)findViewById(R.id.etUserLo);
        etContrasena = (EditText)findViewById(R.id.etPassLo);
        btnEntrar = (Button)findViewById(R.id.btnLogin);
        tvEstado = (TextView)findViewById(R.id.tvEstaActu);

        if(!valor){
            FirebaseApp.initializeApp(this);
            if(mAuth != null){
                mAuth.signOut();
            }
            mAuth = FirebaseAuth.getInstance();
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
                    progressDialog.setMessage("Iniciando sesión");
                    signIn(etUsuario.getText().toString(),etContrasena.getText().toString());
                }
            });
        }
        else
        {
            tvEstado.setText("Iniciando sesión...");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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

    private void signIn(String email, String password) {
        if(email.equals("") || password.equals("")){
            Toast.makeText(LoginActivity.this, "Datos incompletos", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "signIn:" + email);
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        try {
                            AuthResult resultado = task.getResult();
                            Toast.makeText(LoginActivity.this, resultado.getUser().getUid(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                            // obtener excepción
                            if(task != null) {
                                throw task.getException();
                            }
                        }
                        catch (FirebaseAuthInvalidCredentialsException e){
                            // mostrar excepción
                            Toast.makeText(LoginActivity.this, "Datos incompletos", Toast.LENGTH_SHORT).show();
                            return;
                        }catch (RuntimeExecutionException e) {
                            e.printStackTrace();
                            if( e.getMessage().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") ){
                                Toast.makeText(LoginActivity.this, "Ingrese una dirección de correo valida", Toast.LENGTH_SHORT).show();
                            }
                            else if( e.getMessage().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.") ){
                                Toast.makeText(LoginActivity.this,"Datos incorrectos", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(LoginActivity.this, e.getMessage()+"  oli", Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            //Toast.makeText(ActivityFirebase.this, R.string.auth_failed,Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            tvEstado.setText("Fallo la autentificacón");
                        }
                    }
                });
    }

}

