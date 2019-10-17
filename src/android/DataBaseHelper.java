package criteriadb_plugin_cordova;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "location_db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyLocation.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +MyLocation.TABLE_NAME);
        onCreate(db);
    }

    public long insertMyLocation(MyLocation myLocation){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyLocation.COLUMN_LATITUDE,myLocation.getLatitude());
        values.put(MyLocation.COLUMN_LONGITUDE,myLocation.getLongitude());

        long id = database.insert(MyLocation.TABLE_NAME,null,values);
        database.close();
        return id;
    }

    public List<MyLocation> getAllLocation(){
        List<MyLocation> allLocation = new ArrayList<>();
        SQLiteDatabase database= this.getReadableDatabase();
        String query="SELECT * FROM "+MyLocation.TABLE_NAME;
        Cursor cursor=database.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                MyLocation locationCurrent= new MyLocation();
                locationCurrent.setLatitude(cursor.getString(cursor.getColumnIndex(MyLocation.COLUMN_LATITUDE)));
                locationCurrent.setLongitude(cursor.getString(cursor.getColumnIndex(MyLocation.COLUMN_LONGITUDE)));

                allLocation.add(locationCurrent);
            }while (cursor.moveToNext());
        }

        database.close();
        return allLocation;
    }


    public  void deleteAllLocation(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query="DELETE FROM "+MyLocation.TABLE_NAME;
        database.rawQuery(query,null);
        database.close();
    }
}
