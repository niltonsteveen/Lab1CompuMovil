package co.edu.udea.compumovil.gr08_20171.lab4;

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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MostrarEven.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MostrarEven#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostrarEven extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Events> eventos;
    private int selected;

    ImageView imgEvt;
    TextView tvNombreEvt;
    TextView tvFecha;
    TextView tvInfo;
    TextView tvOrg;
    TextView tvPuntuacion;
    TextView tvPais;
    TextView tvDepartamento;
    TextView tvCiudad;
    Button btnVerUbicacion;
    String ubic="";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MostrarEven() {
        // Required empty public constructor
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public List<Events> getEventos() {
        return eventos;
    }

    public void setEventos(List<Events> eventos) {
        this.eventos = eventos;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MostrarEven.
     */
    // TODO: Rename and change types and number of parameters
    public static MostrarEven newInstance(String param1, String param2) {
        MostrarEven fragment = new MostrarEven();
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

    private Bitmap byteImgToBitmap(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mostrar_even, container, false);
        imgEvt=(ImageView)view.findViewById(R.id.imgEvent);
        imgEvt.setImageBitmap(byteImgToBitmap(this.getEventos().get(this.getSelected()).getFoto()));

        tvNombreEvt=(TextView)view.findViewById(R.id.tvNombreEvent);
        tvNombreEvt.setText(this.getEventos().get(this.getSelected()).getNombre());

        tvFecha =(TextView)view.findViewById(R.id.tvFechaEvent);
        tvFecha.setText(this.getEventos().get(this.getSelected()).getFecha());

        tvInfo=(TextView)view.findViewById(R.id.tvInformacionEvent);
        tvInfo.setText(this.getEventos().get(this.getSelected()).getInformación());

        tvOrg=(TextView)view.findViewById(R.id.tvOrganizadorEvent);
        tvOrg.setText(this.getEventos().get(this.getSelected()).getOrganizador());

        tvPais=(TextView)view.findViewById(R.id.tvPais1);
        tvPais.setText(this.getEventos().get(this.getSelected()).getPais());

        tvDepartamento=(TextView)view.findViewById(R.id.tvDepartamento1);
        tvDepartamento.setText(this.getEventos().get(this.getSelected()).getDepartamento());

        tvPuntuacion=(TextView) view.findViewById(R.id.tvPuntuacionEvent);
        tvPuntuacion.setText(this.getEventos().get(this.getSelected()).getPuntuacion());

        tvCiudad=(TextView)view.findViewById(R.id.tvCiudad1);
        tvCiudad.setText(this.getEventos().get(this.getSelected()).getCiudad());

        btnVerUbicacion=(Button)view.findViewById(R.id.btnUbicacion);
        ubic=(this.getEventos().get(this.getSelected()).getLugar());

        ubic= this.getEventos().get(this.getSelected()).getLugar()+", "+
                this.getEventos().get(this.getSelected()).getCiudad()+", "+
                this.getEventos().get(this.getSelected()).getDepartamento()+", "+
                this.getEventos().get(this.getSelected()).getPais();


        btnVerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q= "+ubic);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }});


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
}
