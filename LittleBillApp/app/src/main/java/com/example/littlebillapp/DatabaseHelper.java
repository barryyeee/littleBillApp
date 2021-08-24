package com.example.littlebillapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COST_TITLE="cost_title";
    public static final String COST_DATE="cost_date";
    public static final String COST_MONEY="cost_money";
    public static final String COST="cost";

    public DatabaseHelper(Context context){
        super(context,"daily",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table if not exists cost(" +
                        "id integer primary key, " +
                        "cost_title varchar, " +
                        "cost_money varchar," +
                        "cost_date varchar)");
    }
    public void insertCost(CostBean costBean){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COST_TITLE,costBean.costTitle);
        cv.put(COST_MONEY,costBean.costMoney);
        cv.put(COST_DATE,costBean.costDate);
        database.insert(COST,null,cv);
    }
    public Cursor getAllCostData(){
        SQLiteDatabase database=getWritableDatabase();
        return database.query("cost",null,null,null,null,null,COST_DATE+ " ASC");
    }
    public void deleteAllData()
    {
        SQLiteDatabase database=getWritableDatabase();
        database.delete(COST,null,null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
