package co.edu.udea.compumovil.gr08_20171.lab3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ActualizarBD {

    controladorBD1 controlBD1;
    Context context;
    int[] ids;
    boolean ter;

    public ActualizarBD(Context context) {
        controlBD1 = new controladorBD1(context);
        creaVecDeIds();
        ObtenerEvents obtenerEvents = new ObtenerEvents();
        obtenerEvents.execute();
        this.context = context;
        ter = false;
    }

    private void creaVecDeIds() {
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT id FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA,null);
        ids = new int[cursor.getCount()];
        int cont = 0;
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                ids[cont] = cursor.getInt(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ID));
                cont++;
            }
        }
    }

    public boolean verificaID(int id){
        boolean result = false;
        for(int i = 0;i<ids.length;i++ ){
            if(ids[i]==id){
                result = true;
            }
        }
        return result;
    }

    public class ObtenerEvents extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params){
            Log.i("ConsultaEvents","doInBackground");
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet get = new HttpGet("https://apirest-eventos.herokuapp.com/allEvents/");
            get.setHeader("Content-type","application/json");

            try{

                HttpResponse resp = httpClient.execute(get);
                String respString = EntityUtils.toString(resp.getEntity());

                JSONArray arrayJSON = new JSONArray(respString);
                JSONObject respJSON;
                Log.i("cantidad ",arrayJSON.length()+"");

                for(int i = 0; i<arrayJSON.length();i++){
                    respJSON = arrayJSON.getJSONObject(i);
                    if(!verificaID(respJSON.getInt("idEvent"))) {
                        SQLiteDatabase db = controlBD1.getWritableDatabase();
                        ContentValues valores = new ContentValues();
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_ID, respJSON.getInt("idEvent"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE, respJSON.getString("name"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_FECHA, respJSON.getString("date"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION, respJSON.getString("information"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR, respJSON.getString("organizator"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_PAIS, respJSON.getString("country"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO, respJSON.getString("department"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD, respJSON.getString("city"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_LUGAR, respJSON.getString("place"));
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION, respJSON.getInt("puntuation") + "");
                        valores.put(controladorBD1.DatosTablaEvent.COLUMN_FOTO, Base64.decode(respJSON.getString("photo"), Base64.DEFAULT));

                        Long eventoGuardado = db.insert(controladorBD1.DatosTablaEvent.NOMBRE_TABLA,
                                controladorBD1.DatosTablaEvent.COLUMN_ID, valores);
                        Log.i("guardado ", eventoGuardado + "");
                    }
                    else{
                        Log.i("guardado ", "repetido");

                    }
                }

            }
            catch (Exception ex)
            {
                Log.e("ServicioRest","Error!",ex);
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.i("ServicioRest","onPostExecute");
            ter = true;
        }

        @Override
        protected void onPreExecute(){
            Log.i("ServicioRest","onPreExecute");
        }

    }

    public boolean termino(){
        return ter;
    }

}
