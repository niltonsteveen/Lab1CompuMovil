package co.edu.udea.compumovil.gr08_20171.lab2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    controladorBD2 controlBD2=new controladorBD2(getContext());
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=null;
        String title = getArguments().getString(ARG_SECTION_TITLE);
        items = new ArrayList<>();
        if(title.equals("Eventos")){
            view = inflater.inflate(R.layout.fragment_event, container, false);
            SQLiteDatabase db = controlBD2.getWritableDatabase();
            Cursor cursor=db.rawQuery("select * from"+ controladorBD2.DatosTablaEvent.NOMBRE_TABLA,null);
            Events evt=null;
            while(cursor.moveToNext()){
                String nombre=cursor.getString(cursor.getColumnIndex(controladorBD2.DatosTablaEvent.COLUMN_NOMBRE));
                String informacion=cursor.getString(cursor.getColumnIndex(controladorBD2.DatosTablaEvent.COLUMN_INFORMACION));
                String puntuacion=cursor.getString(cursor.getColumnIndex(controladorBD2.DatosTablaEvent.COLUMN_PUNTUACION));
                byte[] foto=cursor.getBlob(cursor.getColumnIndex(controladorBD2.DatosTablaEvent.COLUMN_FOTO));

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
            TextView nombrePerf = (TextView) view.findViewById(R.id.nombrePerfil);
            nombrePerf.setText("nilton steveen");
        }
        return view;
    }

}
