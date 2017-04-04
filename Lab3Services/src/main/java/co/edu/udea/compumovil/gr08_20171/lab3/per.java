package co.edu.udea.compumovil.gr08_20171.lab3;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link per.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link per#newInstance} factory method to
 * create an instance of this fragment.
 */
public class per extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView tvNombre, tvEmail, tvCelular, tvPais, tvDepartamento, tvCiudad, tvDireccion,
            tvEdad, tvNombreMenu, tvEmailMenu;
    Button btnActualizar;
    private ImageView imgPerfil;
    private String usuario, pass, nombre, email, celular, pais, departamento, ciudad, direccion, edad;
    private byte[] foto;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public per() {
        // Required empty public constructor
    }


    public void setPerfil( String usuario, String pass, String nombre, String email, String celular,
                                String pais, String departamento,String ciudad, String direccion, String edad, byte[] foto) {

        this.usuario = usuario;
        this.pass = pass;
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
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment per.
     */
    // TODO: Rename and change types and number of parameters
    public static per newInstance(String param1, String param2) {
        per fragment = new per();
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
        final Object[] datos = new Object[11];
        View view = inflater.inflate(R.layout.fragment_per, container, false);
        datos[0] = usuario;
        datos[1] = pass;
        tvNombre = (TextView) view.findViewById(R.id.tvNombrePerfil);
        tvNombre.setText(nombre);
        datos[2] = nombre;
        tvEmail = (TextView) view.findViewById(R.id.tvEmailPerfil);
        tvEmail.setText(email);
        datos[3] = email;
        tvCelular = (TextView) view.findViewById(R.id.tvCelularPerfil);
        tvCelular.setText(celular);
        datos[4] = celular;
        tvPais = (TextView) view.findViewById(R.id.tvPaisPerfil);
        tvPais.setText(pais);
        datos[5] = pais;
        tvDepartamento = (TextView) view.findViewById(R.id.tvDepartementoPerfil);
        tvDepartamento.setText(departamento);
        datos[6] = departamento;
        tvCiudad = (TextView) view.findViewById(R.id.tvCiudadPerfil);
        tvCiudad.setText(ciudad);
        datos[7] = ciudad;
        tvDireccion = (TextView) view.findViewById(R.id.tvDireccionPerfil);
        tvDireccion.setText(direccion);
        datos[8] = direccion;
        tvEdad = (TextView) view.findViewById(R.id.tvEdadPerfil);
        tvEdad.setText(edad);
        datos[9] = edad;
        imgPerfil = (ImageView) view.findViewById(R.id.imgPerfil);
        imgPerfil.setImageBitmap(byteImgToBitmap(foto));
        datos[10] = byteImgToBitmap(foto);
        btnActualizar = (Button)view.findViewById(R.id.btnEditPerfil);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(container.getContext(),EditarPerfil.class);
                intent.putExtra("userCorreo",datos);
                startActivity(intent);
            }
        });
        return view;
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

    private Bitmap byteImgToBitmap(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return bitmap;
    }
}
