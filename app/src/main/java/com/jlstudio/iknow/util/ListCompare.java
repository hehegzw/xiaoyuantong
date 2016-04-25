package com.jlstudio.iknow.util;

import com.jlstudio.iknow.bean.ScoreItem;
import com.jlstudio.publish.util.StringUtil;

import java.util.Comparator;

/**
 * Created by gzw on 2015/11/8.
 */
public class ListCompare implements Comparator<ScoreItem>{
    @Override
    public int compare(ScoreItem a, ScoreItem b) {
        if(StringUtil.isEmpty(a.getScore(),b.getScore())){
            return 1;
        }
        int score1 = Integer.parseInt(a.getScore());
        int score2 = Integer.parseInt(b.getScore());
        if(score1 > score2){
            return -1;
        }else if(score1 == score2){
            return 0;
        }else{
            return 1;
        }
    }
}
