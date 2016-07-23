package edu.uwm.diabetesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE = "DiabeticTable";
    private static DatabaseHelper sInstance;


    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, "diabetes.db", null, 1);
    }

/*
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE +
                "(DateTime TEXT PRIMARY KEY, " +
                "EventCode INTEGER, " +
                "BGL INTEGER, " +
                "Diet TEXT," +
                "Exercise TEXT," +
                "Medication TEXT);");
     }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE +
                "(Event INTEGER PRIMARY KEY, " +
                "DateTime TEXT," +
                "EventCode INTEGER," +
                "BGL INTEGER," +
                "Diet TEXT," +
                "Exercise TEXT," +
                "Medication TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public boolean saveEvent (String formattedDate, int code, int bgl, String diet, String exercise, String medication) {
        //String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        SQLiteDatabase db = this.getWritableDatabase();
        long index = getEntryCount();

        ContentValues cv = new ContentValues();

        cv.put("Event", index);
        cv.put("DateTime", formattedDate);
        cv.put("EventCode", code);
        cv.put("BGL", bgl);
        cv.put("Diet", diet);
        cv.put("Exercise", exercise);
        cv.put("Medication", medication);

        //Log.v("DB EVENT", index + "\t" + formattedDate + "\t" + code + "\t" +bgl + "\t" + diet + "\t" + exercise + "\t" + medication);

        long res = db.insert(DATABASE_TABLE, null, cv);

        if( res == -1 )
            return false;
        else {
            Log.v("DB EVENT", index + "\t" + formattedDate + "\t" + code + "\t" +bgl + "\t" + diet + "\t" + exercise + "\t" + medication);
            return true;
        }

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+ DATABASE_TABLE, null);
        return result;
    }


    public long getEntryCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public ArrayList<DiabeticEntry> getAllItems() {
        ArrayList<DiabeticEntry> dbList = new ArrayList<DiabeticEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DATABASE_TABLE, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                DiabeticEntry entry = new DiabeticEntry();
                entry.setIndex(cursor.getLong(0));
                entry.setTime(cursor.getString(1));
                entry.setCode(cursor.getInt(2));
                entry.setBGL(cursor.getInt(3));
                entry.setDiet(cursor.getString(4));
                entry.setExercise(cursor.getString(5));
                entry.setMedication(cursor.getString(6));

                //ADD TO THE QUERY LIST
                dbList.add(entry);
            } while (cursor.moveToNext());
        }
        return dbList;
    }


    public void clearDatabase() {
        String clearDBQuery = "DELETE FROM "+ DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);
    }
}


