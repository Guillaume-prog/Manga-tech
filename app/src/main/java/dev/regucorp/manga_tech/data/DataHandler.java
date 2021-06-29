package dev.regucorp.manga_tech.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHandler extends SQLiteOpenHelper {

    // Instance stuff

    private static DataHandler instance;

    public static DataHandler getInstance(Context c) {
        if(instance == null) instance = new DataHandler(c);
        return instance;
    }

    // DB info
    private static final String DB_NAME = "mangatech";
    private static final int DB_VERSION = 11;

    private DataHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("HI", "onCreate: ");
        MangaModel.getInstance().createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MangaModel.getInstance().dropTable(db);
        onCreate(db);
    }
}
