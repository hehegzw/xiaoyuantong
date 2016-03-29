package com.jlstudio.main.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gzw on 2015/10/20.
 */
public class MyDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "datas";
    public static final String TABLE_PERSON = "persons";
    public static final String TABLE_CATCH = "catch";
    private static final String CREATE_PERSON = "create table persons(_id integer primary key autoincrement" +
            ",name text,classes text)";
    private static final String CREATE_CATCH = "create table catch(_id integer primary key autoincrement" +
            ",url text,content text,time text)";
    //天气
    private static String CREATE_PROVINCE_TABLE = "create table province(_id integer primary key autoincrement," +
            "province_name text,province_code text)";
    private static String CREATE_CITY_TABLE = "create table city(_id integer primary key autoincrement," +
            "city_name text,city_code text,province_id text)";
    private static String CREATE_COUNTRY_TABLE = "create table county(_id integer primary key autoincrement," +
            "county_name text,county_code text,city_id text)";
    private static String CREATE_RECENT_TABLE = "create table recent_contacts(_id integer primary key autoincrement," +
            "contacts_id vachar,contacts_name vachar,contacts_time TimeStamp NOT NULL DEFAULT (datetime('now','localtime')),userid vachar)";


    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSON);
        db.execSQL(CREATE_CATCH);
        db.execSQL(CREATE_PROVINCE_TABLE);
        db.execSQL(CREATE_CITY_TABLE);
        db.execSQL(CREATE_COUNTRY_TABLE);
        db.execSQL(CREATE_RECENT_TABLE);
        Log.d("hehe", "创建数据库成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
