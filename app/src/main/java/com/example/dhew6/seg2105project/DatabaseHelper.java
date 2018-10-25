/*
    Mershab Arafat
    SEG2105 Final Project
    DatabaseHelper class
    Class used to communicate between the database and the application.
 */


package com.example.dhew6.seg2105project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Constants to store Database name, table name and names of columns.
    public static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME = "USER_TABLE";
    public static final String COL_1 ="id";
    public static final String COL_2 ="Name";
    public static final String COL_3 ="email";
    public static final String COL_4 ="password";
    public static final String COL_5 ="typeOfUser";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"+COL_3+" TEXT,"+COL_4+" TEXT,"+COL_5+" TEXT)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Validates the existence of this user if they are trying to create a new user with certain credentials
    public boolean validateNewUser(String userName, String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res1 = db.rawQuery("select "+COL_2+" from " + TABLE_NAME + " WHERE " +COL_2+ "="+userName, null);
        Cursor res2 = db.rawQuery("select "+COL_4+" from " + TABLE_NAME + " WHERE " +COL_4+ "="+email, null);

        if(res1.getCount()==0 && res2.getCount()==0){
            return true;
        }
        else return false;
    }


    //Creates a user in the database with the given credentials
    public void createUser(String fullName, String password, String email, String userType){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(COL_2, fullName);
        content.put(COL_3, email);
        content.put(COL_4, password);
        content.put(COL_5, userType);

        db.insert(TABLE_NAME,null,content);
    }

    //Outputs all the users in an arraylist
    public ArrayList<User> displayAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        ArrayList<User> userList = new ArrayList<User>();

        while(res.moveToNext()){
            if(res.getString(5) == User.HomeOwner){
                HomeOwner temp = new HomeOwner(res.getString(1),res.getString(2),res.getString(3),res.getString(4));
                userList.add(temp);
            }
            else if(res.getString(5) == User.ServiceProvider){
                ServiceProvider temp = new ServiceProvider(res.getString(1),res.getString(2),res.getString(3),res.getString(4));
                userList.add(temp);
            }
        }

        return userList;
    }



}