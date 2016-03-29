package com.jlstudio.weather.util;


import com.jlstudio.main.application.MyApplication;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.weather.model.City;
import com.jlstudio.weather.model.County;
import com.jlstudio.weather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzw on 2015/10/5.
 */
public class XmlUtil {
    public static void xmltodb(String fileName,Success success){
        List<Province> provinces = null;
        List<City> citys = null;
        List<County> countries = null;
        Province p = null;
        City cy = null;
        County c = null;
        try {
            InputStream in = MyApplication.getContext().getAssets().open(fileName);
            XmlPullParser parser =  XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in,"UTF-8");
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        provinces = new ArrayList<Province>();
                        citys = new ArrayList<City>();
                        countries = new ArrayList<County>();
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("province")){
                            p = new Province(parser.getAttributeValue(0),parser.getAttributeValue(1));
                            provinces.add(p);
                        }else if(parser.getName().equals("city")){
                            cy = new City(parser.getAttributeValue(0),parser.getAttributeValue(1),p.getCode());
                            citys.add(cy);
                        }else if(parser.getName().equals("county")){
                            c = new County(parser.getAttributeValue(2),parser.getAttributeValue(1),cy.getCode());
                            countries.add(c);
                        }
                        break;
                }
                eventType = parser.next();
            }
            if(success!=null){
                success.onSuccess(provinces,citys,countries);
            }
            DBOption db = new DBOption(MyApplication.getContext());
            db.getDb().beginTransaction();
            if(provinces!=null){
                for(Province pp :provinces){
                    db.insertProvince(pp);
                }
            }
            if(citys!=null){
                for(City cc :citys){
                    db.insertCity(cc);
                }
            }
            if(countries!=null){
                for(County co :countries){
                    db.insertCountry(co);
                }
            }
            db.getDb().setTransactionSuccessful();
            db.getDb().endTransaction();
            db.getDb().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public interface Success{
        void onSuccess(List<Province> provinces, List<City> citys, List<County> countries);
    }
}
