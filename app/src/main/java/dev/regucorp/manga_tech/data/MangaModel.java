package dev.regucorp.manga_tech.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MangaModel {

    private static final String TAG = "MangaModel";

    private static MangaModel instance;
    public static MangaModel getInstance() {
        if(instance == null) instance = new MangaModel();
        return instance;
    }

    private static final String TABLE_NAME = "manga_ledger";

    private MangaModel() {

    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME +"(" +
                "id           INT          PRIMARY KEY, " +
                "type         INT          NOT NULL, " +
                "person       VARCHAR(255) NOT NULL, " +
                "name         VARCHAR(255) NOT NULL, " +
                "start_volume INT          NOT NULL, " +
                "end_volume   INT          NOT NULL" +
                ");");
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
        cv.put("start_volume", entry.getStartVolume());
        cv.put("end_volume", entry.getEndVolume());

        db.getWritableDatabase().insert(TABLE_NAME, null, cv);
    }

    public MangaEntry[] get(DataHandler db, int type) {
        MangaEntry[] entries = null;

        int numRows = getNumRows(db);
        Log.d(TAG, "get: "+numRows);
        if(numRows > 0) {
            Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM "+ TABLE_NAME +" WHERE type = "+ type +";", null);
            entries = new MangaEntry[numRows];

            c.moveToFirst();
            do {
                entries[c.getPosition()] = new MangaEntry(
                        c.getInt(1),
                        c.getString(2),
                        c.getString(3),
                        c.getInt(4),
                        c.getInt(5));

                entries[c.getPosition()].setId(c.getInt(0));
            } while(c.moveToNext());
        }

        return entries;
    }

    private int getNumRows(DataHandler db) {
        Cursor c = db.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM"+ TABLE_NAME +";", null);
        c.moveToFirst();
        return c.getInt(0);
    }

    public void deleteEntry(DataHandler db, MangaEntry entry) {
        db.getWritableDatabase().execSQL("DELETE FROM "+ TABLE_NAME +" WHERE id = "+ entry.getId() +";");
    }
}
