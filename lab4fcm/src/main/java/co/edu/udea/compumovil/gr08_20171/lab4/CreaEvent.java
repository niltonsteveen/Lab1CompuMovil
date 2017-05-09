package co.edu.udea.compumovil.gr08_20171.lab4;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import retrofit.client.Header;

public class CreaEvent extends AppCompatActivity {

    EditText etNombre,etFecha,etInformacion,etOrganizador,etPais,etDepartamento,etCiudad,etLugar,etPuntuacion;
    ImageView imgEvento;
    controladorBD1 controlBD1;
    Button btnGuardar, btnCambiarImg;
    Button cambiarFoto;
    List<Events> listaEventos;
    byte[] imgB;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;
    String TAG;
    DatabaseReference ref;
    DatabaseReference eventsRef;
    Uri imagenUri;
    int idactual;

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
        controlBD1 = new controladorBD1(getApplication());
        //Firebase
        FirebaseApp.initializeApp(this);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        eventsRef = ref.child("events");
        mStorage = FirebaseStorage.getInstance().getReference();

        eventsRef.child("idmayor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idactual = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cambiarFoto = (Button)findViewById(R.id.btnCambiarFotoEvem);
        btnGuardar = (Button)findViewById(R.id.btnGuardarEven);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagenUri == null){
                    Toast.makeText(CreaEvent.this, "Seleccione una imagen", Toast.LENGTH_SHORT).show();
                    return;
                }
                subirImagen();
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
            imagenUri =uri;
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

    public String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public void subirImagen() {
        progressDialog.setMessage("Creando evento");
        progressDialog.show();
        final StorageReference filepath = mStorage.child("Events").child(getRandomString());
        Log.i("la url de la imagen es",imagenUri.toString());
        filepath.putFile(imagenUri).addOnSuccessListener(CreaEvent.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Uri imgUri = taskSnapshot.getDownloadUrl();
                eventsRef.child((idactual+1)+"")
                        .child("name").setValue(etNombre.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("date").setValue(etFecha.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("information").setValue(etInformacion.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("organizer").setValue(etOrganizador.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("country").setValue(etPais.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("department").setValue(etDepartamento.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("city").setValue(etCiudad.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("place").setValue(etLugar.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("puntuation").setValue(etPuntuacion.getText().toString());
                eventsRef.child((idactual+1)+"")
                        .child("photo").setValue(imgUri.toString());
                eventsRef.child("idmayor").setValue((idactual+1)+"");
                AddEvent addEvent = new AddEvent();
                addEvent.execute(etNombre.getText().toString());
                Toast.makeText(getApplicationContext(), "Evento creado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreaEvent.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(CreaEvent.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CreaEvent.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class AddEvent extends AsyncTask<String, Void, Void> {
        private String TAG = "NOTICIA";

        @Override
        protected Void doInBackground(String... params){

            Log.i(TAG,"doInBackgound");
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
            post.addHeader("content-type","application/json");
            post.addHeader("authorization","key=AAAAtDsrnr4:APA91bGsikFUka0KuSOeE-zUCgRDgj2TIYQoj1HhyWLxye8O7kKu3i55IPvqncDXlL7wbfFb74ctscYYvkw6Q_HXgJYD-YNbmr9PbOYqwgw8z1My6LGHsi1w0mQnAE8UFa0i5nwqbsMo");

            try{

                String json = "";
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("title","Se ha creado un nuevo evento");
                jsonObject2.accumulate("body","Evento: "+params[0]);
                jsonObject.accumulate("to","eMfbZhtLn64:APA91bHLZ4GHTqe7KyJF5FbzHM6cTwslSfwJ4rVh3Rzwl10Osnmcne7r4NF16cAJ5jZZfnAJA2aGf-ncNecuoMO0JDHVriIx4YZiKbUR3iEuNU4BJGpfCWwpKlYcjC-YuTP9VQF0Axn2");
                jsonObject.accumulate("notification",jsonObject2);
                json = jsonObject.toString();
                Log.d(TAG,json);
                StringEntity stringEntity = new StringEntity(json);
                post.setEntity((stringEntity));

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                Log.d(TAG,respStr);
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
            Toast.makeText(getApplicationContext(), "Se guardo el registro", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute(){
            Log.i(TAG,"onPreExecute");

        }

    }
}
