package co.edu.udea.compumovil.gr08_20171.lab3;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        String nombre = "";
        String informacion = "";
        String puntuacion = "";
        byte[] imagen = new byte[0];

       // ActualizarBD actualizarBD = new ActualizarBD(context);
        controladorBD1 controlBD1 = new controladorBD1(context);
        SQLiteDatabase db = controlBD1.getWritableDatabase();
        Log.i("consulta","SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA +
                " WHERE " + controladorBD1.DatosTablaEvent.COLUMN_ID +
                " = (SELECT MAX("+controladorBD1.DatosTablaEvent.COLUMN_ID+") FROM "+
                controladorBD1.DatosTablaEvent.NOMBRE_TABLA + ")");
        Cursor cursor=db.rawQuery("SELECT * FROM "+ controladorBD1.DatosTablaEvent.NOMBRE_TABLA +
                " WHERE " + controladorBD1.DatosTablaEvent.COLUMN_ID +
                        " = (SELECT MAX("+controladorBD1.DatosTablaEvent.COLUMN_ID+") FROM "+
                controladorBD1.DatosTablaEvent.NOMBRE_TABLA + ")",null);

        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                nombre = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_NOMBRE));
                String fecha = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FECHA));
                informacion = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_INFORMACION));
                String organizador = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_ORGANIZADOR));
                String pais = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PAIS));
                String departamento = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_DEPARTAMENTO));
                String ciudad = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_CIUDAD));
                String lugar = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_LUGAR));
                puntuacion = cursor.getString(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_PUNTUACION));
                imagen = cursor.getBlob(cursor.getColumnIndex(controladorBD1.DatosTablaEvent.COLUMN_FOTO));
            }
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setImageViewBitmap(R.id.imagenEventWidget, BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
        views.setTextViewText(R.id.nombreEventWidget, nombre);
        views.setTextViewText(R.id.informacionEventWidget, informacion);
        views.setTextViewText(R.id.puntuacionEventWidget, puntuacion);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

