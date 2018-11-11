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
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Constants to store Database name, table name and names of columns.
    public static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_NAME = "userTable";
    public static final String COL_1 ="id";
    public static final String COL_2 ="Name";
    public static final String COL_3 ="userName";
    public static final String COL_4 ="Password";
    public static final String COL_5 ="email";
    public static final String COL_6 ="typeOfUser";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    /**
     * runs an SQL command to create a table with the default name and default columns
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "( " + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT, " +COL_3+ " TEXT, " +COL_4+ " TEXT, " +COL_5+ " TEXT, " +COL_6+ " TEXT" + ")");
        Log.d("db", "CREATE USER TABLE");
    }

    /**
     * onUpgrade drops the table if it exists then creates the database
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Validates the existence of this user if they are trying to create a new user with certain credentials
     *
     * @param userName
     * @param email
     * @return false if the new user is valid returns true if the new user is not valid
     */
    public boolean validateNewUser(String userName, String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res1 = db.rawQuery("select "+COL_3+ " from " + TABLE_NAME + " WHERE " +COL_3+ " = "+"'"+userName+"'", null);
        Cursor res2 = db.rawQuery("select "+COL_5+ " from " + TABLE_NAME + " WHERE " +COL_5+ " = "+"'"+email+"'", null);

        if(res1.getCount()==0 && res2.getCount()==0)
            return true;
        else return false;
    }

    /**
     * validates your login
     * @param userName
     * @param passWord
     * @return -1 if the user doesn't exist, 0 if the passwords don't match and 1 if they successfully log in.
     */
    public int validateLogin(String userName, String passWord){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] arr = new String[]{COL_3, COL_4};
        String selection = COL_3 + " = ?";
        String[] selectionArgs = { userName };

        Cursor cursor = db.query(TABLE_NAME, arr, selection, selectionArgs, null, null, null, null);
        String cursorPass, cursorUsername;
        cursorPass = cursorUsername = "";


        while(cursor.moveToNext()){
            cursorUsername = cursor.getString(cursor.getColumnIndex(COL_3));
            cursorPass = cursor.getString(cursor.getColumnIndex(COL_4));
        }
        cursor.close();

        //empty therefore username doesn't exist
        if(cursorPass.equals("") || cursorUsername.equals("")){
            return -1;
        }

        //doesn't equal password
        if(!cursorPass.equals(passWord)){
            return 0;
        }

        //good login.
        return 1;

    }

    /**
     * creates new user given values for fullName, userName, password, email, and userType
     * then attempts to insert it into the database
     *
     * @param fullName
     * @param userName
     * @param password
     * @param email
     * @param userType
     */
    //Creates a user in the database with the given credentials
    public void createUser(String fullName, String userName, String password, String email, String userType){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(COL_2, fullName);
        content.put(COL_3, userName);
        content.put(COL_4, password);
        content.put(COL_5, email);
        content.put(COL_6,userType);


        db.insert(TABLE_NAME,null,content);
    }

    /**
     * Outputs all the users in an ArrayList
     *
     * @return ArrayList<User> of all the users
     */
    //
    public ArrayList<User> displayAllUsers(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        ArrayList<User> userList = new ArrayList<User>();
        //res.moveToFirst(); this causes unintended behaviour, makes first user in list not be included
        while(res.moveToNext()){
            if(res.getString(5).equals(User.HomeOwner)){
                HomeOwner temp = new HomeOwner(res.getString(1),res.getString(2),res.getString(3),res.getString(4));
                userList.add(temp);
            }
            else if(res.getString(5).equals(User.ServiceProvider)){
                ServiceProvider temp = new ServiceProvider(res.getString(1),res.getString(2),res.getString(3),res.getString(4));
                userList.add(temp);
            }
        }

        return userList;
    }

    /**
     * retrieve user and create the correct type of user
     * @param userName username of the user you want to access
     * @return user of correct type
     */
    public User getUser(String userName){

        SQLiteDatabase db = this.getReadableDatabase();
        String[] allColumns = new String[]{COL_1,COL_2, COL_3, COL_4, COL_5, COL_6};
        String selection = COL_3 + " = ?";
        String[] selectionArgs = { userName };

        Cursor cursor = db.query(TABLE_NAME, allColumns, selection, selectionArgs, null, null, null, null);
        String name, resUsername, password, email, userType;
        name = resUsername = password = email = userType = "";

        while(cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex(COL_2));
            resUsername = cursor.getString(cursor.getColumnIndex(COL_3));
            password = cursor.getString(cursor.getColumnIndex(COL_4));
            email = cursor.getString(cursor.getColumnIndex(COL_5));
            userType = cursor.getString(cursor.getColumnIndex(COL_6));
        }
        cursor.close();

        if(userType.equals(User.HomeOwner)){
            return new HomeOwner(name, resUsername, password, email);
        }else if(userType.equals(User.ServiceProvider)){
            return new ServiceProvider(name, resUsername, password, email);
        }
        return null;
    }




}
