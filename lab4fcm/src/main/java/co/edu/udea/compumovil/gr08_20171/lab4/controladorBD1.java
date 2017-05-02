package co.edu.udea.compumovil.gr08_20171.lab4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Jesus Gomez on 09/03/2017.
 */

public class controladorBD1 extends SQLiteOpenHelper{

    public static class DatosTablaUser implements BaseColumns {
        public static final String NOMBRE_TABLA = "Usuario";
        public static final String COLUMN_USUARIO = "user";
        public static final String COLUMN_CONTRASENA = "pass";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_CELULAR = "celular";
        public static final String COLUMN_PAIS = "pais";
        public static final String COLUMN_DEPARTAMENTO = "departamento";
        public static final String COLUMN_CIUDAD = "ciudad";
        public static final String COLUMN_DIRECCION = "direccion";
        public static final String COLUMN_EDAD = "edad";
        public static final String COLUMN_FOTO = "foto";

        private static final String TEXT_TYPE = " TEXT";
        private static final String BLOB_TYPE = " BLOB";
        private static final String COMMA_SEP = ",";
        private static final String CREAR_TABLA_USER =
                "CREATE TABLE " + DatosTablaUser.NOMBRE_TABLA + " (" +
                        DatosTablaUser.COLUMN_USUARIO + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_CONTRASENA + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_NOMBRE + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                        DatosTablaUser.COLUMN_CELULAR + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_PAIS + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_DEPARTAMENTO + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_CIUDAD + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_DIRECCION + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_EDAD + TEXT_TYPE + COMMA_SEP +
                        DatosTablaUser.COLUMN_FOTO + BLOB_TYPE + " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DatosTablaUser.NOMBRE_TABLA;
    }

    public static class DatosTablaEvent implements BaseColumns {
        public static final String NOMBRE_TABLA = "Eventos";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_FECHA = "fecha";
        public static final String COLUMN_INFORMACION = "informacion";
        public static final String COLUMN_ORGANIZADOR = "organizador";
        public static final String COLUMN_PAIS = "pais";
        public static final String COLUMN_DEPARTAMENTO = "departamento";
        public static final String COLUMN_CIUDAD = "ciudad";
        public static final String COLUMN_LUGAR = "lugar";
        public static final String COLUMN_PUNTUACION = "puntuacion";
        public static final String COLUMN_FOTO = "foto";
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        private static final String CREAR_TABLA_EVENT =
                "CREATE TABLE " + DatosTablaEvent.NOMBRE_TABLA + " (" +
                        DatosTablaEvent.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        DatosTablaEvent.COLUMN_NOMBRE + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_FECHA + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_INFORMACION + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_ORGANIZADOR + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_PAIS + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_DEPARTAMENTO + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_CIUDAD + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_LUGAR + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_PUNTUACION + TEXT_TYPE + COMMA_SEP +
                        DatosTablaEvent.COLUMN_FOTO + TEXT_TYPE + " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DatosTablaEvent.NOMBRE_TABLA;
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MiBaseDeDatos.db";

    public controladorBD1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatosTablaUser.CREAR_TABLA_USER);
        db.execSQL(DatosTablaEvent.CREAR_TABLA_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatosTablaUser.SQL_DELETE_ENTRIES);
        db.execSQL(DatosTablaEvent.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
