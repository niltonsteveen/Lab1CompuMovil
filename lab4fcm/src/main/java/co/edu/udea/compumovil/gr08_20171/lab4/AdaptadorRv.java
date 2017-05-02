package co.edu.udea.compumovil.gr08_20171.lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nilto on 14/03/2017.
 */

public class AdaptadorRv
        extends RecyclerView.Adapter<AdaptadorRv.EventsViewHolder>
        implements View.OnClickListener {

    Context context;
    private View.OnClickListener listener;
    private List<Events> listaEvents;

    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView tvNombre,tvFecha,tvInformacion,tvOrganizador,tvPais,tvDepartamento,tvCiudad,
                tvLugar,tvPuntuacion;

        public EventsViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagenEventWidget);
            tvNombre = (TextView) v.findViewById(R.id.nombreEventWidget);

            tvInformacion = (TextView) v.findViewById(R.id.informacionEventWidget);
            tvPuntuacion=(TextView) v.findViewById(R.id.puntuacionEventWidget);
        }
    }

    public AdaptadorRv(List<Events> items) {
        this.listaEvents = items;
    }

    @Override
    public int getItemCount() {
        return listaEvents.size();
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vistarv, viewGroup, false);
        v.setOnClickListener(this);
        context = viewGroup.getContext();
        return new EventsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageBitmap(byteImgToBitmap(listaEvents.get(i).getFoto()));
        Picasso.with(context).load("https://firebasestorage.googleapis.com/v0/b/laboratorio-4.appspot.com/o/Events%2Fcn3lu3g40m4r64fb9dtofg2bn8?alt=media&token=6ddc7514-0ebc-4df3-aace-421c13a8cf27").into(viewHolder.imagen);

        viewHolder.tvNombre.setText(listaEvents.get(i).getNombre());
       // viewHolder.tvFecha.setText(listaEvents.get(i).getFecha());
        viewHolder.tvInformacion.setText(listaEvents.get(i).getInformación());
       /* viewHolder.tvOrganizador.setText(listaEvents.get(i).getOrganizador());
        viewHolder.tvPais.setText(listaEvents.get(i).getPais());
        viewHolder.tvDepartamento.setText(listaEvents.get(i).getDepartamento());
        viewHolder.tvCiudad.setText(listaEvents.get(i).getCiudad());
        viewHolder.tvLugar.setText(listaEvents.get(i).getLugar());*/
        viewHolder.tvPuntuacion.setText(listaEvents.get(i).getPuntuacion());
    }

    public Bitmap byteImgToBitmap(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return bitmap;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}