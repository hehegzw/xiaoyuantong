package com.jlstudio.market.fragment;

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
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.util.PopuWindowManager;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.market.activity.ChatActivity;
import com.jlstudio.market.activity.PublishGoodAty;
import com.jlstudio.market.adapter.ShowMyMessagesAdapter;
import com.jlstudio.market.bean.Chat;
import com.jlstudio.market.bean.GoodsDetail;
import com.jlstudio.publish.util.RecycleViewDivider;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzw on 2016/3/29.
 */
public class MyMessagesFragment extends Fragment {
    private LinearLayout view;
    private SwipeRefreshLayout refresh;
    private RecyclerView showMyGoods;
    private List<Chat> chats;
    private ShowMyMessagesAdapter adapter;
    private PopuWindowManager popuWindowManager;
    private GetDataNet gn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (LinearLayout) inflater.inflate(R.layout.fragment_my_goods,container,false);
        gn = GetDataNet.getInstence(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        getDataNet();

    }
    private void initView() {
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setDistanceToTriggerSync(100);
        refresh.setColorSchemeColors(R.color.fresh_one, R.color.fresh_two, R.color.fresh_three, R.color.fresh_four);
        chats = new ArrayList<>();
        adapter = new ShowMyMessagesAdapter(chats,getActivity());
        showMyGoods = (RecyclerView)  view.findViewById(R.id.showMyGoods);
        showMyGoods.setAdapter(adapter);
        showMyGoods.setHasFixedSize(true);
        showMyGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        showMyGoods.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
        initEvent();
    }

    private void initEvent() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataNet();
            }
        });
        adapter.setListener(new ShowMyMessagesAdapter.ClickListener() {
            @Override
            public void click(View v, int position) {
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                if(chats.get(position).getUserfrom().endsWith(Config.loadUser(getActivity()).getUsername())){
                    intent.putExtra("userId",chats.get(position).getUserto());

                }else{
                    intent.putExtra("userId",chats.get(position).getUserfrom());
                }
                Log.d("username",chats.get(position).getUserfrom());
                intent.putExtra("name",chats.get(position).getFromname());

                startActivity(intent);
            }
        });
        adapter.setLongListener(new ShowMyMessagesAdapter.LongClickListener() {
            @Override
            public void click(View v, int position) {
                popuWindowManager.showPopu(showMyGoods,v,position);
            }
        });
        popuWindowManager = new PopuWindowManager(getActivity(), new PopuWindowManager.DeleteListener() {
            @Override
            public void delete(int position) {
                deleteChat(position);
            }
        });
    }

    private void getDataNet(){
        chats.clear();
        JSONObject json = new JSONObject();
        try {
            json.put("userfrom", Config.loadUser(getActivity()).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UplaodFace.getData(Config.URL + "getchathistorys", json.toString(), new UplaodFace.SuccessJson() {
            @Override
            public void onSuccess(JSONArray response) {
                jsonToData(response);
                refresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                refresh.setRefreshing(false);
                ShowToast.show(getActivity(), "获取数据失败，请检查网络");
            }
        });
    }
    private void deleteChat(final int position){
        ProgressUtil.showProgressDialog(getActivity(),"删除中...");
        String userfrom = chats.get(position).getUserfrom();
        String userto = chats.get(position).getUserto();
        JSONObject json = new JSONObject();
        try {
            json.put("userfrom", userfrom);
            json.put("userto", userto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "deletechathistorys", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                chats.remove(position);
                adapter.notifyDataSetChanged();
                ProgressUtil.closeProgressDialog();
                ShowToast.show(getActivity(), "删除成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                refresh.setRefreshing(false);
                ShowToast.show(getActivity(), "删除数据失败，请检查网络");
                ProgressUtil.closeProgressDialog();
            }
        },json.toString());
    }
    private void jsonToData(JSONArray response) {
        for(int i=0;i<response.length();i++){
            try {
                JSONObject json = response.getJSONObject(i);
                Chat chat = new Chat();
                chat.setFromname(json.getString("fromname"));
                chat.setToname(json.getString("toname"));
                chat.setUserfrom(json.getString("userfrom"));
                chat.setText(json.getString("text"));
                chat.setUserto(json.getString("userto"));
                chats.add(chat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }
}
