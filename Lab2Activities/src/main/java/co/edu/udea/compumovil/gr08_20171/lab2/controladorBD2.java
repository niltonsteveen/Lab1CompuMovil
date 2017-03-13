package co.edu.udea.compumovil.gr08_20171.lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Jesus Gomez on 13/03/2017.
 */

public class controladorBD2 extends SQLiteOpenHelper {

    public static class DatosTablaEvent implements BaseColumns {
        public static final String NOMBRE_TABLA = "Eventos";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_FECHA = "fecha";
        public static final String COLUMN_INFORMACION = "informacion";
        public static final String COLUMN_TIPO = "tipo";
        public static final String COLUMN_PAIS = "pais";
        public static final String COLUMN_DEPARTAMENTO = "departamento";
        public static final String COLUMN_CIUDAD = "ciudad";
        public static final String COLUMN_DIRECCION = "direccion";
        public static final String COLUMN_PUNTUACION = "puntuacion";
        public static final String COLUMN_FOTO = "foto";

        private static final String TEXT_TYPE = " TEXT";
        private static final String BLOB_TYPE = " BLOB";
        private static final String COMMA_SEP = ",";
        private static final String CREAR_TABLA_USER =
                "CREATE TABLE " + DatosTablaEvent.NOMBRE_TABLA + " (" +
                        DatosTablaEvent.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DatosTablaEvent.COLUMN_NOMBRE + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_FECHA + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_INFORMACION + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_TIPO + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_PAIS + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_DEPARTAMENTO + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_CIUDAD + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_DIRECCION + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_PUNTUACION + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_FOTO + BLOB_TYPE + " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DatosTablaEvent.NOMBRE_TABLA;
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EventosBD.db";

    public controladorBD2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatosTablaEvent.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
