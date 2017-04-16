package co.edu.udea.compumovil.gr08_20171.lab3;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

        cambiarFoto = (Button)findViewById(R.id.btnCambiarFotoEvem);
        btnGuardar = (Button)findViewById(R.id.btnGuardarEven);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = controlBD1.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE, etNombre.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_FECHA, etFecha.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION, etInformacion.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR, etOrganizador.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_PAIS, etPais.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO, etDepartamento.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD, etCiudad.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_LUGAR, etLugar.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION, etPuntuacion.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_FOTO,imageViewToByte(imgEvento));

                Long eventoGuardado = db.insert(controladorBD1.DatosTablaEvent.NOMBRE_TABLA,
                        controladorBD1.DatosTablaEvent.COLUMN_ID,valores);
                Toast.makeText(getApplication(),"Se guardo el evento"+eventoGuardado, Toast.LENGTH_LONG).show();
                finish();
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
}
