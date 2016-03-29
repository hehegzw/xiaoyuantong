package com.jlstudio.main.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jlstudio.group.bean.Contacts;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.weather.model.City;
import com.jlstudio.weather.model.County;
import com.jlstudio.weather.model.Province;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gzw on 2015/10/20.
 */
public class DBOption {
    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public DBOption(Context context) {
        myDatabase = new MyDatabase(context);
        db = myDatabase.getWritableDatabase();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public void insertCatch(String url,String content,String time){
        ContentValues values = new ContentValues();
        values.put("url",url);
        values.put("content",content);
        values.put("time",time);
        db.insert("catch", null, values);
    }
    public CatchData getCatch(String url){
        Cursor cursor = db.query(MyDatabase.TABLE_CATCH, null, "url = ?", new String[]{url},
                null, null, null);
        if(cursor.moveToNext()){
            return new CatchData(cursor.getString(1),cursor.getString(2),cursor.getString(3),true);
        }else{
            return null;
        }
    }
    public void updateCatch(String url,String content,String time){
        ContentValues values = new ContentValues();
        values.put("url",url);
        values.put("content",content);
        values.put("time",time);
        db.update(MyDatabase.TABLE_CATCH, values, "url=?", new String[]{url});
    }
    public void deleteCatch(String url){
        ContentValues values = new ContentValues();
        values.put("url",url);
        db.delete(MyDatabase.TABLE_CATCH,"url=?",new String[]{url});
    }
    public void insertProvince(Province p) {
        if (p != null) {
            ContentValues values = new ContentValues();
            values.put("province_name",p.getName());
            values.put("province_code",p.getCode());
            db.insert("province", "province_name", values);
        }
    }

    public void insertCity(City c) {
        if (c != null) {
            ContentValues values = new ContentValues();
            values.put("city_name",c.getName());
            values.put("city_code",c.getCode());
            values.put("province_id",c.getProvince());
            db.insert("city", "city_name", values);
        }
    }

    public void insertCountry(County c) {
        if (c != null) {
            ContentValues values = new ContentValues();
            values.put("county_name",c.getName());
            values.put("county_code",c.getCode());
            values.put("city_id",c.getCity());
            myDatabase.getWritableDatabase().insert("county", "country_name", values);
        }
    }
    public List<Province> getProvinces(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("province", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                Province p = new Province(cursor.getString(cursor.getColumnIndex("province_code")),cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(p);
            }while(cursor.moveToNext());
            return list;
        }
        return null;
    }
    public List<City> getCitys(String id){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("city", null, "province_id=?", new String[]{id}, null, null, null);
        if(cursor.moveToFirst()){
            do{
                City p = new City(cursor.getString(cursor.getColumnIndex("city_code")),cursor.getString(cursor.getColumnIndex("city_name")),cursor.getString(cursor.getColumnIndex("province_id")));
                list.add(p);
            }while(cursor.moveToNext());
            return list;
        }
        return null;
    }
    public List<County> getCountrys(String id){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("county", null, "city_id=?",new String[]{id} , null, null, null);
        if(cursor.moveToFirst()){
            do{
                String code = cursor.getString(cursor.getColumnIndex("county_code"));
                String name = cursor.getString(cursor.getColumnIndex("county_name"));
                String city_id = cursor.getString(cursor.getColumnIndex("city_id"));
                County p = new County(cursor.getString(cursor.getColumnIndex("county_code")),cursor.getString(cursor.getColumnIndex("county_name")),cursor.getString(cursor.getColumnIndex("city_id")));
                list.add(p);
            }while(cursor.moveToNext());
            return list;
        }
        return null;
    }


    /**
     * 查询最近联系人
     *
     * @return
     */
    public List<Contacts> getRecentContacts(String userid) {
        List<Contacts> contactsList = new ArrayList<>();
        Cursor cursor = db.query("recent_contacts", null, "userid=?", new String[]{userid}, null, null, "contacts_time desc");
        if (cursor.moveToFirst()) {
            do {
                Contacts contacts = new Contacts();
                contacts.setUsername(cursor.getString(cursor.getColumnIndex("contacts_id")));
                contacts.setRealname(cursor.getString(cursor.getColumnIndex("contacts_name")));
                contacts.setTime(cursor.getString(cursor.getColumnIndex("contacts_time")));
                contactsList.add(contacts);
            } while (cursor.moveToNext());
            return contactsList;
        }
        return null;
    }

    /**
     * 插入最近联系人数据
     *
     * @param contacts
     */
    public void insertRecentContacts(Contacts contacts) {
        if (contacts != null) {
            ContentValues values = new ContentValues();
            values.put("contacts_id", contacts.getContacts_id());
            values.put("contacts_name", contacts.getContacts_name());
            values.put("userid", contacts.getUsername());
            db.insert("recent_contacts", "contacts_id", values);
        }
    }

    /**
     * 最近联系人删除
     *
     * @param contacts_id
     */
    public void deleteRecentContacts(String contacts_id, String userid) {
        db.delete("recent_contacts", "contacts_id=? and userid=?", new String[]{contacts_id, userid});
    }

    /**
     * 更新最近联系人数据
     *
     * @param contacts
     */
    public void updateRecentContacts(Contacts contacts) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues values = new ContentValues();
        values.put("contacts_id", contacts.getContacts_id());
        values.put("contacts_name", contacts.getContacts_name());
        values.put("contacts_time", sdf.format(new Date()));
        values.put("userid", contacts.getUsername());
        db.update("recent_contacts", values, "contacts_id=? and userid=?", new String[]{contacts.getContacts_id(), contacts.getUsername()});
    }
}
