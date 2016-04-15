package com.jlstudio.publish.util;

import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.bean.Person;
import com.jlstudio.publish.bean.PublishData;
import com.jlstudio.publish.bean.PublishListData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzw on 2015/10/20.
 */
public class JsonToPubhlishData {

    /**
     * 获取收到的信息列表
     *
     * @param string
     * @param type   收到的还是发送的
     * @return
     */
    public static List<PublishListData> getePublishListData(String string, String type) {
        List<PublishListData> publish = new ArrayList<>();
        List<PublishListData> apply = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                PublishListData p = new PublishListData();
                p.setTitle(o.getString("title"));
                p.setTime(o.getString("time"));
                p.setNoticeid(o.getString("noticeid"));
                if (type.equals("receive")||type.equals("apply"))
                    p.setFlag(o.getString("flag"));
                else
                    p.setNoticegroup(o.getString("noticegroup"));
                if(o.getString("title").equals("好友申请")){
                    apply.add(p);
                }else{
                    publish.add(p);
                }
            }
            if(type.equals("apply")){
                return apply;
            }else{
                return publish;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return apply;
    }

    /**
     * 获取通知详情
     *
     * @param string
     * @param type   receive send
     * @return
     */
    public static PublishData getPublishDataItem(String string, String type) {
        try {
            JSONObject json = new JSONObject(string);
            PublishData p = new PublishData();
            p.setTitle(json.getString("noticetitle"));
            if (json.has("noticetext"))
                p.setContent(json.getString("noticetext"));
            else
                p.setContent("");
            if (json.has("filePath")) {
                p.setFilePath(json.getString("filePath"));
            } else {
                p.setFilePath("");
            }
            if (type.equals("receive")) {
                p.setFlag(json.getString("noticereceiveflag"));
                p.setUserFrom(json.getString("realname"));
            } else {
                p.setReplyedCount(json.getInt("replyCount") + "");
                p.setUnReplyCount(json.getInt("unReplyCount") + "");
            }
//            p.setNoticefromid(json.getString("noticefrom"));
//            p.setNoticefromname(json.getString("realname"));
            p.setTime(json.getString("datetime"));
            return p;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取系别
     *
     * @param string
     * @return
     */
    public static List<String> getDepartment(String string) {
        String[] str = string.split(",");
        List<String> list = new ArrayList<>();
        for (String s : str) {
            list.add(s);
        }
        return list;
    }

    /**
     * 获取未回执的人
     *
     * @param string
     * @return
     */
    public static List<MyContact> getPerson(String string) {
        List<MyContact> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                MyContact p = new MyContact();
                p.setName(o.getString("userrealname"));
                p.setPhone(o.getString("userphone"));
                list.add(p);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取联系人
     *
     * @param string
     * @return
     */
    public static List<MyContact> getPersonFromJson(String string) {
        List<MyContact> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            if(!json.has("datas"))return list;
            JSONArray array = json.getJSONArray("datas");
            for (int j = 0; j < array.length(); j++) {
                JSONObject o = array.getJSONObject(j);
                MyContact contact = new MyContact();
                contact.setUid(o.getString("userid"));
                contact.setName(o.getString("userrealname"));
                if(o.getInt("isregister") == 1){
                    contact.setIsRegister(true);
                }else{
                    contact.setIsRegister(false);
                }
                contact.setPhone(o.getString("userphone"));
                list.add(contact);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串提取组信息
     *
     * @param string
     * @return
     */
    public static List<Groups> getGroup(String string, String role) {
        List<Groups> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            if (role.equals("普通")) {
                JSONArray array = json.getJSONArray("groupNameList");
                for (int i = 0; i < array.length(); i++) {
                    Groups contact = new Groups();
                    contact.setGroup_name(array.getJSONObject(i).getString("groupName"));
                    contact.setRegisterCount(array.getJSONObject(i).getInt("registerCount"));
                    contact.setSubCounts(array.getJSONObject(i).getInt("totleCount"));
                    list.add(contact);
                }
            } else {
                Groups contact = new Groups();
                contact.setGroup_name("我的分组");
                contact.setRegisterCount(json.getInt("groupNameListregisterCount"));
                contact.setSubCounts(json.getInt("groupNameListtotleCount"));
                list.add(contact);
                //解析用户分组
                if (json.has("grade")) {
                    JSONArray array1 = json.getJSONArray("grade");
                    for (int i = 0; i < array1.length(); i++) {
                        contact = new Groups();
                        contact.setGroup_name(array1.getJSONObject(i).getString("groupName"));
                        contact.setRegisterCount(array1.getJSONObject(i).getInt("registerCount"));
                        contact.setSubCounts(array1.getJSONObject(i).getInt("totleCount"));
                        list.add(contact);
                    }
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串提取联系人
     *
     * @param string
     * @param group  联系人所属组
     * @return
     */
    public static List<List<MyContact>> getContacts(String string, List<Groups> group) {
        List<List<MyContact>> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            for (int i = 0; i < group.size(); i++) {
                List<MyContact> item = new ArrayList<>();
                JSONArray array = json.getJSONArray(group.get(i).getGroup_name());
                for (int j = 0; j < array.length(); j++) {
                    JSONObject o = array.getJSONObject(j);
                    MyContact contact = new MyContact();
                    contact.setUid(o.getString("username"));
                    contact.setName(o.getString("realname"));
                    contact.setSign(o.has("signature")?o.getString("signature"):"");
                    if(o.getInt("isRegister") == 1)
                        contact.setIsRegister(true);
                    else
                        contact.setIsRegister(false);
                    contact.setPhone(o.getString("phone"));
                    item.add(contact);
                }
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串提取班级信息
     *
     * @param string
     * @param group  班级所属组
     * @return
     */
    public static List<List<Groups>> getGroupSub(String string, List<Groups> group) {
        List<List<Groups>> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            for (int i = 0; i < group.size(); i++) {
                List<Groups> item = new ArrayList<>();
                JSONArray array = null;
                if (group.get(i).getGroup_name().equals("我的分组")) {
                    array = json.getJSONArray("groupNameList");
                } else {
                    array = json.getJSONArray(group.get(i).getGroup_name());
                }
                for (int j = 0; j < array.length(); j++) {
                    Groups contact = new Groups();
                    contact.setGroup_name(array.getJSONObject(j).getString("groupName"));
                    contact.setRegisterCount(array.getJSONObject(j).getInt("registerCount"));
                    contact.setSubCounts(array.getJSONObject(j).getInt("totleCount"));
                    item.add(contact);
                }
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //解析服务器返回的需要发送短信的联系人
    public static String[] getResultPeople(String string) {
        return string.split(",");
    }


    public static List<Contacts> getSingleContacts(String string) {
        List<Contacts> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Contacts contact = new Contacts();
                contact.setRealname(o.getString("realname"));
                contact.setUsername(o.getString("username"));
                contact.setSign(o.has("signature")?o.getString("signature"):"");
                contact.setPhone(o.getString("phone"));
                if(o.getInt("isRegister") == 1)
                    contact.setIsRegister(true);
                else
                    contact.setIsRegister(false);
                list.add(contact);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索返回的结果
     * @param string
     * @return
     */
    public static List<Contacts> getSearchData(String string) {
        List<Contacts> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Contacts contact = new Contacts();
                contact.setRealname(o.getString("userrealname"));
                contact.setUsername(o.getString("userid"));
                contact.setClasses(o.getString("classname"));
                contact.setPhone(o.has("userphone")?o.getString("userphone"):"");
                if(o.has("usersignature")){
                    contact.setSign(o.getString("usersignature"));
                }else{
                    contact.setSign("这家伙什么都没写");
                }

                if(o.getInt("isregister") == 1)
                    contact.setIsRegister(true);
                else
                    contact.setIsRegister(false);
                list.add(contact);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json字符串提取组信息
     *
     * @param string
     * @return
     */
    public static List<Groups> getGroup(String string) {
        List<Groups> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("groupNameList");
            for (int i = 0; i < array.length(); i++) {
                Groups contact = new Groups();
                contact.setGroup_name(array.getJSONObject(i).getString("groupName"));
                contact.setRegisterCount(8);
                contact.setSubCounts(10);
                list.add(contact);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Groups> getGrade(String string) {
        List<Groups> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("grade");
            for (int i = 0; i < array.length(); i++) {
                Groups contact = new Groups();
                contact.setGroup_name(array.getJSONObject(i).getString("groupName"));
                list.add(contact);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<List<Groups>> getGroupSub2(String string, List<Groups> group) {
        List<List<Groups>> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            for (int i = 0; i < group.size(); i++) {
                List<Groups> item = new ArrayList<>();
                JSONArray array = json.getJSONArray(group.get(i).getGroup_name());
                for (int j = 0; j < array.length(); j++) {
                    Groups contact = new Groups();
                    contact.setGroup_name(array.getJSONObject(j).getString("groupName"));
                    contact.setRegisterCount(8);
                    contact.setSubCounts(10);
                    item.add(contact);
                }
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Groups> getGroup2(String string) {
        List<Groups> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("groupNameList");
            for (int i = 0; i < array.length(); i++) {
                Groups contact = new Groups();
                contact.setGroup_name(array.getJSONObject(i).getString("groupName"));
                contact.setRegisterCount(8);
                contact.setSubCounts(10);
                list.add(contact);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Contacts> MygetContacts(String string, List<Groups> group) {
        List<Contacts> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.getJSONArray("allPeople");
            for (int j = 0; j < array.length(); j++) {
                JSONObject o = array.getJSONObject(j);
                Contacts contacts = new Contacts();
                contacts.setRealname(o.getString("realname"));
                contacts.setUsername(o.getString("username"));
                if(o.getInt("isRegister") == 1)
                    contacts.setIsRegister(true);
                else
                    contacts.setIsRegister(false);
                list.add(contacts);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
