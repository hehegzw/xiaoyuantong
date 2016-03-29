package com.jlstudio.group.util;

import com.jlstudio.group.bean.Contacts;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by NewOr on 2015/11/10.
 */
public class SortListUtil {

    /**
     * 重新排序获得一个新的List集合
     *
     * @param allNames
     */
    public static  List<Contacts> sortList(String[] allNames, List<Contacts> persons, List<Contacts> newPersons) {
        for (int i = 0; i < allNames.length; i++) {
            //判断时索引，还是姓名
            if (allNames[i].length() != 1) {
                for (int j = 0; j < persons.size(); j++) {
                    if (allNames[i].equals(persons.get(j).getPinYinName())) {
                        Contacts p = new Contacts();
                        p.setRealname(persons.get(j).getRealname());
                        p.setPinYinName(persons.get(j).getPinYinName());
                        p.setSign(persons.get(j).getSign());
                        newPersons.add(p);
                    }
                }
            } else {
                Contacts c = new Contacts();
                c.setRealname(allNames[i]);
                newPersons.add(c);
            }
        }
        return newPersons;
    }

    /**
     * 获取排序后的新数据
     *
     * @param persons
     * @return
     */
    public static String[] sortIndex(List<Contacts> persons) {
        TreeSet<String> set = new TreeSet<String>();
        // 获取初始化数据源中的首字母，添加到set中
        for (Contacts person : persons) {
            set.add(StringHelper.getPinYinHeadChar(person.getRealname()).substring(
                    0, 1));
        }
        // 新数组的长度为原数据加上set的大小
        String[] names = new String[persons.size() + set.size()];
        int i = 0;
        for (String string : set) {
            names[i] = string;
            i++;
        }
        String[] pinYinNames = new String[persons.size()];
        for (int j = 0; j < persons.size(); j++) {
            persons.get(j).setPinYinName(
                    StringHelper
                            .getPingYin(persons.get(j).getRealname().toString()));
            pinYinNames[j] = StringHelper.getPingYin(persons.get(j).getRealname()
                    .toString());
        }
        // 将原数据拷贝到新数据中
        System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
        // 自动按照首字母排序
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }
}
