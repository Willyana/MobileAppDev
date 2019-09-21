package myasn.com.example.inventory;
/*
 *****************************************************
 * @author Willyana - 12067867
 *****************************************************
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROWID = "_id";//declare string id
    static final String KEY_NAME = "name";//declare string name
    static final String KEY_TIME = "time"; //declare string time
    static final String KEY_LONGITUDE = "longitude"; //declare string longitude
    static final String KEY_LATITUDE = "latitude"; //declare string latitude
    static final String KEY_REFNUM = "referenceNum"; //declare string referenceNum
    static final String KEY_DESC = "description";//declare string description
    static final String TAG = "DBAdapter";//declare string DBAdapter
    static final String DATABASE_NAME = "logs";//declare string DATABASE_NAME
    static final String DATABASE_TABLE = "inventory";//declare string DATABASE_TABLE
    static final int DATABASE_VERSION = 1;//declare int DATABASE_VERSION
    static final String DATABASE_CREATE =//declare string DATABASE_CREATE
            "create table inventory (_id integer primary key autoincrement, name text not null, time text not null, " +
                    "longitude text not null, latitude text not null, referenceNum text not null, description text not null);";
    final Context context; //declare context
    DatabaseHelper DBHelper;//declare database helper
    SQLiteDatabase db;//declare database


    public DBAdapter(Context ctx) {
        this.context = ctx; //takes the context to allow the database to be created
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS inventory");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a Entries into the database---
    public long insertEntries(String name, String time, String longitude, String latitude, String referenceNum,
                              String description) {//insert data
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);//insert name
        initialValues.put(KEY_TIME, time);//insert time
        initialValues.put(KEY_LONGITUDE, longitude);//insert longitude
        initialValues.put(KEY_LATITUDE, latitude);//insert latitude
        initialValues.put(KEY_REFNUM, referenceNum);//insert reference number
        initialValues.put(KEY_DESC, description);//insert description
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular Entries---
    public boolean deleteEntries(long rowId) { //delete entries
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---deletes all a particular Entries---
    public boolean deleteAllEntries() {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---retrieves all the Entries---
    public Cursor getAllEntries() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_TIME, KEY_LONGITUDE, KEY_LATITUDE,
                KEY_REFNUM, KEY_DESC}, null, null, null, null, null);
    }

    //---retrieves a particular Entries---
    public Cursor getEntries(long rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                                KEY_NAME, KEY_TIME, KEY_LONGITUDE, KEY_LATITUDE, KEY_REFNUM, KEY_DESC}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a Entries---
    public boolean updateEntries(long rowId, String name, String time, String longitude, String latitude,
                                 String referenceNum, String description) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_TIME, time);
        args.put(KEY_LONGITUDE, longitude);
        args.put(KEY_LATITUDE, latitude);
        args.put(KEY_REFNUM, referenceNum);
        args.put(KEY_DESC, description);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}//End DBAdapter class

