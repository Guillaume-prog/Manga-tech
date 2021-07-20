package dev.regucorp.manga_tech.data;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import dev.regucorp.manga_tech.R;

public class MangaModel {

    private static final String TAG = "MangaModel";

    private static MangaModel instance;

    public static void createInstance(Resources resources) {
        if(instance == null) instance = new MangaModel(resources);
    }

    public static MangaModel getInstance() {
        return instance;
    }

    private static final String TABLE_NAME = "manga_ledger";
    private Resources res;

    private MangaModel(Resources res) {
        this.res = res;
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL(res.getString(R.string.sql_manga_table_create, TABLE_NAME));
        Log.d("HI", "createTable: table created");
    }

    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void addEntry(DataHandler db, MangaEntry entry) {
        ContentValues cv = new ContentValues();
        cv.put("person", entry.getPerson());
        cv.put("type", entry.getType());

        cv.put("name", entry.getName());
        cv.put("num_volumes", entry.getNumVolumes());
        cv.put("owned", entry.getOwned());

        db.getWritableDatabase().insert(TABLE_NAME, null, cv);
    }

    public MangaEntry[] get(DataHandler db, int type) {
        MangaEntry[] entries = null;

        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM "+ TABLE_NAME +" WHERE type = ?;", new String[] {String.valueOf(type)});
        Log.d(TAG, "get: "+c.getCount());
        if(c.getCount() > 0) {
            entries = new MangaEntry[c.getCount()];

            c.moveToFirst();
            do {
                entries[c.getPosition()] = new MangaEntry(
                        c.getInt(1),
                        c.getString(2),
                        c.getString(3),
                        c.getInt(4));

                entries[c.getPosition()].setOwned(c.getString(5));
                entries[c.getPosition()].setId(c.getInt(0));
            } while(c.moveToNext());
        }

        return entries;
    }

    public MangaEntry getByName(DataHandler db, String name) {
        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM "+ TABLE_NAME +" WHERE name = ?;", new String[] {name});

        MangaEntry manga = null;
        if(c.moveToFirst()) {
            manga = new MangaEntry(
                c.getInt(1),
                c.getString(2),
                c.getString(3),
                c.getInt(4));

            manga.setOwned(c.getString(5));
            manga.setId(c.getInt(0));
        }

        return manga;
    }

    public void updateEntry(DataHandler db, MangaEntry entry) {
        String sql = "UPDATE "+ TABLE_NAME +" SET owned = ? WHERE name = ?;";
        Cursor c = db.getWritableDatabase().rawQuery(sql, new String[] {entry.getOwned(), entry.getName()});
        Log.d(TAG, "updateEntry: "+sql);

        int numRows = c.getCount();
        Log.d(TAG, "updateEntry: "+numRows);
    }

    public void deleteEntry(DataHandler db, MangaEntry entry) {
        db.getWritableDatabase().delete(TABLE_NAME, "name = ?", new String[] {entry.getName()});
    }
}
