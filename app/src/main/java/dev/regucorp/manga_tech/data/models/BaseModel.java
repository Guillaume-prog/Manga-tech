package dev.regucorp.manga_tech.data.models;

import android.database.sqlite.SQLiteDatabase;

public abstract class BaseModel {

    protected static String TABLE_NAME;

    protected void createTable(SQLiteDatabase db, String params) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME +"("+ params +");");
    }

    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

}
