package co.edu.udea.compumovil.gr08_20171.lab2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nilto on 14/03/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {
    private List<Events> items;

    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;
        public TextView puntuacion;

        public EventsViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            descripcion = (TextView) v.findViewById(R.id.descripción);
            puntuacion=(TextView) v.findViewById(R.id.puntuacion);
        }
    }

    public EventsAdapter(List<Events> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_card, viewGroup, false);
        return new EventsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageBitmap(byteImgToBitmap(items.get(i).getFoto()));
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.descripcion.setText(items.get(i).getInformación());
        viewHolder.puntuacion.setText(items.get(i).getPuntuacion());
    }

    public Bitmap byteImgToBitmap(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return bitmap;
    }
}