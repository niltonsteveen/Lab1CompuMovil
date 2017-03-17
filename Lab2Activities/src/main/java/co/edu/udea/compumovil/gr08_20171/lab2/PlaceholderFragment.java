package co.edu.udea.compumovil.gr08_20171.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento para el contenido principal
 */
public class PlaceholderFragment extends Fragment {
    /**
     * Este argumento del fragmento representa el título de cada
     * sección
     */
    public static final String ARG_SECTION_TITLE = "section_number";
    List<Events> items;
    controladorBD1 controlBD1=new controladorBD1(getContext());
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private MainActivity mainActivity;
    private TextView tvNombre, tvEmail, tvCelular, tvPais, tvDepartamento, tvCiudad, tvDireccion,
            tvEdad, tvNombreMenu, tvEmailMenu;
    private ImageView imgPerfil,imgPerfilMenu;
    private String nombre, email, celular, pais, departamento, ciudad, direccion, edad;
    private byte[] foto;
    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity, String nombre, String email, String celular,
                                String pais, String departamento,String ciudad, String direccion, String edad, byte[] foto) {
        this.mainActivity = mainActivity;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.pais = pais;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.edad = edad;
        this.foto = foto;

    }


    /**
     * Crea una instancia prefabricada de {@link PlaceholderFragment}
     *
     * @param sectionTitle Título usado en el contenido
     * @return Instancia dle fragmento
     */
    public static PlaceholderFragment newInstance(String sectionTitle) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        String title = getArguments().getString(ARG_SECTION_TITLE);
        View view = null;
        items = new ArrayList<>();
        if(title.equals("Eventos")){
            view = inflater.inflate(R.layout.fragment_event, container, false);
            SQLiteDatabase db = controlBD1.getWritableDatabase();
            Cursor cursor=db.rawQuery("select * from"+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);
            Events evt=null;
            while(cursor.moveToNext()){
                String nombre=cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE));
                String informacion=cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION));
                String puntuacion=cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION));
                byte[] foto=cursor.getBlob(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FOTO));

                evt.setFoto(foto);
                evt.setNombre(nombre);
                evt.setInformación(informacion);
                evt.setPuntuacion(puntuacion);

                items.add(evt);
            }
            // Obtener el Recycler
            recycler = (RecyclerView) view.findViewById(R.id.reciclador);
            recycler.setHasFixedSize(true);

            // Usar un administrador para LinearLayout
            lManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(lManager);

            // Crear un nuevo adaptador
            adapter = new EventsAdapter(items);
            recycler.setAdapter(adapter);
        }else if(title.equals("Perfil")){
            view = inflater.inflate(R.layout.fragment_perfil, container, false);
            tvNombre = (TextView) view.findViewById(R.id.tvNombrePerfil);
            tvNombre.setText(nombre);
            tvEmail = (TextView) view.findViewById(R.id.tvEmailPerfil);
            tvEmail.setText(email);
            tvCelular = (TextView) view.findViewById(R.id.tvCelularPerfil);
            tvCelular.setText(celular);
            tvPais = (TextView) view.findViewById(R.id.tvPaisPerfil);
            tvPais.setText(pais);
            tvDepartamento = (TextView) view.findViewById(R.id.tvDepartementoPerfil);
            tvDepartamento.setText(departamento);
            tvCiudad = (TextView) view.findViewById(R.id.tvCiudadPerfil);
            tvCiudad.setText(ciudad);
            tvDireccion = (TextView) view.findViewById(R.id.tvDireccionPerfil);
            tvDireccion.setText(direccion);
            tvEdad = (TextView) view.findViewById(R.id.tvEdadPerfil);
            tvEdad.setText(edad);
            imgPerfil = (ImageView) view.findViewById(R.id.imgPerfil);
            imgPerfil.setImageBitmap(byteImgToBitmap(foto));
        }else if(title.equals("Cerrar sesión")){
            Intent returnLogin = new Intent(mainActivity,LoginActivity.class);
            returnLogin.putExtra("sali","cerrar");
            startActivity(returnLogin);
        }
        return view;
    }

    private Bitmap byteImgToBitmap(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return bitmap;
    }

}
