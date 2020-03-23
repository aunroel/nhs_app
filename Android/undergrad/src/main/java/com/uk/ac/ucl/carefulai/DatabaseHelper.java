package com.uk.ac.ucl.carefulai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "User_data.db";
    public static final String TABLE_NAME = "Weekly_data";
    public static final String COL_1 = "WEEK_NUM";
    public static final String COL_2 = "STEPS_COUNTED";
    public static final String COL_3 = "CALLS_COUNT";
    public static final String COL_4 = "TEXTS_COUNT";
    public static final String COL_5 = "SCORE";
    public static final String COL_6 = "USER_SCORE";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (WEEK_NUM INTEGER PRIMARY KEY AUTOINCREMENT, STEPS_COUNTED INTEGER, CALLS_COUNT INTEGER, TEXTS_COUNT INTEGER, SCORE INTEGER, USER_SCORE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int stepscount, int callscount,int textcount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, stepscount);
        contentValues.put(COL_3, callscount);
        contentValues.put(COL_4, textcount);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }


    public boolean insertScore(String week, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5, score);
        db.update(TABLE_NAME, contentValues, "WEEK_NUM = ?", new String[] {week});
        return true;
    }

    public boolean insertUserScore(String week, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_6, score);
        db.update(TABLE_NAME, contentValues, "WEEK_NUM = ?", new String[] {week});
        return true;
    }

    public long getThisWeekNumber() {
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT SUM(" + COL_2 + "), SUM(" + COL_3 + "), + SUM(" + COL_4 + "),+ SUM(" + COL_5 + "), + SUM(" + COL_6 + "), " + "FROM" + TABLE_NAME + " ORDER BY " + COL_1 + " DESC LIMIT 1;", null);
        return res;
    }

    public Cursor getScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_5 + " FROM " + TABLE_NAME, null);
        return res;
    }

    public Cursor getAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_1 + ", "+ COL_2 + ", " + COL_3 + ", " + COL_4 + ", " + COL_5 + " FROM " + TABLE_NAME + ";", null);
        return res;
    }

    public Cursor getLastLine() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + COL_2 + ", " + COL_3 + ", " + COL_4 + ", " + COL_5 + ", " + COL_6 + ", " + "FROM" + TABLE_NAME + " ORDER BY " + COL_1 + " DESC LIMIT 1;", null);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
//        db.execSQL("CREATE TABLE " + TABLE_NAME + " (WEEK_NUM INTEGER PRIMARY KEY AUTOINCREMENT, STEPS_COUNTED INTEGER, CALLS_COUNT INTEGER, TEXTS_COUNT INTEGER, SCORE INTEGER, USER_SCORE INTEGER)");
    }
    public boolean isdbempty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (res != null) {
            res.moveToFirst();
            if (res.getInt (0) == 0) {
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }

}
