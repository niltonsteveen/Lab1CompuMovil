package co.edu.udea.compumovil.gr08_20171.lab2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private Bundle bundle;
    private String usuarioEmail;
    private String usuario, clave, nombre, celular, pais, departamento, ciudad, direccion, edad;
    private TextView letterName;
    private CircleImageView imgPerfilCir;
    private byte[] foto;
    controladorBD1 controlBD1;
    List<Events> listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controlBD1=new controladorBD1(getApplicationContext());
        setToolbar(); // Setear Toolbar como action bar
        imgPerfilCir=(CircleImageView)findViewById(R.id.imgPerfilCir);
        bundle = getIntent().getExtras();
        usuarioEmail = bundle.getString("user").toString();
        bundle = getIntent().getExtras();
        consultarUser();
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
                getFragmentManager().beginTransaction().replace(R.id.main_content, fragmentPreferenceConfi.newInstance()).commit();
                break;
            case "Eventos":
                consultarEvents();
                eventos even = new eventos();
                even.setLista(listaEventos);
                fragment = even;
                getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                break;
            case "Perfil":
                per frag = new per();
                frag.setPerfil(nombre,usuarioEmail,celular,pais,departamento,ciudad,direccion,edad,foto);
                fragment = frag;
                getFragmentManager().beginTransaction().replace(R.id.main_content,fragment).commit();
                break;
            case "Cerrar sesión":
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra("sali","cerrar");
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
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);
        Events evt= new Events();
        listaEventos = new ArrayList<Events>();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
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
                evt.setInformación("Descripción: "+informacion);
                evt.setPuntuacion("Puntuación: "+puntuacion);
                evt.setFoto(foto);

                listaEventos.add(evt);
            }
        }
       /*
            Fragment fragment = null;
            crearEvento frag = new crearEvento();
            fragment = frag;
            getFragmentManager().beginTransaction().replace(R.id.main_content,fragment).commit();*/

    }

    private void consultarUser()
    {
        SQLiteDatabase db = controlBD1.getReadableDatabase();
        String[] arqsel = {usuarioEmail};
        String[] projection = {
                controladorBD1.DatosTablaUser.COLUMN_USUARIO,
                controladorBD1.DatosTablaUser.COLUMN_CONTRASENA,
                controladorBD1.DatosTablaUser.COLUMN_NOMBRE,
                controladorBD1.DatosTablaUser.COLUMN_EMAIL,
                controladorBD1.DatosTablaUser.COLUMN_CELULAR,
                controladorBD1.DatosTablaUser.COLUMN_PAIS,
                controladorBD1.DatosTablaUser.COLUMN_DEPARTAMENTO,
                controladorBD1.DatosTablaUser.COLUMN_CIUDAD,
                controladorBD1.DatosTablaUser.COLUMN_DIRECCION,
                controladorBD1.DatosTablaUser.COLUMN_EDAD,
                controladorBD1.DatosTablaUser.COLUMN_FOTO
        };
        Cursor c = db.query(
                controladorBD1.DatosTablaUser.NOMBRE_TABLA,
                projection,
                controladorBD1.DatosTablaUser.COLUMN_EMAIL+"=?",
                arqsel,         // The values for the WHERE clause
                null,           // don't group the rows
                null,           // don't filter by row groups
                null            // The sort order
        );

        c.moveToFirst();

        usuario = c.getString(0);
        clave = c.getString(1);
        nombre = c.getString(2);
        celular = c.getString(4);
        pais = c.getString(5);
        departamento = c.getString(6);
        ciudad = c.getString(7);
        direccion = c.getString(8);
        edad = c.getString(9);
        foto = c.getBlob(10);
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

    public static class fragmentPreferenceConfi extends PreferenceFragment {
        public static fragmentPreferenceConfi newInstance() {

            Bundle args = new Bundle();

            fragmentPreferenceConfi fragment = new fragmentPreferenceConfi();
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.f_configuracion);
        }
    }

}
