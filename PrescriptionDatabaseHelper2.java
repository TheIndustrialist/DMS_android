package edu.uwm.diabetesapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class PrescriptionDatabaseHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE = "PrescriptionTable";
    private static PrescriptionDatabaseHelper2 sInstance;


    public static synchronized PrescriptionDatabaseHelper2 getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PrescriptionDatabaseHelper2(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private PrescriptionDatabaseHelper2(Context context) {
        super(context, "prescription.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE +
                "(ID INTEGER PRIMARY KEY, " +
                "PrescriptionType INTEGER, " +
                "NextOccurrence INTEGER, " + //as epoch
                "Frequency INTEGER, " +
                "EndEvent INTEGER, " + //as epoch
                "BGL TEXT, " +
                "Diet TEXT, " +
                "Exercise TEXT, " +
                "Medication TEXT);");
        //id, type, next year, next month, next day, next hour, next minute, frequency hours, end year, end month, end day, end hour, end minute, bgl, nutrition, exercise, meds)
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public boolean savePrescription (int code, long nextOccurrence, long frequency, long endEvent,
                                     String bgl, String diet, String exercise, String medication) {
        //String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        SQLiteDatabase db = this.getWritableDatabase();
        long index = getEntryCount();

        ContentValues cv = new ContentValues();
        cv.put("id", index);
        cv.put("PrescriptionType", code); //0 to check BGL, 1 to eat something, 2 to fitness, 3 to take meds
        cv.put("NextOccurrence",nextOccurrence);
        cv.put("Frequency",frequency);
        cv.put("EndEvent",endEvent);
        cv.put("BGL", bgl);
        cv.put("Diet", diet);
        cv.put("Exercise", exercise);
        cv.put("Medication", medication);
        /*
        TODO figure out this data model.  maybe have a duration like eat an apple every 4 hours or check BGL every hour...
        use the date/time of the next prescription event and set off alarm and then reset next date/time by adding frequency???
        */

        long res = db.insert(DATABASE_TABLE, null, cv);

        if( res == -1 )
            return false;
        else {
             Log.v("DB EVENT", index + "\t" + code + "\t" + nextOccurrence + "\t" + frequency + "\t" +
                     endEvent + "\t" + bgl + "\t" + diet + "\t" + exercise + "\t" + medication);
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
                entry.setID(cursor.getInt(0));
                entry.setCode(cursor.getInt(1));
                entry.setNextOccurrence(cursor.getLong(2));
                entry.setFrequency(cursor.getLong(3));
                entry.setEventEnd(cursor.getLong(4));
                entry.setBGL(cursor.getString(5));
                entry.setDiet(cursor.getString(6));
                entry.setExercise(cursor.getString(7));
                entry.setMedication(cursor.getString(8));

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

    public void editDatabase(){

    }

    //~~Method to move the next occurrence forward by the frequency time
    public boolean forwardPrescription(int index){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long currentTime = System.currentTimeMillis()/1000;
        long nextOccurrence;
        long frequency;


        String query= "SELECT NextOccurrence, Frequency from " + DATABASE_TABLE + " where id = " + index + ";";

        Cursor result = db.rawQuery(query,null);
        if (result!=null){
            result.moveToFirst();
            nextOccurrence = Long.valueOf(result.getString(0));
            frequency  = Long.valueOf(result.getString(1));
        } else {
            db.close();
            nextOccurrence = 0;
            frequency = 0;

        }

        do { //in case some were missed because the device was off, update to the current time minimum
            nextOccurrence+=frequency*60; //frequency is in minutes, convert to seconds
        } while (nextOccurrence <= currentTime);


        cv.put("NextOccurrence",nextOccurrence);
        long res = db.update(DATABASE_TABLE, cv, "id = " + index, null);
        if( res == -1 )
            return false;
        else {
            Log.v("FORWARD PRESCRIPTION", index + "\t" + nextOccurrence + "\t" + frequency);
            return true;
        }
    }


}


