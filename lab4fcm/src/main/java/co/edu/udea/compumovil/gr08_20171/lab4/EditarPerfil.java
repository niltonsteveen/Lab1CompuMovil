package co.edu.udea.compumovil.gr08_20171.lab4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditarPerfil extends AppCompatActivity {

    Button btnActulizar,btnCambiarImg;
    EditText etUsuario, etContrasena, etNombre, etEmail, etCelular, etPais, etDepartamento,
            etCiudad, etDireccion, etEdad;
    ImageView imgUsuario;
    byte[] imgByte;
   // controladorBD1 controlBD1;
    private String usuarioCorreo;
    Object[] datos;
    Bundle bundle;
    final int REQUEST_CODE_GALLERY = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        bundle = getIntent().getExtras();
        datos = (Object[]) bundle.get("userCorreo");
        usuarioCorreo = (String)datos[3];
        etUsuario = (EditText)findViewById(R.id.etUsuarioPerfil);
        etUsuario.setText((String)datos[0]);
        etContrasena = (EditText)findViewById(R.id.etContrasenaPerfil);
        etContrasena.setText((String)datos[1]);
        etNombre = (EditText)findViewById(R.id.etNombrePerfil);
        etNombre.setText((String)datos[2]);
        etEmail = (EditText)findViewById(R.id.etEmailPerfil);
        etEmail.setText((String)datos[3]);
        etCelular = (EditText)findViewById(R.id.etCelularPerfil);
        etCelular.setText((String)datos[4]);
        etPais = (EditText)findViewById(R.id.etPaisPerfil);
        etPais.setText((String)datos[5]);
        etDepartamento = (EditText)findViewById(R.id.etDepartamentoPerfil);
        etDepartamento.setText((String)datos[6]);
        etCiudad = (EditText)findViewById(R.id.etCiudadPerfil);
        etCiudad.setText((String)datos[7]);
        etDireccion = (EditText)findViewById(R.id.etDireccionPerfil);
        etDireccion.setText((String)datos[8]);
        etEdad = (EditText)findViewById(R.id.etEdadPerfil);
        etEdad.setText((String)datos[9]);
        imgUsuario = (ImageView)findViewById(R.id.img_usuarioPerfil);
        imgUsuario.setImageBitmap((Bitmap)datos[10]);
        imgByte = imageViewToByte(imgUsuario);

        //    controlBD1 = new controladorBD1(getApplicationContext());

        btnActulizar = (Button)findViewById(R.id.btnActuPerfil);
        btnActulizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actuUser tarea = new actuUser();
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

        btnCambiarImg = (Button)findViewById(R.id.btnCambiarFotoPerfil);
        btnCambiarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EditarPerfil.this,
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

    private class actuUser extends AsyncTask<String, Void, Void> {
        private String TAG = "actuUser";
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

            Log.i(TAG,"foto enB4 "+"https://apirest-eventos.herokuapp.com/user_set/"+iEmail);

            HttpPut put = new HttpPut("https://apirest-eventos.herokuapp.com/user_set/"+iEmail+"/");

            put.setHeader("content-type","application/x-www-form-urlencoded");

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
                put.setEntity(new UrlEncodedFormEntity(pairs));

                HttpResponse resp = httpClient.execute(put);
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
            Toast.makeText(getApplicationContext(), "Se actualizo usuario, debe volver a iniciar sesión", Toast.LENGTH_LONG).show();
            Intent verPerfil = new Intent(EditarPerfil.this, LoginActivity.class);
            startActivity(verPerfil);
        }

        @Override
        protected void onPreExecute(){
            Log.i(TAG,"onPreExecute");

        }

    }

    private byte[] imageViewToByte(ImageView imgUsuario) {
        Bitmap bitmap = ((BitmapDrawable)imgUsuario.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
