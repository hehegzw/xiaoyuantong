package com.jlstudio.publish.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.PopuWindowManager;
import com.jlstudio.publish.activity.PublishDatasAty;
import com.jlstudio.publish.activity.ShowSendPublishAty;
import com.jlstudio.publish.adapter.PublishDatasAdapter;
import com.jlstudio.publish.bean.PublishListData;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.RecycleViewDivider;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by gzw on 2015/10/16.
 */
public class FMyPublish extends Fragment implements  PublishDatasAdapter.OnRecyclerViewListener {
    private SwipeRefreshLayout view;
    private RecyclerView listView;
    private PublishDatasAdapter adapter;
    private AlphaInAnimationAdapter adapter2;
    private List<PublishListData> list;
    private GetDataNet gn;
    private CatchData data;
    private DBOption db;
    private String action = null;
    private PopuWindowManager popuWindowManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_publish_datas, container, false);
        action = getActivity().getIntent().getAction();
        gn = GetDataNet.getInstence(getActivity());
        initListView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDatas();
    }

    private void getDatas(){
        getDatasFromNet();
//        db = new DBOption(getActivity());
//        data = db.getCatch(Config.URL+Config.SENDMSG+Config.loadUser(getActivity()).getUsername());
//        if(data == null){
//            getDatasFromNet();
//        }else{
//            Long time = System.currentTimeMillis();
//            long catchtime = Long.parseLong(data.getTime());
//            if((time - catchtime) > Config.CATCHTIME||action!=null){
//                action = null;
//                getDatasFromNet();
//            }else{
//                list = JsonToPubhlishData.getePublishListData(data.getContent(), "send");
//                adapter.setList(list);
//                adapter2.notifyDataSetChanged();
//            }
//
//        }
    }
    private void initListView() {
        list = new ArrayList<>();
        adapter = new PublishDatasAdapter(getActivity(),list,"send");
        listView = (RecyclerView) view.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnRecyclerViewListener(this);
        ScaleInAnimationAdapter adapter1 = new ScaleInAnimationAdapter(adapter);
        adapter1.setFirstOnly(false);
        adapter2 = new AlphaInAnimationAdapter(adapter1);
        adapter2.setFirstOnly(false);
        listView.setAdapter(adapter2);
        listView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
        listView.setItemAnimator(new FadeInAnimator(new OvershootInterpolator(1f)));
        listView.getItemAnimator().setAddDuration(500);
        listView.getItemAnimator().setRemoveDuration(500);
        listView.getItemAnimator().setMoveDuration(1000);
        listView.getItemAnimator().setChangeDuration(1000);
        view.setColorSchemeColors(R.color.fresh_one, R.color.fresh_two, R.color.fresh_three, R.color.fresh_four);
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDatasFromNet();
            }
        });
        popuWindowManager = new PopuWindowManager(getActivity(), new PopuWindowManager.DeleteListener() {
            @Override
            public void delete(int position) {
                deletepublish(position);
                popuWindowManager.getPopupWindow().dismiss();
            }
        });
    }

    private void getDatasFromNet(){
        JSONObject json = new JSONObject();
        try {
            json.put("username", Config.loadUser(getActivity()).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.SENDMSG, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                if (s != null) {
//                    if (data == null) {
//                        db.insertCatch(Config.URL + Config.SENDMSG + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
//                    } else {
//                        db.updateCatch(Config.URL + Config.SENDMSG + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
//                    }
                    list = JsonToPubhlishData.getePublishListData(s, "send");
                    adapter.setList(list);
                    adapter2.notifyDataSetChanged();

                } else {
                    ShowToast.show(getActivity(), "没有通知");
                    list = new ArrayList<>();
                }
                view.setRefreshing(false);
                Log.d("hehe", s);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(getActivity(), "获取数据失败");
                view.setRefreshing(false);
                Log.d("hehe", "error");
            }
        }, json.toString());

    }

    private void deletepublish(final int position) {
        JSONObject json = new JSONObject();
        try {
            json.put("noticeid", list.get(position).getNoticeid());
            json.put("fromorto", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.DELETEMYSENDNOTICE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                ShowToast.show(getActivity(), "删除成功");
                list.remove(position);
                adapter2.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(getActivity(), "删除失败");
            }
        }, json.toString());
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ShowSendPublishAty.class);
        intent.setAction(list.get(position).getNoticeid());
        intent.putExtra("noticegroup",list.get(position).getNoticegroup());
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        popuWindowManager.showPopu(listView,view,position);
        return true;
    }
}
