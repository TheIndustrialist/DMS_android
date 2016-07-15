package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/13/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE = "DiabeticTable";
    private static DatabaseHelper sInstance;
    private int entryCount;

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
    public DatabaseHelper(Context context) {
        super(context, "diabetes.db", null, 1);
    }
*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE + "(DateTime TEXT PRIMARY KEY, " +
                "EventCode INTEGER, " +
                "BGL INTEGER, " +
                "Diet TEXT," +
                "Exercise TEXT," +
                "Medication TEXT);");
        entryCount = 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public boolean saveEvent (int code, int bgl, String diet, String exercise, String medication) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("DateTime", formattedDate);
        cv.put("EventCode", code);
        cv.put("BGL", bgl);
        cv.put("Diet", diet);
        cv.put("Exercise", exercise);
        cv.put("Medication", medication);

        long res = db.insert(DATABASE_TABLE, null, cv);

        if( res == -1 )
            return false;
        else {
            entryCount++;
            return true;
        }

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+ DATABASE_TABLE, null);
        return result;
    }


    public int getEntryCount() {
        return entryCount;
    }

    public ArrayList<DiabeticEntry> getAllItems() {
        ArrayList<DiabeticEntry> dbList = new ArrayList<DiabeticEntry>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                DiabeticEntry entry = new DiabeticEntry();
                entry.setTime(cursor.getString(0));
                entry.setCode(cursor.getInt(1));
                entry.setBGL(cursor.getInt(2));
                entry.setDiet(cursor.getString(3));
                entry.setExercise(cursor.getString(4));
                entry.setMedication(cursor.getString(5));

                //ADD TO THE QUERY LIST
                dbList.add(entry);
            } while (cursor.moveToNext());
        }
        return dbList;
    }
}


