package co.edu.udea.compumovil.gr08_20171.lab2;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditarPerfil extends AppCompatActivity {

    Button btnActulizar,btnCambiarImg;
    EditText etUsuario, etContrasena, etNombre, etEmail, etCelular, etPais, etDepartamento,
            etCiudad, etDireccion, etEdad;
    ImageView imgUsuario;
    controladorBD1 controlBD1;
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

        controlBD1 = new controladorBD1(getApplicationContext());

        btnActulizar = (Button)findViewById(R.id.btnActuPerfil);
        btnActulizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = controlBD1.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(controladorBD1.DatosTablaUser.COLUMN_USUARIO,etUsuario.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_CONTRASENA,etContrasena.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_NOMBRE,etNombre.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_EMAIL,etEmail.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_CELULAR,etCelular.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_PAIS,etPais.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_DEPARTAMENTO,etDepartamento.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_CIUDAD,etCiudad.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_DIRECCION,etDireccion.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_EDAD,etEdad.getText().toString());
                valores.put(controladorBD1.DatosTablaUser.COLUMN_FOTO,imageViewToByte(imgUsuario));
                String[] arqsel = {usuarioCorreo};
                String Selection = controladorBD1.DatosTablaUser.COLUMN_EMAIL+"=?";

                int count = db.update(controladorBD1.DatosTablaUser.NOMBRE_TABLA,valores, Selection,arqsel);
                Toast.makeText(getApplication(),"Actualizo correctamente", Toast.LENGTH_LONG).show();
                finish();
            }

            private byte[] imageViewToByte(ImageView imgUsuario) {
                Bitmap bitmap = ((BitmapDrawable)imgUsuario.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray = stream.toByteArray();
                return byteArray;
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
}
