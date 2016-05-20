package com.beatem.tj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mliljenberg on 09/04/16.
 */
public class MySqLite extends SQLiteOpenHelper {
    private boolean putAllInProgress = false;
    private SQLiteDatabase putAllDatabase;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "traveljournal";
    private static final String CREATE_LOCATIONS_TABLE = "create table Locations(\n" +
            "picturepath varchar(300) not null,\n" +
            "longditude float not null,\n" +
            "latitude float not null,\n" +
            "trip varchar(50) not null,\n" +
            "text varchar(300),\n" +
            "direction varchar(50) not null,\n" +
            "filter varchar(100) not null,\n" +
            "date varchar(100) not null,\n" +
            "primary key(picturepath, longditude,latitude)\n" +
            ");";

    private static final String TABLE_LOCATIONS = "Locations";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGDITUDE = "longditude";
    private static final String KEY_PICTUREPATH = "picturepath";
    private static final String VAR_DIRECTION = "direction";
    private static final String VAR_FILTER = "filter";
    private static final String VAR_DATE = "date";
    private static final String VAR_TRIP = "trip";
    private static final String VAR_TEXT = "text";
    private static final String[] LOCATION_COLUMNS = {KEY_PICTUREPATH, KEY_LONGDITUDE, KEY_LATITUDE, VAR_TRIP,VAR_TEXT,  VAR_DIRECTION, VAR_FILTER,VAR_DATE };

    public MySqLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Skapar databasen
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATIONS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Locations");
        this.onCreate(db);
    }

    /**
     * Locations_Locations
     **/


    public synchronized Boolean addLocation(MyLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PICTUREPATH, location.getPicpath());
        values.put(KEY_LONGDITUDE, location.getLongditude());
        values.put(KEY_LATITUDE, location.getLatitude());

        values.put(VAR_TRIP, location.getTrip());
        values.put(VAR_TEXT, location.getText());

        values.put(VAR_DIRECTION, location.getDirection());
        values.put(VAR_FILTER, location.getFilter());
        values.put(VAR_DATE,location.getDate());
        Boolean inserted = db.insert(TABLE_LOCATIONS, null, values) > 0;

        return inserted;
    }

    public synchronized MyLocation getLocation(float longditude, float latitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_LOCATIONS,             //table
                LOCATION_COLUMNS,            //column names
                KEY_PICTUREPATH + " = ? AND " + KEY_LONGDITUDE + " = ? AND " + KEY_LATITUDE + "= ?",    //selections
                new String[]{longditude + "", latitude + ""}, //selections args
                null,                      //group by
                null,                      //having
                null,                      //order by
                null);                     //limit

        if (cursor != null) {
            cursor.moveToFirst();
        }
        MyLocation location = new MyLocation(cursor.getString(0),Float.valueOf(cursor.getString(1)), Float.valueOf(cursor.getString(2)),  cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        db.close();
        cursor.close();

        return location;
    }

    public synchronized ArrayList<MyLocation> getLocations() {
        //TODO: testa ifal denna fungerar
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<MyLocation> locations = new ArrayList<MyLocation>();
        Cursor cursor = db.query(

                TABLE_LOCATIONS,             //table
                LOCATION_COLUMNS,            //column names
                null,    //selections
                null, //selections args
                null,                      //group by
                null,                      //having
                null,                      //order by
                null
                );                     //limit

        if (cursor.moveToFirst()) {
            do {
                MyLocation location = new MyLocation( cursor.getString(0),Float.valueOf(cursor.getString(1)), Float.valueOf(cursor.getString(2)), cursor.getString(3), cursor.getString(4),cursor.getString(5), cursor.getString(6), cursor.getString(7));
                locations.add(location);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return locations;
    }


    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}