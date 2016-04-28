package com.beatem.tj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mliljenberg on 09/04/16.
 */
public class MySqLite extends SQLiteOpenHelper {
    private boolean putAllInProgress = false;
    private SQLiteDatabase putAllDatabase;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "traveljournal";
    private static final String CREATE_LOCATIONS_TABLE= "create table Locations(\n" +
            "longditude float not null,\n" +
            "latitude float not null,\n" +
            "trip varchar(50) not null,\n" +
            "text varchar(300),\n" +
            "picturepath varchar(300) not null,\n" +
            "primary key(longditude,latitude)\n" +
            ");";

    private static final String TABLE_LOCATIONS = "Locations";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGDITUDE = "longditude";
    private static final String VAR_PICTUREPATH = "picturepath";
    private static final String VAR_TRIP = "trip";
    private static final String VAR_TEXT = "text";
    private static final String[] LOCATION_COLUMNS = {KEY_LONGDITUDE,KEY_LATITUDE,VAR_TRIP,VAR_PICTUREPATH,VAR_TEXT};

    public MySqLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Skapar databasen
     * */
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
        values.put(KEY_LONGDITUDE, location.getLongditude());
        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(VAR_TRIP, location.getTrip());
        values.put(VAR_TEXT, location.getText());
        values.put(VAR_PICTUREPATH, location.getPicpath());
        Boolean inserted = db.insert(TABLE_LOCATIONS, null, values) > 0;

        return inserted;
    }

    public synchronized MyLocation getLocation(float longditude, float latitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_LOCATIONS,             //table
                LOCATION_COLUMNS,            //column names
                KEY_LONGDITUDE + " = ? AND " + KEY_LATITUDE + "= ?",    //selections
                new String[] { longditude+"",latitude+"" }, //selections args
                null,                      //group by
                null,                      //having
                null,                      //order by
                null);                     //limit

        if(cursor != null) {
            cursor.moveToFirst();
        }
        MyLocation location = new MyLocation(Float.valueOf(cursor.getString(0)), Float.valueOf(cursor.getString(1)), cursor.getString(2), cursor.getString(3),cursor.getString(4));
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
                null);                     //limit

        if(cursor.moveToFirst()){
            do{
                MyLocation location = new MyLocation(Float.valueOf(cursor.getString(0)), Float.valueOf(cursor.getString(1)), cursor.getString(2), cursor.getString(3),cursor.getString(4));
                locations.add(location);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return locations;
    }

    }