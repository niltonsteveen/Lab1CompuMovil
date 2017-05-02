package co.edu.udea.compumovil.gr08_20171.lab4;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ActualizarBD {

    controladorBD1 controlBD1;
    Context context;
    String[][] events;
    private FirebaseAuth mAuth;
    private String TAG;
    DatabaseReference ref;
    DatabaseReference eventsRef;
    boolean ter;

    public ActualizarBD(Context context) {
        controlBD1 = new controladorBD1(context);
        creaVecDeIds();
        FirebaseApp.initializeApp(context);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("events");
        this.context = context;
        ter = false;
        ObtenerEvents();
    }

    private void creaVecDeIds() {
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);
        events = new String[cursor.getCount()][11];
        int cont = 0;
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                events[cont][0] = cursor.getInt(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ID))+"";
                events[cont][1] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE));
                events[cont][2] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FECHA));
                events[cont][3] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION));
                events[cont][4] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR));
                events[cont][5] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PAIS));
                events[cont][6] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO));
                events[cont][7] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD));
                events[cont][8] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_LUGAR));
                events[cont][9] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION));
                events[cont][10] = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FOTO));
                cont++;
            }
        }
    }

    public boolean verificaID(int id){
        boolean result = false;
        for(int i = 0;i<events.length;i++ ){
            if(events[i][0].equals(id+"")){
                result = true;
            }
        }
        return result;
    }

    public void ObtenerEvents(){
        Log.i("guardado ",  "Ando en actulizar eventos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    if(!item.getKey().toString().equals("idmayor")) {
                        if (!verificaID(Integer.parseInt(item.getKey().toString()))) {
                            SQLiteDatabase db = controlBD1.getWritableDatabase();
                            ContentValues valores = new ContentValues();
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_ID, item.getKey().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE, item.child("name").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_FECHA, item.child("date").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION, item.child("information").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR, item.child("organizer").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_PAIS, item.child("country").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO, item.child("department").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD, item.child("city").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_LUGAR, item.child("place").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION, item.child("puntuation").getValue().toString());
                            valores.put(controladorBD1.DatosTablaEvent.COLUMN_FOTO, item.child("photo").getValue().toString());

                            Long eventoGuardado = db.insert(controladorBD1.DatosTablaEvent.NOMBRE_TABLA,
                                    controladorBD1.DatosTablaEvent.COLUMN_ID, valores);
                            Log.i("guardado ", eventoGuardado + "");
                        } else {

                        }
                        Log.i("guardado ", "repetido");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public boolean termino(){
        return ter;
    }

}
