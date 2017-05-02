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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

public class EditarPerfil extends AppCompatActivity {

    Button btnActulizar,btnCambiarImg;
    EditText etUsuario, etContrasena, etNombre, etEmail, etCelular, etPais, etDepartamento,
            etCiudad, etDireccion, etEdad;
    ImageView imgUsuario;
    Object[] datos;
    Bundle bundle;
    boolean cambioFoto;
    final int REQUEST_CODE_GALLERY = 999;
    ProgressDialog progressDialog;
    StorageReference mStorage;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    DatabaseReference usersRef;
    Uri imagenUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        cambioFoto = false;
        bundle = getIntent().getExtras();
        datos = (Object[]) bundle.get("userCorreo");
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
        imgUsuario.setImageBitmap(byteImgToBitmap((byte[])datos[10]));
        //Firebase
        FirebaseApp.initializeApp(this);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        usersRef = ref.child("users");
        mStorage = FirebaseStorage.getInstance().getReference();

        btnActulizar = (Button)findViewById(R.id.btnActuPerfil);
        btnActulizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarFotoUser();
            }
        });

        btnCambiarImg = (Button)findViewById(R.id.btnCambiarFotoPerfil);

        imgUsuario.setOnClickListener(new View.OnClickListener() {
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
            imagenUri =uri;
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgUsuario.setImageBitmap(bitmap);
                cambioFoto = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap byteImgToBitmap(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return bitmap;
    }

    public String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public void actualizarUser() {
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
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "El suario ha sido registrado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void actualizarFotoUser() {
        progressDialog.setMessage("Actualizando usuario");
        progressDialog.show();
        if(cambioFoto) {
            usersRef.child(mAuth.getCurrentUser().getUid()).child("photo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String urlImgLast = dataSnapshot.getValue().toString();
                    Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(urlImgLast).delete();
                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(EditarPerfil.this, "Se pudo actualizar la foto", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(EditarPerfil.this, "Fallo la actulización de foto", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    usersRef.child(mAuth.getCurrentUser().getUid()).child("photo").removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            final StorageReference filepath = mStorage.child("Photos").child(getRandomString());
            Log.i("la url de la imagen es", imagenUri.toString());
            filepath.putFile(imagenUri).addOnSuccessListener(EditarPerfil.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri imgUri = taskSnapshot.getDownloadUrl();
                    Log.i("imagen en firebase es", imgUri.toString());
                    usersRef.child(mAuth.getCurrentUser().getUid())
                            .child("photo").setValue(imgUri.toString());
                    actualizarUser();
                }
            }).addOnFailureListener(EditarPerfil.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditarPerfil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            actualizarUser();
        }
    }
}
