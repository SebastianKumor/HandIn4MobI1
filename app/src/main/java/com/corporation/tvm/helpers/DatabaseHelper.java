package com.corporation.tvm.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lubomir on 8.10.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String GALLERY_TABLE_NAME = "gallery";
    private static final String GALLERY_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + GALLERY_TABLE_NAME + " (name VARCHAR, lat DOUBLE, long DOUBLE, description VARCHAR, id INTEGER PRIMARY KEY);";

    public DatabaseHelper(Context context) {
        super(context, "Gallery", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GALLERY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int x, int y) {

    }

  /*  public void createDatabase()
    {
        try {
            dbs = this.openOrCreateDatabase("Gallery", MODE_PRIVATE, null);
            dbs.execSQL("CREATE TABLE IF NOT EXISTS gallery (name VARCHAR, lat DOUBLE, long DOUBLE, description VARCHAR, id INTEGER PRIMARY KEY)");
            //     dbs.execSQL("INSERT INTO gallery (event, year) VALUES ('End Of WW2', 1945)");
            //      dbs.execSQL("INSERT INTO gallery (event, year) VALUES ('Wham split up', 1986)");
            Cursor c = dbs.rawQuery("SELECT * FROM gallery", null);

            int eventIndex = c.getColumnIndex("event");
            int yearIndex = c.getColumnIndex("year");
            c.moveToFirst();

            while (c != null) {
                Log.i("Results - event", c.getString(eventIndex));
                Log.i("Results - year", Integer.toString(c.getInt(yearIndex)));
                c.moveToNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
