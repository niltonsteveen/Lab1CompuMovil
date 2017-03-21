package co.edu.udea.compumovil.gr08_20171.lab2;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreaEvent extends AppCompatActivity {

    EditText etNombre,etFecha,etInformacion,etOrganizador,etPais,etDepartamento,etCiudad,etLugar,etPuntuacion;
    ImageView imgEvento;
    controladorBD1 controlBD1;
    Button btnGuardar, btnCambiarImg;
    Button cambiarFoto;
    Button btnMostrarFecha;
    private int dia, mes, año;
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

        btnMostrarFecha =(Button)findViewById(R.id.btnFechaEvento);
        final Calendar c = Calendar.getInstance();
        año = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        mostrarFecha();

        cambiarFoto = (Button)findViewById(R.id.btnCambiarFotoEvem);
        btnGuardar = (Button)findViewById(R.id.btnGuardarEven);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInfo()) {
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
                    valores.put(controladorBD1.DatosTablaEvent.COLUMN_FOTO, imageViewToByte(imgEvento));

                    Long emailGuardado = db.insert(controladorBD1.DatosTablaEvent.NOMBRE_TABLA,
                            controladorBD1.DatosTablaEvent.COLUMN_ID, valores);
                    Toast.makeText(getApplication(), "Se guardo el evento" + emailGuardado, Toast.LENGTH_LONG).show();
                    finish();
                }
                /*9
                consultarEvents();
                Fragment fragment = null;
                eventos even = new eventos();
                even.setLista(listaEventos);
                fragment = even;
                getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();*/
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


    public void mostrarFecha(){
        etFecha.setText(dia +"/"+(mes +1)+"/"+ año);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public boolean isValidInfo(){
        boolean res=false;
        if(TextUtils.isEmpty(etNombre.getText())) {
            etNombre.setError(getString(R.string.errorGeneral));
            etNombre.requestFocus();
            res = false;
        }else if (TextUtils.isEmpty(etFecha.getText())){
            etFecha.setError(getString(R.string.errorGeneral));
            etFecha.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etInformacion.getText())){
            etInformacion.setError(getString(R.string.errorGeneral));
            etInformacion.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etOrganizador.getText())){
            etOrganizador.setError(getString(R.string.errorGeneral));
            etOrganizador.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etPais.getText())){
            etPais.setError(getString(R.string.errorGeneral));
            etPais.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etDepartamento.getText())){
            etDepartamento.setError(getString(R.string.errorGeneral));
            etDepartamento.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etCiudad.getText())){
            etCiudad.setError(getString(R.string.errorGeneral));
            etCiudad.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etLugar.getText())){
            etLugar.setError(getString(R.string.errorGeneral));
            etLugar.requestFocus();
            res=false;
        }else if(TextUtils.isEmpty(etPuntuacion.getText())){
            etPuntuacion.setError(getString(R.string.errorGeneral));
            etPuntuacion.requestFocus();
            res=false;
        }else if(imgEvento==null){
            res=false;
        }else{
            res=true;
        }
        return res;
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

    private void consultarEvents() {
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + controladorBD1.DatosTablaEvent.NOMBRE_TABLA, null);
        listaEventos = new ArrayList<Events>();
        while (cursor.moveToNext()) {
            Events evt = new Events();
            String nombre = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE));
            String fecha = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FECHA));
            String informacion = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION));
            String organizador = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR));
            String pais = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PAIS));
            String departamento = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO));
            String ciudad = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD));
            String lugar = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_LUGAR));
            String puntuacion = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION));
            byte[] foto = cursor.getBlob(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FOTO));

            evt.setNombre(nombre);
            evt.setFecha(fecha);
            evt.setInformación(informacion);
            evt.setOrganizador(organizador);
            evt.setPais(pais);
            evt.setDepartamento(departamento);
            evt.setCiudad(ciudad);
            evt.setLugar(lugar);
            evt.setPuntuacion(puntuacion);
            evt.setFoto(foto);

            listaEventos.add(evt);
        }
    }

    private byte[] imageViewToByte(ImageView imgUsuario) {
        Bitmap bitmap = ((BitmapDrawable)imgUsuario.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
