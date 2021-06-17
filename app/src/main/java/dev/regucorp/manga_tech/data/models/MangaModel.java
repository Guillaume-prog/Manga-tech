package dev.regucorp.manga_tech.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dev.regucorp.manga_tech.data.DataHandler;

public class MangaModel extends BaseModel {

    /**
     * TABLE STRUCTURE :
     *
     * name             TEXT        manga title
     * total_volumes    INTEGER     number of volumes to the series
     * owned_volumes    TEXT        data on the volumes that are owned, stored in binary (ex: 0b01011 -> volumes 1-2-4)
     *
     */

    public static final int MANGA_NAME = 0;
    public static final int MANGA_VOLUMES = 1;
    public static final int MANGA_OWNED = 2;

    private static MangaModel instance;
    public static MangaModel getInstance() {
        if(instance == null) instance = new MangaModel();
        return instance;
    }

    private MangaModel() {
        TABLE_NAME = "my_manga";
    }

    public void createTable(SQLiteDatabase db) {
        super.createTable(db, "name TEXT, total_volumes INTEGER, owned_volumes TEXT");
    }

    public void addEntry(DataHandler db, String name, int totalVolumes, String ownedVolumes) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("total_volumes", totalVolumes);
        cv.put("owned_volumes", ownedVolumes);

        db.getWritableDatabase().insert(TABLE_NAME, null, cv);
    }

    public Cursor getEntries(DataHandler db) {
        return db.getReadableDatabase().rawQuery("SELECT * FROM "+ TABLE_NAME +";", null);
    }

    public Cursor getEntriesByName(DataHandler db, String name) {
        String query = "SELECT * FROM "+ TABLE_NAME +" WHERE name='"+ name +"';";
        return db.getReadableDatabase().rawQuery(query, null);
    }

    public void updateOwned(DataHandler db, String name, String owned) {
        db.getWritableDatabase().execSQL("UPDATE "+ TABLE_NAME +" SET owned_volumes = '"+ owned +"' WHERE name = '"+ name + "';");
    }

    public void deleteManga(DataHandler db, String name) {
        db.getWritableDatabase().execSQL("DELETE FROM `"+ TABLE_NAME +"` WHERE name = '"+ name +"';");
    }
}
