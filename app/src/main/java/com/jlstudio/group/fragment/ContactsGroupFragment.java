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
import com.jlstudio.group.activity.ContactsActivity;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.adapter.FMyContactAdapter;
import com.jlstudio.publish.util.JsonToPubhlishData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsGroupFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private View view;
    private SwipeRefreshLayout refresh;
    private ExpandableListView listView;//显示分组
    private List<Groups> listParent;//年级，自定义分组
    private List<List<Groups>> listChild;//班级，自定义分组
    private FMyContactAdapter adapter;
    private GetDataNet gn;//网络连接
    private DBOption db;//数据库连接
    private CatchData data;//数据库缓存数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_contacts_group, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gn = GetDataNet.getInstence(getActivity());
        db = new DBOption(getActivity());
        initDate();
        initView();
    }

    private void initView() {
        listView = (ExpandableListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        listView.setOnChildClickListener(this);
        refresh.setDistanceToTriggerSync(100);
        refresh.setColorSchemeColors(R.color.fresh_one, R.color.fresh_two, R.color.fresh_three, R.color.fresh_four);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet();
            }
        });
    }

    private void initDate() {
        ProgressUtil.showProgressDialog(getActivity(), "数据加载中...");
        listParent = new ArrayList<>();
        listChild = new ArrayList<>();
        getDataFromNet();
        adapter = new FMyContactAdapter(getActivity(),listParent,listChild);
//        data = db.getCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(getActivity()).getUsername());
//        if (data == null) {
//            getDataFromNet();
//        } else {
//            refreshList(data.getContent());
//            ProgressUtil.closeProgressDialog();
//        }
    }
    /**
     * 从网络上获取数据
     */
    private void getDataFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", Config.loadUser(getActivity()).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "groups", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                refreshList(s);
//                if (data == null) {
//                    db.insertCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
//                } else {
//                    db.updateCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
//                }
                refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe", "获取数据失败");
                refresh.setRefreshing(false);
            }
        }, json.toString());
        ProgressUtil.closeProgressDialog();
    }

    /**
     * 网络数据获取成功，刷新列表
     * @param content
     */
    private void refreshList(String content) {
        listParent = JsonToPubhlishData.getGroup(content,"高级");//按年级分组
        listChild = JsonToPubhlishData.getGroupSub(content, listParent);//年级中的班级
        adapter.setList(listParent, listChild);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(getActivity(), ContactsActivity.class);
        intent.setAction(listChild.get(groupPosition).get(childPosition).getGroup_name());
        startActivity(intent);
        return true;
    }
}
