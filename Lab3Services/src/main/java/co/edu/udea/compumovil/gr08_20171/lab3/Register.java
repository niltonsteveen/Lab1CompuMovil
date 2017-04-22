package co.edu.udea.compumovil.gr08_20171.lab3;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Base64;

public class Register extends AppCompatActivity {

    Button btnGuardar,btnCambiarIm;
    EditText etUsuario, etContrasena, etNombre, etEmail, etCelular, etPais, etDepartamento,
            etCiudad, etDireccion, etEdad;
    ImageView imgUsuario;
    byte[] imgByte;
  //  controladorBD1 controlBD1;

    final int REQUEST_CODE_GALLERY = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsuario = (EditText)findViewById(R.id.etUsuarioReg);
        etContrasena = (EditText)findViewById(R.id.etContrasenaReg);
        etNombre = (EditText)findViewById(R.id.etNombreReg);
        etEmail = (EditText)findViewById(R.id.etEmailReg);
        etCelular = (EditText)findViewById(R.id.etCelularReg);
        etPais = (EditText)findViewById(R.id.etPaisReg);
        etDepartamento = (EditText)findViewById(R.id.etDepartamentoReg);
        etCiudad = (EditText)findViewById(R.id.etCiudadReg);
        etDireccion = (EditText)findViewById(R.id.etDireccionReg);
        etEdad = (EditText)findViewById(R.id.etEdadReg);
        imgUsuario = (ImageView)findViewById(R.id.img_usuarioReg);
        imgByte = imageViewToByte(imgUsuario);

        btnGuardar = (Button)findViewById(R.id.btnGuardarCuenta);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser tarea = new AddUser();
                tarea.execute(
                        etUsuario.getText().toString(),
                        etContrasena.getText().toString(),
                        etNombre.getText().toString(),
                        etEmail.getText().toString(),
                        etCelular.getText().toString(),
                        etPais.getText().toString(),
                        etDepartamento.getText().toString(),
                        etCiudad.getText().toString(),
                        etDireccion.getText().toString(),
                        etEdad.getText().toString(),
                        Base64.encodeToString(imgByte,Base64.DEFAULT)
                );
            }
        });

        btnCambiarIm = (Button)findViewById(R.id.btnCambiarFotoReg);
        btnCambiarIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        Register.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY

                );

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(getApplicationContext(),"No tienes permiso para acceder a esta carpeta", Toast.LENGTH_LONG).show();

            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgUsuario.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] imageViewToByte(ImageView imgUsuario) {
        Bitmap bitmap = ((BitmapDrawable)imgUsuario.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public boolean isValidInfo(){
        boolean res=false;
        if(TextUtils.isEmpty(etUsuario.getText())) {
            etUsuario.setError(getString(R.string.errorUsuario));
            etUsuario.requestFocus();
            res = false;
        }else if (etContrasena.getText().length()<7 || TextUtils.isEmpty(etContrasena.getText())){
            etContrasena.setError(getString(R.string.errorContraseña));
            etContrasena.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etNombre.getText())){
            etNombre.setError(getString(R.string.errorNombre));
            etNombre.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etEmail.getText())||!validateEmail(etEmail.getText().toString())){
            etEmail.setError(getString(R.string.errorCorreo));
            etEmail.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etCelular.getText())||!TextUtils.isDigitsOnly(etCelular.getText())){
            etCelular.setError(getString(R.string.errorCelular));
            etCelular.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etPais.getText())){
            etPais.setError(getString(R.string.errorGeneral));
            etPais.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etCiudad.getText())){
            etCiudad.setError(getString(R.string.errorGeneral));
            etCiudad.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etDireccion.getText())){
            etDireccion.setError(getString(R.string.errorGeneral));
            etDireccion.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etDepartamento.getText())){
            etDepartamento.setError(getString(R.string.errorGeneral));
            etDepartamento.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etEdad.getText())||!TextUtils.isDigitsOnly(etEdad.getText())){
            etEdad.setError(getString(R.string.errorEdad));
            etEdad.requestFocus();
            res=false;
        }else if(imgUsuario==null){
            res=false;
        }else{
            res=true;
        }
        return res;
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

    private class AddUser extends AsyncTask<String, Void, Void>{
        private String TAG = "AddUser";
        private String respuesta;

        @Override
        protected Void doInBackground(String... params){

            Log.i(TAG,"doInBackgound");

            HttpClient httpClient = new DefaultHttpClient();

            String iUsername = params[0];
            String iPassword = params[1];;
            String iName = params[2];
            String iEmail = params[3];
            String iPhone = params[4];
            String iCountry = params[5];
            String iCity = params[6];
            String iDepartment = params[7];
            String iDirection = params[8];
            String iAge = params[9];
            String foto = params[10];

            Log.i(TAG,"foto enB4 "+params[10]);

            HttpPost post = new HttpPost("https://apirest-eventos.herokuapp.com/allusers/");

            post.setHeader("content-type","application/x-www-form-urlencoded");

            try{
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("username", iUsername));
                pairs.add(new BasicNameValuePair("password", iPassword));
                pairs.add(new BasicNameValuePair("name", iName));
                pairs.add(new BasicNameValuePair("email", iEmail));
                pairs.add(new BasicNameValuePair("phone", iPhone));
                pairs.add(new BasicNameValuePair("country", iCountry));
                pairs.add(new BasicNameValuePair("department", iDepartment));
                pairs.add(new BasicNameValuePair("city", iCity));
                pairs.add(new BasicNameValuePair("direction", iDirection));
                pairs.add(new BasicNameValuePair("age", iAge));
                pairs.add(new BasicNameValuePair("photo",foto));
                post.setEntity(new UrlEncodedFormEntity(pairs));

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);

                respuesta = respJSON.toString();
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
            Log.i(TAG,"onPostExecute + "+respuesta);
            Toast.makeText(getApplicationContext(), "Se guardo el registro", Toast.LENGTH_LONG).show();
            Intent verPerfil = new Intent(Register.this, LoginActivity.class);
            startActivity(verPerfil);
        }

        @Override
        protected void onPreExecute(){
            Log.i(TAG,"onPreExecute");

        }

    }

}
