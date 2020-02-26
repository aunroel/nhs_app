package com.uk.ac.ucl.carefulai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uclsse.comp0102.nhsxapp.api.NhsAPI;
import uclsse.comp0102.nhsxapp.api.NhsDataClass;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "User_data.db";
    public static final String TABLE_NAME = "Weekly_data";
    public static final String TABLE_NAME_2 = "Weekly_Feedback";
    public static final String COL_1 = "WEEK_NUM";
    public static final String COL_2 = "STEPS_COUNTED";
    public static final String COL_3 = "CALLS_COUNT";
    public static final String COL_4 = "TEXTS_COUNT";
    public static final String COL_5 = "SCORE";
    public static final String COL_2_2 = "SCORE";
    public static final String COL_3_2 = "FEEDBACK";

    private NhsAPI nhsAPI;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        Context appContext = context.getApplicationContext();
        nhsAPI = NhsAPI.Companion.getInstance(appContext);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (WEEK_NUM INTEGER PRIMARY KEY AUTOINCREMENT, STEPS_COUNTED INTEGER, CALLS_COUNT INTEGER, TEXTS_COUNT INTEGER, SCORE INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + " (WEEK_NUM_2 INTEGER PRIMARY KEY AUTOINCREMENT, SCORE INTEGER, FEEDBACK TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }

    public boolean insertData(int stepscount, int callscount, int textcount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, stepscount);
        contentValues.put(COL_3, callscount);
        contentValues.put(COL_4, textcount);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) return false;
        NhsDataClass data = new NhsDataClass.Builder()
                .setStepNumber(stepscount)
                .setCallNumber(callscount)
                .setTextNumber(textcount)
                .build();
        nhsAPI.record(data);
        return true;
    }

    public boolean insertFeedback(int score, String feedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_2, score);
        contentValues.put(COL_3_2, feedback);
        long result = db.insert(TABLE_NAME_2, null, contentValues);
        if (result == -1) return false;
        NhsDataClass data = new NhsDataClass.Builder()
                .setRealScore(score)
                .build();
        nhsAPI.record(data);
        return true;
    }

    public boolean insertScore(String week, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5, score);
        db.update(TABLE_NAME, contentValues, "WEEK_NUM = ?", new String[]{week});
        NhsDataClass data = new NhsDataClass.Builder()
                .setPredictedScore(score)
                .build();
        nhsAPI.record(data);
        return true;
    }

    public boolean insertScore2(String week, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET SCORE = " + score + " WHERE WEEK_NUM = " + week);
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
        Cursor res = db.rawQuery("SELECT SUM(" + COL_2 + "), SUM(" + COL_3 + "), + SUM(" + COL_4 + "), + SUM(" + COL_5 + "), " + "FROM" + TABLE_NAME + " ORDER BY " + COL_1 + " DESC LIMIT 1;", null);
        return res;
    }

    public Cursor getAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_2 + ", " + COL_3 + ", " + COL_4 + ", " + COL_5 + " FROM " + TABLE_NAME + ";", null);
        return res;
    }

    public boolean noFeedback() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_2, null);
        if (res != null) {
            res.moveToFirst();
            return res.getInt(0) == 0;
        }
        return true;
    }

    public Cursor getFeedback() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_2_2 + " FROM " + TABLE_NAME_2, null);
        return res;
    }

    public Cursor getFeedbackMessage() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_3_2 + " FROM " + TABLE_NAME_2, null);
        return res;
    }

    public Cursor getScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_5 + " FROM " + TABLE_NAME, null);
        return res;
    }

    public Cursor getLastLine() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_2 + ", " + COL_3 + ", " + COL_4 + ", " + COL_5 + "FROM " + TABLE_NAME + " ORDER BY " + COL_1 + " DESC LIMIT 1;", null);
        return res;
    }

    public Cursor getDataToClassify() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_2 + ", " + COL_3 + ", " + COL_4 + ", " + COL_5 + " FROM " + TABLE_NAME + " ORDER BY " + COL_1 + " DESC LIMIT 1;", null);
        return res;
    }

    public boolean isdbempty() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (res != null) {
            res.moveToFirst();
            return res.getInt(0) == 0;
        }
        return true;
    }

}
