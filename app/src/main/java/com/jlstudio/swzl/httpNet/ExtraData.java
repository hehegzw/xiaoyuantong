package com.jlstudio.swzl.httpNet;

import com.jlstudio.swzl.bean.commons;
import com.jlstudio.swzl.bean.lostfound;

import java.util.ArrayList;

/**
 * Created by Joe on 2015/10/27.
 */
public class ExtraData {
    //从服务器接受的全部失物招领数据
    public static ArrayList<lostfound> All_lostf = new ArrayList<lostfound>();
    //0代表全部失物招领，1代表失物，2代表招领
    public static int LOSTANDFOUND = 0;
    public static int LOST = 1;
    public static int FOUND = 2;
    //从服务器接受的该条目的所有失物招领评论数据
    public static ArrayList<commons> list_conmmons = new ArrayList<commons>();

}
