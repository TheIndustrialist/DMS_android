package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/13/2016.
 */
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

public class PrescriptionDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE = "PrescriptionTable";
    private static PrescriptionDatabaseHelper sInstance;


    public static synchronized PrescriptionDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PrescriptionDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private PrescriptionDatabaseHelper(Context context) {
        super(context, "prescription.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE +
                "(DateTime TEXT PRIMARY KEY, " +
                "PrescriptionType INTEGER, " +
                "BGL INTEGER, " +
                "Diet TEXT," +
                "Exercise TEXT," +
                "Medication TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public boolean savePrescription (int code, int bgl, String diet, String exercise, String medication) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("DateTime", formattedDate);
        cv.put("PrescriptionType", code); //0 to check BGL, 1 to eat something, 2 to fitness, 3 to take meds
        cv.put("BGL", bgl);
        cv.put("Diet", diet);
        cv.put("Exercise", exercise);
        cv.put("Medication", medication);
        /*
        TODO figure out this data model.  maybe have a duration like eat an apple every 4 hours or check BGL every hour
        use the date code as the date/time of the next prescription event and set off alarm???
        */

        long res = db.insert(DATABASE_TABLE, null, cv);

        if( res == -1 )
            return false;
        else {
             Log.v("DB EVENT", formattedDate + "\t" + code + "\t" +bgl + "\t" + diet + "\t" + exercise + "\t" + medication);
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

    public ArrayList<PrescriptionEntry> getAllItems() {
        ArrayList<PrescriptionEntry> dbList = new ArrayList<PrescriptionEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DATABASE_TABLE, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                PrescriptionEntry entry = new PrescriptionEntry();
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


    public void clearDatabase() {
        String clearDBQuery = "DELETE FROM "+ DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);
    }
}


