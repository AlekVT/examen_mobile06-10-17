package alek.examen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import alek.examen.model.Description;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";
    private static final String TABLE_COORDS = "coordinates";
    private static final int DATABASE_VERSION = 5;

    public MySQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Android expects _id to be the primary key
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_COORDS + "(_id INTEGER PRIMARY KEY, latitude REAL, longitude REAL, description TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDS);
        onCreate(db);
    }

    public void addDescription(double latitude, double longitude, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("description", description);

        db.insert(TABLE_COORDS, null, values);
        db.close();
    }

    public List<Description> findDescriptions() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_COORDS, null);

        List<Description> descriptionList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Description description = new Description(cursor.getString(cursor.getColumnIndex("description")), cursor.getDouble(cursor.getColumnIndex("latitude")), cursor.getDouble(cursor.getColumnIndex("longitude")));
                descriptionList.add(description);
                cursor.moveToNext();
            }
        }
        return descriptionList;
    }

    public void clearTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_COORDS);
    }
}
