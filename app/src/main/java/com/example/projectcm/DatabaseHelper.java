package com.example.projectcm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "carapp.db";
    public static final String USER_TABLE_NAME = "users";
    public static final String CAR_LIST_TABLE_NAME = "carlist";
    public static final String USER_CARS_TABLE_NAME = "usercars";
    public static final String CALENDAR_TABLE_NAME = "calendar";

    //USER TABLE VARS
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "username";
    public static final String USER_EMAIL = "useremail";
    public static final String USER_BIRTHDAY = "userbirthday";
    public static final String USER_PASS = "userpassword";

    //CAR LIST TABLE VARS
    public static final String CAR_MAKE = "carmake";
    public static final String CAR_MODEL = "carmodel";
    public static final String CAR_CATEGORY = "carcategory";
    public static final String CAR_YEAR = "caryear";


    //USER CAR TABLE VARS
    public static final String CAR_ID = "carid";
    public static final String USER_CAR_MAKE = "usercarmake";
    public static final String USER_CAR_MODEL = "usercarmodel";
    public static final String USER_CAR_YEAR = "usercaryear";
    public static final String USER_CAR_INFO = "usercarinfo"; //JSON
    public static final String OWNER_ID = "ownerid";
    public static final String IMAGE_URI = "imageuri";


    //CALENDAR TABLE VARS
    public static final String NOTIF_ID = "notifid";
    public static final String NOTIF_TITLE = "notiftitle";
    public static final String NOTIF_BODY = "notifbody";
    public static final String NOTIF_USER_ID = "userid";
    public static final String NOTIF_CAR_ID = "carid";
    public static final String NOTIF_DATE = "notifdate";

    Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate");

        //USER CREATE TABLE
        db.execSQL("create table " + USER_TABLE_NAME +
                "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_BIRTHDAY + " TEXT, " +
                USER_PASS + " TEXT, " +
                IMAGE_URI + " TEXT )");

        //CARLIST CREATE TABLE
        db.execSQL("create table " + CAR_LIST_TABLE_NAME +
                "(" + CAR_MAKE + " TEXT, " +
                CAR_MODEL + " TEXT, " +
                CAR_CATEGORY + " TEXT, " +
                CAR_YEAR + " TEXT )");

        //USER CARS CREATE TABLE
        db.execSQL("create table " + USER_CARS_TABLE_NAME +
                "(" + CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_CAR_MAKE + " TEXT, " +
                USER_CAR_MODEL + " TEXT, " +
                USER_CAR_YEAR + " INTEGER, " +
                USER_CAR_INFO + " TEXT, " +
                OWNER_ID + " INTEGER, " +
                IMAGE_URI + " TEXT )");

        //CALENDAR CREATE TABLE
        db.execSQL("create table " + CALENDAR_TABLE_NAME +
                "(" + NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTIF_TITLE + " TEXT, " +
                NOTIF_BODY + " TEXT, " +
                NOTIF_USER_ID + " INTEGER, " +
                NOTIF_CAR_ID + " INTEGER, " +
                NOTIF_DATE + " TEXT )");

        loadJSONFromAsset(db, context);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("onUpgrade");
        db.execSQL("drop table if exists " + USER_TABLE_NAME);
        db.execSQL("drop table if exists " + CAR_LIST_TABLE_NAME);
        db.execSQL("drop table if exists " + USER_CARS_TABLE_NAME);
        db.execSQL("drop table if exists " + CALENDAR_TABLE_NAME);
        onCreate(db);
    }


    /**
     * Funções de auxílio na comunicação com a base de dados local -> Users
     */
    public long addNewUser(String name, String email, String birth, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_EMAIL, email);
        contentValues.put(USER_BIRTHDAY, birth);
        contentValues.put(USER_PASS, pass);
        contentValues.put(IMAGE_URI, "");
        Cursor verifyExistingUser = db.rawQuery("select username from " + USER_TABLE_NAME + " where useremail ='" + email + "';", null);
        if(verifyExistingUser.getCount() == 0){
            long userID = db.insert(USER_TABLE_NAME, null, contentValues);

            if(userID == -1){
                return -1;
            }else{
                return userID;
            }

        }else{
            return -1;
        }

    }

    public Integer loginUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select userid, userpassword from " + USER_TABLE_NAME + " where useremail ='" + email + "';", null);
        while (result.moveToNext()){
            if (result.getString(1).equals(password)){
                return Integer.valueOf(result.getString(0));
            }
        }
        return -1;
    }

    public Cursor getUserInfo(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select username, useremail, userbirthday, imageuri from " + USER_TABLE_NAME + " where userid =" + id + ";", null);
        return result;
    }

    public Integer deleteUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //deleteAllCarsFromUser(id);
        return db.delete(USER_TABLE_NAME, "userid = ?", new String[]{String.valueOf(id)});
    }

    public boolean updateUser(int id, String imageURI){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_URI, imageURI);
        db.update(USER_TABLE_NAME, contentValues, "userid = ?", new String[]{String.valueOf(id)});
        return true;
    }


    /**
     * Funções de auxílio na comunicação com a base de dados local -> Car List
     */
    public Cursor getAllMakes(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select distinct carmake from " + CAR_LIST_TABLE_NAME, null);
        return result;
    }

    public Cursor getAllModelsFromAMake(String make){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select carmodel, caryear from " + CAR_LIST_TABLE_NAME + " where carmake ='" + make + "';", null);
        return result;
    }

    public void loadAllCars(SQLiteDatabase db, String json){

        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("results");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject singleObj = m_jArry.getJSONObject(i);
                String make = singleObj.getString("Make");
                String model = singleObj.getString("Model");
                String year = singleObj.getString("Year");
                String category = singleObj.getString("Category");

                ContentValues contentValues = new ContentValues();
                contentValues.put(CAR_MAKE, make);
                contentValues.put(CAR_MODEL, model);
                contentValues.put(CAR_CATEGORY, category);
                contentValues.put(CAR_YEAR, year);

                db.insert(CAR_LIST_TABLE_NAME, null, contentValues);

                contentValues.clear();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Funções de auxílio na comunicação com a base de dados local -> User Car List
     */
    public long addACarToAUser(String carMake, String carModel, String carYear, String carInfo, int ownerID, String imageURI){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CAR_MAKE, carMake);
        contentValues.put(USER_CAR_MODEL, carModel);
        contentValues.put(USER_CAR_YEAR, carYear);
        contentValues.put(USER_CAR_INFO, carInfo);
        contentValues.put(OWNER_ID, ownerID);
        contentValues.put(IMAGE_URI, imageURI);
        long carID = db.insert(USER_CARS_TABLE_NAME, null, contentValues);

        if(carID == -1){
            return -1;
        }else{
            return carID;
        }
    }

    public Integer deleteCarFromUser(int carID){
        SQLiteDatabase db = this.getWritableDatabase();
        deleteNotifWhenCarRemoved(carID);
        return db.delete(USER_CARS_TABLE_NAME, "carid = ?", new String[]{String.valueOf(carID)});
    }

    public boolean updateCarInfo(int carID, String carMake, String carModel, String carYear, String carInfo, String imageURI){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CAR_MAKE, carMake);
        contentValues.put(USER_CAR_MODEL, carModel);
        contentValues.put(USER_CAR_YEAR, carYear);
        contentValues.put(USER_CAR_INFO, carInfo);
        contentValues.put(IMAGE_URI, imageURI);
        db.update(USER_CARS_TABLE_NAME, contentValues, "carid = ?", new String[]{String.valueOf(carID)});
        return true;
    }

    public Cursor getCarsFromUser(int userID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + USER_CARS_TABLE_NAME + " where ownerid =" + userID + ";", null);
        return result;
    }

    public Cursor getCarInfo(int carID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + USER_CARS_TABLE_NAME + " where carid =" + carID + ";", null);
        return result;
    }



    /*public Integer deleteAllCarsFromUser(int userID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_CARS_TABLE_NAME, "ownerid = ?", new String[]{String.valueOf(userID)});
    }*/


    /**
     * Funções de auxílio na comunicação com a base de dados local -> Calendar
     */
    public long addNotif(int userID, String notiftitle, String notifbody, String notifdate, int carID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIF_USER_ID, userID);
        contentValues.put(NOTIF_TITLE, notiftitle);
        contentValues.put(NOTIF_BODY, notifbody);
        contentValues.put(NOTIF_DATE, notifdate);
        contentValues.put(NOTIF_CAR_ID, carID);
        long notifID = db.insert(CALENDAR_TABLE_NAME, null, contentValues);

        if(notifID == -1){
            return -1;
        }else{
            return notifID;
        }
    }

    public Integer deleteNotif(int notifID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CALENDAR_TABLE_NAME, "notifid = ?", new String[]{String.valueOf(notifID)});
    }

    public Integer deleteNotifWhenCarRemoved(int carID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CALENDAR_TABLE_NAME, "carid = ?", new String[]{String.valueOf(carID)});
    }

    public Cursor getNotifList(int userID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CALENDAR_TABLE_NAME + " where userid =" + userID + ";", null);
        return result;
    }

    public boolean updateNotif(int notifID, String notiftitle, String notifbody, String notifdate, int userid, int carid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIF_TITLE, notiftitle);
        contentValues.put(NOTIF_BODY, notifbody);
        contentValues.put(NOTIF_DATE, notifdate);
        contentValues.put(NOTIF_USER_ID, userid);
        contentValues.put(NOTIF_CAR_ID, carid);
        db.update(USER_CARS_TABLE_NAME, contentValues, "notifid = ?", new String[]{String.valueOf(notifID)});
        return true;
    }

    public void loadJSONFromAsset(SQLiteDatabase db, Context context) {
        System.out.println("loadJSONFromAsset");
        String json = null;
        try {
            InputStream is = context.getAssets().open("cars.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        loadAllCars(db, json);
    }


}
