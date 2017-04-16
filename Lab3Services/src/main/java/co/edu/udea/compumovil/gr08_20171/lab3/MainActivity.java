package co.edu.udea.compumovil.gr08_20171.lab3;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private Bundle bundle;
    private String[] usuarioDatos;
    private String usuario, clave, nombre, email, celular, pais, departamento, ciudad, direccion, edad;
    private TextView letterName;
    private CircleImageView imgPerfilCir;
    private byte[] foto;
   // controladorBD1 controlBD1;
    List<Events> listaEventos;
    List<Events> listaEventos1;
    SharedPreferences sharpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // controlBD1=new controladorBD1(getApplicationContext());
        setToolbar(); // Setear Toolbar como action bar
        imgPerfilCir=(CircleImageView)findViewById(R.id.imgPerfilCir);
        bundle = getIntent().getExtras();
        usuarioDatos = bundle.getStringArray("datos");
        actualizarDatos();

        sharpref = getSharedPreferences("Preferent",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharpref.edit();
        editor.putString("correoUser",email);
        editor.commit();

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
                consultarEvents();
                eventos even = new eventos();
                even.setLista(listaEventos);
                even.setListaEventos1(listaEventos1);
                fragment = even;
                getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                break;
            case "Perfil":
                per frag = new per();
                frag.setPerfil(usuario,clave,nombre,email,celular,pais,departamento,ciudad,direccion,edad,foto);
                fragment = frag;
                getFragmentManager().beginTransaction().replace(R.id.main_content,fragment).commit();
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

    private void actualizarDatos(){
        usuario = usuarioDatos[0];
        clave = usuarioDatos[1];
        nombre = usuarioDatos[2];
        email = usuarioDatos[3];
        celular = usuarioDatos[4];
        pais = usuarioDatos[5];
        departamento = usuarioDatos[6];
        ciudad = usuarioDatos[7];
        direccion = usuarioDatos[8];
        edad = usuarioDatos[9];
        foto = Base64.decode(usuarioDatos[10],Base64.DEFAULT);
    }

    private void consultarEvents() {
       /* SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);
        Events evt= new Events();
        listaEventos = new ArrayList<Events>();
        listaEventos1 = new ArrayList<Events>();
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
       /*
            Fragment fragment = null;
            crearEvento frag = new crearEvento();
            fragment = frag;
            getFragmentManager().beginTransaction().replace(R.id.main_content,fragment).commit();*/

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


}
