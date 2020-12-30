package com.example.projectcm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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


    //CALENDAR TABLE VARS
    public static final String NOTIF_ID = "notifid";
    public static final String NOTIF_TITLE = "notiftitle";
    public static final String NOTIF_BODY = "notifbody";
    public static final String NOTIF_USER_ID = "userid";
    public static final String NOTIF_CAR_ID = "carid";
    public static final String NOTIF_DATE = "notifdate";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
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
                USER_PASS + " TEXT )");

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
                OWNER_ID + " INTEGER )");

        //CALENDAR CREATE TABLE
        db.execSQL("create table " + CALENDAR_TABLE_NAME +
                "(" + NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTIF_TITLE + " TEXT, " +
                NOTIF_BODY + " TEXT, " +
                NOTIF_USER_ID + " INTEGER, " +
                NOTIF_CAR_ID + " INTEGER, " +
                NOTIF_DATE + " TEXT )");

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

    public boolean loginUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select userpassword from " + USER_TABLE_NAME + " where useremail ='" + email + "';", null);
        while (result.moveToNext()){
            if (result.getString(0).equals(password)){
                return true;
            }
        }
        return false;
    }

    public Cursor getUserInfo(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select username, useremail, userbirthday from " + USER_TABLE_NAME + " where id =" + id + ";", null);
        return result;
    }

    public Integer deleteUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //deleteAllCarsFromUser(id);
        return db.delete(USER_TABLE_NAME, "userid = ?", new String[]{String.valueOf(id)});
    }

    public boolean updateUser(int id, String userName, String userEmail, String userBirthday){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_EMAIL, userEmail);
        contentValues.put(USER_BIRTHDAY, userBirthday);
        db.update(USER_TABLE_NAME, contentValues, "userid = ?", new String[]{String.valueOf(id)});
        return true;
    }


    /**
     * Funções de auxílio na comunicação com a base de dados local -> Car List
     */
    public Cursor getAllMakes(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select carmake from " + CAR_LIST_TABLE_NAME, null);
        return result;
    }

    public Cursor getAllModelsFromAMake(String make){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select carmodel, caryear from " + CAR_LIST_TABLE_NAME + " where make ='" + make + "';", null);
        return result;
    }


    /**
     * Funções de auxílio na comunicação com a base de dados local -> User Car List
     */
    public long addACarToAUser(String carMake, String carModel, String carYear, String carInfo, int ownerID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CAR_MAKE, carMake);
        contentValues.put(USER_CAR_MODEL, carModel);
        contentValues.put(USER_CAR_YEAR, carYear);
        contentValues.put(USER_CAR_INFO, carInfo);
        contentValues.put(OWNER_ID, ownerID);
        long carID = db.insert(USER_CARS_TABLE_NAME, null, contentValues);

        if(carID == -1){
            return -1;
        }else{
            return carID;
        }
    }

    public Integer deleteCarFromUser(int carID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_CARS_TABLE_NAME, "carid = ?", new String[]{String.valueOf(carID)});
    }

    public boolean updateCarInfo(int carID, String carMake, String carModel, String carYear, String carInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CAR_MAKE, carMake);
        contentValues.put(USER_CAR_MODEL, carModel);
        contentValues.put(USER_CAR_YEAR, carYear);
        contentValues.put(USER_CAR_INFO, carInfo);
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
    public Cursor getUserNotifications(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CALENDAR_TABLE_NAME + " where userid =" + userid + ";", null);
        return result;
    }
    public Integer deleteNotif(int notifID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CALENDAR_TABLE_NAME, "notifid = ?", new String[]{String.valueOf(notifID)});
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


}
