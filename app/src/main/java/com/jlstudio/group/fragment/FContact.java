package com.jlstudio.group.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.activity.ContactsDataActivity;
import com.jlstudio.group.adapter.FMyContactSubAdapter;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.util.JsonToPubhlishData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FContact extends Fragment implements ExpandableListView.OnChildClickListener {
    private View view;//fragment视图
    private SwipeRefreshLayout refresh;
    private ExpandableListView listView;
    private List<Groups> listParent;
    private List<List<MyContact>> listChild;
    private FMyContactSubAdapter adapter;
    private GetDataNet gn;//网络连接
    private String role;//角色
    private DBOption db;//数据库连接
    private CatchData data;//数据库缓存数据
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        Config.isLogin(getActivity());
        initView();
        gn = GetDataNet.getInstence(getActivity());
        db = new DBOption(getActivity());
        getDataFromNet();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();

    }

    //初始化数据
    private void initData() {
        data = db.getCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername());
        if (data == null) {
            getDataFromNet();
        } else {
            refreshList(data.getContent());
            ProgressUtil.closeProgressDialog();
        }
    }

    private void initView() {
        listView = (ExpandableListView) view.findViewById(R.id.listView);
        listParent = new ArrayList<>();
        listChild = new ArrayList<>();
        adapter = new FMyContactSubAdapter(getActivity(),listParent,listChild);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(this);
        refresh.setDistanceToTriggerSync(100);
        refresh.setColorSchemeColors(R.color.fresh_one, R.color.fresh_two, R.color.fresh_three, R.color.fresh_four);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet();
            }
        });
        role = Config.loadUser(getActivity()).getRole();
    }
    /**
     * 从网络上获取数据
     */
    private void getDataFromNet() {
        ProgressUtil.showProgressDialog(getActivity(), "数据加载中...");
        JSONObject json = new JSONObject();
        try {
            json.put("username", Config.loadUser(getActivity()).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.GROUPS, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                refreshList(s);
                if (data == null) {
                    db.insertCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
                } else {
                    db.updateCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
                }
                ProgressUtil.closeProgressDialog();
                refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe", "获取数据失败");
                ProgressUtil.closeProgressDialog();
                refresh.setRefreshing(false);
            }
        }, json.toString());
    }

    /**
     * 网络数据获取成功，刷新列表
     * @param content
     */
    private void refreshList(String content) {
        listParent = JsonToPubhlishData.getGroup(content, "普通");
        listChild = JsonToPubhlishData.getContacts(content, listParent);
        adapter.setList(listParent, listChild);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(getActivity(), ContactsDataActivity.class);
        intent.setAction(listChild.get(groupPosition).get(childPosition).getUid());
        intent.putExtra("name",listParent.get(groupPosition).getGroup_name());
        startActivity(intent);
        return true;
    }
}
