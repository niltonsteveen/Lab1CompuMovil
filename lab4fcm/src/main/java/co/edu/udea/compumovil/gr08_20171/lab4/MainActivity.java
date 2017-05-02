package co.edu.udea.compumovil.gr08_20171.lab4;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private String[][] events;
    private Bitmap img;
    private Bitmap imgInicial;
    private ProgressDialog progressDialog;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private Bundle bundle;
    private String usuario, clave, nombre, email, celular, pais, departamento, ciudad, direccion, edad;
    private TextView letterName;
    private CircleImageView imgPerfilCir;
    private byte[] foto;
    int tiempo;
    Context context = this;
    controladorBD1 controlBD1;
    List<Events> listaEventos;
    List<Events> listaEventos1;
    SharedPreferences sharpref;
    private FirebaseAuth mAuth;
    private String TAG;
    private FirebaseUser user;
    DatabaseReference ref;
    DatabaseReference userRef;
    DatabaseReference eventsRef;
    private TextView tvEmail, tvUsername;
    boolean entre = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando datos");
        progressDialog.show();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String temp = pref.getString("tiempoActu","60 s");

        if(temp.length()<5){
            tiempo = Integer.parseInt(temp.substring(0,2));
        }
        else{
            tiempo = Integer.parseInt(temp.substring(0,3));
        }
       // time time = new time();
      //  time.execute(tiempo);

        controlBD1=new controladorBD1(getApplicationContext());

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("users");
        userRef = ref.child(user.getUid());
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        Log.i("Usuario actual:", user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("El cambio ocurrio en:", dataSnapshot.getKey().intern());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvUsername = (TextView)findViewById(R.id.tvUsernameMenu);
                tvEmail = (TextView)findViewById(R.id.tvEmailMenu);
                usuario = dataSnapshot.child("User").getValue().toString();
                tvUsername.setText(usuario);
                clave = dataSnapshot.child("password").getValue().toString();
                nombre = dataSnapshot.child("name").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                tvEmail.setText(email);
                celular = dataSnapshot.child("phone").getValue().toString();
                pais = dataSnapshot.child("country").getValue().toString();
                departamento = dataSnapshot.child("department").getValue().toString();
                ciudad = dataSnapshot.child("city").getValue().toString();
                direccion = dataSnapshot.child("direction").getValue().toString();
                edad = dataSnapshot.child("age").getValue().toString();
                imgPerfilCir=(CircleImageView)findViewById(R.id.imgPerfilCir);
                Picasso.with(MainActivity.this)
                        .load(Uri.parse(dataSnapshot.child("photo").getValue().toString()))
                        .placeholder(R.drawable.perfil1)
                        .into(imgPerfilCir);
                if(!entre){
                    img = ((BitmapDrawable)imgPerfilCir.getDrawable()).getBitmap();
                    entre = true;
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setToolbar(); // Setear Toolbar como action bar
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        drawerTitle = getResources().getString(R.string.Eventos);
        if (savedInstanceState == null) {
            // Seleccionar item
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Marcar item presionado
                menuItem.setChecked(true);
                // Crear nuevo fragmento

                String title = menuItem.getTitle().toString();
                selectItem(title);
                return true;
            }
        });
    }

    private void selectItem(String title) {

        Fragment fragment = null;
        switch(title) {
            case "Configuraciones":
                getFragmentManager().beginTransaction().replace(R.id.main_content, new PreFragConf()).commit();
                break;
            case "Eventos":
                creaVecDeIds();
                ObtenerEvents();
                consultarEvents();
                eventos even = new eventos();
                even.setLista(listaEventos);
                even.setListaEventos1(listaEventos1);
                fragment = even;
                getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                break;
            case "Perfil":
                per frag = new per();
                if(!((BitmapDrawable)imgPerfilCir.getDrawable()).getBitmap().equals(img)&&imgPerfilCir != null) {
                    foto = imageViewToByte(imgPerfilCir);
                    frag.setPerfil(usuario, clave, nombre, email, celular, pais, departamento, ciudad, direccion, edad, foto);
                    fragment = frag;
                    getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Cargando foto, intentelo más tarde", Toast.LENGTH_LONG).show();
                }
                break;
            case "Cerrar sesión":
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("sesion_activa",false);
                editor.commit();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Acerca de":
                AcerqueDe fragm = new AcerqueDe();
                fragment = fragm;
                getFragmentManager().beginTransaction().replace(R.id.main_content,fragment).commit();
                break;
        }
        setTitle(title); // Setear título actual
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void consultarEvents() {
        ImageView aux = new ImageView(getApplicationContext());
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);
        Events evt;
        listaEventos = new ArrayList<Events>();
        listaEventos1 = new ArrayList<Events>();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                evt= new Events();
                String nombre = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE));
                String fecha = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FECHA));
                String informacion = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION));
                String organizador = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR));
                String pais = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PAIS));
                String departamento = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO));
                String ciudad = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD));
                String lugar = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_LUGAR));
                String puntuacion = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION));
                String url = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FOTO));

                Picasso.with(MainActivity.this)
                        .load(Uri.parse(url))
                        .placeholder(R.drawable.perfil1)
                        .into(aux);

                byte[] foto = imageViewToByte(aux);

                evt.setNombre(nombre);
                evt.setInformación("Descripción: "+informacion);
                evt.setPuntuacion("Puntuación: "+puntuacion);
                evt.setFoto(foto);

                listaEventos.add(evt);

                evt.setNombre(nombre);
                evt.setInformación(informacion);
                evt.setPuntuacion(puntuacion);
                evt.setFoto(foto);
                evt.setFecha(fecha);
                evt.setOrganizador(organizador);
                evt.setPais(pais);
                evt.setDepartamento(departamento);
                evt.setCiudad(ciudad);
                evt.setLugar(lugar);
                listaEventos1.add(evt);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hilo(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void volveraIniciar(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String temp = pref.getString("tiempoActu","60 s");
        if(temp.length()<5){
            tiempo = Integer.parseInt(temp.substring(0,2));
        }
        else{
            tiempo = Integer.parseInt(temp.substring(0,3));

        }
        Log.i("tiempo actu",tiempo+"");
        time time = new time();
        time.execute(tiempo);
    }

    public class time extends AsyncTask<Integer,Integer,Boolean>
    {

        @Override
        protected Boolean doInBackground(Integer... params) {

            int tiemp = params[0]/60;
            for(int i = 0; i <= tiemp; i++){
                hilo();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ActualizarBD actualizarBD = new ActualizarBD(context);
            volveraIniciar();
            Toast.makeText(getApplicationContext(), "Se actualizo los eventos", Toast.LENGTH_LONG).show();
        }
    }

    private byte[] imageViewToByte(ImageView imgUsuario) {
        Bitmap bitmap = ((BitmapDrawable)imgUsuario.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    private void creaVecDeIds() {
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);
        events = new String[cursor.getCount()][11];
        int cont = 0;
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                events[cont][0] = cursor.getInt(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ID))+"";
                events[cont][1] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE));
                events[cont][2] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FECHA));
                events[cont][3] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION));
                events[cont][4] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR));
                events[cont][5] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PAIS));
                events[cont][6] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO));
                events[cont][7] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD));
                events[cont][8] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_LUGAR));
                events[cont][9] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION));
                events[cont][10] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FOTO));
                cont++;
            }
        }
    }

    public boolean verificaID(int id){
        boolean result = false;
        for(int i = 0;i<events.length;i++ ){
            if(events[i][0].equals(id+"")){
                result = true;
            }
        }
        return result;
    }

    public void ObtenerEvents(){
        Log.i("guardado ",  "Ando en actulizar eventos");
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    if(!item.getKey().toString().equals("idmayor")) {
                        if (!verificaID(Integer.parseInt(item.getKey().toString()))) {
                            SQLiteDatabase db = controlBD1.getWritableDatabase();
                            ContentValues valores = new ContentValues();
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_ID, item.getKey().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE, item.child("name").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_FECHA, item.child("date").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION, item.child("information").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR, item.child("organizer").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_PAIS, item.child("country").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO, item.child("department").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD, item.child("city").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_LUGAR, item.child("place").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION, item.child("puntuation").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_FOTO, item.child("photo").getValue().toString());

                            Long eventoGuardado = db.insert(controladorBD1.DatosTablaEvent.NOMBRE_TABLA,
                                    controladorBD1.DatosTablaEvent.COLUMN_ID, valores);
                            Log.i("guardado ", eventoGuardado + "");
                        } else {

                        }
                        Log.i("guardado ", "repetido");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
