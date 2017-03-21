package co.edu.udea.compumovil.gr08_20171.lab2;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link crearEvento.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link crearEvento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class crearEvento extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText etNombre,etFecha,etInformacion,etOrganizador,etPais,etDepartamento,etCiudad,etLugar,etPuntuacion;
    ImageView imgEvento;
    controladorBD1 controlBD1;
    Button btnGuardar;
    Button cambiarFoto;
    List<Events> listaEventos;
    final int REQUEST_CODE_GALLERY = 999;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public crearEvento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment crearEvento.
     */
    // TODO: Rename and change types and number of parameters
    public static crearEvento newInstance(String param1, String param2) {
        crearEvento fragment = new crearEvento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_evento, container, false);
        etNombre = (EditText)view.findViewById(R.id.etNombreEven);
        etFecha = (EditText)view.findViewById(R.id.etFechaEven);
        etInformacion = (EditText)view.findViewById(R.id.etInfoEven);
        etOrganizador = (EditText)view.findViewById(R.id.etOrganizadorEven);
        etPais = (EditText)view.findViewById(R.id.etPaisEven);
        etDepartamento = (EditText)view.findViewById(R.id.etDepartamentoEven);
        etCiudad = (EditText)view.findViewById(R.id.etCiudadEven);
        etLugar = (EditText)view.findViewById(R.id.etLugarEven);
        etPuntuacion = (EditText)view.findViewById(R.id.etPuntuacionEven);
        imgEvento = (ImageView)view.findViewById(R.id.img_evenPerfil);
        controlBD1 = new controladorBD1(container.getContext());


        cambiarFoto = (Button)view.findViewById(R.id.btnCambiarFotoEvem);


        btnGuardar = (Button)view.findViewById(R.id.btnGuardarEven);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = controlBD1.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE, etNombre.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_FECHA, etFecha.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION, etInformacion.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR, etOrganizador.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_PAIS, etPais.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO, etDepartamento.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD, etCiudad.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_LUGAR, etLugar.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION, etPuntuacion.getText().toString());
                valores.put(controladorBD1.DatosTablaEvent.COLUMN_FOTO,imageViewToByte(imgEvento));

                Long emailGuardado = db.insert(controladorBD1.DatosTablaEvent.NOMBRE_TABLA,
                        controladorBD1.DatosTablaEvent.COLUMN_ID,valores);
                Toast.makeText(container.getContext(),"Se guardo el evento"+emailGuardado, Toast.LENGTH_LONG).show();
                consultarEvents();
                Fragment fragment = null;
                eventos even = new eventos();
                even.setLista(listaEventos);
                fragment = even;
                getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
            }
        });
        return view;
    }

    private void consultarEvents() {
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);

        listaEventos = new ArrayList<Events>();
        while (cursor.moveToNext()) {
            Events evt= new Events();
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
            evt.setFecha(fecha);
            evt.setInformación(informacion);
            evt.setOrganizador(organizador);
            evt.setPais(pais);
            evt.setDepartamento(departamento);
            evt.setCiudad(ciudad);
            evt.setLugar(lugar);
            evt.setPuntuacion(puntuacion);
            evt.setFoto(foto);

            listaEventos.add(evt);
        }
       /*
            Fragment fragment = null;
            crearEvento frag = new crearEvento();
            fragment = frag;
            getFragmentManager().beginTransaction().replace(R.id.main_content,fragment).commit();*/

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private byte[] imageViewToByte(ImageView imgUsuario) {
        Bitmap bitmap = ((BitmapDrawable)imgUsuario.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
