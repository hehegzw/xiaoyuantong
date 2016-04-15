package com.jlstudio.market.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.market.activity.PublishGoodAty;
import com.jlstudio.market.adapter.ShowMyGoodsAdapter;
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
public class MyGoodsListFragment extends Fragment {
    private LinearLayout view;
    private SwipeRefreshLayout refresh;
    private RecyclerView showMyGoods;
    private List<GoodsDetail> goods;
    private ShowMyGoodsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (LinearLayout) inflater.inflate(R.layout.fragment_my_goods,container,false);
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
        goods = new ArrayList<>();
        adapter = new ShowMyGoodsAdapter(goods,getActivity());
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
        adapter.setListener(new ShowMyGoodsAdapter.ClickListener() {
            @Override
            public void click(View v, int position) {
                Intent intent = new Intent(getActivity(), PublishGoodAty.class);
                intent.setAction("2");
                intent.putExtra("good", goods.get(position));
                startActivity(intent);
            }
        });
    }

    private void getDataNet(){
        goods.clear();
        JSONObject json = new JSONObject();
        try {
            json.put("userid", Config.loadUser(getActivity()).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UplaodFace.getData(Config.URL + "goods", json.toString(), new UplaodFace.SuccessJson() {
            @Override
            public void onSuccess(JSONArray response) {
                jsonToData(response);
                refresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
                ProgressUtil.closeProgressDialog();
            }

        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                refresh.setRefreshing(false);
                ShowToast.show(getActivity(), "获取数据失败，请检查网络");
                ProgressUtil.closeProgressDialog();
            }
        });
    }
    private void jsonToData(JSONArray response) {
        for(int i=0;i<response.length();i++){
            try {
                JSONObject json = response.getJSONObject(i);
                GoodsDetail good = new GoodsDetail();
                good.setId(Integer.valueOf(json.getString("id")));
                good.setFace(json.getString("face"));
                good.setUserid(json.getString("userid"));
                good.setUsername(json.getString("username"));
                good.setDescription(json.getString("description"));
                good.setPrice(json.getDouble("price"));
                good.setDatetime(json.getLong("datetime"));
                good.setCloseFlag(json.getInt("closeFlag"));
                good.setViews(json.getInt("views"));
                JSONArray array = json.getJSONArray("images");
                List<String> paths = new ArrayList<>();
                for(int j=0;j<array.length();j++){
                    paths.add((String) array.get(j));
                }
                good.setImages(paths);
                goods.add(good);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }
}
