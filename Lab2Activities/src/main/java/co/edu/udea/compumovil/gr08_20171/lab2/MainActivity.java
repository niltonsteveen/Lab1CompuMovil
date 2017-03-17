package co.edu.udea.compumovil.gr08_20171.lab2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private Bundle bundle;
    private String usuarioEmail;
    private String usuario, clave, nombre, celular, pais, departamento, ciudad, direccion, edad;
    private byte[] foto;
    controladorBD1 controlBD1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(); // Setear Toolbar como action bar
        bundle = getIntent().getExtras();
        usuarioEmail = bundle.getString("user").toString();
        consultarUser();
        bundle = getIntent().getExtras();
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
        // Enviar título como arguemento del fragmento
        Bundle args = new Bundle();
        args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);
        PlaceholderFragment fragment = PlaceholderFragment.newInstance(title);
        fragment.setMainActivity(this,nombre,usuarioEmail,celular,pais,departamento,ciudad,direccion,edad,foto);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
        drawerLayout.closeDrawers(); // Cerrar drawer
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

    private void consultarUser()
    {
        controlBD1=new controladorBD1(getApplicationContext());
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
}
