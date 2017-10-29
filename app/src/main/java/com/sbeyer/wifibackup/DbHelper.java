////////////////////////////////////////////////////////////////////////////////////////////////////
//  DbHelper.java
//  Helferklasse um alle benötigten Datenbanktransaktionen auszuführen
//
//  Autor: Simon Beyer
//  Letzte Änderung: 17.11.2015
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.sbeyer.wifibackup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{

    String stringAllElementsForQuery = " leid, ssid, password, security, owner, comment, location, provider, speed, tag, category, frequency ";

    public static final String DB_NAME = "WifiBackupDatabase.db";
    public static final int DB_VERSION = 1;
    //LISTELEMENT TABLE
    public static final String TABLE_LISTELEMENT = "listelement";
    public static final String COL_LEID = "leid";
    public static final String COL_SSID = "ssid";
    public static final String COL_SECURITY = "security";
    public static final String COL_PASSWORD = "password";
    public static final String COL_OWNER = "owner";
    public static final String COL_COMMENT = "comment";
    public static final String COL_LOCATION = "location";
    public static final String COL_PROVIDER = "provider";
    public static final String COL_SPEED = "speed";
    public static final String COL_TAG = "tag";
    public static final String COL_CATEGORY = "category";
    public static final String COL_FREQUENCY = "frequency";


    public static final String SQL_CREATE_LIST =
            "create table " + TABLE_LISTELEMENT + "(" +
                    COL_LEID + " integer primary key autoincrement, " +
                    COL_SSID + " text UNIQUE ON CONFLICT REPLACE, " +
                    COL_PASSWORD + " text, " +
                    COL_SECURITY + " text, " +
                    COL_OWNER + " text, " +
                    COL_COMMENT + " text, " +
                    COL_LOCATION + " text, " +
                    COL_PROVIDER + " text, " +
                    COL_SPEED + " text, " +
                    COL_TAG + " text, " +
                    COL_CATEGORY + " text, " +
                    COL_FREQUENCY + " text" + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LIST);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // SETTER //////////////////////////////////////////////////////////////////////////////////////

    public boolean setData(String ssid, String password, String security, String owner, String comment, String location, String provider, String speed, String tag, String category, String frequency) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("ssid", ssid);
        contentValues.put("password", password);
        contentValues.put("security", security);
        contentValues.put("owner", owner);
        contentValues.put("comment", comment);
        contentValues.put("location", location);
        contentValues.put("provider", provider);
        contentValues.put("speed", speed);
        contentValues.put("tag", tag);
        contentValues.put("category", category);
        contentValues.put("frequency", frequency);
        db.insert(TABLE_LISTELEMENT, null, contentValues);
        return true;
    }

    // GETTER //////////////////////////////////////////////////////////////////////////////////////

    public String getAllElements() {
        SQLiteDatabase db = this.getWritableDatabase();

        String allData = "";
        Cursor dbCursor;

        try {
            dbCursor = db.query(TABLE_LISTELEMENT, new String[]{COL_LEID, COL_SSID, COL_PASSWORD, COL_SECURITY,
                    COL_OWNER, COL_COMMENT, COL_LOCATION, COL_PROVIDER, COL_SPEED, COL_TAG, COL_CATEGORY, COL_FREQUENCY}, null, null, null,null, null);


            dbCursor.getCount();
            int iid = dbCursor.getColumnIndex(COL_LEID);
            int iSsid = dbCursor.getColumnIndex(COL_SSID);
            int iPassword = dbCursor.getColumnIndex(COL_PASSWORD);
            int iSecurity = dbCursor.getColumnIndex(COL_SECURITY);
            int iOwner = dbCursor.getColumnIndex(COL_OWNER);
            int iComment = dbCursor.getColumnIndex(COL_COMMENT);
            int iLocation = dbCursor.getColumnIndex(COL_LOCATION);
            int iProvider = dbCursor.getColumnIndex(COL_PROVIDER);
            int iSpeed = dbCursor.getColumnIndex(COL_SPEED);
            int iTag = dbCursor.getColumnIndex(COL_TAG);
            int icategory = dbCursor.getColumnIndex(COL_CATEGORY);
            int iFrequency = dbCursor.getColumnIndex(COL_FREQUENCY);

            if (dbCursor.getCount()>0) {
                for (dbCursor.moveToFirst(); !dbCursor.isAfterLast(); dbCursor.moveToNext()) {
                    allData = allData + dbCursor.getInt(iid)
                            + "\t" + dbCursor.getString(iSsid)
                            + "\t" + dbCursor.getString(iPassword) + "\t" + dbCursor.getString(iSecurity) + "\t" + dbCursor.getString(iOwner)
                            + "\t" + dbCursor.getString(iComment) + "\t" + dbCursor.getString(iLocation)
                            + "\t" + dbCursor.getString(iProvider)+ "\t" + dbCursor.getString(iSpeed)
                            + "\t" + dbCursor.getString(iTag)+ "\t" + dbCursor.getString(icategory)
                            + "\t" + dbCursor.getString(iFrequency)+ "\n";
                }
            }
            dbCursor.close();
        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
        }
        return allData;
    }

    public String getDataSortedBySsid(){
        SQLiteDatabase db = this.getWritableDatabase();

        String dataSortedBySsid = "";
        Cursor dbCursor;

        try {
            dbCursor = db.rawQuery("SELECT" + stringAllElementsForQuery + "FROM listelement ORDER BY upper(ssid)", null);

            dbCursor.getCount();
            int iid = dbCursor.getColumnIndex(COL_LEID);
            int issid = dbCursor.getColumnIndex(COL_SSID);
            int ipassword = dbCursor.getColumnIndex(COL_PASSWORD);
            int isecurity = dbCursor.getColumnIndex(COL_SECURITY);
            int iowner = dbCursor.getColumnIndex(COL_OWNER);
            int icomment = dbCursor.getColumnIndex(COL_COMMENT);
            int ilocation = dbCursor.getColumnIndex(COL_LOCATION);
            int iprovider = dbCursor.getColumnIndex(COL_PROVIDER);
            int ispeed = dbCursor.getColumnIndex(COL_SPEED);
            int itag = dbCursor.getColumnIndex(COL_TAG);
            int icategory = dbCursor.getColumnIndex(COL_CATEGORY);
            int ifrequency = dbCursor.getColumnIndex(COL_FREQUENCY);

            if (dbCursor.getCount()>0) {
                for (dbCursor.moveToFirst(); !dbCursor.isAfterLast(); dbCursor.moveToNext()){
                    dataSortedBySsid = dataSortedBySsid + dbCursor.getInt(iid)
                            + "\t" + dbCursor.getString(issid)
                            + "\t" + dbCursor.getString(ipassword)
                            + "\t" + dbCursor.getString(isecurity)
                            + "\t" + dbCursor.getString(iowner)
                            + "\t" + dbCursor.getString(icomment)
                            + "\t" + dbCursor.getString(ilocation)
                            + "\t" + dbCursor.getString(iprovider)
                            + "\t" + dbCursor.getString(ispeed)
                            + "\t" + dbCursor.getString(itag)
                            + "\t" + dbCursor.getString(icategory)
                            + "\t" + dbCursor.getString(ifrequency)
                            + "\n";
                }
            }
        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
        }

        return dataSortedBySsid;
    }

    public String getDataById(String leid){
        SQLiteDatabase db = this.getWritableDatabase();

        String dataById = "";
        Cursor dbCursor;

        try {
            dbCursor = db.rawQuery("SELECT" + stringAllElementsForQuery +"FROM listelement WHERE leid is '" + leid + "'", null);

            dbCursor.getCount();
            int iid = dbCursor.getColumnIndex(COL_LEID);
            int issid = dbCursor.getColumnIndex(COL_SSID);
            int ipassword = dbCursor.getColumnIndex(COL_PASSWORD);
            int isecurity = dbCursor.getColumnIndex(COL_SECURITY);
            int iowner = dbCursor.getColumnIndex(COL_OWNER);
            int icomment = dbCursor.getColumnIndex(COL_COMMENT);
            int ilocation = dbCursor.getColumnIndex(COL_LOCATION);
            int iprovider = dbCursor.getColumnIndex(COL_PROVIDER);
            int ispeed = dbCursor.getColumnIndex(COL_SPEED);
            int itag = dbCursor.getColumnIndex(COL_TAG);
            int icategory = dbCursor.getColumnIndex(COL_CATEGORY);
            int ifrequency = dbCursor.getColumnIndex(COL_FREQUENCY);


            if (dbCursor.getCount()>0) {
                for (dbCursor.moveToFirst(); !dbCursor.isAfterLast(); dbCursor.moveToNext()){
                    dataById = dataById + dbCursor.getInt(iid)
                            + "\t" + dbCursor.getString(issid)
                            + "\t" + dbCursor.getString(ipassword)
                            + "\t" + dbCursor.getString(isecurity)
                            + "\t" + dbCursor.getString(iowner)
                            + "\t" + dbCursor.getString(icomment)
                            + "\t" + dbCursor.getString(ilocation)
                            + "\t" + dbCursor.getString(iprovider)
                            + "\t" + dbCursor.getString(ispeed)
                            + "\t" + dbCursor.getString(itag)
                            + "\t" + dbCursor.getString(icategory)
                            + "\t" + dbCursor.getString(ifrequency)
                            + "\n";
                }
            }
        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
        }

        return dataById;
    }

    //DELETE////////////////////////////////////////////////////////////////////////////////////////

    public void deleteDataById(String leid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM listelement WHERE leid IS '" + leid + "'");
    }
}
