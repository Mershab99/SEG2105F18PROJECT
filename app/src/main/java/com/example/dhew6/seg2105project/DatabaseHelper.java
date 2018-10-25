package com.example.dhew6.seg2105project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME = "USER_TABLE";
    public static final String COL_1 ="id";
    public static final String COL_2 ="FName";
    public static final String COL_3 ="LName";
    public static final String COL_4 ="email";
    public static final String COL_5 ="password";
    public static final String COL_6 ="TypeOfUser";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"+COL_3+" TEXT,"+COL_4+" TEXT,"+COL_5+" TEXT,"+COL_6+" TEXT)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

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

    public boolean createUser(String firstName, String lastName, String password, String email, String userType){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(COL_2, firstName);
        content.put(COL_3, lastName);
        content.put(COL_4, email);
        content.put(COL_5, password);
        content.put(COL_6,userType);

        return true;
    }


}
