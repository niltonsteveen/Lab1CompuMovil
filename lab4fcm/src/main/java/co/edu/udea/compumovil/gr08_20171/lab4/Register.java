package co.edu.udea.compumovil.gr08_20171.lab4;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.EmptyStackException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    Button btnGuardar,btnCambiarIm;
    EditText etUsuario, etContrasena, etNombre, etEmail, etCelular, etPais, etDepartamento,
            etCiudad, etDireccion, etEdad;
    ImageView imgUsuario;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;
    String TAG;
    DatabaseReference ref;
    DatabaseReference usersRef;
    Uri imagenUri;

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
//Firebase
        FirebaseApp.initializeApp(this);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        usersRef = ref.child("users");
        mStorage = FirebaseStorage.getInstance().getReference();

        btnGuardar = (Button)findViewById(R.id.btnGuardarCuenta);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagenUri == null){
                    Toast.makeText(Register.this, "Seleccione una imagen", Toast.LENGTH_SHORT).show();
                    return;
                }
                crearCuenta(etEmail.getText().toString(),etContrasena.getText().toString());
                subirImagen();
                //etEdad.setText(eventsRef.child("h").getKey());
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
            imagenUri =uri;
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

    public String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public void subirImagen() {
        progressDialog.setMessage("Registrando usuario");
        progressDialog.show();
        final StorageReference filepath = mStorage.child("Photos").child(getRandomString());
        Log.i("la url de la imagen es",imagenUri.toString());
        filepath.putFile(imagenUri).addOnSuccessListener(Register.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Uri imgUri = taskSnapshot.getDownloadUrl();
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("User").setValue(etUsuario.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("password").setValue(etContrasena.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("name").setValue(etNombre.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("email").setValue(etEmail.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("phone").setValue(etCelular.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("country").setValue(etPais.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("department").setValue(etDepartamento.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("city").setValue(etCiudad.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("direction").setValue(etDireccion.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("age").setValue(etEdad.getText().toString());
                usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("photo").setValue(imgUri.toString());
                Toast.makeText(getApplicationContext(), "El suario ha sido registrado", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(Register.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void crearCuenta(String correo, String clave){

        if(correo.equals("") || clave.equals("")){
            Toast.makeText(Register.this, "Datos incompletos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // Si falla el registro, mostrar mensaje al usuario.
                        // Si es correcto, el listener para el estado de la autenticación será notificado
                        // y la lógica para manejar el usuario se puede realizar mediante el listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "fallo la creación de la cuenta", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
