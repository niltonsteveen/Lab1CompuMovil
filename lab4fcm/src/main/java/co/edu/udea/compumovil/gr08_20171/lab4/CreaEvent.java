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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CreaEvent extends AppCompatActivity {

    EditText etNombre,etFecha,etInformacion,etOrganizador,etPais,etDepartamento,etCiudad,etLugar,etPuntuacion;
    ImageView imgEvento;
    controladorBD1 controlBD1;
    Button btnGuardar, btnCambiarImg;
    Button cambiarFoto;
    List<Events> listaEventos;
    byte[] imgB;
    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_event);
        etNombre = (EditText)findViewById(R.id.etNombreEven);
        etFecha = (EditText)findViewById(R.id.etFechaEven);
        etInformacion = (EditText)findViewById(R.id.etInfoEven);
        etOrganizador = (EditText)findViewById(R.id.etOrganizadorEven);
        etPais = (EditText)findViewById(R.id.etPaisEven);
        etDepartamento = (EditText)findViewById(R.id.etDepartamentoEven);
        etCiudad = (EditText)findViewById(R.id.etCiudadEven);
        etLugar = (EditText)findViewById(R.id.etLugarEven);
        etPuntuacion = (EditText)findViewById(R.id.etPuntuacionEven);
        imgEvento = (ImageView)findViewById(R.id.img_evenPerfil);
        imgB = imageViewToByte(imgEvento);
        controlBD1 = new controladorBD1(getApplication());

        cambiarFoto = (Button)findViewById(R.id.btnCambiarFotoEvem);
        btnGuardar = (Button)findViewById(R.id.btnGuardarEven);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEvent addEvent = new AddEvent();
                addEvent.execute(
                        etNombre.getText().toString(),
                        etFecha.getText().toString(),
                        etInformacion.getText().toString(),
                        etOrganizador.getText().toString(),
                        etPais.getText().toString(),
                        etDepartamento.getText().toString(),
                        etCiudad.getText().toString(),
                        etLugar.getText().toString(),
                        etPuntuacion.getText().toString(),
                        Base64.encodeToString(imgB,Base64.DEFAULT)
                );
            }
        });

        btnCambiarImg = (Button)findViewById(R.id.btnCambiarFotoEvem);
        btnCambiarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        CreaEvent.this,
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
                imgEvento.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private byte[] imageViewToByte(ImageView imgUsuario) {
        Bitmap bitmap = ((BitmapDrawable)imgUsuario.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    private class AddEvent extends AsyncTask<String, Void, Void> {
        private String TAG = "AddEvent";
        private String respuesta;

        @Override
        protected Void doInBackground(String... params){

            Log.i(TAG,"doInBackgound");

            HttpClient httpClient = new DefaultHttpClient();

            String iName = params[0];
            String iDate = params[1];;
            String iInformation = params[2];
            String iOrganizator = params[3];
            String iCountry = params[4];
            String iCity = params[5];
            String iDepartment = params[6];
            String iPlace = params[7];
            String iPuntuation = params[8];
            String foto = params[9];

            Log.i(TAG,"foto enB4 "+params[9]);

            HttpPost post = new HttpPost("https://apirest-eventos.herokuapp.com/allEvents/");

            post.setHeader("content-type","application/x-www-form-urlencoded");

            try{
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("name", iName));
                pairs.add(new BasicNameValuePair("date", iDate));
                pairs.add(new BasicNameValuePair("information", iInformation));
                pairs.add(new BasicNameValuePair("organizator", iOrganizator));
                pairs.add(new BasicNameValuePair("country", iCountry));
                pairs.add(new BasicNameValuePair("department", iDepartment));
                pairs.add(new BasicNameValuePair("city", iCity));
                pairs.add(new BasicNameValuePair("place", iPlace));
                pairs.add(new BasicNameValuePair("puntuation", iPuntuation));
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
            Intent verPerfil = new Intent(CreaEvent.this, MainActivity.class);
            startActivity(verPerfil);
        }

        @Override
        protected void onPreExecute(){
            Log.i(TAG,"onPreExecute");

        }

    }
}
