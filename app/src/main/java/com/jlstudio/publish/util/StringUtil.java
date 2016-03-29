package com.jlstudio.publish.util;

/**
 * Created by gzw on 2015/10/21.
 */
public class StringUtil {
    public static boolean isEmpty(String string){
        if(string == null ||string.equals("")){
            return true;
        }
        return false;
    }
    public static boolean isEmpty(String... string){
        for(String s : string){
            if(s == null ||s.equals("")){
                return true;
            }
        }
        return false;
    }
}
