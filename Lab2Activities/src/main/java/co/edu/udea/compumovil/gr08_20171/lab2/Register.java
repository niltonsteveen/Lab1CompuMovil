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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    Button btnGuardar,btnCambiarIm;
    EditText etUsuario, etContrasena, etNombre, etEmail, etCelular, etPais, etDepartamento,
            etCiudad, etDireccion, etEdad;
    ImageView imgUsuario;
    controladorBD1 controlBD1;

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

        controlBD1 = new controladorBD1(getApplicationContext());

        btnGuardar = (Button)findViewById(R.id.btnGuardarCuenta);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInfo()) {
                    SQLiteDatabase db = controlBD1.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_USUARIO, etUsuario.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_CONTRASENA, etContrasena.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_NOMBRE, etNombre.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_EMAIL, etEmail.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_CELULAR, etCelular.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_PAIS, etPais.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_DEPARTAMENTO, etDepartamento.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_CIUDAD, etCiudad.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_DIRECCION, etDireccion.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_EDAD, etEdad.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_USUARIO, etUsuario.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_USUARIO, etUsuario.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_USUARIO, etUsuario.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_USUARIO, etUsuario.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_USUARIO, etUsuario.getText().toString());
                    valores.put(controladorBD1.DatosTablaUser.COLUMN_FOTO, imageViewToByte(imgUsuario));

                    Long emailGuardado = db.insert(controladorBD1.DatosTablaUser.NOMBRE_TABLA,
                            controladorBD1.DatosTablaUser.COLUMN_EMAIL, valores);
                    Toast.makeText(getApplicationContext(), "Se guardo el registro" + emailGuardado, Toast.LENGTH_LONG).show();

                    Intent verPerfil = new Intent(Register.this, LoginActivity.class);
                    verPerfil.putExtra("sali", "");
                    startActivity(verPerfil);
                }
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
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
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
}
